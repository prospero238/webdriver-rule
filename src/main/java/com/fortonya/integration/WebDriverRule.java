package com.fortonya.integration;

import java.io.File;

import org.apache.log4j.Logger;
import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.security.Credentials;
import org.openqa.selenium.security.UserAndPassword;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.WebDriverWait;


public class WebDriverRule implements TestRule {

    private static final Logger LOG = Logger.getLogger(WebDriverRule.class);
    public static final String DEFAULT_USERNAME = "kcollison";
    public static final String DEFAULT_PASSWORD = "kcollison";

    private static final String DEFAULT_BASE_URL = "http://www.google.com";
    private static final long WAIT_TIMEOUT = 5;
    private static final int BROWSER_WIDTH = 1152;
    private static final int BROWSER_HEIGHT = 864;
    private WebDriver driver;
    private String baseUrl;

    private WebDriverWait webDriverWait;
    private String methodName;
    private String className;

    public WebDriverRule() {
        silenceHttpLogger();
        baseUrl = System.getProperty("webtest.base.url", DEFAULT_BASE_URL);
    }

    public Statement apply(final Statement statement, Description description) {
        methodName = description.getMethodName();
        className = description.getClassName();
        return new Statement() {
            @Override
            public void evaluate() throws Throwable {
                before();
                try {
                    statement.evaluate();
                } finally {
                    after();
                }
            }
        };
    }

    public void after() {
        captureScreenshot();
        if (driver != null) {
            driver.close();
        }
    }

    public <T> T getPage(Class<T> clazz) {
        return PageFactory.initElements(driver, clazz);
    }

    public void before() {
        driver = new FirefoxDriver();
        driver.manage().window().setPosition(new Point(0, 0));
        driver.manage().window().setSize(new Dimension(BROWSER_WIDTH, BROWSER_HEIGHT));

        webDriverWait = new WebDriverWait(driver, WAIT_TIMEOUT);
    }

    public void captureScreenshot() {
        String screenshotFileName = methodName + ".png";
        File methodOutputDir = createMethodOutputDirectory();
        File outputFile = new File(methodOutputDir, screenshotFileName);
        captureScreenshot(outputFile);

    }

    private static void silenceHttpLogger() {
        // the apache http library's logging does not seem to obey standard configuration for logging.
        // These system properties were the only way I found to silence its verbose logging.
        System.setProperty("org.apache.commons.logging.Log", "org.apache.commons.logging.impl.SimpleLog");
        System.setProperty("org.apache.commons.logging.simplelog.log.org.apache.http", "warn");
    }

    public void captureScreenshot(String screenshotName) {
        File methodOutputDirectory = createMethodOutputDirectory();
        captureScreenshot(new File(methodOutputDirectory, screenshotName));

    }

    public static UserAndPassword getDefaultCredentials() {
        return new UserAndPassword(DEFAULT_USERNAME, DEFAULT_PASSWORD);
    }

    private File createMethodOutputDirectory() {
        String userdir = System.getProperty("user.dir");
        File targetDirectory = new File(userdir, "target");
        File surefireReportDir = new File(targetDirectory, "surefire-reports");

        File classOutputDir = new File(surefireReportDir, className);
        if (!classOutputDir.exists()) {
            classOutputDir.mkdirs();
        }
        File methodOutputDir = new File(classOutputDir, methodName);
        if (!methodOutputDir.exists()) {
            LOG.debug("creating test method output dir:" + methodOutputDir.getAbsolutePath());
            methodOutputDir.mkdirs();
        }
        return methodOutputDir;
    }

    private void captureScreenshot(File outputFile) {
        WebDriverUtil.takeScreenshot(driver, outputFile);
    }

    public String composeUrl(String path) {
        String usePath;
        if (path.startsWith("/")) {
            usePath = path;
        } else {
            usePath = "/" + path;
        }
        return baseUrl + usePath;
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public WebDriver getDriver() {
        return driver;
    }

    public void setDriver(WebDriver driver) {
        this.driver = driver;
    }

    public WebDriverWait getWebDriverWait() {
        return webDriverWait;
    }
}
