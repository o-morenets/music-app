# Music App - Microservices Architecture

A microservices-based music management system built with Spring Boot, allowing MP3 file uploads with automatic metadata extraction.

## 🏗️ Architecture

### Services
- **Discovery Service** (Eureka) - Service registry on port `8761`
- **Gateway Service** - API Gateway on port `8080`
- **Resource Service** - Handles MP3 file storage on port `8081`
- **Song Service** - Manages song metadata on port `8082`

### Databases
- **resources_db** - PostgreSQL on port `5433` (stores MP3 binary data)
- **songs_db** - PostgreSQL on port `5434` (stores song metadata)

### Communication
- Services communicate via **Feign Client**
- All services register with **Eureka Discovery**
- External access through **API Gateway**

## 🚀 Quick Start

### Prerequisites
- Docker & Docker Compose
- Java 17+ (for local development)
- Maven 3.8+ (for local development)

### Running with Docker

1. **Start all services:**
   ```bash
   ENV=docker POSTGRES_DB=musicdb POSTGRES_USER=postgres POSTGRES_PASSWORD=postgres docker compose up -d --build
   ```

2. **Check services status:**
   ```bash
   docker compose ps
   ```

3. **View logs:**
   ```bash
   docker compose logs -f resource-service
   docker compose logs -f song-service
   ```

4. **Stop all services:**
   ```bash
   docker compose down
   ```

### Service URLs

| Service | URL | Description |
|---------|-----|-------------|
| Eureka Dashboard | http://localhost:8761 | Service registry UI |
| API Gateway | http://localhost:8080 | Main entry point |
| Resource Service | http://localhost:8081 | Direct access (optional) |
| Song Service | http://localhost:8082 | Direct access (optional) |

## 📡 API Endpoints

### Through Gateway (Recommended)

#### Resource Service
```bash
# Upload MP3 file (auto-extracts metadata and saves to Song Service)
POST   http://localhost:8080/resources
Content-Type: multipart/form-data
Body: file=<mp3-file>

# Get MP3 file by ID
GET    http://localhost:8080/resources/{id}
Response: audio/mpeg

# Delete resources by IDs
DELETE http://localhost:8080/resources?id=1,2,3
Response: [1, 2, 3]
```

#### Song Service
```bash
# Create song metadata
POST   http://localhost:8080/songs
Content-Type: application/json
Body: {
  "resourceId": 1,
  "name": "Song Title",
  "artist": "Artist Name",
  "album": "Album Name",
  "length": "00:03:45",
  "year": "2024"
}

# Get song metadata by resource ID
GET    http://localhost:8080/songs/{id}
Response: {
  "resourceId": 1,
  "name": "Song Title",
  "artist": "Artist Name",
  ...
}

# Delete songs by IDs
DELETE http://localhost:8080/songs?id=1,2,3
Response: [1, 2, 3]
```

### Direct Service Access (Alternative)

Replace `8080` with `8081` (resource-service) or `8082` (song-service) in the URLs above.

## 🔄 Upload Flow

```
1. Client uploads MP3 → Resource Service (:8081)
2. Resource Service stores binary data in resources_db
3. Resource Service extracts metadata (using Apache Tika)
4. Resource Service sends metadata → Song Service (via Feign)
5. Song Service stores metadata in songs_db
6. Returns resource ID to client
```

## 🧪 Testing with cURL

### Upload an MP3 file
```bash
curl -X POST http://localhost:8080/resources \
  -F "file=@/path/to/song.mp3" \
  -H "Content-Type: multipart/form-data"
```

### Get MP3 file
```bash
curl http://localhost:8080/resources/1 --output downloaded.mp3
```

### Get song metadata
```bash
curl http://localhost:8080/songs/1
```

### Delete resources
```bash
curl -X DELETE "http://localhost:8080/resources?id=1,2,3"
```

## 🛠️ Technology Stack

- **Java 17** - Programming language
- **Spring Boot 3.4.4** - Application framework
- **Spring Cloud 2024.0.0** - Microservices toolkit
  - Netflix Eureka - Service discovery
  - Spring Cloud Gateway - API gateway
  - OpenFeign - HTTP client
- **PostgreSQL** - Relational database
- **Apache Tika** - MP3 metadata extraction
- **Docker** - Containerization
- **Maven** - Build tool

## 📁 Project Structure

```
music-app/
├── discovery-service/     # Eureka server
├── gateway-service/       # API Gateway
├── resource-service/      # MP3 file management
│   ├── src/main/resources/
│   │   ├── application.yml
│   │   ├── application-docker.yml
│   │   └── application-local.yml
│   └── Dockerfile
├── song-service/         # Song metadata management
│   ├── src/main/resources/
│   │   ├── application.yml
│   │   ├── application-docker.yml
│   │   └── application-local.yml
│   └── Dockerfile
└── docker-compose.yml    # Docker orchestration
```

## 🔧 Configuration Profiles

- **local** - For local development (services connect to local databases)
- **docker** - For Docker deployment (services connect via container names)

## ⚠️ Important Notes

1. **File Storage**: Currently stores MP3 files as BLOBs in PostgreSQL (not recommended for production)
2. **Cascade Delete**: Deleting from resource-service doesn't automatically delete from song-service
3. **Synchronous Communication**: Upload is synchronous; if song-service fails, upload fails
4. **No Pagination**: GET all endpoints not implemented

## 📝 Environment Variables

```bash
ENV=docker                    # Profile: docker or local
POSTGRES_DB=musicdb          # Database name
POSTGRES_USER=postgres       # Database user
POSTGRES_PASSWORD=postgres   # Database password
```

## 🐛 Troubleshooting

### Services not registering with Eureka
```bash
# Check discovery service logs
docker logs discovery-service

# Verify Eureka is running
curl http://localhost:8761/eureka/apps
```

### "Socket hang up" errors
- Wait 30 seconds for services to fully start
- Check service logs: `docker logs <service-name>`
- Verify all services are UP: `docker compose ps`

### Database connection issues
```bash
# Check database containers
docker compose logs resources_db
docker compose logs songs_db

# Verify environment variables are set
echo $POSTGRES_DB $POSTGRES_USER $POSTGRES_PASSWORD
```

## 📈 Scaling (Future)

The architecture supports horizontal scaling:
```bash
# Scale services (remove container_name from docker-compose first)
docker compose up -d --scale resource-service=3 --scale song-service=3
```

## 📄 License

MIT

---

**Note**: This is a learning/demo project. For production use, consider implementing:
- Object storage (S3/MinIO) instead of database BLOBs
- Async communication with message broker (RabbitMQ/Kafka)
- Proper cascade delete handling
- Monitoring and health checks
- Security (OAuth2/JWT)
