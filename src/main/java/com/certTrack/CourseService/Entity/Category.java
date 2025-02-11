package com.certTrack.CourseService.Entity;

//@Entity
//@Table
public class Category {

//	@Id
//	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	private String name;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Category(String name) {
		this.name = name;
	}
	public Category() {
		super();
	}
	
	
}
