package com.vmware.talentboost.ui.pageobjects;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;

public class ProjectPage extends BasePageObject{

    private Locator projectHeading;


    public Locator getProjectHeading() {
        return projectHeading;
    }

    public ProjectPage(Page page) {
        super(page);
        this.projectHeading = page.locator("h1[data-testid='view_header__h1']");
    }
}
