package com.atipera;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.test.context.TestComponent;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static java.text.MessageFormat.format;

@TestComponent
@Slf4j
public class AssertionResourceReader {
    public String readResource(String resourcePath) {
        try {
            return StreamUtils.copyToString(new ClassPathResource(resourcePath).getInputStream(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            log.error(format("Resource not found: {0}", resourcePath));
            throw new RuntimeException(e);
        }
    }
}
