package com.example.resourceservice.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Resource {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@Lob
	private byte[] data;
}

