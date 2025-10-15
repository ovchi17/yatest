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
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.EnumSource;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Epic("Pet API")
@DisplayName("Pet - дополнительные endpoints")
public class PetAdditionalEndpointsTest extends TestBase {

    PetApi petApi = new PetApi();
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
    @DisplayName("(POST) /pet/{petId} - обновление имени/статуса по id и проверка через GET")
    void updatePetWithFormDataById(){
        Pet newPet = PetFactory.createRandomPet(Pet.Status.available);
        Pet createdPet = petApi.addPet200(newPet);
        createdPetId = createdPet.getId();
        Pet.FormDataForUpdateById formData = PetFactory.createRandomPetFormDataForUpdateById(Pet.Status.sold);
        ApiResponse apiResponse = null;

        for (int i = 0; i < 10; i++) {
            try {
                apiResponse = petApi.updatePetByIdUsingFormData200(createdPet.getId(), formData);
                break;
            }catch (AssertionError e){
                try {
                    Thread.sleep(300);
                } catch (InterruptedException exception) {}
            }
        }

        assertThat(apiResponse.getCode()).isEqualTo(200L);
        assertThat(apiResponse.getMessage()).isEqualTo(createdPet.getId().toString());

        Pet updatedPet = null;
        for (int i = 0; i < 10; i++) {
            try {
                updatedPet = petApi.getPet200(createdPet.getId());
                break;
            } catch (AssertionError e) {
                try {
                    Thread.sleep(300);
                } catch (InterruptedException exception) {}
            }
        }
        assertThat(updatedPet.getName()).isEqualTo(formData.getName());
        assertThat(updatedPet.getStatus()).isEqualTo(formData.getStatus());
    }

    @ParameterizedTest(name = "(GET) /pet/findByStatus - одиночный статус = {0}")
    @EnumSource(Pet.Status.class)
    void getAllPetsByOneStatus(Pet.Status status){
        Pet[] petsList = petApi.findPetsByStatus200(List.of(status));
        for (Pet pet : petsList) {
            assertThat(pet.getStatus()).isEqualTo(status);
        }
    }

    @ParameterizedTest(name = "(GET) /pet/findByStatus - комбинация из двух: {0} + {1}")
    @CsvSource({
            "available, pending",
            "available, sold",
            "pending, sold"
    })
    void getAllPetsByTwoStatuses(String s1, String s2){
        Pet[] petsList = petApi.findPetsByStatus200(List.of(Pet.Status.valueOf(s1), Pet.Status.valueOf(s2)));
        HashSet<Pet.Status> statuses = new HashSet<>();
        statuses.add(Pet.Status.valueOf(s1));
        statuses.add(Pet.Status.valueOf(s2));
        for (Pet pet : petsList) {
            assertThat(statuses).contains(pet.getStatus());
        }
    }

    @Test
    @DisplayName("(POST) /pet/{petId}/uploadImage - загрузка изображения")
    void uploadPetImageWithFormDataById() throws IOException {
        Pet newPet = PetFactory.createRandomPet(Pet.Status.available);
        Pet createdPet = petApi.addPet200(newPet);
        createdPetId = createdPet.getId();
        File tempImage = File.createTempFile("petImage", ".png");
        Pet.FormDataForUploadImage formData = new Pet.FormDataForUploadImage("petPhoto", tempImage);

        ApiResponse apiResponse = null;
        for (int i = 0; i < 10; i++) {
            try {
                apiResponse = petApi.uploadPetImageByIdUsingFormData200(createdPet.getId(), formData);
                break;
            }catch (AssertionError e){
                try {
                    Thread.sleep(300);
                } catch (InterruptedException exception) {}
            }
        }
        assertThat(apiResponse.getCode()).isEqualTo(200L);
        assertThat(apiResponse.getMessage()).contains(tempImage.getName());
        assertThat(apiResponse.getMessage()).contains(formData.getAdditionalMetadata());
        assertThat(apiResponse.getMessage()).contains("File uploaded");
        tempImage.deleteOnExit();
    }
}
