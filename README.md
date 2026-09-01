# EQ Prototype

Backend for my final year dissertation project, an educational quizzing platform in the style of Kahoot. Java Spring Boot with MongoDB, built alongside a separate Vue + TypeScript frontend.

## What it does

- User registration, login and password resets (passwords hashed with bcrypt)
- Quiz creation, editing, deletion and reuse
- Multiple question types: MCQ, ordering and match up, each with time limits and scoring
- Quiz sharing via link or QR code
- Participation with pausing/resuming and per question responses
- Result summaries per player, plus aggregate stats per quiz (average, median, dropout rate, completion times)

Feature requirements came from a survey of 10 participants, then iterated on with a dedicated test user over the course of the project.

## Running it

Requires Java 17+ and a local MongoDB on the default port.

```
./mvnw spring-boot:run
```

Tests are plain JUnit + Mockito unit tests, no database needed:

```
./mvnw test
```

## Notes

Auth is intentionally kept simple (no sessions or JWT) since the dissertation focus was the quizzing and analytics side rather than identity management.
