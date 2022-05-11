package br.com.alura.ecommerce;

import org.apache.kafka.clients.consumer.ConsumerRecord;

import java.util.Map;

/* Definindo Class Consumidora do tópico ECOMMERCE_NEW_ORDER*/
public class FraudDetectorService {

    /* Class principal*/
    public static void main(String[] args) {

        /* Definindo um novo FraudDetectorService para utilizar seu método parse*/
        var fraudDetector = new FraudDetectorService();

        /* Try para fechar o serviço caso haja algum erro na execução, chamando o serviço para o frauddetector
        * o método KafdService<> GroupID, ConsumerFunction, o Tipo da mensagem, e Map.of() com as configurações especiais do consumer a ser criado */
        try(var service = new KafkaService<>(FraudDetectorService.class.getSimpleName(),
                "ECOMMERCE_NEW_ORDER",
                fraudDetector::parse,
                Order.class,
                Map.of())) {

            /* Executando o Serviço*/
            service.run();
        }

    }

    /* Método que será executando para cada mensagem recebida*/
    private void parse(ConsumerRecord<String, Order> record) {
        System.out.println("-----------------------------");
        System.out.println("Processing new order, cheking for fraud");
        System.out.println("Chave: " + record.key());// imprime a chave
        System.out.println("Mensage: " + record.value());// imprime a value
        System.out.println("Offset: " + record.offset());// imprime offset
        System.out.println("Partition: " + record.partition());// imprime a partition
    }
}

