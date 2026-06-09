# Tech Stack

This project is a **Spring Boot** application. Use the following stack with the versions below (use the latest stable release within each line).

| Component       | Choice            | Version |
|-----------------|-------------------|---------|
| Language / JDK  | Java (JDK)        | 17 (LTS) |
| Framework       | Spring Boot       | latest 3.x |
| Database        | PostgreSQL        | latest stable (17.x) |
| DB migrations   | Flyway            | latest (11.x) |
| Testing         | JUnit 5 (Jupiter) | latest (5.x) |
| Build tool      | Maven / Gradle    | latest |

> Note: Resolve the exact latest patch versions from the dependency manager (Spring Boot BOM / Maven Central) at build time. The major lines above are fixed; pick the newest stable patch within each.
