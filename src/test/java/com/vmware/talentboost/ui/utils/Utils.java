package com.vmware.talentboost.ui.utils;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserContext;

import java.nio.file.Paths;

public class Utils {
    public static BrowserContext getLoggedInState(Browser browser) {
        return browser.newContext(
                new Browser.NewContextOptions().setStorageStatePath(Paths.get("state.json")));
    }
}
