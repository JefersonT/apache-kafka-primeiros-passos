package br.com.alura.ecommerce;

import br.com.alura.database.LocalDatabase;
import br.com.alura.ecommerce.consumer.ConsumerService;
import br.com.alura.ecommerce.consumer.KafkaService;
import br.com.alura.ecommerce.consumer.ServiceRunner;
import org.apache.kafka.clients.consumer.ConsumerRecord;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

public class CreateUserService implements ConsumerService<Order> {
    private final LocalDatabase database;

    CreateUserService() throws SQLException {
        this.database = new LocalDatabase("users_database");
        this.database.createIfNotExists("create table Users (" +
                "uuid varchar(200) primary key," +
                "email varchar(200))");

    }

    public static void main(String[] args) throws SQLException, ExecutionException, InterruptedException {
        new ServiceRunner(CreateUserService::new).start(1);
    }

    /* Método que será executando para cada mensagem recebida*/
    public void parse(ConsumerRecord<String, Message<Order>> record) throws SQLException {
        System.out.println("-----------------------------");
        System.out.println("Processing new order, cheking for fraud");
        System.out.println("Mensage: " + record.value());// imprime a value
        var message = record.value();

        var order = message.getPayload();

        /* Inserindo novo usuaário caso aindda não exista na base*/
        if(isNewUser(order.getEmail())){
            /* Inserindo o usuário*/
            insertNewUser(order.getEmail());
        }


    }

    @Override
    public String getConsumerGroup() {
        return CreateUserService.class.getSimpleName();
    }

    @Override
    public String getTopic() {
        return "ECOMMERCE_NEW_ORDER";
    }

    /* Método para inserir um novo usuário*/
    private void insertNewUser(String email) throws SQLException {
        var uuid = UUID.randomUUID().toString();
        database.update("insert into Users (uuid, email) values (?, ?)", uuid, email);
        System.out.println("Usuário " + uuid+ " e " + email + "adicionado");

    }

    /* Método de verificação se o usuário é novo ou não*/
    private boolean isNewUser(String email) throws SQLException {
        var results = database.query("select uuid from Users " +
                "where email = ? limit 1", email);

        return !results.next();/* Retorna se houver algo na lista*/
    }
}
