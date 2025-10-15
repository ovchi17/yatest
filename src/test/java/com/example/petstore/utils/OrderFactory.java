package com.example.petstore.utils;

import com.example.petstore.models.Order;

import java.time.OffsetDateTime;
import java.util.Random;

public class OrderFactory {

    static Random random = new Random();

    public static Order createRandomOrder(Long petId, Order.Status status) {
        Order order = new Order();
        order.setId(5L + random.nextInt(5));
        order.setPetId(petId);
        order.setQuantity(1 + random.nextInt(10));
        order.setShipDate(OffsetDateTime.now().toString());
        order.setStatus(status);
        order.setComplete(false);
        return order;
    }
}
