package com.example.petstore.client;

import com.example.petstore.models.ApiResponse;
import com.example.petstore.models.Pet;
import io.restassured.response.ValidatableResponse;

import java.util.List;

import static io.restassured.RestAssured.given;

public class PetApi {

    public ValidatableResponse addPet(Pet pet) {
        return given()
                    .contentType("application/json")
                    .body(pet)
                .when()
                    .post("/pet")
                .then();
    }

    public ValidatableResponse getPet(long id) {
        return given()
                .when()
                    .get("/pet/{id}", id)
                .then();
    }

    public ValidatableResponse updatePet(Pet pet) {
        return given()
                    .contentType("application/json")
                    .body(pet)
                .when()
                    .put("/pet")
                .then();
    }

    public ValidatableResponse deletePet(long id) {
        return given()
                .when()
                    .delete("/pet/{id}", id)
                .then();
    }

    public ValidatableResponse updatePetByIdUsingFormData(long id, Pet.FormDataForUpdateById formData) {
        return given()
                    .contentType("application/x-www-form-urlencoded")
                    .formParam("name", formData.getName())
                    .formParam("status", formData.getStatus())
                .when()
                    .post("/pet/{id}", id)
                .then();
    }

    public ValidatableResponse uploadPetImageByIdUsingFormData(long id, Pet.FormDataForUploadImage formData) {
        return given()
                    .contentType("multipart/form-data")
                    .multiPart("file", formData.getFile())
                    .multiPart("additionalMetadata", formData.getAdditionalMetadata())
                .when()
                    .post("/pet/{petId}/uploadImage", id)
                .then();
    }

    public ValidatableResponse findPetsByStatus(List<Pet.Status> statuses) {
        return given()
                    .queryParam("status", statuses)
                .when()
                    .get("/pet/findByStatus")
                .then();
    }

    public Pet addPet200(Pet pet) {
        return addPet(pet).statusCode(200).extract().as(Pet.class);
    }
    public Pet getPet200(long id) {
        return getPet(id).statusCode(200).extract().as(Pet.class);
    }
    public Pet updatePet200(Pet pet) {
        return updatePet(pet).statusCode(200).extract().as(Pet.class);
    }
    public ApiResponse deletePet200(long id) {
        return deletePet(id).statusCode(200).extract().as(ApiResponse.class);
    }
    public ApiResponse updatePetByIdUsingFormData200(long id, Pet.FormDataForUpdateById formData) {
        return updatePetByIdUsingFormData(id, formData).statusCode(200).extract().as(ApiResponse.class);
    }
    public ApiResponse uploadPetImageByIdUsingFormData200(long id, Pet.FormDataForUploadImage formData) {
        return uploadPetImageByIdUsingFormData(id, formData).statusCode(200).extract().as(ApiResponse.class);
    }
    public Pet[] findPetsByStatus200(List<Pet.Status> statuses) {
        return findPetsByStatus(statuses).statusCode(200).extract().as(Pet[].class);
    }
}
