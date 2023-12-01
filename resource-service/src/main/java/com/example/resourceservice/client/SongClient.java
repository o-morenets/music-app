package com.example.resourceservice.client;

import com.example.resourceservice.entity.Song;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient("${music-app.service.songs.url}")
public interface SongClient {

	@PostMapping()
	void saveSong(@RequestBody Song song);
}
