package br.com.alura.ecommerce;

import br.com.alura.ecommerce.consumer.KafkaService;

import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.function.Function;

public class ServiceProvider {
    public <T> void run(ServiceFactory<T> factory) throws ExecutionException, InterruptedException {
        /* Criando um novo SendEmailService para utilizar seu metodo parse*/
        var emailService = factory.create();

        /* Try para fechar o serviço caso haja algum erro na execução, chamando o serviço para o frauddetector
         * o método KafdService<> GroupID, o Topic, ConsumerFunction, o Tipo da mensagem, e Map.of() com as configurações especiais do consumer a ser criado */
        try(var service = new KafkaService(
                emailService.getConsumerGroup(),
                emailService.getTopic(),
                emailService::parse,
                Map.of())) {

            /* Executando o consumer*/
            service.run();
        }
    }
}
