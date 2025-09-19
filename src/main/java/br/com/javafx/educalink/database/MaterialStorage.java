package br.com.javafx.educalink.database;

import br.com.javafx.educalink.areaprof.Material;
import com.google.gson.*;
import com.google.gson.reflect.TypeToken;

import java.io.*;
import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

// Adapter para serializar/deserializar LocalDateTime
class LocalDateTimeAdapter implements JsonSerializer<LocalDateTime>, JsonDeserializer<LocalDateTime> {
    private static final DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    @Override
    public JsonElement serialize(LocalDateTime src, Type typeOfSrc, JsonSerializationContext context) {
        return new JsonPrimitive(src.format(formatter));
    }

    @Override
    public LocalDateTime deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        return LocalDateTime.parse(json.getAsString(), formatter);
    }
}

public class MaterialStorage {
    private static final String CAMINHO = "materiais.json";
    private static final Gson gson = new GsonBuilder()
            .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
            .setPrettyPrinting()
            .create();

    public static void salvar(List<Material> materiais) {
        try (Writer writer = new FileWriter(CAMINHO)) {
            gson.toJson(materiais, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static List<Material> carregar() {
        try (Reader reader = new FileReader(CAMINHO)) {
            Type tipo = new TypeToken<List<Material>>() {}.getType();
            List<Material> materiais = gson.fromJson(reader, tipo);
            return materiais != null ? materiais : new ArrayList<>();
        } catch (IOException e) {
            return new ArrayList<>();
        }
    }
}
