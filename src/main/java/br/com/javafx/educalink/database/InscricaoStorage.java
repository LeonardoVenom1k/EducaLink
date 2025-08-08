package br.com.javafx.educalink.database;

import br.com.javafx.educalink.alunos.Aluno;
import br.com.javafx.educalink.professores.Professor;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.*;
import java.lang.reflect.Type;
import java.util.*;

public class InscricaoStorage {
    private static final String CAMINHO = "inscricoes.json";
    private static final Gson gson = new Gson();

    public static void salvar(Map<String, List<String>> mapaInscricoes) {
        try (Writer writer = new FileWriter(CAMINHO)) {
            gson.toJson(mapaInscricoes, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Map<String, List<String>> carregar() {
        try (Reader reader = new FileReader(CAMINHO)) {
            Type tipo = new TypeToken<Map<String, List<String>>>() {}.getType();
            return gson.fromJson(reader, tipo);
        } catch (IOException e) {
            return new HashMap<>();
        }
    }
}
