package br.com.javafx.educalink.professores;

public class Professor {
    private String nome;
    private String matricula;
    private String curso;

    public Professor(String nome, String matricula, String curso) {
        this.nome = nome;
        this.matricula = matricula;
        this.curso = curso;
    }

    // Getters e Setters
    public String getCurso() {
        return curso;
    }

    public String getNome() {
        return nome;
    }

    public void setCurso(String curso) {
        this.curso = curso;
    }
}
