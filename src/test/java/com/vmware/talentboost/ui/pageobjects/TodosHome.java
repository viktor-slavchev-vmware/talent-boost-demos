package com.vmware.talentboost.ui.pageobjects;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.assertions.PlaywrightAssertions;
import com.microsoft.playwright.options.AriaRole;
import com.vmware.talentboost.ui.pageobjects.partialpageobjects.SideNavBar;
import com.vmware.talentboost.ui.pageobjects.partialpageobjects.TaskPreview;

import java.text.MessageFormat;

public class TodosHome extends BasePageObject{


    private final String ROUTE = "/app/today";

    private final Locator addNewTaskButton;
    private final Locator taskNameField;

    private final Locator taskDescriptionField;
    private final Locator priorityButton;
    private final Locator priorityDropdown;
    private final Locator addTaskButton;

    private final Locator taskHeading;

    private final TaskPreview taskPreview;

    private final SideNavBar sideNavBar;

    private final Locator completeTaskButton;
    private final Locator optionsButton;
    private final Locator showCompletedOption;

    private final Locator completedItem;

    private final Locator commentsButton;

    private final Locator commentTextBox;

    private final Locator submitCommentButton;
    private final Locator postedComment;

    public Locator getCompleteTaskButton() {
        return completeTaskButton;
    }

    public Locator getAddNewTaskButton() {
        return addNewTaskButton;
    }

    public Locator getCommentsButton() {
        return commentsButton;
    }

    public TaskPreview getTaskPreview() {
        return taskPreview;
    }

    public SideNavBar getSideNavBar() {
        return sideNavBar;
    }

    public Locator getTasksContainer(String dateFormat) {
        String locatorTemplate = MessageFormat.format("ul[data-day-list-id=\"{0}\"]", dateFormat);
        return this.page.locator(locatorTemplate);
    }

    public TodosHome(Page page) {
        super(page);
        this.addNewTaskButton = super.page.locator("button.plus_add_button");
        this.taskNameField = super.page.locator("div[aria-label='Task name']");
        this.taskDescriptionField = super.page.locator("div[aria-label='Description']");
        this.priorityButton = super.page.locator("div[data-action-hint='task-actions-priority-picker']");
        this.priorityDropdown = super.page.locator("ul#dropdown-select-1-listbox");
        this.addTaskButton = super.page.locator("button[data-testid='task-editor-submit-button']");
        this.taskHeading = super.page.locator("div.task_list_item__content");
        this.completeTaskButton = super.page.locator("button[data-action-hint='task-complete']");
        this.optionsButton = super.page.locator("button[aria-label='Project options menu']");
        this.showCompletedOption = super.page.getByRole(AriaRole.MENUITEM).
                filter(new Locator.FilterOptions().setHasText("Show completed"));
        this.completedItem = super.page.locator("li[class*='completed']");
        this.commentsButton = super.page.locator("button[data-testid='open-comment-editor-button']");
        this.commentTextBox = super.page.locator("div[aria-label='Comment']");
        this.submitCommentButton = super.page.locator("button[data-track='comments|add_comment']");
        this.postedComment = super.page.locator("div[id*=comment-]");


        //Partials init
        this.taskPreview = new TaskPreview(this.page);
        this.sideNavBar = new SideNavBar(this.page);
    }

    public void navigate(){
        this.page.navigate(super.BASE_URL + this.ROUTE);
    }

    public void createNewTask(String taskName, String taskDescription, String date){
        this.taskNameField.type(taskName);
        this.taskDescriptionField.type(taskDescription);
        if (!(date.equals("Today"))){
            super.selectFromDatePicker(date);
        }
        this.addTaskButton.click();
    }

    public void deleteTask(String taskName) {
        this.taskHeading.filter(new Locator.FilterOptions().setHasText(taskName)).click();
        this.taskPreview.getMoreActionsButton().click();
        this.taskPreview.getDeleteTaskButton().click();
//        this.page.onDialog(Dialog::accept);
        this.page.locator("form button[type='Submit']").click();
    }

    public void expandCompletedItems(){
        this.optionsButton.click();
        this.showCompletedOption.click();
    }

    public void writeComment(String comment){
        this.commentTextBox.type(comment);
        this.submitCommentButton.click();
    }

    public void selectTask(String taskName){
        this.taskHeading.filter(new Locator.FilterOptions().setHasText(taskName)).click();
    }

    // Assertions

    public void assertTaskAppearsAsCompleted(String taskName){
        Locator compeltedItem = this.completedItem.filter(new Locator.FilterOptions().setHasText(taskName));
        PlaywrightAssertions.assertThat(compeltedItem).isVisible();
    }

    public void assertCommentIsVisible(String comment){
        PlaywrightAssertions.assertThat(this.postedComment).isVisible();
    }
}
