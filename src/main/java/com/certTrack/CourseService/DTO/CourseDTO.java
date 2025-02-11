package com.certTrack.CourseService.DTO;

public class CourseDTO {
	private int id;
	private String name;
	private String description;
	private String category;
	private int module;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getModule() {
		return module;
	}
	public void setModule(int module) {
		this.module = module;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public CourseDTO(int id, String name, String description, String category, int module) {
		super();
		this.id = id;
		this.name = name;
		this.description = description;
		this.category = category;
		this.module=module;
	}
	public CourseDTO() {
	}
	
}
