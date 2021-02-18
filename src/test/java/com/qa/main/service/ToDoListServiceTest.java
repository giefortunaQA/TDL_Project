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

import com.qa.main.dto.ToDoListDto;
import com.qa.main.persistence.domain.ToDoList;
import com.qa.main.persistence.repo.ToDoListRepo;

@SpringBootTest
@ActiveProfiles("test")
public class ToDoListServiceTest {

	@Autowired
	private ToDoListService service;
	
	@MockBean
	private ToDoListRepo repo;
	
	@MockBean
	private ModelMapper mapper;
	
	private ToDoListDto mapToTDLDto(ToDoList list) {
		return this.mapper.map(list, ToDoListDto.class);
	}
	
	// class resources
	private final ToDoList testList1=new ToDoList("Test List 1");
	private final ToDoList testList2=new ToDoList("Test List 2");
	
	
	
	
	@Test
	public void testCreate() throws Exception{
		//resources
		ToDoList toCreate=new ToDoList("Newly created list");
		ToDoList created=new ToDoList(3L,"Newly created list");
		
		//rules
		when(this.repo.save(toCreate)).thenReturn(created);
		//actions
		
		//assertions
		assertThat(this.service.create(toCreate)).isEqualTo(this.mapToTDLDto(created));
		verify(this.repo,times(1)).save(toCreate);
	}
	
	@Test
	public void testReadAll() throws Exception{
		//re
		List<ToDoList> testLists=List.of(testList1,testList2);
		List<ToDoListDto> testListsAsDtos= testLists.stream().map(this::mapToTDLDto).collect(Collectors.toList());
		//ru
		when(this.repo.findAll()).thenReturn(testLists);
		//a
		assertThat(this.service.readAll()).isEqualTo(testListsAsDtos);
		verify(this.repo,times(1)).findAll();
		}
	
	@Test
	public void testReadById() throws Exception{
		//re
		Long id=1L;
		//ru
		when(this.repo.findById(id)).thenReturn(Optional.of(testList1));
		//a
		assertThat(this.service.readById(id)).isEqualTo(this.mapToTDLDto(testList1));
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
}
