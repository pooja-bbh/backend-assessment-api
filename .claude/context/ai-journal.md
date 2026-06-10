# AI Journal — Prompt Log

This file is the prompt log for the project. Each entry records **what** and **when** I (the user) prompted, **what the response was**, and **whether I accepted, rejected, or challenged** it.

Format for each entry:
- **When** — date/time of the prompt.
- **Prompt** — what I asked for.
- **Response** — summary of what was produced.
- **Outcome** — Accepted / Rejected / Challenged (with notes).

---

## 2026-06-09

### Entry 1
- **Prompt:** List the coding convention rules from `java-code-style.md` that will be followed every time code is generated (rules provided inline).
- **Response:** Noted the file was empty; listed 7 conventions (naming, size limits, no magic literals, no boolean params, cyclomatic complexity < 4 via Strategy, no early return with exceptions, Optional over null) and offered to write them to the file.
- **Outcome:** Accepted.

### Entry 2
- **Prompt:** "yes. write"
- **Response:** Wrote the 7 conventions into `.claude/rules/java-code-style.md`.
- **Outcome:** Accepted.

### Entry 3
- **Prompt:** Add logging rules to `.claude/rules/logging.md` (no System.out.println, use SLF4J, no sensitive data, no string concatenation, method path + correlation ID + duration per log, bulk count + page size).
- **Response:** Created `logging.md` with all 6 rules.
- **Outcome:** Accepted.

### Entry 4
- **Prompt:** Add test naming conventions and testing scope to `.claude/rules/testig.md`.
- **Response:** First write attempt produced the testing rules file.
- **Outcome:** Rejected on first attempt (tool use cancelled), then re-prompted with the same request; second write accepted.

### Entry 5
- **Prompt:** Add to `CLAUDE.md` that the rules files must be referenced whenever generating anything; note `requirements.md` holds the problem statement.
- **Response:** Created `CLAUDE.md` with a mandatory-rules section linking the three rule files and a context section pointing to `requirements.md` and this journal.
- **Outcome:** Accepted.

### Entry 6
- **Prompt:** Defined the purpose of this journal (prompt log of what/when prompted, response, accepted/rejected/challenged).
- **Response:** Initialized this journal with the defined structure and backfilled the session's prompts.
- **Outcome:** Accepted.

### Entry 7
- **Prompt:** Update the journal only on explicit request (do not auto-append / no hook).
- **Response:** Confirmed; no hook set up, entries added on request only.
- **Outcome:** Accepted.

### Entry 8
- **Prompt:** Record the tech stack with versions in `tech-stack.md` (Spring Boot latest, JDK 21, PostgreSQL, Flyway, JUnit latest) and reference it in `CLAUDE.md`.
- **Response:** Created `tech-stack.md` with the stack table and added a reference in `CLAUDE.md`.
- **Outcome:** Accepted.

### Entry 9
- **Prompt:** Create a Spring Boot project with root package `com.backend.assessment`; do not create any package unless asked.
- **Response:** Scaffolded a minimal Maven project — `pom.xml` (Spring Boot 3.5.0, Java 21, web/data-jpa/validation, Flyway, PostgreSQL, test starter), main application class in the root package, and `application.yml`. No sub-packages, no tests, no migrations.
- **Outcome:** Accepted.

### Entry 10
- **Prompt:** "yes" — verify the project compiles (`mvn compile`).
- **Response:** Could not build — Maven not installed and only JDK 17 present (project targeted JDK 21). Presented options.
- **Outcome:** User chose to skip compilation for now.

### Entry 11
- **Prompt:** Replace JDK 21 with JDK 17 in `tech-stack.md` and `CLAUDE.md`, and change the project configuration to JDK 17.
- **Response:** Updated `tech-stack.md` and `CLAUDE.md` to JDK 17 and set `<java.version>` to 17 in `pom.xml`.
- **Outcome:** Accepted.

### Entry 12
- **Prompt:** Add an entry to this journal.
- **Response:** Appended entries 6–12 covering the prompts since the journal was initialized.
- **Outcome:** Accepted.

### Entry 13
- **Prompt:** Add a test folder with `BackendAssessmentApplicationTest.java`.
- **Response:** Created `src/test/java/com/backend/assessment/BackendAssessmentApplicationTest.java` — a `@SpringBootTest` context-load test using the `name_what_outcome` convention and a JUnit assert.
- **Outcome:** Accepted.

### Entry 14
- **Prompt:** Create `architecture.md` in `.claude/rules` with the layering principles, package structure, and rules of thumb.
- **Response:** Created `architecture.md` with the provided content and referenced it in `CLAUDE.md`.
- **Outcome:** Accepted.

