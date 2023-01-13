package com.otel.sandbox.logsincontextlog4j2;

import io.opentelemetry.api.GlobalOpenTelemetry;
import io.opentelemetry.api.OpenTelemetry;
import io.opentelemetry.api.trace.SpanKind;
import io.opentelemetry.api.trace.Tracer;
import io.opentelemetry.semconv.trace.attributes.SemanticAttributes;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.util.Random;

@RestController
public class Controller {

    private static final OpenTelemetry openTelemetry = ExampleConfiguration.initOpenTelemetry();
    private static final Tracer TRACER =

/*           Using GlobalOpenTelemetry provider we will not have see trace_id and span_id information.
             ***** Sample output using GlobalOpenTelemetry *****
            {
            "timestamp":"2023-01-13T15:25:34.749+0800",
            "thread.name":"http-nio-8080-exec-1",
            "log.level":"INFO",
            "logger.name":"com.otel.sandbox.logsincontextlog4j2.Controller",
            "message":"A sample log message!",
            "service.name":"logs-in-context"
            }*/
           // GlobalOpenTelemetry.getTracerProvider().get(LogsInContextLog4j2Application.class.getName());
           openTelemetry.getTracer(LogsInContextLog4j2Application.class.getName());
    private static final Logger logger = LogManager.getLogger(Controller.class);


    @GetMapping("/ping")
    public String ping() throws InterruptedException {
        var span =
                TRACER
                        .spanBuilder("/ping")
                        .setAttribute(SemanticAttributes.HTTP_METHOD, "GET")
                        .setAttribute(SemanticAttributes.HTTP_SCHEME, "http")
                        .setAttribute(SemanticAttributes.NET_HOST_NAME, "localhost:8080")
                        .setAttribute(SemanticAttributes.HTTP_TARGET, "/ping")
                        .setSpanKind(SpanKind.SERVER)
                        .startSpan();
        try (var scope = span.makeCurrent()) {
            System.out.println("Span id is :" + span.getSpanContext().getSpanId());
            System.out.println("Trace id is :" + span.getSpanContext().getTraceId());
            var sleepTime = new Random().nextInt(200);
            Thread.sleep(sleepTime);
            logger.info("A sample log message!");
            return "pong";
        } finally {
            span.end();
        }
    }
}
