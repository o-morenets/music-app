resource-service (/resources)
  - POST: upload (saves mp3 metadata in songs-db on song-service using FeignClient)
  - GET: findById (/{id})
  - GET: deleteAllById
song-service (/songs)
  - POST: create
  - GET: findById (/{id})
  - GET: deleteAllById
