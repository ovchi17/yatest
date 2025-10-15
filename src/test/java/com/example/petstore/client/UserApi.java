package com.example.petstore.client;

import com.example.petstore.models.ApiResponse;
import com.example.petstore.models.User;
import io.restassured.response.ValidatableResponse;

import java.util.List;

import static io.restassured.RestAssured.given;

public class UserApi {

    public ValidatableResponse createUser(User user) {
        return given()
                .contentType("application/json")
                .body(user)
                .when()
                .post("/user")
                .then();
    }

    public ValidatableResponse getUserByUsername(String username) {
        return given()
                .when()
                .get("/user/{username}", username)
                .then();
    }

    public ValidatableResponse deleteUserByUsername(String username) {
        return given()
                .when()
                .delete("/user/{username}", username)
                .then();
    }

    public ValidatableResponse updateUserByUsername(String username, User user) {
        return given()
                .contentType("application/json")
                .body(user)
                .when()
                .put("/user/{username}", username)
                .then();
    }

    public ValidatableResponse createWithArray(User[] users) {
        return given()
                    .contentType("application/json")
                    .body(users)
                .when()
                    .post("/user/createWithArray")
                .then();
    }

    public ValidatableResponse createWithList(List<User> users) {
        return given()
                    .contentType("application/json")
                    .body(users)
                .when()
                    .post("/user/createWithList")
                .then();
    }

    public ValidatableResponse login(String username, String password) {
        return given()
                    .queryParam("username", username)
                    .queryParam("password", password)
                .when()
                    .get("/user/login")
                .then();
    }

    public ValidatableResponse logout() {
        return given()
                .when()
                    .get("/user/logout")
                .then();
    }

    public ApiResponse createUser200(User user) {
        return createUser(user).statusCode(200).extract().response().as(ApiResponse.class);
    }

    public User getUserByUsername200(String username) {
        return getUserByUsername(username).statusCode(200).extract().response().as(User.class);
    }

    public ApiResponse deleteUserByUsername200(String username) {
        return deleteUserByUsername(username).statusCode(200).extract().response().as(ApiResponse.class);
    }

    public ApiResponse updateUserByUsername200(String username, User user) {
        return updateUserByUsername(username, user).statusCode(200).extract().as(ApiResponse.class);
    }
    public ApiResponse createWithArray200(User[] array){
        return createWithArray(array).statusCode(200).extract().as(ApiResponse.class);
    }
    public ApiResponse createWithList200(List<User> list){
        return createWithList(list).statusCode(200).extract().as(ApiResponse.class);
    }
    public ApiResponse login200(String username, String password){
        return login(username, password).statusCode(200).extract().as(ApiResponse.class);
    }
    public ApiResponse logout200(){
        return logout().statusCode(200).extract().as(ApiResponse.class);
    }
}