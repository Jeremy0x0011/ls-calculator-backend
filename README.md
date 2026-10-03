[README.md](https://github.com/user-attachments/files/33002222/README.md)
# Calculator Backend

Spring Boot REST API for the front-end/back-end separated calculator assignment.
The server parses and evaluates expressions; the browser never calculates the result.

## Requirements

- JDK 17 or later
- Maven 3.6 or later

## Run

From this directory:

```powershell
mvn spring-boot:run
```

The API listens on `http://localhost:8080`. H2 stores data in `./data/calculator.mv.db`,
so successful calculation history survives application restarts. Do not delete the
`data` directory if you want to keep the local history.

Run the tests with:

```powershell
mvn test
```

## API

### `POST /api/calculate`

Request:

```json
{"expression":"(1 + 2) * 3"}
```

Success (`200`):

```json
{"success":true,"expression":"(1 + 2) * 3","result":"9"}
```

Expressions are limited to 256 characters and support `+`, `-`, `*`, `/`, parentheses,
decimal numbers, unary signs, and the UI symbols `×` and `÷`. Division is rounded to
12 decimal places using `HALF_UP`. Invalid input and division by zero return `400`;
failed calculations are not saved.

### `GET /api/history`

Returns saved records, newest first:

```json
[{"id":1,"expression":"(1 + 2) * 3","result":"9","createdAt":"2026-10-02T05:00:00Z"}]
```

### `DELETE /api/history/{id}`

Deletes the specified database record and returns `404` if it does not exist.

Error response shape:

```json
{"success":false,"error":{"code":"BAD_REQUEST","message":"Division by zero is not allowed."}}
```

## Configuration

Environment variables can override defaults:

| Variable | Default | Purpose |
| --- | --- | --- |
| `SERVER_PORT` | `8080` | HTTP port |
| `DATABASE_URL` | `jdbc:h2:file:./data/calculator;DB_CLOSE_ON_EXIT=FALSE` | JDBC URL |
| `DATABASE_USERNAME` | `sa` | Database username |
| `DATABASE_PASSWORD` | empty | Database password |
| `APP_CORS_ALLOWED_ORIGINS` | local Vite origins | Comma-separated allowed browser origins |
| `H2_CONSOLE_ENABLED` | `false` | Enable the H2 console for local development only |

For deployment, set CORS to the exact frontend origin and use deployment-managed database
credentials. Do not expose the H2 console publicly.

## Design

- `controller`: REST endpoints and standardized error responses
- `service`: calculation workflow, persistence, and history operations
- `util/ExpressionEvaluator`: recursive-descent parser using `BigDecimal`; no `eval`/`exec`
- `model` and `repository`: JPA entity and database access
