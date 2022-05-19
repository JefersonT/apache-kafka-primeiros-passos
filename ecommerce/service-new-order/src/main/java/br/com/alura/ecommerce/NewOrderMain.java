package br.com.alura.ecommerce;

import java.math.BigDecimal;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

/*Criando Classe de envio de mensagem - Producer*/
public class NewOrderMain {

    /*Classe principal com tratativa de exception referente ao producer.sent().get();*/
    public static void main(String[] args) throws ExecutionException, InterruptedException {

        /* try para fechar o Producer da Orden caso haja alguma exception na execução*/
        try(var orderDispatcher = new KafkaDispatcher<Order>()) { /* Criando um KafkaDispatcher para cria um Producer*/

            /* try para fechar o Producer da Email caso haja alguma exception na execução*/
            try(var emailDispatcher = new KafkaDispatcher<String>()) { /* Criando um KafkaDispatcher para cria um Producer*/

                /* criando 100 mensagens para de nova ordem e e-mail*/
                for (int i = 0; i < 10; i++) {
                    /*Criando email aleatório temporarioamente*/
                    var email = Math.random() + "@email.com";

                    /* Declarando um orderID Aleatório*/
                    var orderId = UUID.randomUUID().toString();

                    /* Declarando o amount, o valor da orden em BigDecimal*/
                    var amount = BigDecimal.valueOf(Math.random() * 5000 + 1); /* Valor entre 1 e 5000*/

                    /* Criando uma nova Order*/
                    var order = new Order(orderId, amount, email);
                    /* Enviando a orden com o userID para o topic ECOMMERCE_NEW_ORDER*/
                    orderDispatcher.send("ECOMMERCE_NEW_ORDER",
                            email,
                            order,
                            new CorrelationId(NewOrderMain.class.getSimpleName()));// CorrelationId inicial,
                    // como este é o primeiro a disparar uma mensagem ele receber um novo id para ser passado para os proximos

                    /* Denifindo o valor para o metodo send*/
                    var emailCode = "Thanks You for your new Order!";

                    /* Enviando a Email com o userID para o topic ECOMMERCE_SEND_EMAIL*/
                    emailDispatcher.send("ECOMMERCE_SEND_EMAIL",
                            email,
                            emailCode,
                            new CorrelationId(NewOrderMain.class.getSimpleName()));// CorrelationId inicial,
                    // como este é o primeiro a disparar uma mensagem ele receber um novo id para ser passado para os proximos
                }
            }
        }
    }
}
