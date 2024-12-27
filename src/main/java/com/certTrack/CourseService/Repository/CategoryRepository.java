package com.certTrack.CourseService.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.certTrack.CourseService.Entity.Category;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Integer> {
	Category findByName(String name);
}