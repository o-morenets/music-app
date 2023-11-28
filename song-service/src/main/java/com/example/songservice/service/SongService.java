package com.example.songservice.service;

import com.example.songservice.entity.Song;
import com.example.songservice.exception.Mp3ValidationException;
import com.example.songservice.exception.ResourceNotFoundException;
import com.example.songservice.repository.SongRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SongService {

	private final SongRepository songRepository;

	public Song save(Song song) {
		if (song == null) {
			throw new Mp3ValidationException("Song metadata missing validation error");
		}
		return songRepository.save(song);
	}

	public Song findById(Long id) {
		return songRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("The song metadata with the specified id does not exist"));
	}

	public List<Long> deleteAllById(List<Long> id) {
		songRepository.deleteAllById(id);
		return id;
	}
}
