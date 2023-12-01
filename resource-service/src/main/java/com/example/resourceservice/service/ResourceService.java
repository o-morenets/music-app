package com.example.resourceservice.service;

import com.example.resourceservice.client.SongClient;
import com.example.resourceservice.entity.Resource;
import com.example.resourceservice.entity.Song;
import com.example.resourceservice.exception.Mp3ValidationException;
import com.example.resourceservice.exception.ResourceNotFoundException;
import com.example.resourceservice.repository.ResourceRepository;
import lombok.RequiredArgsConstructor;
import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.mp3.Mp3Parser;
import org.apache.tika.sax.BodyContentHandler;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.InputStream;
import java.time.Duration;
import java.util.List;
import java.util.TimeZone;

@Service
@RequiredArgsConstructor
public class ResourceService {

	private final ResourceRepository resourceRepository;
	private final SongClient songClient;

	public Resource save(MultipartFile file) throws TikaException, IOException, SAXException {
		if (file == null || file.getContentType() == null || !file.getContentType().equalsIgnoreCase("audio/mpeg")) {
			throw new Mp3ValidationException("Validation failed or request body is invalid MP3");
		}

		Resource resource = new Resource();
		resource.setData(file.getBytes());
		Resource savedResource = resourceRepository.save(resource);

		songClient.saveSong(buildSong(file, savedResource.getId()));

		return savedResource;
	}

	private Song buildSong(MultipartFile file, Long id) throws IOException, TikaException, SAXException {

		//detecting the file type
		BodyContentHandler handler = new BodyContentHandler();
		Metadata metadata = new Metadata();
		InputStream inputStream = file.getInputStream();
		ParseContext pContext = new ParseContext();

		//Mp3 parser
		Mp3Parser mp3Parser = new Mp3Parser();
		mp3Parser.parse(inputStream, handler, metadata, pContext);

		return Song.builder()
				.resourceId(id)
				.name(metadata.get("dc:title"))
				.artist(metadata.get("xmpDM:artist"))
				.album(metadata.get("xmpDM:album"))
				.length(formatDuration(metadata.get("xmpDM:duration")))
				.year(metadata.get("xmpDM:releaseDate"))
				.build();
	}

	private String formatDuration(String seconds) {
		Duration d = Duration.ofSeconds(Math.round(Float.parseFloat(seconds)));
		return String.format("%tT", d.getSeconds() * 1000 - TimeZone.getDefault().getRawOffset());
	}

	public Resource findById(Long id) {
		return resourceRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("The resource with the specified id does not exist"));
	}

	public List<Long> deleteAllById(List<Long> id) {
		resourceRepository.deleteAllById(id);
		return id;
	}
}
