# Pocketly Backend

A robust Spring Boot REST API for managing links, content, and resources with JWT-based authentication.

 
*time it took to build:*
[![wakatime](https://wakatime.com/badge/user/018dc268-cd1f-405a-abd9-12f1bce09a2f/project/cc57ebdc-5068-46bc-80bc-485c73fa19c0.svg)](https://wakatime.com/badge/user/018dc268-cd1f-405a-abd9-12f1bce09a2f/project/cc57ebdc-5068-46bc-80bc-485c73fa19c0)

##  Overview

Pocketly Backend is a powerful REST API built with Spring Boot 3.5 that provides complete backend services for a second-brain bookmarking application. It handles user authentication, link management, content storage, and flexible tagging with a focus on security and scalability.
<!--
### Key Features


 - Secure token-based authentication with cookie support
 - Store and manage diverse resource types
 - Hash-based URLs for sharing collections
- Comprehensive request validation with Hibernate Validator
 - Spring Security with BCrypt password hashing
- Built-in health monitoring with Spring Actuator
-Multi-stage Dockerfile for efficient containerization-->

##  Tech Stack

- **Framework**: Spring Boot 
- **Language**: Java 21
- **Database**:  NeonDB(PostgreSQL)
- **ORM**: Spring Data JPA + Hibernate
- **Security**: Spring Security + JWT 
- **Validation**: Hibernate Validator
- **Build Tool**: Maven 3.9+
- **Utilities**: Lombok
- **Monitoring**: Spring Boot Actuator

##  Getting Started

### Prerequisites

- **Java 21** (OpenJDK or Oracle JDK)
- **Maven 3.6+**
- **PostgreSQL 14+**
- **Git**

### Installation

1. **Clone the repository**
   ```bash
   git clone <repository-url>
   cd pocketly/pocketly-be
   ```

2. **Set up environment variables**
   
   Create a `.env` file in the root directory:
   ```env
   # Database Configuration
   DB_URL=jdbc:postgresql://localhost:5432/pocketly
   DB_USER=postgres
   DB_PASSWORD=your_password
   
   # JWT Configuration
   JWT_SECRET=your_super_secret_jwt_key_at_least_32_characters_long
   JWT_EXPIRATION=86400000
   
   # Cookie Configuration
   COOKIE_SECURE=false
   SAME_SITE=Lax
   
   # Server Configuration
   PORT=8080
   ```

3. **Create PostgreSQL database**
   ```bash
   createdb pocketly
   ```

4. **Install dependencies and build**
   ```bash
   ./mvnw clean install
   ```

5. **Run the application**
   ```bash
   ./mvnw spring-boot:run
   ```

##  Configuration

### Application Properties

Located in `src/main/resources/application.properties`:

```properties
# Application
spring.application.name=pocketly-be

# Database (via .env)
spring.datasource.url=${DB_URL}
spring.datasource.username=${DB_USER}
spring.datasource.password=${DB_PASSWORD}

# JPA/Hibernate
spring.jpa.hibernate.ddl-auto=validate
spring.jpa.show-sql=false
spring.jpa.open-in-view=false

# Logging
logging.level.org.hibernate.SQL=WARN

# Server
server.address=0.0.0.0
server.port=${PORT:8080}
```
### Environment Variables

**Required:**
- `DB_URL` - PostgreSQL JDBC URL
- `DB_USER` - Database username
- `DB_PASSWORD` - Database password
- `JWT_SECRET` - Secret key for JWT signing (min 32 chars)

##  Contributing

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

##  License
pending rn

---
## Ending Note
Consider giving the repo a ⭐ if you liked the project!
Checkout my other projects!