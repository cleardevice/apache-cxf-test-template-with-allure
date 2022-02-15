package com.example;

import io.qameta.allure.Attachment;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.TestInstancePreDestroyCallback;

import java.io.ByteArrayOutputStream;

public class LoggingExtension implements TestInstancePreDestroyCallback {
    private final ByteArrayOutputStream request;
    private final ByteArrayOutputStream response;

    LoggingExtension(ByteArrayOutputStream request, ByteArrayOutputStream response) {
        this.request = request;
        this.response = response;
    }

    @Override
    public void preDestroyTestInstance(ExtensionContext context) {
        boolean testFailed = context.getExecutionException().isPresent();
        if (testFailed) {
            logRequest(request);
            logResponse(response);
        }
    }

    @Attachment(value = "request")
    private static byte[] logRequest(ByteArrayOutputStream stream) {
        return attach(stream);
    }

    @Attachment(value = "response")
    private static byte[] logResponse(ByteArrayOutputStream stream) {
        return attach(stream);
    }

    private static byte[] attach(ByteArrayOutputStream log) {
        byte[] array = log.toByteArray();
        log.reset();
        return array;
    }
}