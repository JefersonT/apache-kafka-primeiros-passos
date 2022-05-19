package br.com.alura.ecommerce;

import java.util.UUID;

/* Classe do tipo Id para relacionar os eventos*/
public class CorrelationId {
    private final String id;

    /* Contrutor com id aleatório */
    public CorrelationId(String title) {
        id = title + "(" + UUID.randomUUID().toString() + ")";
    }

    @Override
    public String toString() {
        return "CorrelationId{" +
                "id='" + id + '\'' +
                '}';
    }

    /* método para dá continuidade no id adicionando um titolo a cada envio da mensagem*/
    public CorrelationId continueWith (String title){
        return new CorrelationId(id + "-" + title);
    }
}
