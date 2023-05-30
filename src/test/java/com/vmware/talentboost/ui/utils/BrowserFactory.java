package com.vmware.talentboost.ui.utils;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Playwright;

public  class BrowserFactory {

    private static Playwright playwright;
    private static Browser browser;
    public static Browser create(BrowserTypesEnum browserType, boolean isHeadless, double slowMo){
        playwright = Playwright.create();
        switch (browserType) {
            case FIREFOX:
                browser = playwright.firefox().launch(new BrowserType.LaunchOptions().setHeadless(isHeadless).setSlowMo(slowMo));
                break;
            case CHROMIUM:
                browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(isHeadless).setSlowMo(slowMo));
                break;
        }

        return browser;
    }
}
