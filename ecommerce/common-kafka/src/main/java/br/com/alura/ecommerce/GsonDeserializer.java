package br.com.alura.ecommerce;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.kafka.common.serialization.Deserializer;
import java.util.Map;

/* Implementa Deserializer para ser identificado como um Deserializador e extender as classes necessárias*/
public class GsonDeserializer implements Deserializer<Message> {

    /* Cria um Gson com registerTypeAdapter para especificar como Adaptar do Serializer/Deserializer para o tipo Message*/
    private final Gson gson = new GsonBuilder().registerTypeAdapter(Message.class, new MessageAdapter()).create();

    /* Deserializador para tipo Message*/
    @Override
    public Message deserialize(String s, byte[] bytes) {
        /* Deserializa de bytes para String e de de String para Jason*/
        return gson.fromJson(new String(bytes), Message.class);
    }
}
