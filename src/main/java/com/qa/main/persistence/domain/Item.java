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
	private boolean isDone;
	
//	private String itemURL;
//	private String description;
//	@NotNull
//	private enum category {
//		beverages, bakery, canned, confectionary, dairy, fresh_meat, frozen, other, pantry, produce
//	};

	@ManyToOne
	private ToDoList tdList=null;

public Item(Long id, @NotNull String name, @NotNull boolean isDone, ToDoList tdList) {
	super();
	this.id = id;
	this.name = name;
	this.isDone = isDone;
	this.tdList = tdList;
}

public Item(@NotNull String name, @NotNull boolean isDone, ToDoList tdList) {
	super();
	this.name = name;
	this.isDone = isDone;
	this.tdList = tdList;
}

	
}
