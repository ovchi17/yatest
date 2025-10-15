package com.example.petstore.client;

import com.example.petstore.models.ApiResponse;
import com.example.petstore.models.Order;
import io.restassured.response.ValidatableResponse;

import java.util.Map;

import static io.restassured.RestAssured.given;

public class StoreApi {

    public ValidatableResponse placeOrder(Order order) {
        return given()
                    .contentType("application/json")
                    .body(order)
                .when()
                    .post("/store/order")
                .then();
    }

    public ValidatableResponse getOrderById(long id) {
        return given()
                .when()
                    .get("/store/order/{id}", id)
                .then();
    }

    public ValidatableResponse deleteOrderById(long id) {
        return given()
                .when()
                    .delete("/store/order/{id}", id)
                .then();
    }

    public ValidatableResponse getInventory() {
        return given()
                .when()
                    .get("/store/inventory")
                .then();
    }

    public Order placeOrder200(Order order) {
        return placeOrder(order).statusCode(200).extract().response().as(Order.class);
    }

    public Order getOrderById200(Long id) {
        return getOrderById(id).statusCode(200).extract().response().as(Order.class);
    }

    public ApiResponse deleteOrderById200(Long id) {
        return deleteOrderById(id).statusCode(200).extract().response().as(ApiResponse.class);
    }

    public Map<String, Integer> getInventory200(){
        return getInventory().statusCode(200).extract().response().as(Map.class);
    }

}
