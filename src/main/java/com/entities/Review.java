package com.entities;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;

import lombok.Data;

@Entity
@Data
public class Review {

	@Id 
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private Integer rating;
	
	private String comment;
	
	private LocalDateTime createdAt;
	
	@PrePersist
	protected void onCreate() {
		createdAt = LocalDateTime.now();
	}
	
}
