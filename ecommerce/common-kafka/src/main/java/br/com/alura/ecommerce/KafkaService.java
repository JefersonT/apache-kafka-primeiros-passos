package br.com.alura.ecommerce;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.io.Closeable;
import java.time.Duration;
import java.util.Collections;
import java.util.Map;
import java.util.Properties;
import java.util.UUID;
import java.util.regex.Pattern;

public class KafkaService<T> implements Closeable {/*Closeable para podermos implementar o método que fecha o producer*/
    /* Definindo um Consumer com chave String e Values T*/
    private final KafkaConsumer<String, Message<T>> consumer;

    /* Definindo um ConsumerFunction para o parse*/
    private final ConsumerFunction parse;

    /*Construtor*/
    public KafkaService(String groupId, String topic, ConsumerFunction<T> parse, Class<T> type, Map<String, String> properties) {
        this(parse, groupId, type, properties);

        /*Subscrevendo o consumer criado anteriomete ao Topico*/
        consumer.subscribe(Collections.singletonList(topic));
    }

    public KafkaService(String groupId, Pattern topic, ConsumerFunction<T> parse, Class<T> type, Map<String, String> properties) {
        this(parse, groupId, type, properties);

        /*Subscrevendo o consumer criado anteriomete ao Topico*/
        consumer.subscribe(topic);
    }

    public KafkaService(ConsumerFunction parse, String groupId, Class<T> type, Map<String, String> properties) {
        /* Iniciando a variável parse com os parametros passados no método*/
        this.parse = parse;

        /* Iniciando a variável que recebe um Kafka Consumer, que tem como parametros suas propriedades defininas
         * no método properties*/
        this.consumer = new KafkaConsumer<>(getProperties(type, groupId, properties));
    }

    /* Executando o disparo das mensagem*/
    void run(){

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
                    try {
                        parse.consume(record);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

        }
    }


    /*Definindo as propriedades do Consumer*/
    private Properties getProperties(Class<T> type, String groupId, Map<String, String> overridProperties) {
        /*Definindo uma variável que recepe um novo Properties()*/
        var properties = new Properties();

        /*Setando as configurações de onde o Consumer vai escutar a mensagem = "IP:Porta"*/
        properties.setProperty(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");

        /*Setando os a forma de deserializar a chave
         * desconverter bits em string*/
        properties.setProperty(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());

        /*Setando os a forma de deserializar a mensagem
         * desconverter bits em string*/
        properties.setProperty(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, GsonDeserializer.class.getName());

        /*defini o id group para que ele possa receber as mensagem, id igual o nome do método
         * Este recurso permite a distribuição das mensagnes quando há mais de um consumidor com o mesmo grupo
         * Desde que haja partições suficiente para a quantidade de consumidores no mesmo grupo
         * OBS.: A distribuição depende da chave passada no Producer*/
        properties.setProperty(ConsumerConfig.GROUP_ID_CONFIG, groupId);

        /* Defini o padrão de nome do consumidor*/
        properties.setProperty(ConsumerConfig.CLIENT_ID_CONFIG, UUID.randomUUID().toString());

        /* Esta configuração define com que frequencia de mensagens, o consumer irá commitar as mensagens
         * Aumentar a frequencia permite reduzir os problemas com rebalanceamento devido a quantidade de mensagens */
        properties.setProperty(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, "1");/* de uma em uma */

        /* Sobrepondo as properties de acordo com o que é passado na construção do objeto*/
        properties.putAll(overridProperties);
        return properties;
    }

    /* Métosdo para fechar o Serviço*/
    @Override
    public void close() {
        consumer.close();
    }
}
