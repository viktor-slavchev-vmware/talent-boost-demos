package com.vmware.talentboost;

import io.github.cdimascio.dotenv.Dotenv;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.BeforeAll;

/**
 * Base class for common code in all test classes.
 */
public class BaseTest {


    protected static RequestSpecBuilder builder;

    protected static RequestSpecification reqSpec;

    protected static Dotenv dotenv = Dotenv.load();

    @BeforeAll
    static void setUp() {
        String baseURL = dotenv.get("BASE_URL");
        String githubToken = dotenv.get("GITHUB_TOKEN");
        builder = new RequestSpecBuilder();
        builder.setBaseUri(baseURL);
        builder.addHeader("X-GitHub-Api-Version", "2022-11-28");
        builder.addHeader("Authorization", "Bearer " + githubToken); // don't commit valid tokens to a repo :)
        builder.addHeader("Accept", "application/vnd.github+json");
        reqSpec = builder.build();
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
    }
}
