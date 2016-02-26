package com.fortonya.page;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class GoogleSearchPage extends Page {
    public GoogleSearchPage(WebDriver driver) {
        super(driver);
    }

    @FindBy(name = "q")
    private WebElement searchField;



    public WebElement getSearchField() {
        return searchField;
    }

    public void setSearchField(WebElement searchField) {
        this.searchField = searchField;
    }

    public GoogleResults submitSearch(String searchTerm) {
        searchField.sendKeys(searchTerm);
        searchField.submit();
        return new GoogleResults(driver);
    }
}

