package com.certTrack.CourseService.Entity;


import java.util.List;

import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

//@Entity
//@Table(name="course")
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "courses")
@Data
public class Course {
	
	@JsonIgnore
	private int id;
	private String name;
	private int authorId;
	private String description;
    private String category;
    private List<String> modules;
    
    
	public Course(String name, int authorId, String description, String category, List<String> modules) {
		super();
		this.name = name;
		this.authorId = authorId;
		this.description = description;
		this.category = category;
		this.modules = modules;
	}
}
