package com.certTrack.CourseService.Repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.certTrack.CourseService.Entity.Category;

@Repository
public interface CategoryRepository extends MongoRepository<Category, Integer> {
//	Category findByName(String name);
}