package br.com.alura.ecommerce;

import br.com.alura.database.LocalDatabase;
import br.com.alura.ecommerce.consumer.ConsumerService;
import br.com.alura.ecommerce.consumer.ServiceRunner;
import br.com.alura.ecommerce.dispatcher.KafkaDispatcher;
import org.apache.kafka.clients.consumer.ConsumerRecord;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.concurrent.ExecutionException;

/* Definindo Class Consumidora do tópico ECOMMERCE_NEW_ORDER*/
public class FraudDetectorService implements ConsumerService<Order> {

    /* Class principal*/
    public static void main(String[] args){
        new ServiceRunner<>(FraudDetectorService::new).start(1);
    }

    private final LocalDatabase database;
    public FraudDetectorService() throws SQLException {
        this.database = new LocalDatabase("frauds_database");
        this.database.createIfNotExists("create table Orders (" +
                "uuid varchar(200) primaty key," +
                "is_fraud boolean)");
    }

    /* Declarando um producer*/
    private final KafkaDispatcher<Order> orderDispatcher = new KafkaDispatcher<>();


    /* Método que será executando para cada mensagem recebida*/
    public void parse(ConsumerRecord<String, Message<Order>> record) throws ExecutionException, InterruptedException, SQLException {
        System.out.println("-----------------------------");
        System.out.println("Processing new order, cheking for fraud");
        System.out.println("Chave: " + record.key());// imprime a chave
        System.out.println("Mensage: " + record.value());// imprime a value
        System.out.println("Offset: " + record.offset());// imprime offset
        System.out.println("Partition: " + record.partition());// imprime a partition

        var message = record.value();
        var order = message.getPayload();
        if(wasProcessed(order)){
            System.out.println("order " + order.getOrderId() + " was already processed");
            return;
        }
        if (isFraud(order)){
            database.update("insert into Orders (uuid, is_froud) values (?, true)", order.getOrderId());
            // fingindo encontrar um fraud com uma compra de <= 4500
            System.out.println("Order is a fraud!");
            orderDispatcher.send("ECOMMERCE_ORDER_REJECTED",
                    order.getEmail(),
                    order,
                    //correlationId, pegando id atual e adicionando o id do processo atual
                    message.getId().continueWith(FraudDetectorService.class.getSimpleName()));
        } else {
            database.update("insert into Orders (uuid, is_froud) values (?, false)", order.getOrderId());
            System.out.println("Approved: "+ order);
            orderDispatcher.send("ECOMMERCE_ORDER_APPROVED",
                    order.getEmail(),
                    order,
                    //correlationId, pegando id atual e adicionando o id do processo atual
                    message.getId().continueWith(FraudDetectorService.class.getSimpleName()));
        }
    }

    private boolean wasProcessed(Order order) throws SQLException {
        var results = database.query("select uuid from Orders where uuid = ? limit 1", order.getOrderId());
        return results.next();
    }

    @Override
    public String getConsumerGroup() {
        return FraudDetectorService.class.getSimpleName();
    }

    @Override
    public String getTopic() {
        return "ECOMMERCE_NEW_ORDER";
    }

    /* Verifica se é fraude, (compras acima de 4500 = fraude)*/
    private boolean isFraud(Order order) {
        /* Comprara o valor da ordem com um valor de 4500*/
        return order.getValue().compareTo(new BigDecimal("4500")) >= 0;
    }
}

