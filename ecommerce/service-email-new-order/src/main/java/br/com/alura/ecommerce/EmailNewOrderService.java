package br.com.alura.ecommerce;

import br.com.alura.ecommerce.consumer.ConsumerService;
import br.com.alura.ecommerce.consumer.KafkaService;
import br.com.alura.ecommerce.consumer.ServiceRunner;
import br.com.alura.ecommerce.dispatcher.KafkaDispatcher;
import org.apache.kafka.clients.consumer.ConsumerRecord;

import java.util.Map;
import java.util.concurrent.ExecutionException;

/* Definindo Class Consumidora do tópico ECOMMERCE_NEW_ORDER*/
public class EmailNewOrderService implements ConsumerService<Order> {

    /* Class principal*/
    public static void main(String[] args) {
        /* Criando um Runner para executar o serviço 1x*/
        new ServiceRunner(EmailNewOrderService::new).start(1);

    }


    /* Declarando um producer*/
    private final KafkaDispatcher<String> emailDispatcher = new KafkaDispatcher<>();


    /* Método que será executando para cada mensagem recebida*/
    public void parse(ConsumerRecord<String, Message<Order>> record) throws ExecutionException, InterruptedException {
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

    @Override
    public String getConsumerGroup() {
        return EmailNewOrderService.class.getSimpleName();
    }

    @Override
    public String getTopic() {
        return "ECOMMERCE_NEW_ORDER";
    }

}

