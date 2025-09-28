package br.com.javafx.educalink.database;

import br.com.javafx.educalink.areaprof.Correcao;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class CorrecaoStorage {
    private static final String ARQUIVO = "correcoes.json";

    public static void adicionar(Correcao correcao) {
        List<Correcao> correcoes = carregar();
        correcoes.add(correcao);

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String json = gson.toJson(correcoes);

        try {
            Files.write(Paths.get(ARQUIVO), json.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static List<Correcao> carregar() {
        try {
            if (!Files.exists(Paths.get(ARQUIVO))) return new ArrayList<>();

            String json = new String(Files.readAllBytes(Paths.get(ARQUIVO)));
            Type tipoLista = new TypeToken<List<Correcao>>() {}.getType();

            List<Correcao> lista = new Gson().fromJson(json, tipoLista);
            return lista != null ? lista : new ArrayList<>();

        } catch (IOException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
}