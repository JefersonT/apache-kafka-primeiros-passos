package br.com.alura.ecommerce;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;

import java.time.Duration;
import java.util.Collections;
import java.util.Properties;

/*Definindo Class Consumidora do tópico ECOMMERCE_NEW_ORDER*/
public class FraudDetectorService {
    /*Callse principal*/
    public static void main(String[] args) {

        /*Definindo um variável que recebe um Kafka Consumer, que tem como parametros suas propriedades defininas
        * no método properties*/
        var consumer = new KafkaConsumer<String, String>(properties());

        /*Subscrevendo o consumer criado anteriomete ao Topico*/
        consumer.subscribe(Collections.singletonList("ECOMMERCE_NEW_ORDER"));

        /*Loop para manter o consumer executando e recebendo os eventos do Producer*/
        while (true) {

            /*o consumer.poll irá perguntar se tem mensagem a receber durante 100 milisegundos
            * o resultado será setado na variável records */
            var records = consumer.poll(Duration.ofMillis(100));

            /*se records possuir algum registro*/
            if (!records.isEmpty()) {
                /*printa a quantidade de registros*/
                System.out.println("Encontrei" + records.count() + "Registros");

                /*Percorre o records e imprime as informações de cada registro*/
                for (var record : records) {
                    System.out.println("-----------------------------");
                    System.out.println("Processing new order, cheking for fraud");
                    System.out.println(record.key());// imprime a chave
                    System.out.println(record.offset());// imprime offset
                    System.out.println(record.value());// imprime a value
                    System.out.println(record.partition());// imprime a partition
                }
            }

        }
    }

    /*Definindo as propriedades do Consumer*/
    private static Properties properties() {
        /*Definindo uma variável que recepe um novo Properties()*/
        var properties = new Properties();

        /*Setando as configurações de onde o Consumer vai escutar a mensagem = "IP:Porta"*/
        properties.setProperty(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");

        /*Setando os a forma de deserializar a chave
        * desconverter bits em string*/
        properties.setProperty(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());

        /*Setando os a forma de deserializar a mensagem
         * desconverter bits em string*/
        properties.setProperty(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());

        /*defini o id group para que ele possa receber as mensagem, id igual o nome do método*/
        properties.setProperty(ConsumerConfig.GROUP_ID_CONFIG, FraudDetectorService.class.getSimpleName());
        return properties;
    }
}
