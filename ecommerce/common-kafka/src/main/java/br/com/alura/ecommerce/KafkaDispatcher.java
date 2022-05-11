package br.com.alura.ecommerce;

import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;
import java.io.Closeable;
import java.util.Properties;
import java.util.concurrent.ExecutionException;

class KafkaDispatcher<T> implements Closeable {/*Closeable para podermos implementar o método que fecha o producer*/
    /* Declarando uma variável que recebe um novo KafkaProducer
    * responsável pelo envio de mensagens
    * com dois prarámtros <chave, mensagem>, neste caso strings*/
    private final KafkaProducer<String, T> producer;

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

        /*retornando a variável com as propriedades*/
        return properties;
    }

    /* Metódo para dispara a Mensagem que recebe o topico, a chave e o valor da mensagem
    * podendo ser de qualquer tipo*/
    public void send(String topic, String key, T value) throws ExecutionException, InterruptedException {
        /* Declarando uma variável com um novo produtor de registro que deve receber como parametro o topico, a chave e a mensagem
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
        producer.send(record, callback).get();// como o send é assincrono utilizamos o .get() para esperar a feture terminar
    }

    /* Método para fechar o servise*/
    @Override
    public void close(){
        producer.close();
    }
}
