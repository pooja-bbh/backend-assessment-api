# Architecture Rules

## Layering Principles
- Dependencies point inward: api -> service -> domain, and infrastructure -> domain.
- The domain layer has no dependencies on api, service, infrastructure, frameworks, or persistence concerns.
- The api layer never talks to infrastructure directly - it goes through service.
- DTOs never cross out of the api layer. Persistence entities never cross out of the infrastructure layer. Only domain models move between layers.
- Each boundary has an explicit mapper.

## Package Structure
All production code lives under base package `src/main/java/com/backend/assessment`:

```
[api]
    [controller]            REST controllers; thin, delegate to service.
    [dto]
        [request]           Inbound request DTOs.
        [response]          Outbound response DTOs.
    [mapper]
        RequestDtoToDomain  Maps request DTOs -> domain models.
        DomainToResponseDto Maps domain models -> response DTOs.
    [exception]             @RestControllerAdvice handler(s).

[domain]
    [models]                Pure business models. No framework/persistence deps.

[service]                   Business logic / use cases. Operates on domain models.

[infrastructure]
    [persistence]
        [repository]        Spring Data repositories.
        [entity]            JPA entities.
        [mapper]
            EntityToDomain  Maps entities <-> domain models.
    [cache]                 Caching adapters.

[config]                    Spring configuration classes.

[common]
    [exception]             Generic/technical exceptions used across layers.
    [util]                  Stateless helpers.
    [logging]               Logging and correlation ID concerns.
```

## Rules of Thumb
- Controllers: validate input, call service, map result. No business logic.
- Services: operate on domain models, not DTOs or entities.
- Mappers: dedicated classes; no inline mapping.
- Cross-cutting concerns belong in common.

## Exception Handling
- `api.exception.GlobalExceptionHandler` is the single global `@RestControllerAdvice` for the API layer. It converts uncaught exceptions into a standard `ErrorResponse`.
- `api.dto.response.ErrorResponse` is the standard error payload (status, message, correlationId).
- Generic/technical exceptions live in `common.exception`; API-facing handling stays in `api.exception`.

## Utilities
- Stateless helpers live in `common.util`. No state, no business logic.
