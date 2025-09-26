package br.com.javafx.educalink.database;

import br.com.javafx.educalink.areaalu.Entrega;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class EntregaStorage {
    private static final File ARQUIVO = new File("entregas.json");

    private static ObjectMapper criarMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS); // formato ISO-8601
        return mapper;
    }

    // Salvar lista de entregas no arquivo
    public static void salvar(List<Entrega> entregas) {
        try {
            ObjectMapper mapper = criarMapper();
            mapper.writerWithDefaultPrettyPrinter().writeValue(ARQUIVO, entregas);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Carregar lista de entregas do arquivo
    public static List<Entrega> carregar() {
        try {
            if (ARQUIVO.exists()) {
                if (ARQUIVO.length() == 0) { // arquivo vazio → retorna lista vazia
                    return new ArrayList<>();
                }
                ObjectMapper mapper = criarMapper(); // <-- corrigido: usar criarMapper()
                return mapper.readValue(ARQUIVO, new TypeReference<List<Entrega>>() {});
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }
}
