package com.certTrack.CourseService.Service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;
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
import com.certTrack.CourseService.DTO.CourseDTO;
import com.certTrack.CourseService.DTO.ResponseMessage;
import com.certTrack.CourseService.Entity.Course;
import com.certTrack.CourseService.Repository.CourseRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CourseService {

	private final CourseRepository courseRepository;
	private final AmazonS3 amazonS3;
	@Value("${aws.s3.bucket}")
	private String bucket;

	public List<CourseDTO> getCourses() {
//        List<Course> courses = courseRepository.findAllWithCategories();
		List<Course> courses = courseRepository.findAll();
		return courses.stream().map(course -> new CourseDTO(course.getId(), course.getName(), course.getDescription(),
				course.getCategory(), course.getModules())).collect(Collectors.toList());
	}

	public CourseDTO getCourseById(int id) {
		Course course = courseRepository.findById(id).get();
		return new CourseDTO(course.getId(), course.getName(), course.getDescription(), course.getCategory(),
				course.getModules());
	}

//    public List<CourseDTO> getCoursesByName(String name) {
//        List<Course> courses = courseRepository.findByNameContainingIgnoreCase(name);
//        return courses.stream()
//                .map(course -> new CourseDTO(
//                        course.getId(),
//                        course.getName(),
//                        course.getDescription(),
//                        course.getCategory().getName(),
//                        course.getModule()/*,
//                        course.getCreatorId()*/
//                ))
//                .collect(Collectors.toList());
//    }
//
//    public List<CourseDTO> getCoursesByCategory(String categoryName) {
//        List<Course> courses = courseRepository.findByCategory_NameContainingIgnoreCase(categoryName);
//        return courses.stream()
//                .map(course -> new CourseDTO(
//                        course.getId(),
//                        course.getName(),
//                        course.getDescription(),
//                        course.getCategory().getName(),
//                        course.getModule()/*,
//                        course.getCreatorId()*/
//                ))
//                .collect(Collectors.toList());
//    }
//    public void saveCourse(CourseDTO dto) {
//        Category category = categoryRepository.findByName(dto.getCategory());
//        if (category == null) {
//            category = new Category(dto.getCategory());
//            categoryRepository.save(category);
//        }
//        Course course = new Course(dto.getName(), dto.getDescription(), category, dto.getModule());
//        courseRepository.save(course);
//    }

	public ResponseEntity<?> deleteCourse(int id) {
		if (courseRepository.findById(id).isPresent()) {
			courseRepository.deleteById(id);
			return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage("course succesfully deleted"));
		}
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseMessage("no course by this id"));
	}

	public ResponseEntity<?> createCourse(String courseName, String courseDescription, MultipartFile zipModule,
			int authorId) {

		if (!zipModule.getOriginalFilename().endsWith(".zip")) {
			return ResponseEntity.badRequest().body(new ResponseMessage("This is not zip"));
		}
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

		//String fileName = courseId + "_" + authorId + "_" + date + zipModule.getOriginalFilename();
		String fileName = date + "_" + authorId + "_" + zipModule.getOriginalFilename();
		amazonS3.putObject(new PutObjectRequest(bucket, fileName, inputStream, metadata));

		courseRepository.save(new Course(courseName, authorId, courseDescription, "Java", List.of(fileName)));
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseMessage("succesfully created"));
	}

	public ResponseEntity<?> addModule(int courseId, MultipartFile zipModule, int authorId) {

		// TODO: confirm that the course belongs to this author

		if (!zipModule.getOriginalFilename().endsWith(".zip")) {
			return ResponseEntity.badRequest().body(new ResponseMessage("This is not zip"));
		}
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

		String fileName = courseId + "_" + authorId + "_" + date + zipModule.getOriginalFilename();
		amazonS3.putObject(new PutObjectRequest(bucket, fileName, inputStream, metadata));

		Course course = courseRepository.findById(courseId).get();
		List<String> modules = course.getModules();
		modules.add(fileName);
		
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseMessage("succesfully created"));
	}

}