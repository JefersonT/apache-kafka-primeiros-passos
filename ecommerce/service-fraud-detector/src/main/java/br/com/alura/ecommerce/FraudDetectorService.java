package br.com.alura.ecommerce;

import br.com.alura.ecommerce.consumer.KafkaService;
import br.com.alura.ecommerce.dispatcher.KafkaDispatcher;
import org.apache.kafka.clients.consumer.ConsumerRecord;

import java.math.BigDecimal;
import java.util.Map;
import java.util.concurrent.ExecutionException;

/* Definindo Class Consumidora do tópico ECOMMERCE_NEW_ORDER*/
public class FraudDetectorService {

    /* Class principal*/
    public static void main(String[] args) throws ExecutionException, InterruptedException {

        /* Definindo um novo FraudDetectorService para utilizar seu método parse*/
        var fraudDetector = new FraudDetectorService();

        /* Try para fechar o serviço caso haja algum erro na execução, chamando o serviço para o frauddetector
        * o método KafdService<>, ConsumerFunction, o Tipo da mensagem, e Map.of() com as configurações especiais do consumer a ser criado */
        try(var service = new KafkaService<>(FraudDetectorService.class.getSimpleName(),
                "ECOMMERCE_NEW_ORDER",
                fraudDetector::parse,
                Map.of())) {

            /* Executando o Serviço*/
            service.run();
        }

    }

    /* Declarando um producer*/
    private final KafkaDispatcher<Order> orderDispatcher = new KafkaDispatcher<>();


    /* Método que será executando para cada mensagem recebida*/
    private void parse(ConsumerRecord<String, Message<Order>> record) throws ExecutionException, InterruptedException {
        System.out.println("-----------------------------");
        System.out.println("Processing new order, cheking for fraud");
        System.out.println("Chave: " + record.key());// imprime a chave
        System.out.println("Mensage: " + record.value());// imprime a value
        System.out.println("Offset: " + record.offset());// imprime offset
        System.out.println("Partition: " + record.partition());// imprime a partition

        var message = record.value();
        var order = message.getPayload();
        if (isFraud(order)){
            // fingindo encontrar um fraud com uma compra de <= 4500
            System.out.println("Order is a fraud!");
            orderDispatcher.send("ECOMMERCE_ORDER_REJECTED",
                    order.getEmail(),
                    order,
                    //correlationId, pegando id atual e adicionando o id do processo atual
                    message.getId().continueWith(FraudDetectorService.class.getSimpleName()));
        } else {
            System.out.println("Approved: "+ order);
            orderDispatcher.send("ECOMMERCE_ORDER_APPROVED",
                    order.getEmail(),
                    order,
                    //correlationId, pegando id atual e adicionando o id do processo atual
                    message.getId().continueWith(FraudDetectorService.class.getSimpleName()));
        }
    }

    /* Verifica se é fraude, (compras acima de 4500 = fraude)*/
    private boolean isFraud(Order order) {
        /* Comprara o valor da ordem com um valor de 4500*/
        return order.getValue().compareTo(new BigDecimal("4500")) >= 0;
    }
}

