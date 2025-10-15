package com.example.petstore.tests.store;

import com.example.petstore.client.PetApi;
import com.example.petstore.client.StoreApi;
import com.example.petstore.configuration.TestBase;
import com.example.petstore.models.ApiResponse;
import com.example.petstore.models.Order;
import com.example.petstore.models.Pet;
import com.example.petstore.utils.OrderFactory;
import com.example.petstore.utils.PetFactory;
import io.qameta.allure.Epic;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import java.util.Map;
import java.util.Objects;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@Epic("Store API")
@DisplayName("Store - endpoints")
public class StoreTest extends TestBase {

    StoreApi storeApi = new StoreApi();
    PetApi petApi = new PetApi();
    Long createdPetId;
    Long createdOrderId;

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
        if (createdOrderId != null) {
            try {
                storeApi.deleteOrderById(createdOrderId).extract().statusCode();
            }catch (Exception e) {
            }finally {
                createdOrderId = null;
            }
        }
    }

    @Test
    @DisplayName("(GET) /store/inventory - количество статусов заказа")
    void getInventory() {
        Map<String, Integer> response = storeApi.getInventory200();
        assertThat(response).isNotEmpty();
        for (String key : response.keySet()) {
            assertThat(response.get(key)).isGreaterThan(0);
        }
        assertThat(response.keySet()).containsAnyOf("available", "pending", "sold");
    }

    @ParameterizedTest(name = "(POST) /store/order - создание заказа, status = {0}")
    @EnumSource(Order.Status.class)
    void placeOrder(Order.Status status) {
        Pet pet = petApi.addPet200(PetFactory.createRandomPet(Pet.Status.available));
        createdPetId = pet.getId();
        Order order = OrderFactory.createRandomOrder(pet.getId(), status);
        Order createdOrder = storeApi.placeOrder200(order);
        createdOrderId = createdOrder.getId();

        assertThat(createdOrder.getPetId()).isEqualTo(order.getPetId());
        assertThat(createdOrder.getQuantity()).isEqualTo(order.getQuantity());
        assertThat(createdOrder.getStatus()).isEqualTo(order.getStatus());
    }

    @ParameterizedTest(name = "(POST + DELETE) /store/order '{'id'}' - удаление, status = {0}")
    @EnumSource(Order.Status.class)
    void placeAndDeleteOrder(Order.Status status) {
        Pet pet = petApi.addPet200(PetFactory.createRandomPet(Pet.Status.available));
        createdPetId = pet.getId();
        Order createdOrder = storeApi.placeOrder200(OrderFactory.createRandomOrder(pet.getId(), status));
        createdOrderId = createdOrder.getId();

        ApiResponse response = null;
        for (int i = 0; i < 10; i++) {
            try {
                response = storeApi.deleteOrderById200(createdOrder.getId());
                break;
            }catch (AssertionError e){
                try {
                    Thread.sleep(300);
                } catch (InterruptedException exception) {}
            }
        }
        assertThat(response.getCode()).isEqualTo(200L);
    }

    @ParameterizedTest(name = "(POST + GET) /store/order '{'id'}' - создание и чтение, status = {0}")
    @EnumSource(Order.Status.class)
    void placeAndGetOrder(Order.Status status) {
        Pet pet = petApi.addPet200(PetFactory.createRandomPet(Pet.Status.available));
        createdPetId = pet.getId();
        Order order = OrderFactory.createRandomOrder(pet.getId(), status);
        Order createdOrder = storeApi.placeOrder200(order);
        createdOrderId = createdOrder.getId();

        Order getOrder = null;
        for (int i = 0; i < 10; i++) {
            try {
                getOrder = storeApi.getOrderById200(createdOrder.getId());
                if (getOrder.getId().equals(createdOrder.getId()) && getOrder.getPetId().equals(createdPetId) && getOrder.getStatus() == order.getStatus() && Objects.equals(getOrder.getQuantity(), order.getQuantity())) {
                    break;
                }
            }catch (AssertionError e){
                try {
                    Thread.sleep(300);
                } catch (InterruptedException exception) {}
            }
        }

        assertThat(getOrder.getId()).isEqualTo(createdOrder.getId());
        assertThat(getOrder.getPetId()).isEqualTo(pet.getId());
        assertThat(getOrder.getQuantity()).isEqualTo(order.getQuantity());
        assertThat(getOrder.getStatus()).isEqualTo(order.getStatus());
    }
}
