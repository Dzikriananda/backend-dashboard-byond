package com.dzikriananda.multimatic_backend.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ReadListener;
import jakarta.servlet.ServletInputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;
import org.owasp.encoder.Encode;

import java.io.ByteArrayInputStream;
import java.io.IOException;


public class XssRequestWrapper extends HttpServletRequestWrapper {

    private final ObjectMapper objectMapper = new ObjectMapper();


    // Constructor to initialize the wrapper with the original request
    public XssRequestWrapper(HttpServletRequest request) {
        super(request);
    }

    // Override the method to get parameter values and sanitize each value
    @Override
    public String[] getParameterValues(String parameter) {
        // Get the original parameter values from the wrapped request
        String[] values = super.getParameterValues(parameter);

        // If the original values are null, return null
        if (values == null) {
            return null;
        }

        // Create an array to store the sanitized values
        int count = values.length;
        String[] sanitizedValues = new String[count];

        // Iterate through the original values, sanitizing each one
        for (int i = 0; i < count; i++)
        {
            sanitizedValues[i] = sanitizeInput(values[i]);
        }

        // Return the array of sanitized values
        return sanitizedValues;
    }

    @Override
    public ServletInputStream getInputStream() throws IOException {
        ServletInputStream originalInputStream = super.getInputStream();
        String requestBody = new String(originalInputStream.readAllBytes());

        // Sanitize the JSON body
        String sanitizedBody = sanitizeInput(requestBody);

        return new ServletInputStream() {
            private final ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(
                    sanitizedBody.getBytes()
            );

            @Override
            public int read() throws IOException {
                return byteArrayInputStream.read();
            }

            @Override
            public boolean isFinished() {
                return byteArrayInputStream.available() == 0;
            }

            @Override
            public boolean isReady() {
                return true;
            }

            @Override
            public void setReadListener(ReadListener readListener) {

            }
        };
    }


    // Method to sanitize the input using OWASP Java Encoder
    private String sanitizeInput(String input) {
        if (input == null) {
            return null;
        }

        // Apply encoding only to parameter values, NOT JSON body
        if (isJson(input)) {
            return input; // Do not modify JSON data
        }

        return Encode.forHtml(input); // Apply encoding only for form parameters
    }

    private boolean isJson(String input) {
        try {
            new ObjectMapper().readTree(input);
            return true;
        } catch (IOException e) {
            return false;
        }
    }
}

