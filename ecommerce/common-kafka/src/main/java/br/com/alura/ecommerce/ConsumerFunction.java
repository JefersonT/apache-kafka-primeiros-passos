package br.com.alura.ecommerce;

import org.apache.kafka.clients.consumer.ConsumerRecord;

/* Esta Interface define uma função a qual irá manipular as respostas recebidas pelo concumer*/
public interface ConsumerFunction<T> {
    void consume(ConsumerRecord<String, T> record);
}
