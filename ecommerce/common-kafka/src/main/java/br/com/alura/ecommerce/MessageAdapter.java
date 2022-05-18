package br.com.alura.ecommerce;

import com.google.gson.*;

import java.lang.reflect.Type;

/* Class para Adaptar a Serializer/Deserializer para o tipo Message*/
public class MessageAdapter implements JsonSerializer<Message>, JsonDeserializer<Message> {

    /* Adapter para Serializer*/
    @Override
    public JsonElement serialize(Message message, Type type, JsonSerializationContext context) {
        /* novo objeto Json*/
        JsonObject obj = new JsonObject();

        /* Adicionando ao objeto a propriedade "type" e especificando seu tipo como o tipo do payload da mensagem*/
        obj.addProperty("type", message.getPayload().getClass().getName());

        /* Serializando "payload" */
        obj.add("payload", context.serialize(message.getPayload()));

        /* Serializando o "correlationID"*/
        obj.add("correlationId", context.serialize(message.getId()));

        /* Retorna o objeto contendo os valores serializados*/
        return obj;
    }

    /* Adapter para deserialize do Message*/
    @Override
    public Message deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext context) throws JsonParseException {
        /* Converte o JsonElemento recebido em um JsonObject*/
        var obj = jsonElement.getAsJsonObject();

        /* Armazena o "type" do ogjeto como String*/
        var payloadType = obj.get("type").getAsString();

        /* Deserializa o "correlationId"*/
        var correlationId = (CorrelationId) context.deserialize(obj.get("correlationId"), CorrelationId.class);
        try {
            /* Deserializa o "payload"*/
            var payload = context.deserialize(obj.get("payload"), Class.forName(payloadType));

            /* Retorna a Message Deserializado*/
            return new Message<>(correlationId, payload);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
}
