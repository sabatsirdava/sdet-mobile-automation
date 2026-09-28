package com.sdet.mobile.config;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.util.Optional;
import java.util.Properties;

public final class FrameworkConfig {

    private static final String DEFAULTS_RESOURCE = "config/config.properties";
    private static final Path LOCAL_OVERRIDES = Path.of("local.properties");

    private static final FrameworkConfig INSTANCE = new FrameworkConfig();

    private final Properties properties = new Properties();

    private FrameworkConfig() {
        loadDefaults();
        loadLocalOverrides();
    }

    public static FrameworkConfig get() {
        return INSTANCE;
    }

    public URL serverUrl() {
        String value = required("appium.server.url");
        try {
            return URI.create(value).toURL();
        } catch (MalformedURLException | IllegalArgumentException e) {
            throw new IllegalStateException("Invalid 'appium.server.url': " + value, e);
        }
    }

    public String platformName() {
        return required("platform.name");
    }

    public String automationName() {
        return required("automation.name");
    }

    public String deviceName() {
        return required("device.name");
    }

    public String appPackage() {
        return required("app.package");
    }

    public String appActivity() {
        return required("app.activity");
    }

    public Optional<String> appPath() {
        return optional("app.path");
    }

    public Duration explicitWait() {
        String value = required("wait.timeout.seconds");

        try {
            return Duration.ofSeconds(Long.parseLong(value));
        } catch (NumberFormatException e) {
            throw new IllegalStateException(
                    "'wait.timeout.seconds' must be a whole number: " + value, e);
        }
    }

    public Path reportsDir() {
        return Path.of(required("reports.dir"));
    }

    private String required(String key) {
        return optional(key)
                .orElseThrow(() ->
                        new IllegalStateException(
                                "Missing required configuration key '" + key + "'"));
    }

    private Optional<String> optional(String key) {
        String value = System.getProperty(key, properties.getProperty(key));

        return value == null || value.isBlank()
                ? Optional.empty()
                : Optional.of(value.trim());
    }

    private void loadDefaults() {
        try (InputStream in = FrameworkConfig.class.getClassLoader()
                .getResourceAsStream(DEFAULTS_RESOURCE)) {

            if (in == null) {
                throw new IllegalStateException(
                        "Classpath resource not found: " + DEFAULTS_RESOURCE);
            }

            properties.load(in);
        } catch (IOException e) {
            throw new IllegalStateException(
                    "Could not read " + DEFAULTS_RESOURCE, e);
        }
    }

    private void loadLocalOverrides() {
        if (!Files.isRegularFile(LOCAL_OVERRIDES)) {
            return;
        }

        try (Reader reader = Files.newBufferedReader(LOCAL_OVERRIDES)) {
            properties.load(reader);
        } catch (IOException e) {
            throw new IllegalStateException(
                    "Could not read " + LOCAL_OVERRIDES.toAbsolutePath(), e);
        }
    }
}