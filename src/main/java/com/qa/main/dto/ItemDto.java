package com.qa.main.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ItemDto {
	private Long id;
	private String name;
	private boolean isDone;
}
