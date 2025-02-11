package com.certTrack.CourseService.Controllers;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.certTrack.CourseService.DTO.CourseDTO;
import com.certTrack.CourseService.DTO.ResponseMessage;
import com.certTrack.CourseService.Service.CourseService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/courses")
@RequiredArgsConstructor
public class CourseController {

    private final CourseService courseService;
    
    @GetMapping("/id")
    public CourseDTO getCoursesByName(@RequestParam int id) {
    	return courseService.getCourseById(id);
    }

//    @GetMapping("/name")
//    public List<CourseDTO> getCoursesByName(@RequestParam String name) {
//        return courseService.getCoursesByName(name);
//    } 
//    
//
//    @GetMapping("/category")
//    public List<CourseDTO> getCoursesByCategory(@RequestParam String category) {
//        return courseService.getCoursesByCategory(category);
//    }

    
    
    @GetMapping("/")
    public List<CourseDTO> getAllCourses() {
        return courseService.getCourses();
    }
    
    @PostMapping("/addModule")
    public ResponseEntity<?> createCourses(@RequestParam Integer courseId,
    									@RequestParam List<String> links,
    									@RequestParam int authorId) {
        return courseService.addModule(courseId, links, authorId);
    }
    
    @DeleteMapping("/admin/delete")
    public ResponseEntity<?> deleteCourses(@RequestParam int id) {
        return courseService.deleteCourse(id);
    }
}
