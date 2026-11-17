# REST API Module - Sample Requests & Documentation

## How to Run

Run the application:
```bash
cd jmp-service-rest
mvn spring-boot:run
```

Or from `task6-rest-api`:
```bash
mvn clean install
mvn -pl jmp-service-rest spring-boot:run
```

- Application URL: `http://localhost:8080`
- Swagger UI: `http://localhost:8080/swagger-ui.html`
- OpenAPI Docs: `http://localhost:8080/api-docs`
- H2 Console: `http://localhost:8080/h2-console` (JDBC URL: `jdbc:h2:mem:testdb`, user: `sa`, password: empty)

---

## 1. UserController Sample Requests & Responses

### 1.1 Create User (POST)
**Request:**
```bash
curl -X POST http://localhost:8080/api/users \
  -H "Content-Type: application/json" \
  -d '{
    "name": "John",
    "surname": "Doe",
    "birthday": "1990-05-15"
  }'
```

**Response (HTTP 201 Created):**
```json
{
  "id": 1,
  "name": "John",
  "surname": "Doe",
  "birthday": "1990-05-15",
  "_links": {
    "self": {
      "href": "http://localhost:8080/api/users/1"
    },
    "users": {
      "href": "http://localhost:8080/api/users"
    }
  }
}
```

### 1.2 Get User by ID (GET)
**Request:**
```bash
curl -X GET http://localhost:8080/api/users/1
```

**Response (HTTP 200 OK):**
```json
{
  "id": 1,
  "name": "John",
  "surname": "Doe",
  "birthday": "1990-05-15",
  "_links": {
    "self": {
      "href": "http://localhost:8080/api/users/1"
    },
    "users": {
      "href": "http://localhost:8080/api/users"
    }
  }
}
```

### 1.3 Update User (PUT)
**Request:**
```bash
curl -X PUT http://localhost:8080/api/users \
  -H "Content-Type: application/json" \
  -d '{
    "id": 1,
    "name": "John",
    "surname": "Smith",
    "birthday": "1990-05-15"
  }'
```

**Response (HTTP 200 OK):**
```json
{
  "id": 1,
  "name": "John",
  "surname": "Smith",
  "birthday": "1990-05-15",
  "_links": {
    "self": {
      "href": "http://localhost:8080/api/users/1"
    },
    "users": {
      "href": "http://localhost:8080/api/users"
    }
  }
}
```

### 1.4 Get All Users (GET)
**Request:**
```bash
curl -X GET http://localhost:8080/api/users
```

**Response (HTTP 200 OK):**
```json
[
  {
    "id": 1,
    "name": "John",
    "surname": "Smith",
    "birthday": "1990-05-15",
    "_links": {
      "self": {
        "href": "http://localhost:8080/api/users/1"
      }
    }
  }
]
```

### 1.5 Delete User (DELETE)
**Request:**
```bash
curl -X DELETE http://localhost:8080/api/users/1
```

**Response (HTTP 204 No Content)**

---

## 2. ServiceController Sample Requests & Responses

### 2.1 Create Subscription (POST)
**Request:**
```bash
curl -X POST http://localhost:8080/api/subscriptions \
  -H "Content-Type: application/json" \
  -d '{
    "userId": 1
  }'
```

**Response (HTTP 201 Created):**
```json
{
  "id": 1,
  "userId": 1,
  "startDate": "2024-03-20",
  "_links": {
    "self": {
      "href": "http://localhost:8080/api/subscriptions/1"
    },
    "subscriptions": {
      "href": "http://localhost:8080/api/subscriptions"
    },
    "user": {
      "href": "http://localhost:8080/api/users/1"
    }
  }
}
```

### 2.2 Get Subscription by ID (GET)
**Request:**
```bash
curl -X GET http://localhost:8080/api/subscriptions/1
```

**Response (HTTP 200 OK):**
```json
{
  "id": 1,
  "userId": 1,
  "startDate": "2024-03-20",
  "_links": {
    "self": {
      "href": "http://localhost:8080/api/subscriptions/1"
    },
    "subscriptions": {
      "href": "http://localhost:8080/api/subscriptions"
    },
    "user": {
      "href": "http://localhost:8080/api/users/1"
    }
  }
}
```

### 2.3 Update Subscription (PUT)
**Request:**
```bash
curl -X PUT http://localhost:8080/api/subscriptions \
  -H "Content-Type: application/json" \
  -d '{
    "id": 1,
    "userId": 1
  }'
```

**Response (HTTP 200 OK):**
```json
{
  "id": 1,
  "userId": 1,
  "startDate": "2024-03-20",
  "_links": {
    "self": {
      "href": "http://localhost:8080/api/subscriptions/1"
    },
    "subscriptions": {
      "href": "http://localhost:8080/api/subscriptions"
    },
    "user": {
      "href": "http://localhost:8080/api/users/1"
    }
  }
}
```

### 2.4 Get All Subscriptions (GET)
**Request:**
```bash
curl -X GET http://localhost:8080/api/subscriptions
```

**Response (HTTP 200 OK):**
```json
[
  {
    "id": 1,
    "userId": 1,
    "startDate": "2024-03-20",
    "_links": {
      "self": {
        "href": "http://localhost:8080/api/subscriptions/1"
      },
      "user": {
        "href": "http://localhost:8080/api/users/1"
      }
    }
  }
]
```

### 2.5 Delete Subscription (DELETE)
**Request:**
```bash
curl -X DELETE http://localhost:8080/api/subscriptions/1
```

**Response (HTTP 204 No Content)**

---

## 3. Richardson Maturity Model Implementation

- **Level 0 (The Swamp of POX):** Single URI endpoint using HTTP for RPC.
- **Level 1 (Resources):** Distinct URIs for individual resources (`/api/users`, `/api/subscriptions`, `/api/users/{id}`).
- **Level 2 (HTTP Verbs & Status Codes):** Proper use of standard HTTP methods (`GET`, `POST`, `PUT`, `DELETE`) with appropriate response codes (`200 OK`, `201 Created`, `204 No Content`, `404 Not Found`).
- **Level 3 (Hypermedia Controls / HATEOAS):** Responses include hypermedia links (`_links` with `self`, collection, and related resource links) using Spring HATEOAS `EntityModel`.
