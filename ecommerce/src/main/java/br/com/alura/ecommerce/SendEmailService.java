package br.com.alura.ecommerce;

import org.apache.kafka.clients.consumer.ConsumerRecord;

import java.util.Map;

/*Definindo Class Consumidora do tópico ECOMMERCE_NEW_ORDER*/
public class SendEmailService {
    /*Callse principal*/
    public static void main(String[] args) {

        /* Criando um novo SendEmailService para utilizar seu metodo parse*/
        var sendEmail = new SendEmailService();

        /* Try para fechar o serviço caso haja algum erro na execução, chamando o serviço para o SendEmailService*/
        try(var service = new KafkaService(SendEmailService.class.getSimpleName(),
                "ECOMMERCE_SEND_EMAIL",
                sendEmail::parse,
                String.class,
                Map.of())) {
            service.run();
        }
        
    }
    
    private void parse(ConsumerRecord<String, String> record){
        System.out.println("-----------------------------");
        System.out.println("Processing new order, cheking for fraud");
        System.out.println("Chave: " + record.key());// imprime a chave
        System.out.println("Mensage: " + record.value());// imprime a value
        System.out.println("Offset: " + record.offset());// imprime offset
        System.out.println("Partition: " + record.partition());// imprime a partition
    }
}
