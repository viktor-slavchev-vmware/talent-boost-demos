package com.vmware.talentboost.ui.pageobjects;

import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.options.AriaRole;
import com.vmware.talentboost.ui.pageobjects.partialpageobjects.TaskPreview;

import java.text.MessageFormat;

public class TodosHome extends BasePageObject{


    private final String ROUTE = "/app/today";

    private final Locator addNewTaskButton;
    private final Locator taskNameField;

    private final Locator taskDescriptionField;

//    private final Locator datePickerButton;
//    private final Locator date;
    private final Locator priorityButton;
    private final Locator priorityDropdown;
    private final Locator addTaskButton;

    private final Locator taskHeading;

    private final TaskPreview taskPreview = new TaskPreview(this.page);


    public Locator getTasksContainer(String dateFormat) {
        String locatorTemplate = MessageFormat.format("ul[data-day-list-id=\"{0}\"]", dateFormat);
        return this.page.locator(locatorTemplate);
    }

    public TodosHome(BrowserContext context) {
        super(context);
        this.addNewTaskButton = super.page.locator("button.plus_add_button");
        this.taskNameField = super.page.locator("div[aria-label='Task name']");
        this.taskDescriptionField = super.page.locator("div[aria-label='Description']");
//        this.datePickerButton = super.page.locator("div[aria-label='Set due date']");
//        this.date = super.page.locator("div.date-picker");
        this.priorityButton = super.page.locator("div[data-action-hint='task-actions-priority-picker']");
        this.priorityDropdown = super.page.locator("ul#dropdown-select-1-listbox");
        this.addTaskButton = super.page.locator("button[data-testid='task-editor-submit-button']");
        this.taskHeading = super.page.locator("div.task_list_item__content");
    }

    public void navigate(){
        this.page.navigate(super.BASE_URL + this.ROUTE);
    }

    public void createNewTask(String taskName, String taskDescription, String date, String priority){
        this.addNewTaskButton.click();
        this.taskNameField.type(taskName);
        this.taskDescriptionField.type(taskDescription);
        if (!date.equals("Today")){
            super.selectFromDatePicker(date);
        }
        this.priorityButton.click();
        this.priorityDropdown.getByRole(AriaRole.OPTION).filter(new Locator.FilterOptions().setHasText(priority)).click();
        this.addTaskButton.click();
    }

    public void deleteTask(String taskName) {
        this.taskHeading.filter(new Locator.FilterOptions().setHasText(taskName)).click();
        this.taskPreview.getMoreActionsButton().click();
        this.taskPreview.getDeleteTaskButton().click();
//        this.page.onDialog(Dialog::accept);
        this.page.locator("form button[type='Submit']").click();
    }
}
