# WhatsApp Chatbot Backend Simulation — Spring Boot

A simple WhatsApp chatbot backend simulation built with **Java 17** and **Spring Boot 3**.  
It exposes a `POST /webhook` REST endpoint that accepts a JSON message and replies with a chatbot response.

---

## Project Structure

```
whatsapp-chatbot/
│
├── pom.xml
│
└── src/
    ├── main/
    │   ├── java/com/chatbot/whatsappbot/
    │   │   ├── WhatsappChatbotApplication.java     ← Main entry point
    │   │   ├── controller/
    │   │   │   └── WebhookController.java          ← REST API (@RestController)
    │   │   ├── service/
    │   │   │   └── ChatbotService.java             ← Business logic (@Service)
    │   │   ├── model/
    │   │   │   ├── MessageRequest.java             ← Request DTO
    │   │   │   └── MessageResponse.java            ← Response DTO
    │   │   └── exception/
    │   │       └── GlobalExceptionHandler.java     ← Validation error handler
    │   └── resources/
    │       └── application.properties              ← App + Logging config
    │
    └── test/
        └── java/com/chatbot/whatsappbot/service/
            └── ChatbotServiceTest.java             ← Unit tests
```

---

## Technologies Used

| Technology          | Purpose                                   |
|---------------------|-------------------------------------------|
| Java 17             | Programming language                      |
| Spring Boot 3.2.5   | Framework / embedded Tomcat server        |
| Spring Validation   | `@NotBlank` request validation            |
| Lombok              | Boilerplate reduction (`@Slf4j`, `@Getter`, etc.) |
| SLF4J / Logback     | Logging with timestamp                    |
| JUnit 5 + AssertJ   | Unit testing                              |

---

## How to Run

### Prerequisites
- Java 17+ installed
- Maven installed (or use the Maven wrapper `./mvnw`)

### Run the Application

```bash
# From the project root directory (where pom.xml is)
mvn spring-boot:run
```

The server starts at: **http://localhost:8080**

---

## API Reference

### `POST /webhook`

| Field     | Value              |
|-----------|--------------------|
| URL       | `http://localhost:8080/webhook` |
| Method    | POST               |
| Content-Type | `application/json` |

#### Request Body

```json
{
  "message": "Hi"
}
```

#### Response Body

```json
{
  "reply": "Hello"
}
```

#### Reply Logic

| Input Message (case-insensitive) | Reply                |
|----------------------------------|----------------------|
| `Hi`                             | `Hello`              |
| `Bye`                            | `Goodbye`            |
| Anything else                    | `I don't understand` |

---

## Testing with curl

### Test 1 — "Hi"
```bash
curl -X POST http://localhost:8080/webhook \
  -H "Content-Type: application/json" \
  -d '{"message": "Hi"}'
```
Expected: `{"reply":"Hello"}`

---

### Test 2 — "Bye"
```bash
curl -X POST http://localhost:8080/webhook \
  -H "Content-Type: application/json" \
  -d '{"message": "Bye"}'
```
Expected: `{"reply":"Goodbye"}`

---

### Test 3 — Unknown message
```bash
curl -X POST http://localhost:8080/webhook \
  -H "Content-Type: application/json" \
  -d '{"message": "What is the weather?"}'
```
Expected: `{"reply":"I don't understand"}`

---

### Test 4 — Empty message (Validation error)
```bash
curl -X POST http://localhost:8080/webhook \
  -H "Content-Type: application/json" \
  -d '{"message": ""}'
```
Expected HTTP 400:
```json
{
  "status": 400,
  "error": "Bad Request",
  "messages": ["message: Message must not be null or empty"]
}
```

---

## Testing with Postman

1. Open **Postman**
2. Create a new request:
   - **Method**: `POST`
   - **URL**: `http://localhost:8080/webhook`
3. Go to **Body** → select **raw** → set type to **JSON**
4. Enter the request body:
   ```json
   {
     "message": "Hi"
   }
   ```
5. Click **Send**
6. You should see in the response:
   ```json
   {
     "reply": "Hello"
   }
   ```

---

## Console Log Output (Sample)

When you send a message, you will see timestamped logs in the console:

```
2026-04-28 00:20:15 [http-nio-8080-exec-1] INFO  c.c.w.service.ChatbotService - [2026-04-28 00:20:15] Received message: 'Hi'
2026-04-28 00:20:15 [http-nio-8080-exec-1] INFO  c.c.w.service.ChatbotService - [2026-04-28 00:20:15] Sending reply  : 'Hello'
```

### Enable File Logging (Optional)

Uncomment these lines in `application.properties` to write logs to a file:

```properties
logging.file.name=logs/chatbot.log
logging.pattern.file=%d{yyyy-MM-dd HH:mm:ss} [%thread] %-5level %logger{36} - %msg%n
```

---

## Running Unit Tests

```bash
mvn test
```

All 4 unit tests in `ChatbotServiceTest` should pass:
- `Hi` → `Hello`
- `Bye` → `Goodbye`
- Unknown message → `I don't understand`
- Case-insensitive matching (`HI`, `hi`, `BYE` all work)

---

## Key Design Decisions

- **Constructor Injection** via Lombok `@RequiredArgsConstructor` (preferred over `@Autowired`)
- **`@Valid` + `@NotBlank`** for input validation — rejects null/empty messages with HTTP 400
- **`@RestControllerAdvice`** for centralized exception handling — no try/catch in controller
- **`switch` expression** (Java 14+) in the service for clean, readable reply logic
- **Case-insensitive matching** — `"hi"`, `"HI"`, `"Hi"` all reply with `"Hello"`
