package com.vmware.talentboost.ui.pageobjects.partialpageobjects;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;

public class TaskPreview {
    private Locator moreActionsButton;
    private Locator deleteTaskButton;

    public Locator getMoreActionsButton() {
        return moreActionsButton;
    }

    public Locator getDeleteTaskButton() {
        return deleteTaskButton;
    }

    public TaskPreview(Page page) {
        this.moreActionsButton = page.locator("button[aria-label='More actions']");
        this.deleteTaskButton = page.locator("button[aria-label='Delete task…']");
    }
}
