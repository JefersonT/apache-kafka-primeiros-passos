package br.com.alura.ecommerce;

import br.com.alura.ecommerce.consumer.ConsumerService;
import br.com.alura.ecommerce.consumer.ServiceRunner;
import org.apache.kafka.clients.consumer.ConsumerRecord;

public class SendEmailService implements ConsumerService<String> {
    /*Callse principal*/
    public static void main(String[] args) {
        /* Criando um Runner para executar o serviço 5x*/
        new ServiceRunner(SendEmailService::new).start(5);

    }

    /* Definindo o ConsumerGroup do serviço*/
    public String getConsumerGroup(){
        return SendEmailService.class.getSimpleName();
    }

    /* Definindo o Topic do serviço*/
    public String getTopic(){
        return "ECOMMERCE_SEND_EMAIL";
    }

    /* Método que será executando para cada mensagem recebida*/
    public void parse(ConsumerRecord<String, Message<String>> record){
        System.out.println("-----------------------------");
        System.out.println("Processing new order, cheking for fraud");
        System.out.println("Chave: " + record.key());// imprime a chave
        System.out.println("Mensage: " + record.value());// imprime a value
        System.out.println("Offset: " + record.offset());// imprime offset
        System.out.println("Partition: " + record.partition());// imprime a partition
    }
}
/*Definindo Class Consumidora do tópico ECOMMERCE_NEW_ORDER*/
