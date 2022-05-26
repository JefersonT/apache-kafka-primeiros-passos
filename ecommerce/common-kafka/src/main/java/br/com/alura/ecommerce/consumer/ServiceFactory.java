package br.com.alura.ecommerce.consumer;

/* Interface que cria um serviço consumidor*/
public interface ServiceFactory<T> {
    /* Criando o consumerservice*/
    ConsumerService<T> create();
}
