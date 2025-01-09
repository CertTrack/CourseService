package com.certTrack.CourseService.Service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.certTrack.CourseService.DTO.CourseDTO;
import com.certTrack.CourseService.Entity.Category;
import com.certTrack.CourseService.Entity.Course;
import com.certTrack.CourseService.Repository.CategoryRepository;
import com.certTrack.CourseService.Repository.CourseRepository;

@Service
public class CourseService {

    private final CourseRepository courseRepository;
    private final CategoryRepository categoryRepository;
    
    public CourseService(CourseRepository courseRepository, CategoryRepository categoryRepository) {
    	this.courseRepository=courseRepository;
    	this.categoryRepository=categoryRepository;
    }
    
    public List<CourseDTO> getCourses() {
        List<Course> courses = courseRepository.findAllWithCategories();
        return courses.stream()
                .map(course -> new CourseDTO(
                        course.getId(),
                        course.getName(),
                        course.getDescription(),
                        course.getCategory().getName(),
                        course.getModule()
                ))
                .collect(Collectors.toList());
    }

    public CourseDTO getCourseById(int id) {
        Course course = courseRepository.findByIdWithCategory(id);
        return new CourseDTO(
            course.getId(),
            course.getName(),
            course.getDescription(),
            course.getCategory().getName(),
            course.getModule()
        );
    }

    
    public List<CourseDTO> getCoursesByName(String name) {
        List<Course> courses = courseRepository.findByNameContainingIgnoreCase(name);
        return courses.stream()
                .map(course -> new CourseDTO(
                        course.getId(),
                        course.getName(),
                        course.getDescription(),
                        course.getCategory().getName(),
                        course.getModule()/*,
                        course.getCreatorId()*/
                ))
                .collect(Collectors.toList());
    }

    public List<CourseDTO> getCoursesByCategory(String categoryName) {
        List<Course> courses = courseRepository.findByCategory_NameContainingIgnoreCase(categoryName);
        return courses.stream()
                .map(course -> new CourseDTO(
                        course.getId(),
                        course.getName(),
                        course.getDescription(),
                        course.getCategory().getName(),
                        course.getModule()/*,
                        course.getCreatorId()*/
                ))
                .collect(Collectors.toList());
    }
    public void saveCourse(CourseDTO dto) {
        Category category = categoryRepository.findByName(dto.getCategory());
        if (category == null) {
            category = new Category(dto.getCategory());
            categoryRepository.save(category);
        }
        Course course = new Course(dto.getName(), dto.getDescription(), category, dto.getModule());
        courseRepository.save(course);
    }

	public String deleteCourse(int id) {
		if(courseRepository.findById(id).isPresent()) {
			courseRepository.deleteById(id);
			return "course succesfully deleted";
		}
		return "no course by this id";
	}
    
}