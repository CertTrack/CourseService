package com.certTrack.CourseService.Service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.certTrack.CourseService.DTO.CourseDTO;
import com.certTrack.CourseService.DTO.ResponseMessage;
import com.certTrack.CourseService.Entity.Category;
import com.certTrack.CourseService.Entity.Course;
import com.certTrack.CourseService.Repository.CategoryRepository;
import com.certTrack.CourseService.Repository.CourseRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CourseService {

    private final CourseRepository courseRepository;
    private final CategoryRepository categoryRepository;
    
    public List<CourseDTO> getCourses() {
//        List<Course> courses = courseRepository.findAllWithCategories();
        List<Course> courses = courseRepository.findAll();
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
        Course course = courseRepository.findById(id).get();
        return new CourseDTO(
            course.getId(),
            course.getName(),
            course.getDescription(),
            course.getCategory().getName(),
            course.getModule()
        );
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
		if(courseRepository.findById(id).isPresent()) {
			courseRepository.deleteById(id);
			return ResponseEntity.status(HttpStatus.OK)
					.body(new ResponseMessage("course succesfully deleted"));
		}
		return ResponseEntity.status(HttpStatus.BAD_REQUEST)
				.body(new ResponseMessage("no course by this id"));
	}

	public ResponseEntity<?> addModule(Integer courseId, List<String> files, int authorId) {
		if(courseId==null) {
			courseRepository.save(new Course(1, "coursename", authorId, "description", new Category("Java"), 10));
		}else {
			courseRepository.save(new Course(courseId, "coursename", authorId, "description", new Category("Java"), 10));
			System.out.println("sohraneno");
		}
//		private int id;
//		private String name;
//		private int authorId;
//		private String description;
////	    @ManyToOne(fetch = FetchType.LAZY)
////	    @JoinColumn(name = "category_id", nullable = false)
//	    private Category category;	
//	    private int module;
//		courseRepository.findById(name)
		return ResponseEntity.status(HttpStatus.BAD_REQUEST)
				.body(new ResponseMessage("succesfully created"));
	}
    
}