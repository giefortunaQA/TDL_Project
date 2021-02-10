package com.qa.main.persistence.domain;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
public class GroceryList {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@NotNull
	private String name;
	
	private String description;
	private Double value;
	
	@OneToMany(mappedBy="grocery",fetch=FetchType.EAGER)
	@OnDelete(action=OnDeleteAction.CASCADE)
	private List<Item> items;

	public GroceryList(Long id, @NotNull String name, String description, Double value, List<Item> items) {
		super();
		this.id = id;
		this.name = name;
		this.description = description;
		this.value = value;
		this.items = items;
	}

	public GroceryList(@NotNull String name, String description, Double value, List<Item> items) {
		super();
		this.name = name;
		this.description = description;
		this.value = value;
		this.items = items;
	}
	
	
	


	
}
