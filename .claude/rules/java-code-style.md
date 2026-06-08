# Java Code Style Rules

These rules are mandatory for every piece of Java code generated in this project.

## 1. Naming conventions (standard Java)
- `camelCase` for methods and variables.
- `PascalCase` for class, interface, enum, and record names.
- `UPPER_SNAKE_CASE` for constants (`static final` fields).

## 2. Size limits
- No file longer than **200 lines**.
- No method longer than **50 lines**.

## 3. No magic numbers or magic strings
- Never inline literal numbers or strings that carry meaning.
- Extract them into named constants or externalized configuration.

## 4. No boolean parameters in methods
- A boolean flag makes a method do two things, which violates the **Single Responsibility Principle (SRP)**.
- Split into separate, intention-revealing methods, or use a dedicated type/enum instead.

## 5. Limit branching complexity (`if-else` / `switch-case`)
- Keep cyclomatic complexity **< 4**.
- Long conditional chains violate the **Open/Closed Principle (OCP)**.
- Prefer the **Strategy design pattern** (or polymorphism / lookup maps) over heavy branching.

## 6. No early return values when exceptions are thrown
- Do not mix returning a value and throwing an exception in the same control path.
- Let exceptions propagate cleanly; do not swallow them with a fallback return.

## 7. Null handling
- Use `Optional<T>` instead of returning or passing `null` wherever applicable.
