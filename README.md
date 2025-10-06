# Marine Unit Monitoring - microservice commons - library

## Exception Handling

A library for Spring exception handling, designed for use in your microservice project and applicable to other projects
as well.

## Offset Transaction

A library for handling compensation transactions.

## Auth Conversion

A library that offers converters for individual authentication conversions, including:

- `JwtAuthenticationConverter`
- `JwtGrantedAuthoritiesConverter`
- Other authentication-related converters.

## Global IDM Service

A library that provides IDM (Identity Management) services, including:

- User creation and deletion processes in Keycloak.
- All IDM processes secured by offset transactions.
- Abstract classes and templates for IDM operations and services.

## Implementation Details

Each library/specific functionality provides implementations for both Reactor and Servlet models.

## Logging with MDC - log tracing [MENTION]

Some classes in this library (e.g., `AbstractIDMService`, `ResponseModelExceptionHandler`) use **MDC** to generate a
unique `traceId` for each operation.

To make logs fully readable and correlate related entries, you need to include the following pattern in your logger
configuration:

```properties
logging.pattern.console=%d{yyyy-MM-dd HH:mm:ss} %-5level traceId=[%X{traceId}] %logger{36} - %msg%n
```

Constant `TRACE_ID="traceId"` by default - you can change it in the mum-commons code in
the [LogTraceConstants.java](src/main/java/pl/bartlomiej/mumcommons/coreutils/constants/LogTraceConstants.java).

**Without this pattern, traceId will not appear in logs, making it harder to trace operations.**