# Patient Management

Patient Management is a Spring Boot microservices project for managing patients and creating billing accounts over gRPC.

## Services

| Service | Description | HTTP Port | gRPC Port |
| --- | --- | --- | --- |
| `patient-service` | REST API for creating, reading, updating, and deleting patients. Calls the billing service when a patient is created. | `4000` | N/A |
| `billing-service` | gRPC service that creates a billing account for a patient. | `4001` | `9001` |

## Tech Stack

- Java 21
- Spring Boot
- Spring Web MVC
- Spring Data JPA
- PostgreSQL
- H2 for local in-memory development
- gRPC and Protocol Buffers
- Maven
- Docker
- Springdoc OpenAPI

## Project Structure

```text
.
├── api-request/              # REST request examples
├── grpc-requests/            # gRPC request examples
├── billing-service/          # Billing gRPC service
└── patient-service/          # Patient REST API service
```

## Prerequisites

- Java 21
- Docker, optional
- PostgreSQL, unless using the commented H2 configuration in `patient-service/src/main/resources/application.properties`

## Running Locally

Start the billing service first because the patient service calls it when creating a patient.

```bash
cd billing-service
./mvnw spring-boot:run
```

In a second terminal, start the patient service:

```bash
cd patient-service
./mvnw spring-boot:run
```

The patient API will be available at:

```text
http://localhost:4000
```

The billing gRPC service will be available at:

```text
localhost:9001
```

## Database Configuration

`patient-service` is configured for PostgreSQL by default:

```properties
spring.jpa.database-platform=org.hibernate.dialect.PostgreSQLDialect
server.port=4000
```

Provide your datasource settings using environment variables or properties, for example:

```bash
export SPRING_DATASOURCE_URL=jdbc:postgresql://localhost:5432/patient_management
export SPRING_DATASOURCE_USERNAME=postgres
export SPRING_DATASOURCE_PASSWORD=password
```

For local in-memory development, uncomment the H2 configuration in:

```text
patient-service/src/main/resources/application.properties
```

## API Endpoints

Base URL:

```text
http://localhost:4000
```

| Method | Endpoint | Description |
| --- | --- | --- |
| `GET` | `/patients` | Get all patients |
| `POST` | `/patients` | Create a new patient |
| `PUT` | `/patients/{id}` | Update an existing patient |
| `DELETE` | `/patients/{id}` | Delete a patient |

Example create request:

```http
POST http://localhost:4000/patients
Content-Type: application/json

{
  "name": "John Dore Billing",
  "email": "billings@gmail.com",
  "address": "123 Geekie Road, Merrivale, 3291",
  "dateOfBirth": "1990-04-22",
  "registeredDate": "2026-05-18"
}
```

More request examples are available in:

```text
api-request/patient-service/
```

## OpenAPI Documentation

When `patient-service` is running, the Swagger UI should be available at:

```text
http://localhost:4000/swagger-ui.html
```

## gRPC

Both services use the same protobuf contract:

```text
billing-service/src/main/proto/billing_service.proto
patient-service/src/main/proto/billing_service.proto
```

The patient service connects to the billing service using these defaults:

```properties
billing.service.address=localhost
billing.service.port=9001
```

Override them with environment variables if needed:

```bash
export BILLING_SERVICE_ADDRESS=localhost
export BILLING_SERVICE_PORT=9001
```

## Build and Test

Build the billing service:

```bash
cd billing-service
./mvnw clean package
```

Build the patient service:

```bash
cd patient-service
./mvnw clean package
```

Run tests:

```bash
cd billing-service
./mvnw test
```

```bash
cd patient-service
./mvnw test
```

## Docker

Build the billing service image:

```bash
docker build -t billing-service ./billing-service
```

Build the patient service image:

```bash
docker build -t patient-service ./patient-service
```

Run the billing service:

```bash
docker run --rm -p 4001:4001 -p 9001:9001 billing-service
```

Run the patient service:

```bash
docker run --rm -p 4000:4000 \
  -e BILLING_SERVICE_ADDRESS=host.docker.internal \
  -e BILLING_SERVICE_PORT=9001 \
  patient-service
```

If running on Linux, replace `host.docker.internal` with the reachable hostname or IP address for the billing service.
