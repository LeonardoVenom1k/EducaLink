package br.com.javafx.educalink.alunos;

import java.util.List;

public class Aluno {
    private String nome;
    private String matricula;
    private String curso;
    private String endereco;
    private String bairro;
    private String numero;
    private String email;
    private List<String> idsProfessores;

    public Aluno(String nome, String matricula) {
        this.nome = nome;
        this.matricula = matricula;
        this.curso = "Sistemas de Informação";
        this.endereco = "Rua Exemplo, 123";
        this.bairro = "Centro";
        this.numero = "123";
    }

    public Aluno(String nome, List<String> idsProfessores) {
        this.nome = nome;
        this.idsProfessores = idsProfessores;
    }

    public String getNome() {
        return nome;
    }

    public String getMatricula() {
        return matricula;
    }

    public List<String> getIdsProfessores() {
        return idsProfessores;
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

    public String getEmail() {
        return email;
    }

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

    public void setEmail(String email) {
        this.email = email;
    }

    public void setIdsProfessores(List<String> ids) { this.idsProfessores = ids; }
}
