package com.certTrack.CourseService.Entity;


import java.util.List;

import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;

//@Entity
//@Table(name="course")
@AllArgsConstructor
@Document(collection = "courses")
@Data
public class Course {
	
	//	@Id
//	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	private String name;
	private int authorId;
	private String description;
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "category_id", nullable = false)
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