### Entry 15
- **Prompt:** Create the folder/package structure from `architecture.md`; also add Exception and util related folders and note them in the doc.
- **Response:** Asked for clarification on which extra exception/util folders. User answered: "Please create global exception handler." Created all 21 packages (each with a documented `package-info.java`), added `GlobalExceptionHandler` (`@RestControllerAdvice`) in `api.exception` and `ErrorResponse` DTO in `api.dto.response`, and documented the exception handling + utilities in `architecture.md`.
- **Outcome:** Accepted.

### Entry 16
- **Prompt:** Add an entry to this journal.
- **Response:** Appended entries 13–16.
- **Outcome:** Accepted.

## 2026-06-10

### Entry 17
- **Prompt:** Following CLAUDE.md and requirements.md, create the OpenAPI 3.0 contract before implementation — `src/main/resources/static/openapi/policies-api.yaml` with schemas only (no paths), point springdoc at the static spec (`swagger-ui.url`, `api-docs.enabled: false`), and confirm the app starts and Swagger UI loads.
- **Response:** Created the schemas-only contract (`PolicySummaryResponse`, `PagedPolicyResponse`, `Premium`, `ErrorResponse`); added the springdoc dependency and config; the user installed Maven mid-task so the build ran. App started against PostgreSQL `policydb`; static spec served at `/openapi/policies-api.yaml` (200), but `/swagger-ui.html` returned 200. Set: `api-docs.enabled: true` with `swagger-ui.disable-swagger-default-url: true` (the only way to mount the UI in springdoc), kept `swagger-ui.url` on the static spec. Added the `GET /api/policies` operation to the static YAML (page/size params, 200→PagedPolicyResponse, 503→ErrorResponse). Generated the Maven wrapper (`mvnw`). Verified all four checks live: app starts, `/swagger-ui.html` → 200, swagger-config points at the static spec which contains `/api/policies`, and the raw spec → 200.
- **Outcome:** Accepted.

### Entry 18
- **Prompt:** Add entries to this journal.
- **Response:** Appended entries 17–19 under 2026-06-10.
- **Outcome:** Accepted.


### Entry 19
- **Prompt:** Following CLAUDE.md, architecture.md, and java-code-style.md, create the domain layer based on the OpenAPI spec.
- **Response:** Created pure, framework-free domain models in `domain.models`: `Region` and `PolicyStatus` enums (codes only — display mapping deferred to `api.mapper`), `Money` value object (BigDecimal + Currency), and `Policy` record with the `isExpiringSoon(LocalDate)` business rule (30-day window as a named constant). Null-safety via `Objects.requireNonNull`; no DTO/JPA/Spring imports. Compiled clean.
- **Outcome:** Accepted.

### Entry 20
- **Prompt:** Following CLAUDE.md, architecture.md, tech-stack.md, and java-code-style.md, create the infrastructure persistence layer (entity, repository, entity-to-domain mapper, Flyway migrations) for PostgreSQL 17 `policydb`, with Flyway creating and seeding tables on startup; confirm `./mvnw test` passes.
- **Response:** Created `PolicyEntity` (JPA, `@Enumerated(STRING)`, explicit `@Column` names so Hibernate validate matches the schema), `PolicyRepository` (`JpaRepository`, paginated `findAll`), `EntityToDomain` mapper (`@Component`), and Flyway `V1__create_policies_table.sql` + `V2__seed_policies.sql` (12 policies across all regions/statuses, dates relative to CURRENT_DATE to exercise isExpiringSoon). `./mvnw test` ran the full context against `policydb`: Flyway applied both migrations, Hibernate validate passed, Tests run: 1, Failures: 0, Errors: 0.
- **Outcome:** Accepted. (User will restart the running app later themselves.)

### Entry 21
- **Prompt:** Add journal entries for the persistence layer and the previous domain layer step.
- **Response:** Appended entries 19–22.
- **Outcome:** Accepted.

### Entry 22
- **Prompt:** Following CLAUDE.md, architecture.md, and logging.md, create the service layer based on the OpenAPI spec. No DTOs; service works with domain models only.
- **Response:** Created `PolicyService` (`@Service`) with `getPolicies(Pageable)` returning `Page<Policy>` — calls `PolicyRepository`, maps each entity to domain via `EntityToDomain`; no entity/DTO leaves the layer. Added bulk-operation logging (pageSize + totalElements + correlationId + duration) via parameterized SLF4J. Constructor injection, no branching, compiled clean.
- **Outcome:** Accepted.

