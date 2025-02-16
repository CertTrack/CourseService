package com.certTrack.CourseService.Controllers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.certTrack.CourseService.DTO.ResponseMessage;
import com.certTrack.CourseService.Entity.Course;
import com.certTrack.CourseService.Service.CourseService;
import com.fasterxml.jackson.databind.ObjectMapper;

@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class CourseControllerTest {

	@Autowired
	MockMvc api;

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private CourseService courseService;

	@Test
	public void NotAuthorizedUserCanNotSeeAnyEndpoint() throws Exception {
		api.perform(get("/courses/")).andExpect(status().is4xxClientError());
		api.perform(get("/courses/id?id=1")).andExpect(status().is4xxClientError());
		api.perform(get("/courses/name?name=Java")).andExpect(status().is4xxClientError());
		api.perform(get("/courses/category?category=Java")).andExpect(status().is4xxClientError());
	}

	@WithMockUser
	@Test
	public void AuthorizedUserCanSeeCoursesById() throws Exception {
		Course Course = new Course("C123 For pro", 1, "This course is for people who want to continue learn C123",
				"Java", 
				List.of("12_2025-02-15_09:52_1_Module1.zip",
						"12_2025-02-15_21:13_1_Module2.zip",
						"12_2025-02-15_21:46_1_Module2.zip",
						"12_2025-02-15_21:46_1_Module2.zip",
						"12_2025-02-15_21:51_1_Module2.zip"));
		String responseJson = objectMapper.writeValueAsString(Course);
		api.perform(get("/courses/id?id=12").contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andExpect(content().json(responseJson));
	}

	@WithMockUser
	@Test
	public void AuthorizedUserCanSeeCoursesByName() throws Exception {
		List<Course> Course = courseService.getCoursesByName("JAVA");
		String responseJson = objectMapper.writeValueAsString(Course);
		api.perform(get("/courses/name?name=Java").contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andExpect(content().json(responseJson));
	}

	@WithMockUser
	@Test
	public void AuthorizedUserCanSeeCoursesByCategory() throws Exception {
		List<Course> Course = courseService.getCoursesByCategory("JAVA");
		String responseJson = objectMapper.writeValueAsString(Course);
		api.perform(get("/courses/category?category=Java").contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andExpect(content().json(responseJson));
	}

	@WithMockUser
	@Test
	public void AuthorizedUserCanSeeAllCourses() throws Exception {
		List<Course> courses = courseService.getCourses();
		String responseJson = objectMapper.writeValueAsString(courses);
		api.perform(get("/courses/").contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andExpect(content().json(responseJson));
	}

	@WithMockUser(auth = "ROLE_ADMIN")
	@Test
	public void AdminCanDeleteCourses() throws Exception {
		ResponseMessage message = new ResponseMessage("no course by this id");
		String responseJson = objectMapper.writeValueAsString(message);
		api.perform(delete("/courses/admin/delete?id=100").contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andExpect(content().json(responseJson));
	}

}
