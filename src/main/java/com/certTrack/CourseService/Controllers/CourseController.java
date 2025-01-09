package com.certTrack.CourseService.Controllers;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.certTrack.CourseService.DTO.CourseDTO;
import com.certTrack.CourseService.DTO.ResponseMessage;
import com.certTrack.CourseService.Service.CourseService;

@RestController
@RequestMapping("/courses")
public class CourseController {

    private final CourseService courseService;

    public CourseController(CourseService courseService) {
    	this.courseService=courseService;
    }
    
    @GetMapping("/id")
    public CourseDTO getCoursesByName(@RequestParam int id) {
    	return courseService.getCourseById(id);
    }

    @GetMapping("/name")
    public List<CourseDTO> getCoursesByName(@RequestParam String name) {
        return courseService.getCoursesByName(name);
    } 
    

    @GetMapping("/category")
    public List<CourseDTO> getCoursesByCategory(@RequestParam String category) {
        return courseService.getCoursesByCategory(category);
    }

    
    
    @GetMapping("/")
    public List<CourseDTO> getAllCourses() {
        return courseService.getCourses();
    }
    
    
    
    @PostMapping("/")
    public ResponseMessage createCourses(@RequestBody CourseDTO course) {
    	courseService.saveCourse(course);
        return new ResponseMessage("course succesfully uploated");
    }
    
    @DeleteMapping("/admin/delete")
    public ResponseMessage deleteCourses(@RequestParam int id) {
    	String message = courseService.deleteCourse(id);
        return new ResponseMessage(message);
    }
}
