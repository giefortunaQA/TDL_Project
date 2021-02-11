package com.qa.main.service;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.qa.main.dto.ToDoListDto;
import com.qa.main.persistence.domain.ToDoList;
import com.qa.main.persistence.repo.ToDoListRepo;

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

}
