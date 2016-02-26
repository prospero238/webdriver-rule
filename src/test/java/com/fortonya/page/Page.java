package com.fortonya.page;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.WebDriverWait;

public abstract class Page {

    private static final long DEFAULT_TIMEOUT_SECONDS = 8;
    protected WebDriver driver;
    protected WebDriverWait wait;

    /*
     * Constructor injecting the WebDriver interface
     *
     * @param webDriver
     */
    public Page(WebDriver driver) {
        this.driver = driver;
        wait = new WebDriverWait(driver, DEFAULT_TIMEOUT_SECONDS);
    }


    public String getTitle() {
        return driver.getTitle();
    }

    protected <T> T createPage(Class<T> pageClass) {
        return PageFactory.initElements(driver, pageClass);

    }


}

