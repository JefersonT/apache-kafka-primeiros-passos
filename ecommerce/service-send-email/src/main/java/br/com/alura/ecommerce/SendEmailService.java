package br.com.alura.ecommerce;

import br.com.alura.ecommerce.consumer.KafkaService;
import org.apache.kafka.clients.consumer.ConsumerRecord;

import java.util.Map;
import java.util.concurrent.ExecutionException;

/*Definindo Class Consumidora do tópico ECOMMERCE_NEW_ORDER*/
public class SendEmailService {
    /*Callse principal*/
    public static void main(String[] args) throws ExecutionException, InterruptedException {

        /* Criando um novo SendEmailService para utilizar seu metodo parse*/
        var sendEmail = new SendEmailService();

        /* Try para fechar o serviço caso haja algum erro na execução, chamando o serviço para o frauddetector
         * o método KafdService<> GroupID, o Topic, ConsumerFunction, o Tipo da mensagem, e Map.of() com as configurações especiais do consumer a ser criado */
        try(var service = new KafkaService(SendEmailService.class.getSimpleName(),
                "ECOMMERCE_SEND_EMAIL",
                sendEmail::parse,
                Map.of())) {

            /* Executando o consumer*/
            service.run();
        }
        
    }

    /* Método que será executando para cada mensagem recebida*/
    private void parse(ConsumerRecord<String, String> record){
        System.out.println("-----------------------------");
        System.out.println("Processing new order, cheking for fraud");
        System.out.println("Chave: " + record.key());// imprime a chave
        System.out.println("Mensage: " + record.value());// imprime a value
        System.out.println("Offset: " + record.offset());// imprime offset
        System.out.println("Partition: " + record.partition());// imprime a partition
    }
}
