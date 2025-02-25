# IT Support Ticket System

## Description

The IT Support Ticket System is a simple ticket management application that allows employees to report and track IT issues. Key features include:

- **Ticket Creation:** Employees can create tickets with a title, description, priority (Low, Medium, High), and category (Network, Hardware, Software, Other). The creation date is set automatically and the status defaults to "New".
- **Status Tracking:** Tickets have statuses such as New, In Progress, and Resolved. IT Support personnel can update the status.
- **User Roles:**
    - **Employees:** Can create and view their own tickets.
    - **IT Support:** Can view all tickets, change statuses, and add comments.
- **Audit Log:** All changes to ticket status and added comments are recorded in an audit log.
- **Search & Filter:** The API provides endpoints to search by ticket ID and filter by status.
- **API Documentation:** The API is automatically documented using Swagger/OpenAPI, which can be accessed via the Swagger UI.

## Technologies

- Java 17
- Spring Boot 3.4.3
- Spring Security
- Spring Data JPA
- Oracle Database (production configuration in `application.properties`)
- H2 Database (test configuration in `application-test.properties`)
- Swagger/OpenAPI (via `springdoc-openapi-starter-webmvc-ui`)
- JUnit & Mockito for testing

## Project Structure
it-support-ticket/
├── src/
│   ├── main/
│   │   ├── java/com/example/itsupportticket/
│   │   │   ├── config/         # Configuration (ex: SecurityConfig)
│   │   │   ├── controller/     # Contrôleurs REST (Ticket, User, Category)
│   │   │   ├── entity/         # Entités (Ticket, User, Category, AuditLog, etc.)
│   │   │   ├── enums/          # Enums (Priority, Role, Status)
│   │   │   ├── repository/     # Repositories (TicketRepository, UserRepository, etc.)
│   │   │   └── service/        # Services (TicketService, AuditLogService, etc.)
│   │   └── resources/
│   │       └── application.properties   # Configuration Oracle pour la production
│   └── test/
│       └── java/com/example/itsupportticket/   # Tests unitaires et d'intégration
│       └── resources/
│           └── application-test.properties   # Configuration H2 pour les tests
├── database/
│   └── schema-oracle.sql     # Script SQL pour créer le schéma Oracle
├── pom.xml
└── README.md

## Configuration

### Oracle (Production)

In `src/main/resources/application.properties`, configure your Oracle connection:

```properties
# DATABASE CONFIGURATION
spring.datasource.url=jdbc:oracle:thin:@localhost:1521/XEPDB1
spring.datasource.username=ticket_admin
spring.datasource.password=M3hdi@2025!
spring.datasource.driver-class-name=oracle.jdbc.OracleDriver

# Connection Pool (HikariCP)
spring.datasource.hikari.maximum-pool-size=10
spring.datasource.hikari.minimum-idle=2
spring.datasource.hikari.idle-timeout=30000
spring.datasource.hikari.max-lifetime=1800000
spring.datasource.hikari.connection-timeout=30000
spring.datasource.hikari.leak-detection-threshold=5000

# Hibernate Configuration
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.open-in-view=false
spring.jpa.properties.hibernate.format_sql=true
spring.jpa.properties.hibernate.connection.provider_disables_autocommit=false
spring.jpa.properties.hibernate.jdbc.lob.non_contextual_creation=true

# Logging Configuration
logging.level.org.hibernate.SQL=DEBUG
logging.level.org.hibernate.type.descriptor.sql.BasicBinder=TRACE
logging.level.com.zaxxer.hikari=DEBUG
logging.level.org.springframework.jdbc.core.JdbcTemplate=DEBUG

# Spring Security (example for basic mode)
spring.security.user.name=admin
spring.security.user.password=admin123
H2 (Testing)
In src/test/resources/application-test.properties, configure H2 as follows:
spring.datasource.url=jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE;MODE=Oracle;INIT=CREATE SCHEMA IF NOT EXISTS TICKET_ADMIN;SET SCHEMA TICKET_ADMIN;
spring.datasource.driverClassName=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=password

spring.jpa.database-platform=org.hibernate.dialect.H2Dialect
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true
```

API Documentation

The API is automatically documented using Swagger/OpenAPI. Once the application is running, you can access the Swagger UI at:

http://localhost:8080/swagger-ui/index.html

This interface provides details on all available endpoints and allows you to test them interactively.

Schema Creation

A SQL script is provided in the database folder to create the Oracle schema. The file database/schema-oracle.sql contains the following:
-- Create sequence for TICKETS (if using sequence strategy)

