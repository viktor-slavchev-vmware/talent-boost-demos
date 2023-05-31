package com.vmware.talentboost.ui;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.assertions.PlaywrightAssertions;
import com.vmware.talentboost.ui.pageobjects.LoginPage;
import com.vmware.talentboost.ui.pageobjects.ProjectPage;
import com.vmware.talentboost.ui.pageobjects.TodosHome;
import com.vmware.talentboost.ui.utils.BrowserFactory;
import com.vmware.talentboost.ui.utils.BrowserTypesEnum;
import com.vmware.talentboost.ui.utils.Utils;
import io.github.cdimascio.dotenv.Dotenv;
import org.junit.jupiter.api.*;

import java.awt.*;
import java.io.File;
import java.nio.file.Paths;

import static org.junit.Assert.assertThat;

public class E2EUITest {

    public static final String STATE_JSON = "state.json";

    private static final String PROJECT_NAME = "Talent boost";
    static BrowserContext context;

    private static Browser browser;

    private static File stateFile;

    private static Page currentPage;

    private static Dotenv dotenv = Dotenv.load();

    private static Boolean isNewLogin;
    @BeforeAll
    public static void setUp(){
        browser = BrowserFactory.create(BrowserTypesEnum.FIREFOX, false, 300);
        stateFile = new File(STATE_JSON);
        context = browser.newContext();

        if (!(stateFile.exists())){
            currentPage = context.newPage();
            String username = dotenv.get("UI_UNSERNAME");
            String password = dotenv.get("UI_PASSWORD");
            LoginPage loginPage = new LoginPage(currentPage);
            loginPage.navigate();
            loginPage.login(username, password);
            // Save storage state into the file.
            context.storageState(new BrowserContext.StorageStateOptions().setPath(Paths.get(STATE_JSON)));
            isNewLogin = true;

        }else {
            context = Utils.getLoggedInState(browser);
            currentPage = context.newPage();
            isNewLogin = false;

        }
    }

    @BeforeEach
    void createContext(){
        if (context == null){
            context = browser.newContext();
            currentPage = context.newPage();
        }
        context = Utils.getLoggedInState(browser);
    }

    @AfterEach
    void closeContext() {
        context.close();
    }

    @AfterAll
    static void cleanUpState(){
//        stateFile.delete();
    }

    /**
     * Scenario:
     * 1. Login OK
     * 2. Create project from side bar
     *  Add name, add to favs OK
     * 4. Add 2 tasks to it: OK
     *  finish project for today OK
     *  Rock on presentation & Party - for 07 of June OK
     * 5. Complete the first one OK
     * 6. Add comment I'm so excited to the second one. OK
     */

    @Test
    public void createBoardWithTasksAndUpdateTest(){
        //Initialize landing page and fix screen resolution to get to the size of the screen.
        TodosHome todosHomePage = new TodosHome(currentPage);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        todosHomePage.page.setViewportSize(screenSize.width, screenSize.height);
        // When we land after login we don't need
        if (!(isNewLogin)){
            todosHomePage.navigate();
        }
        todosHomePage.page.waitForURL("**/app/today");

        // Create project
        todosHomePage.getSideNavBar().createNewProject(PROJECT_NAME, true);
        ProjectPage projectPage = new ProjectPage(currentPage);
        PlaywrightAssertions.assertThat(projectPage.getProjectHeading()).containsText(PROJECT_NAME);

        // Create two tasks, one current one upcoming
        String currentTaskHeading = "Complete TB project";
        todosHomePage.getAddNewTaskButton().click();
        todosHomePage.createNewTask(currentTaskHeading, "Finish all your tasks!", "Today");
        String futureTaskHeading = "Party like a rock star!";
        todosHomePage.createNewTask(futureTaskHeading, "Remove your head", "7 June");

        //Mark first task as complete.
        todosHomePage.getCompleteTaskButton().first().click();
        todosHomePage.expandCompletedItems();
        todosHomePage.assertTaskAppearsAsCompleted(currentTaskHeading);

        // Open the second one and add comment to it.
        todosHomePage.selectTask(futureTaskHeading);
        todosHomePage.getCommentsButton().click();
        String comment = "I'm soooo excited about the party!";
        todosHomePage.writeComment(comment);
        todosHomePage.assertCommentIsVisible(comment);
    }

}
