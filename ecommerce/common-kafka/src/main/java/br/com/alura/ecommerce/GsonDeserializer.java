package br.com.alura.ecommerce;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.kafka.common.serialization.Deserializer;
import java.util.Map;

public class GsonDeserializer<T> implements Deserializer<T> { /* Implementa Deserializer para ser identificado como um Deserializador e extender as classes necessárias*/
    public static final String TYPE_CONFIG = "br.com.alura.ecommerce.type_config";
    private final Gson gson = new GsonBuilder().create();
    private Class<T> type;

    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {
        String typeName = String.valueOf(configs.get(TYPE_CONFIG));
        try {
            this.type = (Class<T>) Class.forName(typeName);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Type for deserialization does not exist in the class path!");
        }
    }

    /* Deserializador*/
    @Override
    public T deserialize(String s, byte[] bytes) {
        /* Deserializa de bytes para String e de de String para Jason*/
        return gson.fromJson(new String(bytes), type);
    }
}
