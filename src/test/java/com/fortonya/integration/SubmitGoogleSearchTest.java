package com.fortonya.integration;

import org.hamcrest.collection.IsEmptyCollection;
import org.hamcrest.core.IsNot;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;

import com.fortonya.page.GoogleResults;
import com.fortonya.page.GoogleSearchPage;

import static org.hamcrest.collection.IsEmptyCollection.empty;
import static org.hamcrest.core.IsNot.not;
import static org.junit.Assert.assertThat;

public class SubmitGoogleSearchTest {
    @Rule
    public WebDriverRule webDriverRule = new WebDriverRule();
    protected WebDriver driver;


    @Before
    public void setup() {
        driver = webDriverRule.getDriver();
    }
    @Test
    public void should_provide_results() {
        GoogleSearchPage googleSearchPage = new GoogleSearchPage(driver);
        webDriverRule.getWebDriverWait().until(ExpectedConditions.presenceOfElementLocated(By.name("q")));

        webDriverRule.captureScreenshot("before-search");
        GoogleResults results = googleSearchPage.submitSearch("keith");
        assertThat(results, not((GoogleResults) empty()));

    }
}
