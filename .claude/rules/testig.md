# Testing Rules

These rules are mandatory for every test generated in this project.

## 1. Test naming convention
- Use `camelCase` with segments delimited by `_`:
  `nameOfTheTest_whatIsThatYouAreTesting_outcome`

## 2. Assertions
- Use **JUnit asserts** for all assertions.

## 3. Test coverage scope
Generate:
- **Unit tests** for the service layer.
- **Unit tests** for the controller layer.
- **Integration tests**, including the DB.
