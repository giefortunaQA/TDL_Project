package com.qa.main.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.qa.main.dto.ToDoListDto;
import com.qa.main.persistence.domain.ToDoList;
import com.qa.main.service.ToDoListService;

@SpringBootTest
public class ToDoListControllerTest {

	@Autowired
	private ToDoListController controller;

	@MockBean
	private ToDoListService service;

	@MockBean
	private ModelMapper mapper;

	private ToDoListDto mapToTDLDto(ToDoList list) {
		return this.mapper.map(list, ToDoListDto.class);
	}

	private final ToDoList testList1 = new ToDoList("Test List 1");
	private final ToDoList testList2 = new ToDoList("Test List 2");
	private final ToDoListDto testList1AsDto = this.mapToTDLDto(testList1);

//	@Test
//	public void testMapper() {
//		assertThat(testList1AsDto).isEqualTo(this.mapToTDLDto(testList1));
//	}
//
//	@Test
//	public void testCreate() throws Exception {
//		when(this.service.create(testList1)).thenReturn(testList1AsDto);
//		ResponseEntity<ToDoListDto> expected = new ResponseEntity<ToDoListDto>(testList1AsDto, HttpStatus.CREATED);
//		assertThat(this.controller.create(testList1)).isEqualTo(expected);
//		verify(this.service, times(1)).create(testList1);
//	}
//
//	@Test
//	public void testReadAll() throws Exception {
//		List<ToDoList> testLists = List.of(testList1, testList2);
//		List<ToDoListDto> testListsAsDtos = testLists.stream().map(this::mapToTDLDto).collect(Collectors.toList());
//		// ru
//		when(this.service.readAll()).thenReturn(testListsAsDtos);
//		// a
//		assertThat(this.service.readAll()).isEqualTo(testListsAsDtos);
//		verify(this.service, times(1)).readAll();
//	}
}
