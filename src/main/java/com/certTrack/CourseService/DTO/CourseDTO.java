package com.certTrack.CourseService.DTO;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CourseDTO {
	private int id;
	private String name;
	private String description;
	private String category;
	private List<String> modules;
	
}
