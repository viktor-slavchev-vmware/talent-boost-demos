package com.vmware.talentboost.ui.pageobjects;

import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;

public class LoginPage extends BasePageObject{

    private static final String PATH = "/auth/login";
    private Locator emailField;
    private Locator passwordField;

    private Locator loginButton;
    public LoginPage(Page page) {
        super(page);
        this.emailField = super.page.locator("input#element-0");
        this.passwordField = super.page.locator("input#element-3");
        this.loginButton = super.page.locator("button[data-gtm-id='start-email-login']");
    }

    public void navigate(){
        super.page.navigate(super.BASE_URL + PATH);
    }

    public void login(String email, String password){
        super.page.waitForURL("**" + PATH);
        this.emailField.type(email);
        this.passwordField.type(password);
        this.loginButton.click();
        super.page.waitForURL("**/app/today");
    }
}
