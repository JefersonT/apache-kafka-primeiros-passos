package br.com.alura.ecommerce;

import java.math.BigDecimal;
/* Classe Order para utilizar como mensagem de envio referente*/
public class Order {

    /* Dados da ordem, ID do usuário, ID da ordem e valor da ordem*/
    private final String orderId;
    private final BigDecimal value;
    private final String email;

    /* Contrutor recebando os dados da Ordem*/
    public Order(String orderId, BigDecimal value, String email) {
        this.orderId = orderId;
        this.value = value;
        this.email = email;
    }

    public BigDecimal getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "Order{" +
                "orderId='" + orderId + '\'' +
                ", value=" + value +
                ", email='" + email + '\'' +
                '}';
    }

    public String getEmail() {
        return this.email;
    }
}
