package com.qa.main.persistence.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

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


	@ManyToOne
	private ToDoList tdList = null;

	public Item(String name, boolean done, ToDoList tdList) {
		super();
		this.name = name;
		this.done = done;
		this.tdList = tdList;
	}

	public Item(String name, boolean done) {
		super();
		this.name=name;
		this.done=done;
	}

}
