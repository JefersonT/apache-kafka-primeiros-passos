package br.com.alura.ecommerce;

import br.com.alura.ecommerce.consumer.KafkaService;
import org.apache.kafka.clients.consumer.ConsumerRecord;

import java.util.Map;
import java.util.concurrent.ExecutionException;

public class SendEmailService implements ConsumerService<String> {
    /*Callse principal*/
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        new ServiceProvider().run(SendEmailService::new);

    }

    public String getConsumerGroup(){
        return SendEmailService.class.getSimpleName();
    }

    public String getTopic(){
        return "ECOMMERCE_SEND_EMAIL";
    }

    /* Método que será executando para cada mensagem recebida*/
    public void parse(ConsumerRecord<String, String> record){
        System.out.println("-----------------------------");
        System.out.println("Processing new order, cheking for fraud");
        System.out.println("Chave: " + record.key());// imprime a chave
        System.out.println("Mensage: " + record.value());// imprime a value
        System.out.println("Offset: " + record.offset());// imprime offset
        System.out.println("Partition: " + record.partition());// imprime a partition
    }
}
/*Definindo Class Consumidora do tópico ECOMMERCE_NEW_ORDER*/
