package com.example.resourceservice.client;

import com.example.resourceservice.entity.Song;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient("song-service")
public interface SongClient {

	@PostMapping("/songs")
	void saveSong(@RequestBody Song song);
}
