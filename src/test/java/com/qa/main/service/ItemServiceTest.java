package com.qa.main.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import com.qa.main.dto.ItemDto;
import com.qa.main.persistence.domain.Item;
import com.qa.main.persistence.domain.ToDoList;
import com.qa.main.persistence.repo.ItemRepo;

@SpringBootTest
@ActiveProfiles("test")
public class ItemServiceTest {

	@Autowired
	private ItemService service;
	
	@MockBean
	private ItemRepo repo;
	
	@MockBean
	private ModelMapper mapper;
	
	private ItemDto mapToIDto(Item item) {
		return this.mapper.map(item, ItemDto.class);
	}
	
	
	//class resources
	private Item testItem1=new Item("Test Item 1",false);
	private Item testItem2=new Item("Test Item 2",false);
	private final ToDoList testList1=new ToDoList("Test List 1");
	
	@Test
	public void testCreate() throws Exception{
		Item toCreate=new Item("Newly created item",false,testList1);
		Item created=new Item(3L,"Newly created item",false,testList1);
		
		//rules
		when(this.repo.save(toCreate)).thenReturn(created);
		//actions
		
		//assertions
		assertThat(this.service.create(toCreate)).isEqualTo(this.mapToIDto(created));
		verify(this.repo,times(1)).save(toCreate);
	}
	
	@Test
	public void testReadAll() throws Exception{
		//re
		List<Item> testItems=List.of(testItem1,testItem2);
		List<ItemDto> testItemsAsDtos= testItems.stream().map(this::mapToIDto).collect(Collectors.toList());
		//ru
		when(this.repo.findAll()).thenReturn(testItems);
		//a
		assertThat(this.service.readAll()).isEqualTo(testItemsAsDtos);
		verify(this.repo,times(1)).findAll();
		}
	
	@Test
	public void testReadById() throws Exception{
		//re
		Long id=1L;
		//ru
		when(this.repo.findById(id)).thenReturn(Optional.of(testItem1));
		//a
		assertThat(this.service.readById(id)).isEqualTo(this.mapToIDto(testItem1));
		verify(this.repo,times(1)).findById(id);
		
	}
	
//	@Test
//	public void testUpdate() throws Exception{
//		//re
//		Long id=1L;
//		ToDoList updated=new ToDoList(1L,"Updated List 1");
//		ToDoListDto updatedAsDto=mapToTDLDto(updated);
//		//ru
//		when(this.repo.findById(id)).thenReturn(Optional.of(testList1));
//		when(this.repo.save(updated)).thenReturn(updated);
//		//a
//		assertThat(this.service.update(updatedAsDto, id)).isEqualTo(updatedAsDto);
//		verify(this.repo,times(1)).findById(id);
//		verify(this.repo,times(1)).save(updated);
//	}
	
	@Test
	public void testDelete() throws Exception{
		Long id=1L;
		//ru
		when(this.repo.existsById(id)).thenReturn(false);
		//a
		assertThat(this.service.delete(id)).isEqualTo(true);
		verify(this.repo,times(1)).existsById(id);
	}

	@Test
	public void testFindItemsInList() throws Exception{
		Long listId=1L;
		List<Item> testItems=List.of(testItem1,testItem2);
		List<ItemDto> testItemsAsDtos= testItems.stream().map(this::mapToIDto).collect(Collectors.toList());
		//ru
		when(this.repo.findItemsInList(listId)).thenReturn(testItems);
		//
		assertThat(this.service.findItemsInList(listId)).isEqualTo(testItemsAsDtos);
		verify(this.repo,times(1)).findItemsInList(listId);
	}
}
