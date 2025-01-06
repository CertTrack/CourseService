package com.certTrack.CourseService.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.certTrack.CourseService.Entity.Course;

@Repository
public interface CourseRepository extends JpaRepository<Course, Integer> {

    @EntityGraph(attributePaths = {"category"})
    List<Course> findByNameContainingIgnoreCase(String name);

    @EntityGraph(attributePaths = {"category"})
    List<Course> findByCategory_NameContainingIgnoreCase(String categoryName);
    
    @Query("SELECT c FROM Course c JOIN FETCH c.category") 
    List<Course> findAllWithCategories();
    
    @Query("SELECT c FROM Course c JOIN FETCH c.category WHERE c.id = :id") 
    Course findByIdWithCategory(@Param("id") int id);
}