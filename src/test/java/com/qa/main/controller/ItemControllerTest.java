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

import com.qa.main.dto.ItemDto;
import com.qa.main.persistence.domain.Item;
import com.qa.main.persistence.domain.ToDoList;
import com.qa.main.service.ItemService;

@SpringBootTest
public class ItemControllerTest {

	@Autowired
	private ItemController controller;

	@MockBean
	private ItemService service;

	private static final ModelMapper mapper = new ModelMapper();

	private ItemDto mapToIDto(Item item) {
		return ItemControllerTest.mapper.map(item, ItemDto.class);
	}

	// class resources
	private ToDoList testToDoList = new ToDoList("Test List 1");
	private Item testItem1 = new Item("Test Item 1", false, testToDoList);
	private Item testItem2 = new Item("Test Item 2", false, testToDoList);
	private ItemDto test1AsDto = this.mapToIDto(testItem1);
	private ItemDto test2AsDto = this.mapToIDto(testItem2);

	@Test
	public void testCreate() throws Exception {
		when(this.service.create(testItem1)).thenReturn(test1AsDto);
		ResponseEntity<ItemDto> expected = new ResponseEntity<ItemDto>(test1AsDto, HttpStatus.CREATED);
		assertThat(this.controller.create(testItem1)).isEqualTo(expected);
		verify(this.service, times(1)).create(testItem1);
	}

	@Test
	public void testReadAll() throws Exception {
		List<ItemDto> testItems = List.of(test1AsDto, test2AsDto);
		when(this.service.readAll()).thenReturn(testItems);
		ResponseEntity<List<ItemDto>> expected = ResponseEntity.ok(testItems);
		assertThat(this.controller.readAll()).isEqualTo(expected);
		verify(this.service, times(1)).readAll();
	}

	@Test
	public void testReadById() throws Exception {
		ItemDto toRead = test1AsDto;
		toRead.setId(1L);
		when(this.service.readById(1L)).thenReturn(toRead);
		ResponseEntity<ItemDto> expected = ResponseEntity.ok(toRead);
		assertThat(this.controller.readById(1L)).isEqualTo(expected);
		verify(this.service, times(1)).readById(1L);
	}

	@Test
	public void testUpdate() throws Exception {
		ItemDto toUpdate = test1AsDto;
		test1AsDto.setName("Test Item 1 Updated");
		when(this.service.update(toUpdate, 1L)).thenReturn(toUpdate);
		ResponseEntity<ItemDto> expected = new ResponseEntity<>(toUpdate, HttpStatus.ACCEPTED);
		assertThat(this.controller.update(1L, toUpdate)).isEqualTo(expected);
	}

	@Test
	public void testDelete() throws Exception {
		when(this.service.delete(1L)).thenReturn(false);
		ResponseEntity<Boolean> expected = new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		assertThat(this.controller.delete(1L)).isEqualTo(expected);
	}
}
