package com.certTrack.CourseService.Entity;


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
    private Category category;	
    private int module;
}
