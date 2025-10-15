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
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;

@Epic("User API")
@DisplayName("User - дополнительные endpoints")
public class UserAdditionalEndpointsTest extends TestBase {

    UserApi userApi = new UserApi();
    List<String> createdUsernames = new ArrayList<>();

    @AfterEach
    void cleanUp() {
        for (String u : createdUsernames) {
            try { userApi.deleteUserByUsername(u).extract().statusCode(); }
            catch (Exception ignore) {}
        }
        createdUsernames.clear();
    }

    @ParameterizedTest(name = "(POST) /user/createWithArray - пачка, count={0}")
    @ValueSource(ints = {1, 2, 5})
    void createUsersWithArray(int count) {
        User[] payload = IntStream.range(0, count)
                .mapToObj(i -> UserFactory.createRandomUser())
                .toArray(User[]::new);

        ApiResponse response = userApi.createWithArray200(payload);
        assertThat(response.getCode()).isEqualTo(200L);

        for (User expected : payload) {
            createdUsernames.add(expected.getUsername());
            User actual = null;
            for (int i = 0; i < 10; i++) {
                try {
                    actual = userApi.getUserByUsername200(expected.getUsername());
                    break;
                } catch (AssertionError e) {
                    try { Thread.sleep(300); } catch (InterruptedException ignore) {}
                }
            }
            assertThat(actual).isNotNull();
            assertThat(actual.getId()).isEqualTo(expected.getId());
            assertThat(actual.getUsername()).isEqualTo(expected.getUsername());
            assertThat(actual.getEmail()).isEqualTo(expected.getEmail());
            assertThat(actual.getPassword()).isEqualTo(expected.getPassword());
            assertThat(actual.getFirstName()).isEqualTo(expected.getFirstName());
            assertThat(actual.getLastName()).isEqualTo(expected.getLastName());
            assertThat(actual.getPhone()).isEqualTo(expected.getPhone());
            assertThat(actual.getUserStatus()).isEqualTo(expected.getUserStatus());
        }
    }

    @ParameterizedTest(name = "(POST) /user/createWithList — пачка, count={0}")
    @ValueSource(ints = {1, 2, 5})
    void createUsersWithList(int count) {
        List<User> payload = new java.util.ArrayList<>();
        for (int i = 0; i < count; i++) {
            payload.add(UserFactory.createRandomUser());
        }

        ApiResponse response = userApi.createWithList200(payload);
        assertThat(response.getCode()).isEqualTo(200L);

        for (User expected : payload) {
            createdUsernames.add(expected.getUsername());
            User actual = null;
            for (int i = 0; i < 10; i++) {
                try {
                    actual = userApi.getUserByUsername200(expected.getUsername());
                    break;
                } catch (AssertionError e) {
                    try { Thread.sleep(300); } catch (InterruptedException ignore) {}
                }
            }
            assertThat(actual).isNotNull();
            assertThat(actual.getId()).isEqualTo(expected.getId());
            assertThat(actual.getUsername()).isEqualTo(expected.getUsername());
            assertThat(actual.getEmail()).isEqualTo(expected.getEmail());
            assertThat(actual.getPassword()).isEqualTo(expected.getPassword());
            assertThat(actual.getFirstName()).isEqualTo(expected.getFirstName());
            assertThat(actual.getLastName()).isEqualTo(expected.getLastName());
            assertThat(actual.getPhone()).isEqualTo(expected.getPhone());
            assertThat(actual.getUserStatus()).isEqualTo(expected.getUserStatus());
        }
    }

    @Test
    @DisplayName("(GET) /user/login - успешная авторизация")
    void loginUser() {
        User user = UserFactory.createRandomUser();
        userApi.createUser200(user);
        createdUsernames.add(user.getUsername());

        ApiResponse login = null;
        for (int i = 0; i < 10; i++) {
            try {
                login = userApi.login200(user.getUsername(), user.getPassword());
                break;
            } catch (AssertionError e) {
                try { Thread.sleep(300); } catch (InterruptedException ignore) {}
            }
        }

        assertThat(login).isNotNull();
        assertThat(login.getCode()).isEqualTo(200L);
    }

    @Test
    @DisplayName("(GET) /user/logout - успешный выход (после login)")
    void logoutUser() {
        User user = UserFactory.createRandomUser();
        userApi.createUser200(user);
        createdUsernames.add(user.getUsername());

        ApiResponse login = null;
        for (int i = 0; i < 10; i++) {
            try {
                login = userApi.login200(user.getUsername(), user.getPassword());
                break;
            } catch (AssertionError e) {
                try { Thread.sleep(300); } catch (InterruptedException ignore) {}
            }
        }
        assertThat(login).isNotNull();
        assertThat(login.getCode()).isEqualTo(200L);

        ApiResponse logout = null;
        for (int i = 0; i < 10; i++) {
            try {
                logout = userApi.logout200();
                break;
            } catch (AssertionError e) {
                try { Thread.sleep(300); } catch (InterruptedException ignore) {}
            }
        }
        assertThat(logout).isNotNull();
        assertThat(logout.getCode()).isEqualTo(200L);
        assertThat(logout.getType()).isEqualTo("unknown");
    }
}
