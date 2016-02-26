package com.fortonya.page;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class GoogleResults extends Page {
    public GoogleResults(WebDriver driver) {
        super(driver);
    }

    @FindBy(className = "rc")
    private List<WebElement> resultRows = new ArrayList<WebElement>();

    public List<WebElement> getResultRows() {
        return resultRows;
    }
}
