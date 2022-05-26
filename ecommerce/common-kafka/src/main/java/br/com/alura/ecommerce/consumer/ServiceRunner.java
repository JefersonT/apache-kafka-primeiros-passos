package br.com.alura.ecommerce.consumer;

import java.util.concurrent.Executors;

/* Classe que executa o serviço*/
public class ServiceRunner<T> {
    /* criando o serviço*/
    private final ServiceProvider<T> provider;

    /* Recebendo o serviço a ser criado*/
    public ServiceRunner(ServiceFactory<T> factory) {
        this.provider = new ServiceProvider<T>(factory);
    }

    /* Executando os serviço threadCount vezes*/
    public void start(int threadCount){
        /* Criando um executor*/
        var pool= Executors.newFixedThreadPool(threadCount);

        /* Loop para executar o serviço de acordo com a quantidade */
        for (int i = 0; i <= threadCount; i++){
            /* Executando uma thread do serviço*/
            pool.submit(provider);
        }
    }
}
