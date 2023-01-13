package com.otel.sandbox.logsincontextlog4j2;


import io.opentelemetry.api.OpenTelemetry;
import io.opentelemetry.sdk.autoconfigure.AutoConfiguredOpenTelemetrySdk;
import io.opentelemetry.sdk.resources.Resource;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.UUID;

@SpringBootApplication
public class LogsInContextLog4j2Application {

	public static void main(String[] args) throws InterruptedException {
		SpringApplication.run(LogsInContextLog4j2Application.class, args);
	}



}
