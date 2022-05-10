package br.com.alura.ecommerce;


import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;

import java.math.BigDecimal;
import java.util.Properties;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
/*Criando Classe de envio de mensagem - Producer*/
public class NewOrderMain {

    /*Classe principal com tratativa de exception referente ao producer.sent().get();*/
    public static void main(String[] args) throws ExecutionException, InterruptedException {

        /* try para fechar o Producer caso haja alguma exception na execução*/
        try(var orderDispatcher = new KafkaDispatcher<Order>()) { /* Criando um KafkaDispatcher para cria um Producer*//* try para fechar o Producer caso haja alguma exception na execução*/
            try(var emailDispatcher = new KafkaDispatcher<String>()) { /* Criando um KafkaDispatcher para cria um Producer*/
                /* criando 100 mensagens para de nova ordem e e-mail*/
                for (int i = 0; i < 100; i++) {
                    /*Declarando uma userID*/
                    var userID = UUID.randomUUID().toString();/*Chave criada aleatóriamente. Ela irá influênciar na distribuição das mensagens para cada partição do topic*/

                    /* Declarando um orderID*/
                    var orderId = UUID.randomUUID().toString();

                    /* Declarando o amount, o valor da orden em BigDecimal*/
                    var amount = new BigDecimal(Math.random() * 5000 + 1); /* Valor entre 1 e 5000*/

                    /* Criando uma nova Order*/
                    var order = new Order(userID, orderId, amount);

                    orderDispatcher.send("ECOMMERCE_NEW_ORDER", userID, order);

                    /* Denifindo o valor para o metodo send*/
                    var email = "Thanks You for your new Order!";
                    emailDispatcher.send("ECOMMERCE_SEND_EMAIL", userID, email);
                }
            }
        }
    }


}
