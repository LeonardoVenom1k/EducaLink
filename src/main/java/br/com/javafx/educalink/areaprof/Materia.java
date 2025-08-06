package br.com.javafx.educalink.areaprof;

public class Materia {
    private String nome;
    private String professor;

    public Materia(String nome, String professor) {
        this.nome = nome;
        this.professor = professor;
    }

    public String getNome() {
        return nome;
    }

    public String getProfessor() {
        return professor;
    }
}
