package br.com.alura.ecommerce.consumer;

import br.com.alura.ecommerce.Message;
import org.apache.kafka.clients.consumer.ConsumerRecord;

import java.io.IOException;

/* Interface para criar um serviço consumer
*  Ele irá padronizar os dados necessários para para criação de um consumer*/
public interface ConsumerService<T> {

    /* Método que será executando para cada Message*/
    void parse(ConsumerRecord<String, Message<T>> record) throws IOException;
    /* método onde será atriuido o consumerGroup do consumer a ser criado*/
    String getConsumerGroup();

    /* método onde será atriuido o Topic escutado do consumer a ser criado*/
    String getTopic();
}
