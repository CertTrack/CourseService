package com.certTrack.CourseService.Repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import com.certTrack.CourseService.Entity.Course;

@Repository
public interface CourseRepository extends MongoRepository<Course, Integer> {

    List<Course> findByNameContainingIgnoreCase(String name);

    List<Course> findByCategoryContainingIgnoreCase(String categoryName);
    
    @Query("{'category' : {$exists: true}}")
    List<Course> findAllWithCategories();
    
}
