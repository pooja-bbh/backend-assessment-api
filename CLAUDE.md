# Project Context for Claude

## Mandatory rules
Whenever you generate **anything** (code, tests, configuration, etc.), you MUST first refer to and comply with the rule files in [.claude/rules/](.claude/rules/):

- [java-code-style.md](.claude/rules/java-code-style.md) — Java coding conventions.
- [logging.md](.claude/rules/logging.md) — logging rules.
- [testig.md](.claude/rules/testig.md) — testing rules.

These rules are not optional. Apply every applicable rule on every generation.

## Context
- [.claude/context/requirements.md](.claude/context/requirements.md) — contains the **problem statement** and requirements for this project.
- [.claude/context/ai-journal.md](.claude/context/ai-journal.md) — working context / journal.
