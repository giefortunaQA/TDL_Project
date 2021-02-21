package com.qa.main.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
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

	
	private ModelMapper mapper=new ModelMapper();

	private ToDoListDto mapToTDLDto(ToDoList list) {
		return this.mapper.map(list, ToDoListDto.class);
	}

	private final ToDoList testList1 = new ToDoList("Test List 1");
	private final ToDoListDto testList1AsDto = this.mapToTDLDto(testList1);
	private final ToDoListDto prePopList1AsDto=this.mapToTDLDto(new ToDoList(1L,"Prepopulated List 1"));
	private final ToDoListDto prePopList2AsDto=this.mapToTDLDto(new ToDoList(1L,"Prepopulated List 2"));
	private final List<ToDoListDto> prePopLists=List.of(prePopList1AsDto,prePopList2AsDto);


	@Test
	public void testCreatePass() throws Exception {
		when(this.service.create(testList1)).thenReturn(testList1AsDto); 
		ResponseEntity<ToDoListDto> expected = new ResponseEntity<ToDoListDto>(testList1AsDto, HttpStatus.CREATED);
		assertThat(this.controller.create(testList1)).isEqualTo(expected);
		verify(this.service, times(1)).create(testList1);
	}
	

	@Test
	public void testReadAll() throws Exception {
		when(this.service.readAll()).thenReturn(prePopLists);
		ResponseEntity<List<ToDoListDto>> expected=new ResponseEntity<>(prePopLists,HttpStatus.OK);
		assertThat(this.controller.readAll()).isEqualTo(expected);
		verify(this.service, times(1)).readAll();
	}
	
	@Test
	public void testReadById() throws Exception{
		Long id=1L;
		when(this.service.readById(id)).thenReturn(prePopList1AsDto);
		ResponseEntity<ToDoListDto> expected=new ResponseEntity<ToDoListDto>(prePopList1AsDto, HttpStatus.OK);
		assertThat(this.controller.readById(id)).isEqualTo(expected);
		verify(this.service,times(1)).readById(id);
	}
	
	@Test
	public void testUpdate() throws Exception{
		ToDoListDto prePop1Update=prePopList1AsDto;
		prePop1Update.setName("Prepopulated List 1- Updated");
		when(this.service.update(prePop1Update, 1L)).thenReturn(prePop1Update);
		ResponseEntity<ToDoListDto> expected=new ResponseEntity<>(prePop1Update,HttpStatus.ACCEPTED);
		assertThat(this.controller.update(1L, prePop1Update)).isEqualTo(expected);
		verify(this.service,times(1)).update(prePop1Update, 1L);
	}
	
	@Test
	public void testDeletePass() throws Exception{
		Long id=1L;
		when(this.service.delete(id)).thenReturn(true);
		ResponseEntity<Boolean> expected=new ResponseEntity<>(HttpStatus.NO_CONTENT);
		assertThat(this.controller.delete(id)).isEqualTo(expected);
		verify(this.service,times(1)).delete(id);
	}
	
	@Test
	public void testDeleteFail() throws Exception{
		Long id=1L;
		when(this.service.delete(id)).thenReturn(false);
		ResponseEntity<Boolean> expected=new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		assertThat(this.controller.delete(id)).isEqualTo(expected);
		verify(this.service,times(1)).delete(id);
	}
	
}
