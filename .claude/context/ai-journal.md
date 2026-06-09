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
- **Outcome:** Pending.
