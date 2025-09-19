package br.com.javafx.educalink.professores;

import br.com.javafx.educalink.areaprof.Material;
import java.util.ArrayList;
import java.util.List;

public class Professor {
    private String nome;
    private String matricula; // também usado como id
    private String curso;
    private List<String> materias; // apenas nomes das matérias
    private List<Material> materiais; // objetos de Material vinculados ao professor

    public Professor(String nome, String matricula, String curso) {
        this.nome = nome;
        this.matricula = matricula;
        this.curso = curso;
        this.materias = new ArrayList<>();
        this.materiais = new ArrayList<>();
    }

    // Getters e Setters
    public String getCurso() {
        return curso;
    }

    public void setCurso(String curso) {
        this.curso = curso;
    }

    public String getNome() {
        return nome;
    }

    public String getMatricula() {
        return matricula;
    }

    public String getId() {
        return matricula; // id é a matrícula
    }

    public List<String> getMaterias() {
        return materias;
    }

    public void adicionarMateria(String materia) {
        materias.add(materia);
    }

    // 🔥 Lista de materiais reais
    public List<Material> getMateriais() {
        return materiais;
    }

    public void adicionarMaterial(Material material) {
        materiais.add(material);
    }
}