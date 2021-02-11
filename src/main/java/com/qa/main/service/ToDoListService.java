package com.qa.main.service;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.qa.main.dto.ToDoListDto;
import com.qa.main.persistence.domain.ToDoList;
import com.qa.main.persistence.repo.ToDoListRepo;
import com.qa.main.utils.SpringBeanUtil;

@Service
public class ToDoListService {
	private ToDoListRepo repo;

	private ModelMapper mapper;

	private ToDoListDto mapToTDLDto(ToDoList tdList) {
		return this.mapper.map(tdList, ToDoListDto.class);
	}

	@Autowired
	public ToDoListService(ToDoListRepo repo, ModelMapper mapper) {
		super();
		this.repo = repo;
		this.mapper = mapper;
	}

	public ToDoListDto create(ToDoList tdList) {
		return this.mapToTDLDto(this.repo.save(tdList));
	}

	public List<ToDoListDto> readAll() {
		return this.repo.findAll().stream().map(this::mapToTDLDto).collect(Collectors.toList());
	}

	public ToDoListDto readById(Long id) {
		return this.mapToTDLDto(this.repo.findById(id).orElseThrow());
	}
	
	public ToDoListDto update(ToDoListDto toDoListDto, Long id) {
		ToDoList calledTDL = this.repo.findById(id).orElseThrow();
		calledTDL.setName(calledTDL.getName());
		SpringBeanUtil.mergeNotNull(toDoListDto, calledTDL);
		return this.mapToTDLDto(this.repo.save(calledTDL));
	}
	
	public boolean delete(Long id) {
		this.repo.deleteById(id);
		return !this.repo.existsById(id);
	}

}