CREATE SEQUENCE TICKET_SEQ START WITH 1 INCREMENT BY 1 NOCACHE;

-- Table USERS
CREATE TABLE USERS (
    ID NUMBER PRIMARY KEY,
    USERNAME VARCHAR2(255) NOT NULL UNIQUE,
    ROLE VARCHAR2(255)
);

-- Table CATEGORIES
CREATE TABLE CATEGORIES (
    ID NUMBER PRIMARY KEY,
    CATEGORY_NAME VARCHAR2(255) NOT NULL UNIQUE
);

-- Table TICKETS
CREATE TABLE TICKETS (
    ID NUMBER PRIMARY KEY,
    TITLE VARCHAR2(255) NOT NULL,
    DESCRIPTION CLOB NOT NULL,
    PRIORITY VARCHAR2(255),
    CATEGORY VARCHAR2(255),
    STATUS VARCHAR2(255),
    CREATED_AT TIMESTAMP NOT NULL,
    USER_ID NUMBER,
    CATEGORY_ID NUMBER,
    CONSTRAINT FK_TICKET_USER FOREIGN KEY (USER_ID) REFERENCES USERS(ID),
    CONSTRAINT FK_TICKET_CATEGORY FOREIGN KEY (CATEGORY_ID) REFERENCES CATEGORIES(ID)
);

-- Table AUDIT_LOG
CREATE TABLE AUDIT_LOG (
    ID NUMBER PRIMARY KEY,
    ACTION VARCHAR2(255) NOT NULL,
    ENTITY VARCHAR2(255) NOT NULL,
    ENTITY_ID NUMBER NOT NULL,
    DESCRIPTION VARCHAR2(255) NOT NULL,
    TIMESTAMP TIMESTAMP NOT NULL,
    DETAILS CLOB,
    TICKET_ID NUMBER NOT NULL,
    USER_ID NUMBER NOT NULL,
    CONSTRAINT FK_AUDIT_TICKET FOREIGN KEY (TICKET_ID) REFERENCES TICKETS(ID),
    CONSTRAINT FK_AUDIT_USER FOREIGN KEY (USER_ID) REFERENCES USERS(ID)
);

-- Table TICKET_COMMENTS
CREATE TABLE TICKET_COMMENTS (
    ID NUMBER PRIMARY KEY,
    TICKET_ID NUMBER,
    COMMENT_TEXT CLOB NOT NULL,
    CREATED_AT TIMESTAMP,
    CONSTRAINT FK_COMMENT_TICKET FOREIGN KEY (TICKET_ID) REFERENCES TICKETS(ID)
);
To execute this script in Oracle, connect to your database (for example, using SQL*Plus) and run:
@/path/to/database/schema-oracle.sql
Running the Application
Build and Run
Build the application:
mvn clean install
Run the application:
java -jar target/it-support-ticket-0.0.1-SNAPSHOT.jar
Access the API Documentation:
Open your browser and navigate to:
http://localhost:8080/swagger-ui/index.html

Testing
Run the tests using:
mvn test
Security
Spring Security is configured using HTTP Basic authentication. The in-memory users are:

admin/admin123 (role: ADMIN)
employee/employee123 (role: EMPLOYEE)
Endpoints for Swagger (/swagger-ui/**, /v3/api-docs/**, /swagger-ui.html) are accessible without authentication.


###Docker Setup
Prerequisites
Assure-toi d'avoir Docker et Docker Compose installés sur ta machine.

Build and Run Containers
Pour construire et démarrer les conteneurs de l'application et de la base de données Oracle, exécute la commande suivante :
docker-compose up --build
Stopping Containers
Pour arrêter les conteneurs en cours d'exécution :
docker-compose down
Database Access Inside Docker
Pour te connecter à la base de données Oracle à l'intérieur du conteneur :
docker exec -it oracle-xe sqlplus system/oracle@//localhost:1521/XEPDB1

Configuration

Dans docker-compose.yml, les services sont configurés comme suit :
version: '3.8'
services:
  backend:
    build: .
    container_name: it-support-backend
    ports:
      - "8080:8080"
    environment:
      - SPRING_PROFILES_ACTIVE=prod
    depends_on:
      - oracledb

  oracledb:
    image: gvenzl/oracle-xe:21-slim
    container_name: oracle-xe
    environment:
      - ORACLE_PASSWORD=oracle
    ports:
      - "1521:1521"
    volumes:
      - oracle-data:/opt/oracle/oradata

volumes:
  oracle-data:
