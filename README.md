# 🎬 MovieEvaluator

A Spring Boot REST API for rating movies. Includes JWT-based authentication, integration tests, Docker Compose setup with H2, Prometheus & Grafana for monitoring.

---

## 🚀 Getting Started

### 🔧 Prerequisites
- Docker
- Java 17+
- Gradle or use provided wrapper (`./gradlew`)

---

### ▶️ Run the Full Stack

```bash
git clone https://github.com/yourname/movieevaluator.git
cd movieevaluator
./gradlew build
docker-compose up --build
```

| Endpoint                 | Method | Auth Required | Description                   |
| ------------------------ | ------ | ------------- | ----------------------------- |
| `/api/movies`            | GET    | ❌             | List all movies               |
| `/api/movies/top-rated`  | GET    | ❌             | Get top-rated movie           |
| `/api/ratings/{movieId}` | POST   | ✅             | Add/update rating for a movie |
| `/api/auth/signup`       | POST   | ❌             | Register new user             |
| `/api/auth/login`        | POST   | ❌             | Login and get JWT             |

# 🧠 What’s Inside

### Core Features
- RESTful API with proper HTTP verbs and status codes
- Movie listing and top-rated query
- JWT-based authentication
- Database integration (H2 for testing)
- Custom exception handling with global handler

### Unit tests with JUnit and AssertJ
- Integration tests using Spring Boot's @SpringBootTest and TestRestTemplate
- Coverage for:
/auth/signup,
/auth/login
- Adding/updating movie ratings with JWT

### Observability
- Metrics exposed via Micrometer
- Integrated Prometheus via docker-compose.yml
- Grafana dashboards included (on port 3000)
- Metrics endpoint: http://localhost:8080/actuator/prometheus

### Dockerization
###### Application containerized via Dockerfile
Docker Compose file includes:
- Spring Boot app
- H2 database
- Prometheus
- Grafana

# 📝 Notes from the Developer
- JWT Implementation
The authentication and authorization mechanism using JWT was implemented based on best practices outlined in a Medium article, adapted to fit the project’s architecture.

- Dockerization & Monitoring
The entire stack is containerized using Docker Compose. It includes the Spring Boot application, H2, Prometheus, and Grafana.
JVM metrics are exposed via Micrometer and integrated into Grafana dashboards through Prometheus. These metrics are accessible and visible after starting the stack.

- Testing Approach
The project contains focused unit tests covering essential business logic, along with an integration test validating JWT token generation and authentication flow. Due to time constraints, the test suite is not exhaustive.

- API Documentation
Swagger UI is enabled for easier exploration and testing of REST endpoints. You can access it at:
http://localhost:8080/swagger-ui/index.html