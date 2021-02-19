package com.qa.main.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;
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
	private final ToDoList prePopList1=new ToDoList(1L,"Prepopulated List 1");
	private final Item prePopItem1=new Item(1L,"Prepopulated Item 1",false,prePopList1);
	private final Item prePopItem2=new Item(2L,"Prepopulated Item 2",false,prePopList1);
	private final List<Item> prePopItemsPojo=List.of(prePopItem1,prePopItem2);
	private final ItemDto prePopItem1AsDto=new ItemDto(1L,"Prepopulated Item 1",false);
	private final ItemDto prePopItem2AsDto=new ItemDto(2L,"Prepopulated Item 2",false);
	private final List<ItemDto> prePopItems=List.of(prePopItem1AsDto,prePopItem2AsDto);
	
	@Test
	public void testCreate() throws Exception{
		Item toCreate=new Item("Newly created item",false,prePopList1);
		Item created=new Item(3L,"Newly created item",false,prePopList1);
		//rules
		when(this.repo.save(toCreate)).thenReturn(created);
		//actions
		
		//assertions
		assertThat(this.service.create(toCreate)).isEqualTo(this.mapToIDto(created));
		verify(this.repo,times(1)).save(toCreate);
	}
	
	@Test
	public void testReadAll() throws Exception{
		when(this.repo.findAll()).thenReturn(prePopItemsPojo);
		assertThat(this.service.readAll()).isEqualTo(prePopItems);
		verify(this.repo,times(1)).findAll();
	}
	
	@Test
	public void testReadById() throws Exception{
		Long id=1L;
		when(this.repo.findById(id)).thenReturn(Optional.of(prePopItem1));
		assertThat(this.service.readById(id)).isEqualTo(prePopItem1AsDto);
		verify(this.repo,times(1)).findById(id);
		
	}
	
	@Test
	public void testUpdate() throws Exception{
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
		when(this.repo.findItemsInList(listId)).thenReturn(prePopItemsPojo);
		//
		assertThat(this.service.findItemsInList(listId)).isEqualTo(prePopItems);
		verify(this.repo,times(1)).findItemsInList(listId);
	}
}
