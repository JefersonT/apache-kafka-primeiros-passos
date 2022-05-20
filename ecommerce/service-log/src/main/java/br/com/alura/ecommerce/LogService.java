package br.com.alura.ecommerce;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.regex.Pattern;

/*Definindo Class Consumidora do tópico ECOMMERCE_NEW_ORDER*/
public class LogService {
    /*Callse principal*/
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        /* Definindo um novo LogService para utilizar seu método parse*/
        var logService = new LogService();

        /* Try para fechar o serviço caso haja algum erro na execução, chamando o serviço para o frauddetector
         * o método KafdService<> GroupID, o Topic, ConsumerFunction, o Tipo da mensagem, e Map.of() com as configurações especiais do consumer a ser criado
         * Subscrevendo o consumer em todos os topicos com inicio ECOMMERCE*/
        try (var consumer = new KafkaService(LogService.class.getSimpleName(),
                Pattern.compile("ECOMMERCE.*"),
                logService::parse,
                Map.of(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG,
                        StringDeserializer.class.getName()))) { /* Sobrepões o Deserializer padrão da classe*/

            /* Executando o Consumer*/
            consumer.run();
        }
    }

    /* Método que será executando para cada mensagem recebida*/
    private void parse(ConsumerRecord<String, String> record) {
        System.out.println("-----------------------------");
        System.out.println("Processing new order, cheking for fraud");
        System.out.println("Topico: " + record.topic());// imprime o topico
        System.out.println("Chave: " + record.key());// imprime a chave
        System.out.println("Mensage: " + record.value());// imprime a value
        System.out.println("Offset: " + record.offset());// imprime offset
        System.out.println("Partition: " + record.partition());// imprime a partition
    }
}
