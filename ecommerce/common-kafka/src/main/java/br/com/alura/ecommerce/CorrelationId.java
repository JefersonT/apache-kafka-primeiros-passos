package br.com.alura.ecommerce;

import java.util.UUID;

/* Classe do tipo Id para relacionar os eventos*/
public class CorrelationId {
    private final String id;

    /* Contrutor com id aleatório (Temporário)*/
    public CorrelationId() {
        id = UUID.randomUUID().toString();
    }

    @Override
    public String toString() {
        return "CorrelationId{" +
                "id='" + id + '\'' +
                '}';
    }
}
