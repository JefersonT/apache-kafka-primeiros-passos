package br.com.alura.ecommerce.consumer;

import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;

public class ServiceProvider<T> implements Callable<Void> {
    /* Criando o criador de serviço*/
    private final ServiceFactory<T> factory;

    /* Construtor sercebendo um ServiceFactoy a ser cruadi*/
    public ServiceProvider(ServiceFactory<T> factory) {
        this.factory = factory;
    }

    public Void call() throws Exception {
        /* Criando um novo Service através do factory.create (ServiceFactroy)*/
        var myService = factory.create();

        /* Try para fechar o serviço caso haja algum erro na execução
         * o método KafdService<> GroupID, o Topic, ConsumerFunction, e Map.of() com as configurações especiais do consumer a ser criado */
        try(var service = new KafkaService(
                myService.getConsumerGroup(),
                myService.getTopic(),
                myService::parse,
                Map.of())) {

            /* Executando o consumer*/
            service.run();
        }
        return null;
    }
}
