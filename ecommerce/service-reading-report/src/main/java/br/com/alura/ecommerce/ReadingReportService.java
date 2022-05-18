package br.com.alura.ecommerce;

import org.apache.kafka.clients.consumer.ConsumerRecord;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Path;
import java.util.Map;
import java.util.concurrent.ExecutionException;

/* Definindo Class Consumidora do tópico ECOMMERCE_NEW_ORDER*/
public class ReadingReportService {
    private static final Path SOURCE = new File("src/main/resources/report.txt").toPath();

    /* Class principal*/
    public static void main(String[] args) {

        /* Definindo um novo FraudDetectorService para utilizar seu método parse*/
        var reportService = new ReadingReportService();

        /* Try para fechar o serviço caso haja algum erro na execução, chamando o serviço para o frauddetector
        * o método KafdService<>, ConsumerFunction, o Tipo da mensagem, e Map.of() com as configurações especiais do consumer a ser criado */
        try(var service = new KafkaService<>(ReadingReportService.class.getSimpleName(),
                "USER_GENERATE_READING_REPORT",
                reportService::parse,
                User.class,
                Map.of())) {

            /* Executando o Serviço*/
            service.run();
        }

    }

    /* Método que será executando para cada mensagem recebida*/
    private void parse(ConsumerRecord<String, User> record) throws IOException {
        System.out.println("-----------------------------");
        System.out.println("Processing report for " + record.value());

        var user = record.value();
        var target = new File(user.getReportPath());
        IO.copyTo(SOURCE, target);
        IO.append(target, "Created for" + user.getUuid());

        System.out.println("File created: " + target.getAbsolutePath());

    }

}
