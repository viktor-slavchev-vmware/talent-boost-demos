package com.vmware.talentboost.ui.pageobjects;


import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import io.github.cdimascio.dotenv.Dotenv;

public class BasePageObject {

    private final Dotenv dotenv = Dotenv.load();
    private final Locator dateExpand;
    private final Locator dateSearchField;

    protected final String BASE_URL;
    public Page page;

    public BasePageObject(Page page) {
        this.BASE_URL = dotenv.get("BASE_UI_URL");
        this.page = page;
        this.dateExpand = this.page.locator("div[aria-label='Set due date']");
        this.dateSearchField = this.page.locator("div.scheduler-input input");
    }

    public void selectFromDatePicker(String date){
        this.dateExpand.click();
        this.dateSearchField.type(date);
        this.dateSearchField.press("Enter");
    }
}
