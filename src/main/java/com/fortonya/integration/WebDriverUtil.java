package com.fortonya.integration;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

public class WebDriverUtil {
    private static final Logger LOG = Logger.getLogger(WebDriverUtil.class);

    public static File takeScreenshot(WebDriver webDriver) {
        File image = null;
        try {
            image = File.createTempFile("browser", ".png");
            takeScreenshot(webDriver, image);
        } catch (IOException e) {
            LOG.warn("unable to get screenshot", e);
        }

        return image;
    }

    public static void takeScreenshot(WebDriver webDriver, File outputFile) {
        TakesScreenshot takesScreenshot = (TakesScreenshot) webDriver;
        File screenshot = takesScreenshot.getScreenshotAs(OutputType.FILE);
        LOG.debug("screenshot save location[" + outputFile.getAbsolutePath() + "] ");

        try {
            if (outputFile.exists()) {
                LOG.info("Found existing file at[" + screenshot.getAbsolutePath() + "].  Will be overwritten.");
                outputFile.delete();
            }
            FileUtils.moveFile(screenshot, outputFile);
        } catch (IOException e) {
            LOG.warn("Unable to capture screenshot.  outputfile[" + outputFile.getAbsolutePath() + "] ", e);
        }
    }

    public static void navigateToPath(WebDriver driver, String path) {
        String targetUrl = constructUrl(driver, path);
        LOG.debug("going to target url[" + targetUrl + "] ");
        driver.get(targetUrl);
    }

    public static String constructUrl(WebDriver driver, String path) {
        String urlWithPath = null;
        try {
            URL url = new URL(driver.getCurrentUrl());

            String dcBaseUrl = StringUtils.remove(driver.getCurrentUrl(), url.getPath());
            if (path.startsWith("/")) {
                urlWithPath = dcBaseUrl + path;
            } else {
                urlWithPath = dcBaseUrl + "/" + path;
            }
        } catch (MalformedURLException ignore) {

        }

        return urlWithPath;

    }
}
