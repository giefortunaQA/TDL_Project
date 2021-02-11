package com.qa.main.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.qa.main.dto.ItemDto;
import com.qa.main.persistence.domain.Item;
import com.qa.main.service.ItemService;

@RestController
@CrossOrigin
@RequestMapping("/item")
public class ItemController {
	private ItemService service;

	@Autowired
	public ItemController(ItemService service) {
		super();
		this.service = service;
	}

	@PostMapping("/create")
	public ResponseEntity<ItemDto> create(@RequestBody Item item) {
		ItemDto created = this.service.create(item);
		return new ResponseEntity<>(created, HttpStatus.CREATED);
	}

}
