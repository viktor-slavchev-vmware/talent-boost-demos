package com.vmware.talentboost;

import com.vmware.talentboost.objects.Issue;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.module.jsv.JsonSchemaValidator;
import io.restassured.specification.RequestSpecification;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.junit.AfterClass;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.io.File;
import java.text.MessageFormat;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class RESTAssuredFunctionalTests {

    private static final String OWNER = "viktor-slavchev-vmware";
    private static final String REPO = "talent-boost-demos";

    private String issuesPath = MessageFormat.format("/repos/{0}/{1}/issues", OWNER, REPO);

    private static RequestSpecBuilder builder;

    private static RequestSpecification reqSpec;

    @BeforeAll
    static void setUp() {
        builder = new RequestSpecBuilder();
        builder.setBaseUri("https://api.github.com/");
        builder.addHeader("X-GitHub-Api-Version", "2022-11-28");
        builder.addHeader("Authorization", "Bearer "); // don't commit valid tokens to a repo :)
        builder.addHeader("Accept", "application/vnd.github+json");
        reqSpec = builder.build();
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
    }

    @Test
    public void testGetIssues200() {
        given()
                .spec(reqSpec)
        .when()
            .get(MessageFormat.format("/repos/{0}/{1}/issues", OWNER, REPO))
                .prettyPeek()

        .then()
            .assertThat() // syntactic sugar, we can do it w/o it
            .statusCode(200)
//                .body("size()", is(5)) // anti-pattern, please DON'T do that
                .body("title", hasItems("Issue number 1", "Issue number 2"))
                .rootPath("user") // example of root path usage when we need to get nested property
                .body("login", hasItem(OWNER))
                .body("id", hasItem(77064887));
    }

    @Test
    public void testGetSpecificIssue200(){
        given()
                .spec(reqSpec)
        .when()
            .get(MessageFormat.format("/repos/{0}/{1}/issues/1", OWNER, REPO))
            .prettyPeek()

        .then()
            .assertThat() // syntactic sugar, we can do it w/o it
            .statusCode(200)
            .body("title", is("Issue number 1"));
    }

    @Test
    public void testGetSpecificIssueJsonSchema(){
        given()
                .spec(reqSpec)
        .when()
                .get(MessageFormat.format("/repos/{0}/{1}/issues/1", OWNER, REPO))
                .prettyPeek()

        .then()
            .assertThat()
                .body(JsonSchemaValidator.matchesJsonSchema(new File("src/JSONSchemas/IssueJsonSchema.json")));
    }

    @Test
    public void testSpecificIssueObjectMapping(){
        Issue queriedIssue = given()
            .spec(reqSpec)
        .when()
            .get(MessageFormat.format("/repos/{0}/{1}/issues/1", OWNER, REPO))
                .as(Issue.class);
        assertEquals(queriedIssue.getTitle(), "Issue number 1");
    }

    @ParameterizedTest
    @CsvSource({
            "1, Issue number 1, Some meaningful description",
            "2, Issue number 2, Another meaningful description"
    })
    public void testSpecificIssueDataDriven(int number, String title, String body){
        given()
            .spec(reqSpec)
        .when()
            .get(MessageFormat.format("/repos/{0}/{1}/issues/{2}", OWNER, REPO, number))
            .prettyPeek()

        .then()
            .assertThat() // syntactic sugar, we can do it w/o it
            .statusCode(200)
            .body("title", is(title))
            .body("body", is(body));
    }


    @Test
    public void testPostIssue201(){
        JSONObject postParams = new JSONObject();
        postParams.put("title", "Issue created by API");
        postParams.put("body", "Also created by API");
        JSONArray assignees = new JSONArray();
        assignees.add(OWNER);
        postParams.put("assignees", assignees);
        given()
                .spec(reqSpec)
                .body(postParams.toJSONString())
//                .body("{ \"title\": \"Issue created by API\", ...")
        .when()
                .post(issuesPath)
                .prettyPeek()
        .then()
                .assertThat()
                .statusCode(201);
    }

    @Test
    public void testPatchIssue200(){
        JSONObject postParams = new JSONObject();
        postParams.put("title", "Edited by API");
        postParams.put("body", "Also edited by API");
        given()
                .spec(reqSpec)
                .body(postParams.toJSONString())
        .when()
                .patch(issuesPath + "/5")
                .prettyPeek()
        .then()
            .assertThat()
                .statusCode(200);
    }

    @Test
    @Order(1)
    public void testPutIssueLock204(){
        given()
                .spec(reqSpec)
        .when()
                .put(issuesPath + "/5/lock")
                .prettyPeek()
        .then()
            .assertThat()
                .statusCode(204);

        // example of a second g/w/t chain to validate the result of the first request
        given()
                .spec(reqSpec)
        .when()
                .get(issuesPath + "/5")
        .then()
            .assertThat()
                .body("locked", is(true));

    }

    @Test
    @Order(2)
    public void testDeleteUnlockIssue200(){
        given()
                .spec(reqSpec)
        .when()
                .delete(issuesPath + "/5/lock")
                .prettyPeek()
        .then()
            .assertThat()
                .statusCode(204);
    }

    @Test
    public void testDelete(){
        deleteCreatedIssues();
    }

    @AfterClass
    public static void tearDown(){
        deleteCreatedIssues();
    }

    // Note this won't work, as github API doesn't support deletion of issues, so consider it just POC
    public  static void deleteCreatedIssues(){
        List<Integer> issues =
            given()
                .spec(reqSpec)
            .when()
                .get("/issues")
            .then()
                .extract()
                .jsonPath()
                .get("number");

        for (int issueId: issues){
            given()
                    .spec(reqSpec)
                .when();
//                    .delete(issuesPath + "/" + issueId);
        }
    }
}
