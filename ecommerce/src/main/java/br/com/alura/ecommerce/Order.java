package br.com.alura.ecommerce;

import java.math.BigDecimal;

public class Order {

    private final String userID, orderId;
    private final BigDecimal value;

    public Order(String userID, String orderId, BigDecimal value) {
        this.userID = userID;
        this.orderId = orderId;
        this.value = value;
    }
}
