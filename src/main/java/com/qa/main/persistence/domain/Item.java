package com.qa.main.persistence.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
public class Item {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@NotNull
	private String name;
	@NotNull
	private int qty;
	private String itemPhotoURL;
	private String description;
	
	public Item(Long id, @NotNull String name, @NotNull int qty, String itemPhotoURL, String description) {
		super();
		this.id = id;
		this.name = name;
		this.qty = qty;
		this.itemPhotoURL = itemPhotoURL;
		this.description = description;
	}
	public Item(@NotNull String name, @NotNull int qty, String itemPhotoURL, String description) {
		super();
		this.name = name;
		this.qty = qty;
		this.itemPhotoURL = itemPhotoURL;
		this.description = description;
	}
	
	
	

//	@NotNull
//	private enum category {
//		beverages, bakery, canned, confectionary, dairy, fresh_meat, frozen, other, pantry, produce
//	};

	@ManyToOne
	private ToDoList tdList=null;

	
}
