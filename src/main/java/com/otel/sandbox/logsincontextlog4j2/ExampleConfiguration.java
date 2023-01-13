/*
 * Copyright The OpenTelemetry Authors
 * SPDX-License-Identifier: Apache-2.0
 */

package com.otel.sandbox.logsincontextlog4j2;

import io.opentelemetry.api.OpenTelemetry;
import io.opentelemetry.api.trace.propagation.W3CTraceContextPropagator;
import io.opentelemetry.api.baggage.propagation.W3CBaggagePropagator;
import io.opentelemetry.context.propagation.ContextPropagators;
import io.opentelemetry.exporters.logging.LoggingSpanExporter;
import io.opentelemetry.sdk.OpenTelemetrySdk;
import io.opentelemetry.sdk.trace.SdkTracerProvider;
import io.opentelemetry.sdk.trace.export.SimpleSpanProcessor;

/**
 * All SDK management takes place here, away from the instrumentation code, which should only access
 * the OpenTelemetry APIs.
 */
class ExampleConfiguration {

  /**
   * Initializes the OpenTelemetry SDK with a logging span exporter and the W3C Trace Context
   * propagator.
   *
   * @return A ready-to-use {@link OpenTelemetry} instance.
   */
  static OpenTelemetry initOpenTelemetry() {
    SdkTracerProvider sdkTracerProvider =
        SdkTracerProvider.builder()
/*          If this line is added we will see much more logging details on top of what we have defined in the Log4j2EventLayout.json.
            ***** Sample output using SimpleSpanProcessor *****
           {
              "timestamp": "2023-01-13T15:29:21.832+0800",
              "thread.name": "http-nio-8080-exec-1",
              "log.level": "INFO",
              "logger.name": "io.opentelemetry.exporters.logging.LoggingSpanExporter",
              "message": "span: SpanData{spanContext=ImmutableSpanContext{traceId=0d6952a505f68fb8b2e11aae4a4f3f78, spanId=dbed50962bad9023, traceFlags=01, traceState=ArrayBasedTraceState{entries=[]}, remote=false, valid=true}, parentSpanContext=ImmutableSpanContext{traceId=00000000000000000000000000000000, spanId=0000000000000000, traceFlags=00, traceState=ArrayBasedTraceState{entries=[]}, remote=false, valid=false}, resource=Resource{schemaUrl=null, attributes={service.name=\"unknown_service:java\", telemetry.sdk.language=\"java\", telemetry.sdk.name=\"opentelemetry\", telemetry.sdk.version=\"1.19.0\"}}, instrumentationScopeInfo=InstrumentationScopeInfo{name=com.otel.sandbox.logsincontextlog4j2.LogsInContextLog4j2Application, version=null, schemaUrl=null, attributes={}}, name=/ping, kind=SERVER, startEpochNanos=1673594961756398000, endEpochNanos=1673594961830829117, attributes=AttributesMap{data={net.host.name=localhost:8080, http.target=/ping, http.method=GET, http.scheme=http}, capacity=128, totalAddedValues=4}, totalAttributeCount=4, events=[], totalRecordedEvents=0, links=[], totalRecordedLinks=0, status=ImmutableStatusData{statusCode=UNSET, description=}, hasEnded=true}",
              "service.name": "logs-in-context"
            }*/
//            .addSpanProcessor(SimpleSpanProcessor.create(new LoggingSpanExporter()))
            .build();

    OpenTelemetrySdk sdk =
        OpenTelemetrySdk.builder()
            .setTracerProvider(sdkTracerProvider)
            .setPropagators(ContextPropagators.create(W3CTraceContextPropagator.getInstance()))
            .setPropagators(ContextPropagators.create(W3CBaggagePropagator.getInstance()))
            .build();

    Runtime.getRuntime().addShutdownHook(new Thread(sdkTracerProvider::close));
    return sdk;
  }
}
