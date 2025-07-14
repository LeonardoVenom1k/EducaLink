package br.com.javafx.educalink.alunos;

public class Aluno {
    private String nome;
    private String matricula;
    private String curso;
    private String endereco;
    private String bairro;
    private String numero;

    public Aluno(String nome, String matricula) {
        this.nome = nome;
        this.matricula = matricula;
        this.curso = "Sistemas de Informação";
        this.endereco = "Rua Exemplo, 123";
        this.bairro = "Centro";
        this.numero = "123";
    }

    // Getters
    public String getNome() {
        return nome;
    }

    public String getMatricula() {
        return matricula;
    }

    public String getCurso() {
        return curso;
    }

    public String getEndereco() {
        return endereco;
    }

    public String getBairro() {
        return bairro;
    }

    public String getNumero() {
        return numero;
    }

    // (Opcional) Setters se quiser permitir alteração depois
    public void setCurso(String curso) {
        this.curso = curso;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public void setBairro(String bairro) {
        this.bairro = bairro;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

}
