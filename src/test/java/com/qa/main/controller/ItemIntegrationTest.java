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
import com.qa.main.dto.ItemDto;
import com.qa.main.persistence.domain.Item;
import com.qa.main.persistence.domain.ToDoList;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Sql (scripts = { "classpath:schema.sql", "classpath:data.sql" }, executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
public class ItemIntegrationTest {

	@Autowired
	private MockMvc mvc;
	@Autowired
	private ObjectMapper jsonify;
	
	private ModelMapper mapper=new ModelMapper();
	private ItemDto mapToIDto(Item item) {
		return this.mapper.map(item, ItemDto.class);
	}
	
	private String URI = "/item";
	private final ToDoList prePopList1=new ToDoList(1L,"Prepopulated List 1");
	private final ItemDto prePopItem1AsDto=new ItemDto(1L,"Prepopulated Item 1",false);
	private final ItemDto prePopItem2AsDto=new ItemDto(2L,"Prepopulated Item 2",false);
	private final List<ItemDto> prePopItems=List.of(prePopItem1AsDto,prePopItem2AsDto);
	
	@Test
	public void testCreate() throws Exception{
		ItemDto toCreateAsDto=this.mapToIDto(new Item("Test Create Item",false,prePopList1)); //adding new task to prepop list 1
		ItemDto expectedAsDto=this.mapToIDto(new Item(3L,"Test Create Item",false,prePopList1));
		String toCreateAsJson=this.jsonify.writeValueAsString(toCreateAsDto);
		String expectedAsJson=this.jsonify.writeValueAsString(expectedAsDto);
		RequestBuilder request=post(URI+"/create").contentType(MediaType.APPLICATION_JSON).content(toCreateAsJson);
		ResultMatcher confirmStatus = status().isCreated();
		ResultMatcher confirmBody = content().json(expectedAsJson);
		this.mvc.perform(request).andExpect(confirmBody).andExpect(confirmStatus);
	}

	@Test
	public void testReadAll() throws Exception {
		String prePopListsAsJson=this.jsonify.writeValueAsString(prePopItems);
		RequestBuilder request = get(URI + "/read");
		ResultMatcher confirmStatus = status().isOk();
		ResultMatcher confirmBody=content().json(prePopListsAsJson);
		this.mvc.perform(request).andExpect(confirmStatus).andExpect(confirmBody);
	}
	
	@Test
	public void testReadById() throws Exception{
		String prePop1AsJson=this.jsonify.writeValueAsString(prePopItem1AsDto);
		RequestBuilder request=get(URI+"/read/1");
		ResultMatcher confirmStatus=status().isOk();
		ResultMatcher confirmBody=content().json(prePop1AsJson);
		this.mvc.perform(request).andExpect(confirmBody).andExpect(confirmStatus);
	}
	
	@Test
	public void testUpdate() throws Exception{
		ItemDto prePop1Update=prePopItem1AsDto;
		prePop1Update.setName("Prepopulated Item 1 - Updated");
		ItemDto expectedAsDto = this.mapToIDto(new Item(1L, "Prepopulated Item 1 - Updated",false,prePopList1));
		String updateAsJson = this.jsonify.writeValueAsString(prePop1Update);
		String expectedAsJson = this.jsonify.writeValueAsString(expectedAsDto);
		RequestBuilder request=put(URI+"/update/1").contentType(MediaType.APPLICATION_JSON).content(updateAsJson);
		ResultMatcher confirmStatus = status().isAccepted();
		ResultMatcher confirmBody = content().json(expectedAsJson);
		this.mvc.perform(request).andExpect(confirmBody).andExpect(confirmStatus);
	}
	
	@Test
	public void testDeletePass() throws Exception{
		RequestBuilder request=delete(URI+"/delete/1");
		ResultMatcher confirmStatus=status().isNoContent();
		ResultMatcher confirmBody=content().string("");
		this.mvc.perform(request).andExpect(confirmBody).andExpect(confirmStatus);
	}
	
//	@Test
//	public void testDeleteFail() throws Exception{
//		RequestBuilder request=delete(URI+"/delete/1000");
//		MockServerClientHttpResponse
//		this.mvc.perform(request);
//	}
	
	@Test
	public void testFindItemsInList() throws Exception{
		String prePopItemsAsJson=jsonify.writeValueAsString(prePopItems);
		RequestBuilder request=get(URI+"/read/in-list/1");
		ResultMatcher confirmStatus=status().isOk();
		ResultMatcher confirmBody=content().json(prePopItemsAsJson);
		this.mvc.perform(request).andExpect(confirmBody).andExpect(confirmStatus);
	}
}
