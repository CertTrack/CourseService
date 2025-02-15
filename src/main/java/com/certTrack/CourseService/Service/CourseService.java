package com.certTrack.CourseService.Service;

import static org.springframework.data.mongodb.core.FindAndModifyOptions.options;
import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.services.kms.model.NotFoundException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amazonaws.util.IOUtils;
import com.certTrack.CourseService.DTO.ResponseMessage;
import com.certTrack.CourseService.Entity.Course;
import com.certTrack.CourseService.Entity.DatabaseSequence;
import com.certTrack.CourseService.Repository.CourseRepository;
import com.certTrack.CourseService.Security.UserPrincipal;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CourseService {

	private final MongoOperations mongoOperations;
	private final CourseRepository courseRepository;
	private final AmazonS3 amazonS3;
	@Value("${aws.s3.bucket}")
	private String bucket;

	public int generateSequence(String seqName) {
		DatabaseSequence counter = mongoOperations.findAndModify(query(where("_id").is(seqName)),
				new Update().inc("seq", 1), options().returnNew(true).upsert(true), DatabaseSequence.class);
		return (int) (!Objects.isNull(counter) ? counter.getSeq() : 1);
	}

	public List<Course> getCourses() {
		List<Course> courses = courseRepository.findAll();
		return courses;
	}

	public Course getCourseById(int id) {
	    return courseRepository.findById(id)
	            .orElseThrow(() -> new NotFoundException("Course not found with id:" + id));
	}

	public byte[] getModule(int courseId, int moduleId) {
	    Course course = courseRepository.findById(courseId)
	            .orElseThrow(() -> new NotFoundException("Course not found with id:" + courseId));
		S3Object object = amazonS3.getObject(bucket, course.getModules().get(moduleId-1));
		S3ObjectInputStream inputStream = object.getObjectContent();
		try {
			byte[] content = IOUtils.toByteArray(inputStream);
			return content;
		}catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public List<Course> getCoursesByName(String name) {
		List<Course> courses = courseRepository.findByNameContainingIgnoreCase(name);
		return courses;
	}

	public List<Course> getCoursesByCategory(String categoryName) {
		return courseRepository.findByCategoryContainingIgnoreCase(categoryName);
	}

	public ResponseEntity<?> deleteCourse(int id) {
		if (courseRepository.findById(id).isPresent()) {
			courseRepository.deleteById(id);
			return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage("course succesfully deleted"));
		}
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseMessage("no course by this id"));
	}

	public ResponseEntity<?> createCourse(String courseName, String courseDescription, MultipartFile zipModule,
			int authorId) {

		int courseId = this.generateSequence(Course.SEQUENCE_NAME);
		String fileName = uploadFile(zipModule, courseId, authorId);
		Course course = new Course(courseId, courseName, authorId, courseDescription, "Java", List.of(fileName));
		
		courseRepository.save(course);
		return ResponseEntity.status(HttpStatus.CREATED).body(new ResponseMessage("Successfully created"));
	}

	public ResponseEntity<?> addModule(UserPrincipal user, int courseId, MultipartFile zipModule) {

		int authorId = user.getUserId();
		if (this.getCourseById(courseId).getAuthorId() != authorId) {
			return ResponseEntity.badRequest()
					.body(new ResponseMessage("This is not your course, so you can not update it"));
		}

		String fileName = uploadFile(zipModule, courseId, authorId);
		Course course = courseRepository.findById(courseId).get();
		List<String> modules = course.getModules();
		modules.add(fileName);

		courseRepository.save(course);
		return ResponseEntity.ok(new ResponseMessage("Successfully added module"));
	}

	public String uploadFile(MultipartFile zipModule, int courseId, int authorId) {
	    byte[] fileBytes;
	    try {
	        fileBytes = zipModule.getBytes();
	    } catch (IOException e) {
	        throw new RuntimeException("Failed to read file bytes", e);
	    }

	    ByteArrayInputStream inputStream = new ByteArrayInputStream(fileBytes);
	    ObjectMetadata metadata = new ObjectMetadata();
	    metadata.setContentLength(fileBytes.length);

	    String fileName = String.format("%d_%s_%d_%s", courseId, LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH:mm")), authorId, zipModule.getOriginalFilename());
	    amazonS3.putObject(new PutObjectRequest(bucket, fileName, inputStream, metadata));

	    return fileName;
	}

	public ResponseEntity<?> validateZIP(MultipartFile zipModule) {
	    if (!zipModule.getOriginalFilename().endsWith(".zip")) {
	        return ResponseEntity.badRequest().body(new ResponseMessage("The file is not a zip archive"));
	    }

	    try (ZipInputStream zis = new ZipInputStream(zipModule.getInputStream())) {
	        ZipEntry entry;
	        while ((entry = zis.getNextEntry()) != null) {
	            String fileName = entry.getName();
	            if (!(fileName.endsWith(".pdf") || fileName.endsWith(".mp4"))) {
	                return ResponseEntity.badRequest().body(new ResponseMessage("The archive contains prohibited files: " + fileName));
	            }
	        }
	    } catch (IOException e) {
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseMessage("An error occurred while processing the file"));
	    }

	    return ResponseEntity.ok(new ResponseMessage("The zip file is valid"));
	}


}