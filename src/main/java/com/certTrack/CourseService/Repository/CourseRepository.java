package com.certTrack.CourseService.Repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.certTrack.CourseService.Entity.Course;

@Repository
public interface CourseRepository extends MongoRepository<Course, Integer> {

//    @EntityGraph(attributePaths = {"category"})
//    List<Course> findByNameContainingIgnoreCase(String name);
//
//    @EntityGraph(attributePaths = {"category"})
//    List<Course> findByCategory_NameContainingIgnoreCase(String categoryName);
//    
//    @Query("SELECT c FROM Course c JOIN FETCH c.category") 
//    List<Course> findAllWithCategories();
//    
//    @Query("SELECT c FROM Course c JOIN FETCH c.category WHERE c.id = :id") 
//    Course findByIdWithCategory(@Param("id") int id);
}