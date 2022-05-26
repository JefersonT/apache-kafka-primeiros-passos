package br.com.alura.ecommerce;

import br.com.alura.ecommerce.consumer.KafkaService;
import br.com.alura.ecommerce.dispatcher.KafkaDispatcher;
import org.apache.kafka.clients.consumer.ConsumerRecord;

import java.util.Map;
import java.util.concurrent.ExecutionException;

/* Definindo Class Consumidora do tópico ECOMMERCE_NEW_ORDER*/
public class EmailNewOrderService {

    /* Class principal*/
    public static void main(String[] args) throws ExecutionException, InterruptedException {

        /* Definindo um novo FraudDetectorService para utilizar seu método parse*/
        var emailService = new EmailNewOrderService();

        /* Try para fechar o serviço caso haja algum erro na execução, chamando o serviço para o frauddetector
        * o método KafdService<>, ConsumerFunction, o Tipo da mensagem, e Map.of() com as configurações especiais do consumer a ser criado */
        try(var service = new KafkaService<>(EmailNewOrderService.class.getSimpleName(),
                "ECOMMERCE_NEW_ORDER",
                emailService::parse,
                Map.of())) {

            /* Executando o Serviço*/
            service.run();
        }

    }


    /* Declarando um producer*/
    private final KafkaDispatcher<String> emailDispatcher = new KafkaDispatcher<>();


    /* Método que será executando para cada mensagem recebida*/
    private void parse(ConsumerRecord<String, Message<Order>> record) throws ExecutionException, InterruptedException {
        System.out.println("-----------------------------");
        System.out.println("Processing new order, preparin email");
        var message = record.value();
        System.out.println("Mensage: " + message);// imprime a value

        /* Denifindo o valor para o metodo send*/
        var emailCode = "Thanks You for your new Order!";

        var order = message.getPayload();

        /* Enviando a Email com o userID para o topic ECOMMERCE_SEND_EMAIL*/
        CorrelationId id = message.getId().continueWith(EmailNewOrderService.class.getSimpleName());
        emailDispatcher.send("ECOMMERCE_SEND_EMAIL",
                order.getEmail(),
                emailCode,
                id);
        // como este é o primeiro a disparar uma mensagem ele receber um novo id para ser passado para os proximos
    }

}

