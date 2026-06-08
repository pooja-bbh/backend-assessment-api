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
- **Outcome:** Pending.
