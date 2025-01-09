package com.certTrack.CourseService.Controllers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import com.certTrack.CourseService.DTO.CourseDTO;
import com.certTrack.CourseService.DTO.ResponseMessage;
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
	public void AuthorizedUserCanSeeCoursesById() throws Exception{
		 CourseDTO courseDTO = new CourseDTO(1, "java for beginers", "this course is for beginers witch wont to start learn java","Java", 10);
		 String responseJson = objectMapper.writeValueAsString(courseDTO);
		 api.perform(get("/courses/id?id=1")
	            .contentType(MediaType.APPLICATION_JSON))
	            .andExpect(status().isOk())
	            .andExpect(content().json(responseJson));
	}
	
	@WithMockUser
	@Test
	public void AuthorizedUserCanSeeCoursesByName() throws Exception{
		 List<CourseDTO> courseDTO = courseService.getCoursesByName("JAVA");
		 String responseJson = objectMapper.writeValueAsString(courseDTO);
		 api.perform(get("/courses/name?name=Java")
	            .contentType(MediaType.APPLICATION_JSON))
	            .andExpect(status().isOk())
	            .andExpect(content().json(responseJson));
	}
	
	@WithMockUser
	@Test
	public void AuthorizedUserCanSeeCoursesByCategory() throws Exception{
		 List<CourseDTO> courseDTO = courseService.getCoursesByCategory("JAVA");
		 String responseJson = objectMapper.writeValueAsString(courseDTO);
		 api.perform(get("/courses/category?category=Java")
	            .contentType(MediaType.APPLICATION_JSON))
	            .andExpect(status().isOk())
	            .andExpect(content().json(responseJson));
	}
	
	@WithMockUser
	@Test
	public void AuthorizedUserCanSeeAllCourses() throws Exception{
		 List<CourseDTO> courses = courseService.getCourses();
		 String responseJson = objectMapper.writeValueAsString(courses);
		 api.perform(get("/courses/")
	            .contentType(MediaType.APPLICATION_JSON))
	            .andExpect(status().isOk())
	            .andExpect(content().json(responseJson));
	}
	
	@WithMockUser(roles = "ADMIN")
	@Test
	public void AdminCanDeleteCourses() throws Exception{
		 ResponseMessage message = new ResponseMessage("no course by this id");
		 String responseJson = objectMapper.writeValueAsString(message);
		 api.perform(delete("/courses/admin/delete?id=100")
	            .contentType(MediaType.APPLICATION_JSON))
	            .andExpect(status().isOk())
	            .andExpect(content().json(responseJson));
	}

}
