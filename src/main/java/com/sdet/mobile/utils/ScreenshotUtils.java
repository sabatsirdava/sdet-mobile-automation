package com.sdet.mobile.utils;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public final class ScreenshotUtils {

    private static final DateTimeFormatter TIMESTAMP = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss_SSS");
    private static final int MAX_FILE_NAME_LENGTH = 120;

    private ScreenshotUtils() {
    }

    public static Path capture(WebDriver driver, String testName, Path targetDir) {
        byte[] png = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
        String fileName = sanitize(testName) + "_" + LocalDateTime.now().format(TIMESTAMP) + ".png";
        try {
            Files.createDirectories(targetDir);
            return Files.write(targetDir.resolve(fileName), png);
        } catch (IOException e) {
            throw new UncheckedIOException("Could not save screenshot to " + targetDir.toAbsolutePath(), e);
        }
    }

    static String sanitize(String name) {
        String cleaned = name.replaceAll("[^A-Za-z0-9._-]+", "_");
        return cleaned.length() > MAX_FILE_NAME_LENGTH ? cleaned.substring(0, MAX_FILE_NAME_LENGTH) : cleaned;
    }
}
