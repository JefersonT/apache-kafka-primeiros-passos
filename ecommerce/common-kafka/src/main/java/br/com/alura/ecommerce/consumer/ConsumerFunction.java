package br.com.alura.ecommerce.consumer;

import br.com.alura.ecommerce.Message;
import org.apache.kafka.clients.consumer.ConsumerRecord;

import java.util.concurrent.ExecutionException;

/* Esta Interface define uma função a qual irá manipular as respostas recebidas pelo concumer*/
public interface ConsumerFunction<T> {
    void consume(ConsumerRecord<String, Message<T>> record) throws Exception;
}
