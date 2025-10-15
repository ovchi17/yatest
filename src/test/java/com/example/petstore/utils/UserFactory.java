package com.example.petstore.utils;

import com.example.petstore.models.User;

import java.util.Random;

public class UserFactory {

    static Random random = new Random();

    public static User createRandomUser() {
        User user = new User();
        user.setId(System.currentTimeMillis() % 10000 + random.nextInt(10000));
        user.setUsername("User" + user.getId());
        user.setFirstName("FirstName" + user.getId());
        user.setLastName("LastName" + user.getId());
        user.setEmail("email" + user.getId() + "@email.com");
        user.setPassword("password" + user.getId());
        user.setPhone("phone" + user.getId());
        user.setUserStatus(random.nextInt(100) + 1);
        return user;
    }
}
