package br.com.alura.ecommerce;

import org.apache.kafka.clients.consumer.ConsumerRecord;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class CreateUserService {
    private final Connection connection;

    public CreateUserService() throws SQLException {
        String url = "jdbc:sqlite:target/users_database.db";
        this.connection = DriverManager.getConnection(url);
        connection.createStatement().execute("create table Users (" +
                "uuid varchar(200) primary key," +
                "email varchar(200))");
    }

    public static void main(String[] args) throws SQLException {

        /* Definindo um novo FraudDetectorService para utilizar seu método parse*/
        var createUser = new CreateUserService();

        /* Try para fechar o serviço caso haja algum erro na execução, chamando o serviço para o frauddetector
         * o método KafdService<> GroupID, ConsumerFunction, o Tipo da mensagem, e Map.of() com as configurações especiais do consumer a ser criado */
        try(var service = new KafkaService<>(CreateUserService.class.getSimpleName(),
                "ECOMMERCE_NEW_ORDER",
                createUser::parse,
                Order.class,
                Map.of())) {

            /* Executando o Serviço*/
            service.run();
        }

    }

    private final KafkaDispatcher<Order> orderDispatcher = new KafkaDispatcher<>();


    /* Método que será executando para cada mensagem recebida*/
    private void parse(ConsumerRecord<String, Order> record) throws SQLException {
        System.out.println("-----------------------------");
        System.out.println("Processing new order, cheking for fraud");
        System.out.println("Mensage: " + record.value());// imprime a value

        var order = record.value();
        if(isNewUser(order.getEmail())){
            insertNewUser(order.getEmail());
        }


    }

    private void insertNewUser(String email) throws SQLException {
        var insert = connection.prepareStatement("insert into User (uuid, email) values (?, ?)");
        insert.setString(1, "uuid");
        insert.setString(2,email);
        insert.execute();
        System.out.println("Usuário uuid e " + email + "adicionado");

    }

    private boolean isNewUser(String email) throws SQLException {
        var exists =  connection.prepareStatement("select uuid from Users" +
                "where email = ? limit 1");
        exists.setString(1, email);
        var results = exists.executeQuery();
        return !results.next();
    }
}
