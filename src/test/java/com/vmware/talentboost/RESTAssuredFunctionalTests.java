package com.vmware.talentboost;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;
import org.junit.jupiter.params.provider.ArgumentsSource;

import java.util.stream.Stream;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class RESTAssuredFunctionalTests {

    @BeforeAll
    static void setUp() {

        RestAssured.baseURI = "https://swapi.dev/api/";
    }

    @Test
    public void testGetRequest200() {
        Response response = given()
                .when()
                .get("/people/2")
                .then()
                .extract().response();

        assertEquals(200, response.statusCode());
        assertEquals("C-3PO", response.jsonPath().getString("name"));
    }

//    @ParameterizedTest
////    @CsvSource({
////            "1, 200, Luke Skywalker",
////            "2, 200, C-3PO",
////            "3, 200, R2-D2"
////    })
//    @ArgumentsSource(SWArgumentsProvider.class)
//    public void testGetRequestLuke(int id, int statusCode, String name) {
//
//        Response response = given()
//                .when()
//                .get("/people/" + id)
//                .then()
//                .extract().response();
//
//        assertEquals(statusCode, response.statusCode());
//        assertEquals(name, response.jsonPath().getString("name"));
//    }
//
//    static class SWArgumentsProvider implements ArgumentsProvider {
//
//        @Override
//        public Stream<? extends Arguments> provideArguments(ExtensionContext extensionContext) throws Exception {
//            return Stream.of(
//                    Arguments.of(1, 200, "Luke Skywalker"),
//                    Arguments.of(2, 200, "C-3PO"));
//        }
//    }

//    @Test
//    public void testGetRequestC3PO() {
//
//        Response response = given()
//                .when()
//                .get("/people/2")
//                .then()
//                .extract().response();
//
//        assertEquals(200, response.statusCode());
//        assertEquals("C-3PO", response.jsonPath().getString("name"));
//
//    }
//
//    @Test
//    public void testGetRequestR2D2() {
//
//        Response response = given()
//                .when()
//                .get("/people/3")
//                .then()
//                .extract().response();
//
//        assertEquals(200, response.statusCode());
//        assertEquals("R2-D2", response.jsonPath().getString("name"));
//
//    }
}
