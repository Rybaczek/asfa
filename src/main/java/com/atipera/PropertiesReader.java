package com.atipera;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Properties;

import static java.text.MessageFormat.format;

@Slf4j
public final class PropertiesReader {
    private static final Properties PROPERTIES;
    private static final String PROPERTIES_FILE = "apikey.properties";

    private PropertiesReader() {
    }

    static {
        PROPERTIES = new Properties();
        final URL props = ClassLoader.getSystemResource(PROPERTIES_FILE);
        try (InputStream input = props.openStream()) {
            PROPERTIES.load(input);
        } catch (IOException ex) {
            log.debug(ex.getClass().getName() + " PropertiesReader ", ex);
        }
    }

    public static String getProperty(final String name) {

        if (PROPERTIES.getProperty(name) != null) {
            return PROPERTIES.getProperty(name);
        } else {
            log.warn(format("Token from property {0} not found. Provide one to authorize your requests", name));
            return "Undefined Token ";
        }
    }
}

