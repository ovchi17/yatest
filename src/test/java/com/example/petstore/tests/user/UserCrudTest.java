package com.example.petstore.tests.user;

import com.example.petstore.client.UserApi;
import com.example.petstore.configuration.TestBase;
import com.example.petstore.models.ApiResponse;
import com.example.petstore.models.User;
import com.example.petstore.utils.UserFactory;
import io.qameta.allure.Epic;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@Epic("User API")
@DisplayName("User - CRUD endpoints")
public class UserCrudTest extends TestBase {

    UserApi userApi = new UserApi();
    String createdUserUsername;

    @AfterEach
    void cleanUp() {
        if (createdUserUsername != null) {
            try {
                userApi.deleteUserByUsername(createdUserUsername).extract().statusCode();
            }catch (Exception e) {
            }finally {
                createdUserUsername = null;
            }
        }
    }

    @Test
    @DisplayName("(POST) /user - создание пользователя")
    void createUser() {
        User user = UserFactory.createRandomUser();
        ApiResponse response = userApi.createUser200(user);
        createdUserUsername = user.getUsername();

        assertThat(response.getCode()).isEqualTo(200L);
        assertThat(response.getMessage()).isEqualTo(user.getId().toString());
    }

    @Test
    @DisplayName("(POST + GET) /user/{username} - создание и получение пользователя по username")
    void createAndGetUserByUsername() {
        User user = UserFactory.createRandomUser();
        userApi.createUser200(user);
        createdUserUsername = user.getUsername();

        User getUser = null;
        for (int i = 0; i < 10; i++) {
            try {
                getUser = userApi.getUserByUsername200(user.getUsername());
                break;
            }catch (AssertionError e){
                try {
                    Thread.sleep(300);
                } catch (InterruptedException exception) {}
            }
        }

        assertThat(getUser).isNotNull();
        assertThat(getUser.getId()).isEqualTo(user.getId());
        assertThat(getUser.getUsername()).isEqualTo(user.getUsername());
        assertThat(getUser.getEmail()).isEqualTo(user.getEmail());
        assertThat(getUser.getPassword()).isEqualTo(user.getPassword());
        assertThat(getUser.getFirstName()).isEqualTo(user.getFirstName());
        assertThat(getUser.getLastName()).isEqualTo(user.getLastName());
        assertThat(getUser.getPhone()).isEqualTo(user.getPhone());
        assertThat(getUser.getUserStatus()).isEqualTo(user.getUserStatus());
    }

    @Test
    @DisplayName("(POST + PUT + GET) /user/{username} - обновление данных пользователя по username")
    void createAndUpdateUserByUsername() {
        User user = UserFactory.createRandomUser();
        userApi.createUser200(user);
        User updatedUser = UserFactory.createRandomUser();

        ApiResponse response = null;
        for (int i = 0; i < 10; i++) {
            try {
                response = userApi.updateUserByUsername200(user.getUsername(), updatedUser);
                break;
            }catch (AssertionError e){
                try {
                    Thread.sleep(300);
                } catch (InterruptedException exception) {}
            }
        }
        assertThat(response.getCode()).isEqualTo(200L);
        assertThat(response.getMessage()).isEqualTo(updatedUser.getId().toString());

        User getUser = null;
        for (int i = 0; i < 10; i++) {
            try {
                getUser = userApi.getUserByUsername200(updatedUser.getUsername());
                break;
            }catch (AssertionError e){
                try {
                    Thread.sleep(300);
                } catch (InterruptedException exception) {}
            }
        }
        createdUserUsername = getUser.getUsername();

        assertThat(getUser).isNotNull();
        assertThat(getUser.getId()).isEqualTo(updatedUser.getId());
        assertThat(getUser.getUsername()).isEqualTo(updatedUser.getUsername());
        assertThat(getUser.getEmail()).isEqualTo(updatedUser.getEmail());
        assertThat(getUser.getPassword()).isEqualTo(updatedUser.getPassword());
        assertThat(getUser.getFirstName()).isEqualTo(updatedUser.getFirstName());
        assertThat(getUser.getLastName()).isEqualTo(updatedUser.getLastName());
        assertThat(getUser.getPhone()).isEqualTo(updatedUser.getPhone());
        assertThat(getUser.getUserStatus()).isEqualTo(updatedUser.getUserStatus());
    }

    @Test
    @DisplayName("(POST + DELETE + GET) /user/{username} - удаление пользователя и проверка 404")
    void createAndDeleteUserByUsername() {
        User user = UserFactory.createRandomUser();
        userApi.createUser200(user);

        ApiResponse response = null;
        for (int i = 0; i < 10; i++) {
            try {
                response = userApi.deleteUserByUsername200(user.getUsername());
                break;
            }catch (AssertionError e){
                try {
                    Thread.sleep(300);
                } catch (InterruptedException exception) {}
            }
        }
        assertThat(response.getCode()).isEqualTo(200L);

        ApiResponse notFoundResponse = null;
        for (int i = 0; i < 10; i++) {
            try {
                notFoundResponse = userApi.getUserByUsername(user.getUsername()).statusCode(404).extract().as(ApiResponse.class);
                break;
            } catch (AssertionError e) {
                try { Thread.sleep(300); } catch (InterruptedException ignore) {}
            }
        }

        assertThat(notFoundResponse).isNotNull();
        assertThat(notFoundResponse.getMessage()).isEqualTo("User not found");
        assertThat(notFoundResponse.getType()).isEqualTo("error");
    }
}
