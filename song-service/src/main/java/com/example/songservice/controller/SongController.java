package com.example.songservice.controller;

import com.example.songservice.entity.Song;
import com.example.songservice.exception.Mp3ValidationException;
import com.example.songservice.exception.ResourceNotFoundException;
import com.example.songservice.service.SongService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/songs")
@RequiredArgsConstructor
public class SongController {

	private final SongService songService;

	@PostMapping
	public ResponseEntity<String> create(@RequestBody Song song) {
		try {
			Song savedSong = songService.save(song);
			return new ResponseEntity<>(String.format("{\"id\":%d}", savedSong.getResourceId()), HttpStatus.OK);
		} catch (Mp3ValidationException e) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("/{id}")
	public ResponseEntity<Song> findById(@PathVariable Long id) {
		try {
			Song song = songService.findById(id);
			return new ResponseEntity<>(song, HttpStatus.OK);
		} catch (ResourceNotFoundException e) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@DeleteMapping
	public ResponseEntity<List<Long>> deleteAllById(@RequestParam List<Long> id) {
		try {
			List<Long> deletedIds = songService.deleteAllById(id);
			return new ResponseEntity<>(deletedIds, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}
