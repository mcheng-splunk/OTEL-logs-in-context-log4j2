# Logs In Context With Log4j2

## Introduction

This project contains a Java application configured to use [Log4j2](https://logging.apache.org/log4j/2.x/) to write JSON structured logs that propagate OpenTelemetry trace context onto log messages. 

The [log4j2.xml](./src/main/resources/log4j2.xml) configures the application to log out to the console with a [JSON Template Layout](https://logging.apache.org/log4j/2.x/manual/json-template-layout.html) defined in [Log4j2EventLayout.json](./src/main/resources/Log4j2EventLayout.json).

The application uses the [OpenTelemetry Log4j2 Integration](https://github.com/open-telemetry/opentelemetry-java-instrumentation/tree/main/instrumentation/log4j/log4j-2.13.2/library) to inject trace context to Log4j2 [thread context](https://logging.apache.org/log4j/2.x/manual/thread-context.html).

The result is JSON structured logs, with one JSON object per line, which have the `span_id` and `trace_id` from OpenTelemetry included:

```json
{
  "timestamp": "2021-05-19T15:51:16.063-05:00",
  "thread.name": "http-nio-8080-exec-1",
  "log.level": "INFO",
  "logger.name": "com.newrelic.app.Controller",
  "message": "A sample log message!",
  "trace_id": "6aae93314fe034149cd85f07eac24bc5",
  "span_id": "f1be31bc6e4471d8"
}
```

The OpenTelemetry Log specification defines that when propagating [trace context in legacy formats](https://github.com/open-telemetry/opentelemetry-specification/blob/main/specification/logs/README.md#trace-context-in-legacy-formats), `trace_id` and `span_id` should be used. 

## Run

Next, build and run the application:

```shell
// Build & Run the application with maven
./mvnw spring-boot:run

