package com.certTrack.CourseService.Service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.certTrack.CourseService.DTO.ResponseMessage;
import com.certTrack.CourseService.Entity.Course;
import com.certTrack.CourseService.Repository.CourseRepository;
import com.certTrack.CourseService.Security.UserPrincipal;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CourseService {

	private final CourseRepository courseRepository;
	private final AmazonS3 amazonS3;
	@Value("${aws.s3.bucket}")
	private String bucket;

	public List<Course> getCourses() {
		List<Course> courses = courseRepository.findAll();
		return courses;
	}

	public Course getCourseById(int id) {
		return courseRepository.findById(id).get();
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

		byte[] pdfBytes = null;
		try {
			pdfBytes = zipModule.getBytes();
		} catch (IOException e) {
			e.printStackTrace();
		}
		ByteArrayInputStream inputStream = new ByteArrayInputStream(pdfBytes);
		ObjectMetadata metadata = new ObjectMetadata();
		metadata.setContentLength(pdfBytes.length);

		LocalDateTime dateTime = LocalDateTime.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH:mm");
		String date = dateTime.format(formatter);

		String fileName = courseName + "_" + date + "_" + authorId + "_" + zipModule.getOriginalFilename();
		amazonS3.putObject(new PutObjectRequest(bucket, fileName, inputStream, metadata));

		courseRepository.save(new Course(courseName, authorId, courseDescription, "Java", List.of(fileName)));
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseMessage("succesfully created"));
	}

	public ResponseEntity<?> addModule(UserPrincipal user, int courseId, MultipartFile zipModule) {

		int authorId = user.getUserId();
		if (this.getCourseById(courseId).getAuthorId() != authorId) {
			return ResponseEntity.badRequest()
					.body(new ResponseMessage("This is not your course, so you can not update it"));
		}

		byte[] pdfBytes = null;
		try {
			pdfBytes = zipModule.getBytes();
		} catch (IOException e) {
			e.printStackTrace();
		}
		ByteArrayInputStream inputStream = new ByteArrayInputStream(pdfBytes);
		ObjectMetadata metadata = new ObjectMetadata();
		metadata.setContentLength(pdfBytes.length);

		LocalDateTime dateTime = LocalDateTime.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH:mm");
		String date = dateTime.format(formatter);

		String fileName = this.getCourseById(courseId).getName() + "_" + date + "_" + authorId + "_"
				+ zipModule.getOriginalFilename();
		amazonS3.putObject(new PutObjectRequest(bucket, fileName, inputStream, metadata));

		Course course = courseRepository.findById(courseId).get();
		List<String> modules = course.getModules();
		modules.add(fileName);

		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseMessage("succesfully created"));
	}

	public ResponseEntity<?> validateZIP(MultipartFile zipModule) {
		if (!zipModule.getOriginalFilename().endsWith(".zip")) {
			return ResponseEntity.badRequest().body(new ResponseMessage("This is not zip"));
		}

		// Check if zip contains only mp4 or pdf files
		try (InputStream is = zipModule.getInputStream(); ZipInputStream zis = new ZipInputStream(is)) {
			ZipEntry entry;
			while ((entry = zis.getNextEntry()) != null) {
				String fileName = entry.getName();
				if (fileName.endsWith(".pdf") || fileName.endsWith(".mp4")) {
					System.out.println("Файл " + fileName + " має дозволений формат.");
				} else {
					System.out.println("Файл " + fileName + " має заборонений формат.");
					return ResponseEntity.badRequest()
							.body(new ResponseMessage("The archive contains prohibited files"));
				}
				zis.closeEntry();
			}
		} catch (IOException e) {
			e.printStackTrace();
			return ResponseEntity.status(500).body("Сталася помилка під час обробки файлу.");
		}
		return ResponseEntity.ok().build();
	}

}