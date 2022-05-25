package br.com.alura.ecommerce.dispatcher;

import br.com.alura.ecommerce.CorrelationId;
import br.com.alura.ecommerce.Message;
import org.apache.kafka.clients.producer.*;
import org.apache.kafka.common.serialization.StringSerializer;
import java.io.Closeable;
import java.util.Properties;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class KafkaDispatcher<T> implements Closeable {/*Closeable para podermos implementar o método que fecha o producer*/
    /* Declarando uma variável que recebe um novo KafkaProducer
    * responsável pelo envio de mensagens
    * com dois prarámtros <chave, mensagem>, neste caso strings*/
    private final KafkaProducer<String, Message<T>> producer;

    /* Contrutor*/
    public KafkaDispatcher() {
         /* iniciado o producer com uma nova KafkaProducer com uma classe properties() contendo as suas propriedades
         * do producer */
        this.producer= new KafkaProducer<>(properties());
    }

    /*Criando classe de propriedades para o Producer*/
    private static Properties properties() {
        /*Declarando variável recebendo uma nova Properties()*/
        var  properties = new Properties();

        /*Setando nas properties as configurações do Producer referente ao BOOTSTRAP SERVER "IP:PORTA"*/
        properties.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");

        /*Setando nas properties as configurações do Producer referente ao método de serialização da chave
         * responsável por converter a string em bytes*/
        properties.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());

        /*Setando nas properties as configurações do Producer referente ao método de serialização da mensagem
         * responsável por converter a Order em bits, confertendo em Json e em seguida para bytes*/
        properties.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, GsonSerializer.class.getName());

        /* Garante a sincronia de informaçõa entre todos as instáncias do kafka*/
        properties.setProperty(ProducerConfig.ACKS_CONFIG, "all");

        /*retornando a variável com as propriedades*/
        return properties;
    }

    /* Metódo para dispara a Mensagem que recebe o topico, a chave, o valor da mensagem
    *  e um CorralationId para manter a relação de envio da mensagem
    * podendo ser de qualquer tipo
    * Sincrono*/
    public void send(String topic, String key, T payload, CorrelationId id) throws ExecutionException, InterruptedException {

        /* Chamando o método assincrono para torna-lo sincrona*/
        java.util.concurrent.Future<org.apache.kafka.clients.producer.RecordMetadata> future = sendAsync(topic, key, payload, id);
        future.get();// como o send é assincrono utilizamos o .get() para esperar a future terminar
    }

    /* Metódo para dispara a Mensagem que recebe o topico, a chave, o valor da mensagem
     *  e um CorralationId para manter a relação de envio da mensagem
     * podendo ser de qualquer tipo
     * Assincrono*/
    public Future<RecordMetadata> sendAsync(String topic, String key, T payload, CorrelationId id) {
        /* "Envelopando" tudo em um Message*/
        var value = new Message<>(id.continueWith("_" + topic), payload);

        /* Declarando uma variável com um novo produtor de registro que deve receber como parametro o topico,
         * a chave e a mensage
         * existem diversas override do método para se implementado*/
        var record = new ProducerRecord<>(topic, key, value);

        /* Declarando variável de callback para o envio de ProducerRecord*/
        Callback callback = (data, ex) -> {

            /*Se as exceptions diferente de null imprime a ex*/
            if (ex != null) {
                ex.printStackTrace();
                return;
            }
            /*se as ex for null imprime os dados de criação da mensagem*/
            System.out.println("sucesso enviado" + data.topic() + ":::partition" + data.partition() + "/offset" + data.offset() + "/" + data.timestamp());
        };
        /* Realizando o envio do ProducerRecord pelo producer
         * e uma variável de callback para tratar as exceptions ou dados retornados do producer.send*/
        var future = producer.send(record, callback);
        return future;
    }

    /* Método para fechar o servise*/
    @Override
    public void close(){
        producer.close();
    }
}
