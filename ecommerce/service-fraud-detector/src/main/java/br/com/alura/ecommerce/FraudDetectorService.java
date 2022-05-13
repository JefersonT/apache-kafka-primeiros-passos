package br.com.alura.ecommerce;

import org.apache.kafka.clients.consumer.ConsumerRecord;

import java.math.BigDecimal;
import java.util.Map;
import java.util.concurrent.ExecutionException;

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

    private final KafkaDispatcher<Order> orderDispatcher = new KafkaDispatcher<>();


    /* Método que será executando para cada mensagem recebida*/
    private void parse(ConsumerRecord<String, Order> record) throws ExecutionException, InterruptedException {
        System.out.println("-----------------------------");
        System.out.println("Processing new order, cheking for fraud");
        System.out.println("Chave: " + record.key());// imprime a chave
        System.out.println("Mensage: " + record.value());// imprime a value
        System.out.println("Offset: " + record.offset());// imprime offset
        System.out.println("Partition: " + record.partition());// imprime a partition

        var order = record.value();
        if (isFraud(order)){
            // fingindo encontrar um fraud com uma compra de <= 4500
            System.out.println("Order is a fraud!");
            orderDispatcher.send("ECOMMERCE_ORDER_REJECTED", order.getEmail(), order);
        } else {
            System.out.println("Approved: "+ order);
            orderDispatcher.send("ECOMMERCE_ORDER_APPROVED", order.getEmail(), order);

        }
    }

    private boolean isFraud(Order order) {
        return order.getValue().compareTo(new BigDecimal("4500")) >= 0;
    }
}

