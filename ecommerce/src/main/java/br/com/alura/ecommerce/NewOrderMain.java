package br.com.alura.ecommerce;


import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.Properties;
import java.util.concurrent.ExecutionException;
/*Criando Classe de envio de mensagem - Producer*/
public class NewOrderMain {

    /*Classe principal com tratativa de exception referente ao producer.sent().get();*/
    public static void main(String[] args) throws ExecutionException, InterruptedException {

        /*Declarando uma variável que recebe um novo KafkaProducer
        * responsável pelo envio de mensagens
        * com dois prarámtros <chave, mensagem>, neste caso strings
        * iniciado com uma classe properties() contendo as suas propriedades*/
        var producer= new KafkaProducer<String, String>(properties());

        /*Declarando uma variável com Valor da mensagem, e neste caso também é a chave*/
        var value = "12313242, 32423423, 5768578685";
        var email = "Thanks You for your new Order!";

        /*Declarando uma variável com um novo produtor de registro que deve receber como parametro o topico, a chave e a mensagem
        * existem diversas override do método para se implementado*/
        var record = new ProducerRecord<>("ECOMMERCE_NEW_ORDER", value, value);
        var sendEmail = new ProducerRecord<>("ECOMMERCE_SEND_EMAIL", email, email);

        /*Declarando variável de callback para o envio de ProducerRecord*/
        Callback callback = (data, ex) -> {

            /*Se as exceptions diferente de null imprime a ex*/
            if (ex != null) {
                ex.printStackTrace();
                return;
            }
            /*se as ex for null imprime os dados de criação da mensagem*/
            System.out.println("sucesso enviado" + data.topic() + ":::partition" + data.partition() + "/offset" + data.offset() + "/" + data.timestamp());
        };
        /*Realizando o envio do ProducerRecord pelo producer
        * e uma variável de callback para tratar as exceptions ou dados retornados do producer.send*/
        producer.send(record, callback).get();// como o send é assincrono utilizamos o .get() para esperar a feture terminar
        producer.send(sendEmail, callback).get();// como o send é assincrono utilizamos o .get() para esperar a feture terminar
    }

    /*Criando classe de propriedades para o Producer*/
    private static Properties properties() {
        /*Declarando variável recebendo uma nova Properties()*/
        var  properties = new Properties();

        /*Setando nas properties as configurações do Producer referente ao BOOTSTRAP SERVER "IP:PORTA"*/
        properties.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");

        /*Setando nas properties as configurações do Producer referente ao método de serialização da chave
        * responsável por converter a string em bits*/
        properties.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());

        /*Setando nas properties as configurações do Producer referente ao método de serialização da mensagem
        * responsável por converter a string em bits*/
        properties.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());

        /*retornando a variável com as propriedades*/
        return properties;
    }
}