### Entry 23
- **Prompt:** Following CLAUDE.md, architecture.md, and logging.md, create the common logging infrastructure based on the OpenAPI spec. No Javadoc.
- **Response:** Created `common.logging.CorrelationId` (shared MDC key + `X-Correlation-Id` header constants) and `CorrelationIdFilter` (`OncePerRequestFilter`, highest precedence) that resolves/generates the correlation ID, puts it in MDC, echoes it on the response, logs each completed request (method + path + status + duration), and clears MDC in finally. Refactored `PolicyService` and `GlobalExceptionHandler` to use the shared `CorrelationId.MDC_KEY` (removed duplicated literals). No Javadoc; compiled clean.
- **Outcome:** Accepted.

### Entry 24
- **Prompt:** Following CLAUDE.md, architecture.md, java-code-style.md, and requirements.md, create the api layer based on the OpenAPI spec. No Javadoc; no boolean method parameters.
- **Response:** Created `PolicyController` (`GET /api/policies`, `Pageable` with default size 10 as a constant, thin/delegating), `DomainToResponseDto` mapper (region/status display via EnumMap lookups — no switches; isExpiringSoon via injected `Clock`), response DTOs (`PolicySummaryResponse`, `PagedPolicyResponse`, `PremiumResponse`), and `config.ClockConfig`. Added `DataAccessException → 503` to `GlobalExceptionHandler` for AC6, and stripped Javadoc from the touched api files. Did not create `RequestDtoToDomain` (no inbound body). `./mvnw test` context loaded with all beans wired (exit 0). Flagged the `boolean isExpiringSoon` record component as a contract-mandated data field, not a behavioral flag.
- **Outcome:** Accepted.

### Entry 25
- **Prompt:** Following CLAUDE.md, testig.md, java-code-style.md, and architecture.md, write unit tests using JUnit 5 and JUnit asserts only (no AssertJ, no Mockito unless already on classpath; prefer manual stubs/fakes).
- **Response:** Wrote 18 new unit tests across `PolicyTest`, `MoneyTest`, `DomainToResponseDtoTest`, `PolicyServiceTest`, `PolicyControllerTest` — JUnit asserts only, manual stubs/fakes (controller uses a hand-written `PolicyService` subclass; mapper/controller use `Clock.fixed`), Mockito used solely to stub `PolicyRepository` in the service test. Added a public all-args constructor to `PolicyEntity` for testability (no boolean params). Full suite: 19 tests, 0 failures, 0 errors.
- **Outcome:** Accepted. (Integration tests including the DB, per testig.md, remain to be added.)

### Entry 26
- **Prompt:** Add entries to this journal.
- **Response:** Appended entries 22–26 covering the service layer, common logging, api layer, and unit tests.
- **Outcome:** Accepted.

### Entry 27
- **Prompt:** Following CLAUDE.md, testig.md, and architecture.md, write integration tests using the real Spring context; confirm `./mvnw test` passes.
- **Response:** Added `PolicyControllerIntegrationTest` (`@SpringBootTest` + `@AutoConfigureMockMvc`, JUnit asserts only, JSON parsed via the context `ObjectMapper`) — 6 tests driving the whole stack against `policydb`: default paging (totalElements=12, totalPages=2), explicit page size, region/status display mapping, both expiring/non-expiring present, correlation-ID response header, and 404 with correlationId. Verified correlation IDs propagate filter→service. Full suite: 25 tests, 0 failures, 0 errors.
- **Outcome:** Accepted.

### Entry 28
- **Prompt:** Do a final review of the entire codebase against CLAUDE.md rules (file/method size, no System.out, no magic strings/numbers, no boolean params, no Javadoc/comments, SLF4J parameterized, no sensitive logging; architecture: DTOs stay in api, entities stay in infrastructure, controller has no business logic, service has no JPA imports). Fix violations, run `./mvnw test`, then start the app and give the Swagger URL.
- **Response:** Audited via grep + line counts. No size/System.out/architecture/logging violations. Fixed: removed Javadoc/comments from 8 functional files (domain models, repository, mapper, entity incl. the `// Required by JPA.` comment, service) and extracted the hardcoded `/api/policies` into a `POLICIES_PATH` constant in `PolicyController`. Flagged (unchanged, pending decision): the contract-mandated `boolean isExpiringSoon` record component, and the 21 `package-info.java` package-level Javadocs. Defended display strings (EnumMap data) and test fixtures as non-violations. `./mvnw test` → 25 tests, 0 failures. Started the app; Swagger at http://localhost:8080/swagger-ui.html, `GET /api/policies` → 200.
- **Outcome:** Accepted.

### Entry 29
- **Prompt:** Add entries to this journal.
- **Response:** Appended entries 27–29 (integration tests and the final review).
- **Outcome:** Pending.
