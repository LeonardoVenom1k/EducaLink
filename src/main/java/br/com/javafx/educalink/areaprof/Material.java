package br.com.javafx.educalink.areaprof;

import java.time.LocalDateTime;

public class Material {
    private String tipo;        // Material ou Atividade
    private String assunto;
    private String materia;
    private LocalDateTime prazo;
    private String professorId; // identificador do professor que lançou

    // Construtor completo (5 parâmetros)
    public Material(String tipo, String assunto, String materia, LocalDateTime prazo, String professorId) {
        this.tipo = tipo;
        this.assunto = assunto;
        this.materia = materia;
        this.prazo = prazo;
        this.professorId = professorId;
    }

    // Construtor alternativo (sem professorId)
    public Material(String tipo, String assunto, String materia, LocalDateTime prazo) {
        this(tipo, assunto, materia, prazo, null); // professorId fica null
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
