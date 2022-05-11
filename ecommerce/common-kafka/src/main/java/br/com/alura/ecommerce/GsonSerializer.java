package br.com.alura.ecommerce;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.kafka.common.serialization.Serializer;

/* Serializador de Json, que receberar o Order
*  Herda da classe Serializer para ser identificado como um Serializador*/
public class GsonSerializer<T> implements Serializer<T> {

    /* Criando um objeto do tipo GsonBuilder para utilizar os recursos de json*/
    private final Gson gson = new GsonBuilder().create();

    /* Sobrescrevendo o serializador em byte para serializar os recursos em json*/
    @Override
    public byte[] serialize(String s, T object) {
        /* Primeiro converte o objeto em Json depois em Bytes*/
        return gson.toJson(object).getBytes();
    }
}
