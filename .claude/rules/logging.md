# Logging Rules

These rules are mandatory for every log statement generated in this project.

## 1. No `System.out.println`
- Never use `System.out.println` (or `System.err.println`) for logging.

## 2. Use SLF4J
- Use the SLF4J API for all logging.

## 3. Do not log sensitive information
- Never log sensitive data such as **policy numbers** and **policy amounts**.

## 4. No string concatenation in logs
- Do not concatenate strings to build log messages.
- Use SLF4J parameterized placeholders instead: `log.info("Processed item {}", id);`

## 5. Required context in every log statement
- Every log statement must include:
  - **Method path** (class/method origin)
  - **Request ID** (correlation ID)
  - **Duration**

## 6. Bulk operations
- For bulk operations, log the **total count** and the **page size**.
