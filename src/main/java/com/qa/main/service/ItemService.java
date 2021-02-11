package com.qa.main.service;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.qa.main.dto.ItemDto;
import com.qa.main.persistence.domain.Item;
import com.qa.main.persistence.repo.ItemRepo;

@Service
public class ItemService {
	private ItemRepo repo;

	private ModelMapper mapper;

	private ItemDto mapToIDto(Item item) {
		return this.mapper.map(item, ItemDto.class);
	}

	@Autowired
	public ItemService(ItemRepo repo, ModelMapper mapper) {
		super();
		this.repo = repo;
		this.mapper = mapper;
	}
	
	public ItemDto create(Item item) {
		return this.mapToIDto(this.repo.save(item));
	}
	
	
	public List<ItemDto> readAll() {
		return this.repo.findAll().stream().map(this::mapToIDto).collect(Collectors.toList());
	}
	
	public ItemDto readById(Long id) {
		return this.mapToIDto(this.repo.findById(id).orElseThrow());
	}
	
}