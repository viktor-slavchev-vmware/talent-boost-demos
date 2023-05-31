package com.vmware.talentboost.ui;

import com.microsoft.playwright.*;
import com.microsoft.playwright.assertions.PlaywrightAssertions;
import com.microsoft.playwright.options.AriaRole;
import com.vmware.talentboost.ui.pageobjects.TodosHome;
import com.vmware.talentboost.ui.utils.BrowserFactory;
import com.vmware.talentboost.ui.utils.BrowserTypesEnum;
import org.junit.jupiter.api.*;

import java.awt.*;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class FunctionalUITest {
    public static final String TASK_NAME = "QE FE homework";
    public static final String DESCRIPTION = "Finish your assignment";
    public static final String DATE = "2023-06-07";
    public static final String PRIORITY = "Priority 1";
    private static Browser browser;

    private static Page currentPage;

    BrowserContext context;
    @BeforeAll
    public static void setUp(){
//        playwright = Playwright.create();
//        browser = playwright.firefox().launch(
//                new BrowserType.LaunchOptions().setHeadless(false).setSlowMo(100)
//        );
        browser = BrowserFactory.create(BrowserTypesEnum.FIREFOX, false, 100);
//        currentPage = browser.newPage();
    }


    @BeforeEach
    void createContext(){
//        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        context = browser.newContext();
    }

    @AfterEach
    void closeContext() {
        context.close();
    }

//    @Test
    public void testExample(){
        Page page = context.newPage();
        page.navigate("http://playwright.dev");
        System.out.println(page.title());
        page.pause();
    }

    @Test
    @Order(1)
    public void testLogin(){
        Page loginPage = context.newPage();
        loginPage.navigate("https://todoist.com/");
        loginPage.getByText("Log in").nth(0).click(); // flaky strategy, there's two elements on page that match this description
        loginPage.waitForURL("**/auth/login");
//        assertThat(loginPage.title(), is("Log in to Todoist"));
        PlaywrightAssertions.assertThat(loginPage).hasTitle("Log in to Todoist");
        Locator emailField = loginPage.locator("input#element-0");
        Locator passwordField = loginPage.locator("#element-3");
        emailField.type("vslavchev@vmware.com");
        passwordField.type("pass4test");
        loginPage.getByRole(AriaRole.BUTTON).filter(new Locator.FilterOptions().setHasText("Log in")).click(); // example of filtering
        loginPage.waitForURL("**/app/today");

        // Save storage state into the file.
        context.storageState(new BrowserContext.StorageStateOptions().setPath(Paths.get("state.json")));
    }

    @Test
    @Order(2)
    public void testCreateTask(){
        // Create a new context with the saved storage state.
        context = getLoggedInState();
        currentPage = context.newPage();

        // Without page object
//        Page todoPage = context.newPage();
//        todoPage.navigate("https://todoist.com/");
//        todosPage.waitForURL("**/app/today");

        // With Page object
        TodosHome todosPage = new TodosHome(currentPage);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        todosPage.page.setViewportSize(screenSize.width, screenSize.height);
        todosPage.navigate();
        todosPage.page.waitForURL("**/app/today");
        PlaywrightAssertions.assertThat(todosPage.page).hasTitle("Today: Todoist");

        todosPage.getAddNewTaskButton().click();
        todosPage.createNewTask(TASK_NAME, DESCRIPTION, "Today");
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDateTime today = LocalDateTime.now();

        PlaywrightAssertions.assertThat(todosPage.getTasksContainer(dtf.format(today))).containsText(TASK_NAME);
        PlaywrightAssertions.assertThat(todosPage.getTasksContainer(dtf.format(today))).containsText(DESCRIPTION);

    }

    @Test
    @Order(3)
    public void testDeleteTask(){
        context = getLoggedInState();
        currentPage = context.newPage();

        TodosHome todosPage = new TodosHome(currentPage);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        todosPage.page.setViewportSize(screenSize.width, screenSize.height);
        todosPage.navigate();
        todosPage.page.waitForURL("**/app/today");
        todosPage.deleteTask(TASK_NAME);
        PlaywrightAssertions.assertThat(todosPage.getTasksContainer(DATE)).not().isVisible();
    }

    private static BrowserContext getLoggedInState() {
        return browser.newContext(
                new Browser.NewContextOptions().setStorageStatePath(Paths.get("state.json")));
    }
}
