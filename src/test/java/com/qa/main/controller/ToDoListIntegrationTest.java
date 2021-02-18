package com.qa.main.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.ResultMatcher;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.qa.main.dto.ToDoListDto;
import com.qa.main.persistence.domain.ToDoList;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Sql(scripts = { "classpath:schema.sql", "classpath:data.sql" }, executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
public class ToDoListIntegrationTest {

	@Autowired
	private MockMvc mvc;
	@Autowired
	private ObjectMapper jsonify;
	@Autowired
	private ModelMapper mapper;

	private ToDoListDto mapToTDLDto(ToDoList list) {
		return this.mapper.map(list, ToDoListDto.class);
	}

	private String URI = "/toDoList";

	private final ToDoList testList1 = new ToDoList("Test List 1");
	private final ToDoList testList2 = new ToDoList("Test List 2");

	@Test
	public void testCreate() throws Exception {
		// resources
		ToDoListDto actualAsDto = this.mapToTDLDto(new ToDoList("Newly created list"));
		ToDoListDto expectedAsDto = this.mapToTDLDto(new ToDoList(2L, "Newly created list"));
		String actualAsJson = this.jsonify.writeValueAsString(actualAsDto);
		String expectedAsJson = this.jsonify.writeValueAsString(expectedAsDto);
		// request
		RequestBuilder request = post(URI + "/create").contentType(MediaType.APPLICATION_JSON).content(actualAsJson);
		ResultMatcher confirmStatus = status().isCreated();
		ResultMatcher confirmBody = content().json(expectedAsJson);
		// assert
		this.mvc.perform(request).andExpect(confirmBody).andExpect(confirmStatus);

	}

	@Test
	public void testReadAll() throws Exception {
		RequestBuilder request = get(URI + "/read");
		ResultMatcher confirmStatus = status().isOk();
		this.mvc.perform(request).andExpect(confirmStatus);
	}
	
	@Test
	public void testReadById() throws Exception{
		
	}
}
