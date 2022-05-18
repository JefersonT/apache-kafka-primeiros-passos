package br.com.alura.ecommerce;

import org.apache.kafka.clients.consumer.ConsumerRecord;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

/* Class para gerar os relatórios*/
public class BatchSendMessageService {
    /* Criando a conexão*/
    private final Connection connection;

    /* Criando tabala de Users*/
    public BatchSendMessageService() throws SQLException {
        String url = "jdbc:sqlite:target/users_database.db";
        connection = DriverManager.getConnection(url);
        /* try para ignorar o erro nas proximas vezes que já existir o banco*/
        try {
            /* Criando a tablea Users*/
            connection.createStatement().execute("create table Users (" +
                    "uuid varchar(200) primary key," +
                    "email varchar(200))");
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public static void main(String[] args) throws SQLException {

        /* Definindo um novo BatchSendMessageService para utilizar seu método parse*/
        var batchService = new BatchSendMessageService();

        /* Try para fechar o serviço caso haja algum erro na execução, chamando o serviço para o batchService
         * o método KafkaService<> GroupID, ConsumerFunction, o Tipo da mensagem, e Map.of() com as configurações especiais do consumer a ser criado */
        try (var service = new KafkaService<>(BatchSendMessageService.class.getSimpleName(),
                "SEND_MESSAGE_TO_ALL_USERS",
                batchService::parse,
                String.class,
                Map.of())) {

            /* Executando o Serviço*/
            service.run();
        }

    }

    /* Definindo um novo producer*/
    private final KafkaDispatcher<User> userDispatcher = new KafkaDispatcher<User>();

    /* Método que será executando para cada User recebida*/
    private void parse(ConsumerRecord<String, String> record) throws ExecutionException, InterruptedException, SQLException {
        System.out.println("-----------------------------");
        System.out.println("Processing new batch");
        System.out.println("Topic: " + record.value());// imprime a value

        /* Disparando o relatório para cada user*/
        for (User user : getAllUsers()) {
            userDispatcher.send(record.value(), user.getUuid(), user);
        }
    }

    /* Consultando os usuários no bd*/
    private List<User> getAllUsers() throws SQLException {
        var results = connection.prepareStatement("select uuid from Users").executeQuery();
        List<User> users = new ArrayList<>();// criando uma lista users
        while (results.next()) {
            users.add(new User(results.getString(1)));//adicionando cada uuid de usuário à lista users
        }
        return users; // retornando a lista de uuid dos usuarios
    }
}