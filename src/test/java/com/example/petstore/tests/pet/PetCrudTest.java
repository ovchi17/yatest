package com.example.petstore.tests.pet;

import com.example.petstore.client.PetApi;
import com.example.petstore.configuration.TestBase;
import com.example.petstore.models.ApiResponse;
import com.example.petstore.models.Pet;
import com.example.petstore.utils.PetFactory;
import io.qameta.allure.Epic;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@Epic("Pet API")
@DisplayName("Pet - CRUD endpoints")
public class PetCrudTest extends TestBase {

    private final PetApi petApi = new PetApi();
    private Long createdPetId;

    @AfterEach
    void cleanUp() {
        if (createdPetId != null) {
            try {
                petApi.deletePet(createdPetId).extract().statusCode();
            }catch (Exception e) {
            }finally {
                createdPetId = null;
            }
        }
    }

    @Test
    @DisplayName("(POST) /pet - Создание питомца и сравнение полей")
    void createPet() {
        Pet newPet = PetFactory.createRandomPet(Pet.Status.available);
        Pet createdPet = petApi.addPet200(newPet);
        createdPetId = createdPet.getId();

        assertThat(createdPet.getId()).isEqualTo(newPet.getId());
        assertThat(createdPet.getName()).isEqualTo(newPet.getName());
        assertThat(createdPet.getStatus()).isEqualTo(newPet.getStatus());
    }

    @Test
    @DisplayName("(POST + GET) /pet/{petId} - Создание питомца и его считывание")
    void createAndGetPet() {
        Pet newPet = PetFactory.createRandomPet(Pet.Status.available);
        Pet createdPet = petApi.addPet200(newPet);
        createdPetId = createdPet.getId();

        Pet getPet = null;
        for (int i = 0; i < 10; i++) {
            try {
                getPet = petApi.getPet200(createdPet.getId());
                break;
            }catch (AssertionError e){
                try {
                    Thread.sleep(300);
                } catch (InterruptedException exception) {}
            }
        }
        assertThat(getPet.getId()).isEqualTo(createdPet.getId());
        assertThat(getPet.getName()).isEqualTo(createdPet.getName());
        assertThat(getPet.getStatus()).isEqualTo(newPet.getStatus());
    }

    @Test
    @DisplayName("(POST + PUT) /pet - Создание питомца и его изменение")
    void createAndUpdatePet () {
        Pet newPet = PetFactory.createRandomPet(Pet.Status.available);
        Pet createdPet = petApi.addPet200(newPet);
        createdPetId = createdPet.getId();
        Pet toUpdateCat = PetFactory.createRandomUpdatedPet(createdPet, Pet.Status.sold);

        Pet updatedPet = null;
        for (int i = 0; i < 10; i++) {
            try {
                updatedPet = petApi.updatePet200(toUpdateCat);
                break;
            }catch (AssertionError e){
                try {
                    Thread.sleep(300);
                } catch (InterruptedException exception) {}
            }
        }
        assertThat(updatedPet.getId()).isEqualTo(createdPet.getId());
        assertThat(updatedPet.getId()).isEqualTo(toUpdateCat.getId());
        assertThat(updatedPet.getName()).isEqualTo(toUpdateCat.getName());
        assertThat(updatedPet.getStatus()).isEqualTo(toUpdateCat.getStatus());
    }

    @Test
    @DisplayName("(POST + DELETE) /pet/{petId} - Создание питомца и его удаление")
    void createAndDeletePet() {
        Pet newPet = PetFactory.createRandomPet(Pet.Status.available);
        Pet createdPet = petApi.addPet200(newPet);

        ApiResponse apiResponse = null;
        for (int i = 0; i < 10; i++) {
            try {
                apiResponse = petApi.deletePet200(createdPet.getId());
                break;
            }catch (AssertionError e){
                try {
                    Thread.sleep(300);
                } catch (InterruptedException exception) {}
            }
        }
        assertThat(apiResponse.getCode()).isEqualTo(200L);
        assertThat(apiResponse.getMessage()).isEqualTo(createdPet.getId().toString());
    }
}
