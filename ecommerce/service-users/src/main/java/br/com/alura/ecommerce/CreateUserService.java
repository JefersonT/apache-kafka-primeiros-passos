package br.com.alura.ecommerce;

import org.apache.kafka.clients.consumer.ConsumerRecord;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

public class CreateUserService {
    private final Connection connection;

    public CreateUserService() throws SQLException {
        String url = "jdbc:sqlite:target/users_database.db";
        connection = DriverManager.getConnection(url);
        /* try para ignorar o erro nas proximas vezes que já existir o banco*/
        try {
            /* Criando a tablea Users*/
            connection.createStatement().execute("create table Users (" +
                    "uuid varchar(200) primary key," +
                    "email varchar(200))");
        } catch (SQLException ex){
            ex.printStackTrace();
        }
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

    /* Método que será executando para cada mensagem recebida*/
    private void parse(ConsumerRecord<String, Order> record) throws SQLException {
        System.out.println("-----------------------------");
        System.out.println("Processing new order, cheking for fraud");
        System.out.println("Mensage: " + record.value());// imprime a value

        var order = record.value();

        /* Inserindo novo usuaário caso aindda não exista na base*/
        if(isNewUser(order.getEmail())){
            /* Inserindo o usuário*/
            insertNewUser(order.getEmail());
        }


    }

    /* Método para inserir um novo usuário*/
    private void insertNewUser(String email) throws SQLException {
        /* variável com a chamada da conexão e script de insersão sql*/
        var insert = connection.prepareStatement("insert into Users (uuid, email) values (?, ?)");

        /* Denifinindo o valor da primeira variável do script*/
        insert.setString(1, UUID.randomUUID().toString());

        /* Definindo o valor da segunda variável da do script*/
        insert.setString(2, email);

        /* Executando o script*/
        insert.execute();

        System.out.println("Usuário uuid e " + email + "adicionado");

    }

    /* Método de verificação se o usuário é novo ou não*/
    private boolean isNewUser(String email) throws SQLException {
        /*armazenando o script se seleção de usuário com o email do parâmentro*/
        var exists =  connection.prepareStatement("select uuid from Users " +
                "where email = ? limit 1");

        /* Definindo o valor da primeira variável do script*/
        exists.setString(1, email);

        /* Esecutando o Script e armazenando a listagem em reults*/
        var results = exists.executeQuery();

        return !results.next();/* Retorna se houver algo na lista*/
    }
}
