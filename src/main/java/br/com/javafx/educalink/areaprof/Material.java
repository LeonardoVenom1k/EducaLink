package br.com.javafx.educalink.areaprof;

import java.time.LocalDateTime;
import java.util.UUID;

public class Material {
    private String id; // novo campo
    private String tipo;        // Material ou Atividade
    private String assunto;
    private String materia;
    private LocalDateTime prazo;
    private String professorId; // identificador do professor que lançou
    private String caminhoArquivo;

    // Construtor completo (5 parâmetros)
    public Material(String tipo, String assunto, String materia, LocalDateTime prazo, String professorId) {
        this.id = UUID.randomUUID().toString(); // gera um ID único
        this.tipo = tipo;
        this.assunto = assunto;
        this.materia = materia;
        this.prazo = prazo;
        this.professorId = professorId;
    }

    public Material() {
        this.id = UUID.randomUUID().toString();
    }

    // Construtor alternativo (sem professorId)
    public Material(String tipo, String assunto, String materia, LocalDateTime prazo) {
        this(tipo, assunto, materia, prazo, null);
    }

    public String getId() {
        return id;
    }

    public String getCaminhoArquivo() {
        return caminhoArquivo;
    }

    public void setCaminhoArquivo(String caminhoArquivo) {
        this.caminhoArquivo = caminhoArquivo;
    }

    public String getTipo() {
        return tipo;
    }

    public String getAssunto() {
        return assunto;
    }

    public String getMateria() {
        return materia;
    }

    public LocalDateTime getPrazo() {
        return prazo;
    }

    public String getProfessorId() {
        return professorId;
    }
}
