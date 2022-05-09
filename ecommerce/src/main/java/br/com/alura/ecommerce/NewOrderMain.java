package br.com.alura.ecommerce;


import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.Properties;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
/*Criando Classe de envio de mensagem - Producer*/
public class NewOrderMain {

    /*Classe principal com tratativa de exception referente ao producer.sent().get();*/
    public static void main(String[] args) throws ExecutionException, InterruptedException {

        /* try para fechar o Producer caso haja alguma exception na execução*/
        try(var dispatcher = new KafkaDispatcher()) { /* Criando um KafkaDispatcher para cria um Producer*/
            /* criando 100 mensagens para de nova ordem e e-mail*/
            for (int i = 0; i < 100; i++) {
                /*Declarando uma variável com Valor da KEY*/
                var key = UUID.randomUUID().toString();/*Chave criada aleatóriamente. Ela irá influênciar na distribuição das mensagens para cada partição do topic*/
                /* Denifindo o valor para o metodo send*/
                var value = key + "12313242, 32423423, 5768578685";
                dispatcher.send("ECOMMERCE_NEW_ORDER", key, value);

                /* Denifindo o valor para o metodo send*/
                var email = "Thanks You for your new Order!";
                dispatcher.send("ECOMMERCE_SEND_EMAIL", key, email);
            }
        }
    }


}
