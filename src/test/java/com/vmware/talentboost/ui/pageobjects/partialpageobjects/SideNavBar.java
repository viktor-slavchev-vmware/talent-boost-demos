package com.vmware.talentboost.ui.pageobjects.partialpageobjects;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;

public class SideNavBar {

    private Locator newProjectButton;
    private Locator projectNameField;

    private Locator boardView;
    private Locator favoriteRadio;

    private Locator addButton;
    public SideNavBar(Page page) {
        this.newProjectButton = page.locator("button[aria-label='Add project']");
        this.projectNameField = page.locator("input#edit_project_modal_field_name");
        this.favoriteRadio = page.locator("input[name='is_favorite']");
        this.boardView = page.locator("label#project_list_board_style_option");
        this.addButton = page.locator("form button[type=submit]");
    }

    public void createNewProject(String name, Boolean isFavorite){
        this.newProjectButton.click();
        this.projectNameField.type(name);
        if (isFavorite){
            this.favoriteRadio.click(new Locator.ClickOptions().setForce(true));
        }
        this.addButton.click();
    }
}
