package com.example.petstore.configuration;

import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeAll;

public abstract class TestBase {

    @BeforeAll
    protected static void setup() {
        RestAssured.baseURI = "https://petstore.swagger.io";;
        RestAssured.basePath = "/v2";
        RestAssured.filters(new AllureRestAssured());
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
    }
}
