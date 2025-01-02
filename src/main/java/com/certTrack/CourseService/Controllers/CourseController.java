package com.certTrack.CourseService.Controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.certTrack.CourseService.DTO.CourseDTO;
import com.certTrack.CourseService.DTO.ResponseMessage;
import com.certTrack.CourseService.Entity.Course;
import com.certTrack.CourseService.Service.CourseService;

@RestController
@RequestMapping("/courses")
public class CourseController {

    private final CourseService courseService;

    public CourseController(CourseService courseService) {
    	this.courseService=courseService;
    }
    
    @GetMapping("/id/{id}")
    public CourseDTO getCoursesByName(@PathVariable int id) {
    	return courseService.getCourseById(id);
    }

    @GetMapping("/name/{name}")
    public List<CourseDTO> getCoursesByName(@PathVariable String name) {
        return courseService.getCoursesByName(name);
    }
    

    @GetMapping("/category/{categoryName}")
    public List<CourseDTO> getCoursesByCategory(@PathVariable String categoryName) {
        return courseService.getCoursesByCategory(categoryName);
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
}
