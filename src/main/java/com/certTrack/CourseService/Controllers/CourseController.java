package com.certTrack.CourseService.Controllers;

import java.util.List;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.certTrack.CourseService.Entity.Course;
import com.certTrack.CourseService.Security.UserPrincipal;
import com.certTrack.CourseService.Service.CourseService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/courses")
@RequiredArgsConstructor
public class CourseController {

	//getModule
	
	private final CourseService courseService;

	@GetMapping("/id")
	public Course getCoursesByName(@RequestParam int id) {
		return courseService.getCourseById(id);
	}

	@GetMapping("/id")
	public ResponseEntity<ByteArrayResource> getCoursesByName(@RequestParam int courseId, @RequestParam int moduleId) {
		byte[] data = courseService.getModule(courseId, moduleId);
    	ByteArrayResource arrayResource = new ByteArrayResource(data);
    	return ResponseEntity
    			.ok()
    			.contentLength(data.length)
    			.header("Content-type", "application/octet-stream")
    			.header("Content-disposition", "attachment; filename=\""+"module.pdf"+"\"")
    			.body(arrayResource);
	}
	@GetMapping("/name")
	public List<Course> getCoursesByName(@RequestParam String name) {
		return courseService.getCoursesByName(name);
	}

	@GetMapping("/category")
	public List<Course> getCoursesByCategory(@RequestParam String category) {
		return courseService.getCoursesByCategory(category);
	}

	@GetMapping("/")
	public List<Course> getAllCourses() {
		return courseService.getCourses();
	}

	@PostMapping("/createCourse")
	public ResponseEntity<?> createCourses(@AuthenticationPrincipal UserPrincipal principal, @RequestParam String courseName, @RequestParam String courseDescription,
			@RequestParam MultipartFile zipModule) {
		return courseService.createCourse(courseName, courseDescription, zipModule, principal.getUserId());
	}

	@PostMapping("/addModule")
	public ResponseEntity<?> addModule(@AuthenticationPrincipal UserPrincipal principal, @RequestParam int courseId,
			@RequestParam MultipartFile zipModule) {
		ResponseEntity<?> validationResponse = courseService.validateZIP(zipModule);
		if (validationResponse.getStatusCode() != HttpStatus.OK) {
			return validationResponse;
		}
		return courseService.addModule(principal, courseId, zipModule);
	}

	@DeleteMapping("/admin/delete")
	public ResponseEntity<?> deleteCourses(@RequestParam int id) {
		return courseService.deleteCourse(id);
	}
}
