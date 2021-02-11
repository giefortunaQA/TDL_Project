package com.qa.main.persistence.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import com.sun.istack.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Item {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@NotNull
	private String name;
	@NotNull
	private boolean done;

//	private String itemURL;
//	private String description;
//	@NotNull
//	private enum category {
//		beverages, bakery, canned, confectionary, dairy, fresh_meat, frozen, other, pantry, produce
//	};

	@ManyToOne
	private ToDoList tdList = null;

	public Item(String name, boolean done, ToDoList tdList) {
		super();
		this.name = name;
		this.done = done;
		this.tdList = tdList;
	}


}
