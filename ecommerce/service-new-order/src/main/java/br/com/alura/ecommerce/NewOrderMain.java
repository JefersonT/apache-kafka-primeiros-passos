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

                /*Criando email aleatório temporarioamente*/
                var email = Math.random() + "@email.com";

                /* criando 100 mensagens para de nova ordem e e-mail*/
                for (int i = 0; i < 10; i++) {

                    /*Declarando uma userID Aleatório*/
                    var userID = UUID.randomUUID().toString();/*Chave criada aleatóriamente. Ela irá influênciar na distribuição das mensagens para cada partição do topic*/

                    /* Declarando um orderID Aleatório*/
                    var orderId = UUID.randomUUID().toString();

                    /* Declarando o amount, o valor da orden em BigDecimal*/
                    var amount = BigDecimal.valueOf(Math.random() * 5000 + 1); /* Valor entre 1 e 5000*/



                    /* Criando uma nova Order*/
                    var order = new Order(userID, orderId, amount, email);
                    /* Enviando a orden com o userID para o topic ECOMMERCE_NEW_ORDER*/
                    orderDispatcher.send("ECOMMERCE_NEW_ORDER", userID, order);

                    /* Denifindo o valor para o metodo send*/
                    var emailCode = "Thanks You for your new Order!";

                    /* Enviando a Email com o userID para o topic ECOMMERCE_SEND_EMAIL*/
                    emailDispatcher.send("ECOMMERCE_SEND_EMAIL", userID, emailCode);
                }
            }
        }
    }
}
