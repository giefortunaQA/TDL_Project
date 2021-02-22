package com.qa.main.persistence.domain;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
public class ToDoList {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@NotNull
	private String name;
	public ToDoList(Long id, @NotNull String name) {
		super();
		this.id = id;
		this.name = name;
	}
	
	public ToDoList(@NotNull String name) {
		super();
		this.name = name;
	}
	
	 @OneToMany(mappedBy="tdList",fetch=FetchType.EAGER)
	 @OnDelete(action=OnDeleteAction.CASCADE) private List<Item> items;
	 

	
	


	
}
