package com.example.resourceservice.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Song {

	private Long resourceId;

	private String name;

	private String artist;

	private String album;

	private String length;

	private String year;
}
