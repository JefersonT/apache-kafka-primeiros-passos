package br.com.alura.ecommerce;

import br.com.alura.ecommerce.consumer.ConsumerService;
import br.com.alura.ecommerce.consumer.KafkaService;
import br.com.alura.ecommerce.consumer.ServiceRunner;
import org.apache.kafka.clients.consumer.ConsumerRecord;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Map;
import java.util.concurrent.ExecutionException;

/* Definindo Class Consumidora do tópico ECOMMERCE_NEW_ORDER*/
public class ReadingReportService implements ConsumerService<User> {
    /* Definindo o local e arquivo de base de relatórios*/
    private static final Path SOURCE = new File("src/main/resources/report.txt").toPath();

    /* Class principal*/
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        /* Criando um Runner para executar o serviço 5x*/
        new ServiceRunner(ReadingReportService::new).start(5);

    }

    /* Método que será executando para cada Usuário*/
    public void parse(ConsumerRecord<String, Message<User>> record) throws IOException {
        System.out.println("-----------------------------");
        System.out.println("Processing report for " + record.value());

        var message = record.value();
        /* Guardadno valor de record*/
        var user = message.getPayload();

        /* Definindo arquivo de destino*/
        var target = new File(user.getReportPath());

        /* Copiando SOURCE para target*/
        IO.copyTo(SOURCE, target);

        /* Adicionando os dados no target com o UUID do user*/
        IO.append(target, "Created for" + user.getUuid());

        System.out.println("File created: " + target.getAbsolutePath());

    }

    /* Definindo o ConsumerGroup do serviço*/
    @Override
    public String getConsumerGroup() {
        return ReadingReportService.class.getSimpleName();
    }

    /* Definindo o Topic do serviço*/
    @Override
    public String getTopic() {
        return "ECOMMERCE_USER_GENERATE_READING_REPORT";
    }
}

