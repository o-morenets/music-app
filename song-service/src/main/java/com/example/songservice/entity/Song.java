package com.example.songservice.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;

@Entity
@Getter
public class Song {

	@Id
	private Long resourceId;

	private String name;

	private String artist;

	private String album;

	private String length;

	private String year;
}
