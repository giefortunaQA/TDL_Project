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
	
	
	private ModelMapper mapper=new ModelMapper();
	
	private ItemDto mapToIDto(Item item) {
		return this.mapper.map(item, ItemDto.class);
	}
	
	
	//class resources
	private Item testItem1=new Item("Test Item 1",false);
	private Item testItem2=new Item("Test Item 2",false);
	private final ToDoList testList1=new ToDoList("Test List 1");
	private final ToDoList prePopList1=new ToDoList(1L,"Prepopulated List 1");
	private final ItemDto prePopItem1AsDto=new ItemDto(1L,"Prepopulated Item 1",false);
	private final ItemDto prePopItem2AsDto=new ItemDto(2L,"Prepopulated Item 2",false);
	private final List<ItemDto> prePopItems=List.of(prePopItem1AsDto,prePopItem2AsDto);
	
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
	
	@Test
	public void testUpdate() throws Exception{
		//re
		Long id=1L;
		Item toUpdate=new Item(1L,"Prepopoulated Item 1 - Updated",false,prePopList1);
		ItemDto toUpdateDto=this.mapToIDto(toUpdate);
		Item target=new Item(1L,"Prepopoulated Item 1",false,prePopList1);
		//ru
		when(this.repo.findById(id)).thenReturn(Optional.of(target));
		when(this.repo.save(toUpdate)).thenReturn(toUpdate);
		//a
		assertThat(this.service.update(toUpdateDto, id)).isEqualTo(toUpdateDto);
		verify(this.repo,times(1)).findById(id);
		verify(this.repo,times(1)).save(toUpdate);
	}
	
	@Test
	public void testDeletePass() throws Exception{
		Long id=1L;
		//ru
		when(this.repo.existsById(id)).thenReturn(false);
		//a
		assertThat(this.service.delete(id)).isEqualTo(true);
		verify(this.repo,times(1)).existsById(id);
	}
	@Test
	public void testDeleteFail() throws Exception{
		Long id=1L;
		//ru
		when(this.repo.existsById(id)).thenReturn(true);
		//a
		assertThat(this.service.delete(id)).isEqualTo(false);
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
