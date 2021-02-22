package com.qa.main.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;
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
	
	private ModelMapper mapper=new ModelMapper();

	private ToDoListDto mapToTDLDto(ToDoList list) {
		return this.mapper.map(list, ToDoListDto.class);
	}

	private String URI = "/toDoList";
	private final ToDoListDto prePopList1AsDto=this.mapToTDLDto(new ToDoList(1L,"Prepopulated List 1"));
	private final ToDoListDto prePopList2AsDto=this.mapToTDLDto(new ToDoList(2L,"Prepopulated List 2"));
	private final List<ToDoListDto> prePopLists=List.of(prePopList1AsDto,prePopList2AsDto);


	@Test
	void testCreate() throws Exception {
		// resources
		ToDoListDto toCreateAsDto = this.mapToTDLDto(new ToDoList("Test Create List"));
		ToDoListDto expectedAsDto = this.mapToTDLDto(new ToDoList(3L, "Test Create List"));
		String toCreateAsJson = this.jsonify.writeValueAsString(toCreateAsDto);
		String expectedAsJson = this.jsonify.writeValueAsString(expectedAsDto);
		// fake request
		RequestBuilder request = post(URI + "/create").contentType(MediaType.APPLICATION_JSON).content(toCreateAsJson);
		// assert
		ResultMatcher confirmStatus = status().isCreated();
		ResultMatcher confirmBody = content().json(expectedAsJson);
		this.mvc.perform(request).andExpect(confirmBody).andExpect(confirmStatus);
	}

	@Test
	void testReadAll() throws Exception {
		String prePopListsAsJson=this.jsonify.writeValueAsString(prePopLists);
		RequestBuilder request = get(URI + "/read");
		ResultMatcher confirmStatus = status().isOk();
		ResultMatcher confirmBody=content().json(prePopListsAsJson);
		this.mvc.perform(request).andExpect(confirmStatus).andExpect(confirmBody);
	
	}
	
	@Test
	 void testReadById() throws Exception{
		String prePop1AsJson=this.jsonify.writeValueAsString(prePopList1AsDto);
		RequestBuilder request=get(URI+"/read/1");
		ResultMatcher confirmStatus=status().isOk();
		ResultMatcher confirmBody=content().json(prePop1AsJson);
		this.mvc.perform(request).andExpect(confirmBody).andExpect(confirmStatus);
	}
	
	@Test
	void testUpdate() throws Exception{
		ToDoListDto prePop1Update=prePopList1AsDto;
		prePop1Update.setName("Prepopulated List 1 - Updated");
		ToDoListDto expectedAsDto = this.mapToTDLDto(new ToDoList(1L, "Prepopulated List 1 - Updated"));
		String updateAsJson = this.jsonify.writeValueAsString(prePop1Update);
		String expectedAsJson = this.jsonify.writeValueAsString(expectedAsDto);
		RequestBuilder request=put(URI+"/update/1").contentType(MediaType.APPLICATION_JSON).content(updateAsJson);
		ResultMatcher confirmStatus = status().isAccepted();
		ResultMatcher confirmBody = content().json(expectedAsJson);
		this.mvc.perform(request).andExpect(confirmBody).andExpect(confirmStatus);
	}
	
	@Test
	void testDeletePass() throws Exception{
		RequestBuilder request=delete(URI+"/delete/1");
		ResultMatcher confirmStatus=status().isNoContent();
		ResultMatcher confirmBody=content().string("");
		this.mvc.perform(request).andExpect(confirmBody).andExpect(confirmStatus);
	}

}
