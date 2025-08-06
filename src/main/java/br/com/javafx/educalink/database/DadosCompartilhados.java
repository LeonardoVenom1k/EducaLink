package br.com.javafx.educalink.database;

import br.com.javafx.educalink.alunos.Aluno;
import br.com.javafx.educalink.professores.Professor;

import java.util.*;

public class DadosCompartilhados {

    private static DadosCompartilhados instancia;

    // Mapa de professor -> lista de alunos inscritos
    private Map<Professor, List<Aluno>> inscricoesPorProfessor = new HashMap<>();

    private DadosCompartilhados() {}

    public static DadosCompartilhados getInstancia() {
        if (instancia == null) {
            instancia = new DadosCompartilhados();
        }
        return instancia;
    }

    public void inscreverAluno(Professor professor, Aluno aluno) {
        inscricoesPorProfessor.computeIfAbsent(professor, k -> new ArrayList<>()).add(aluno);
    }

    public List<Aluno> getAlunosDoProfessor(Professor professor) {
        return inscricoesPorProfessor.getOrDefault(professor, new ArrayList<>());
    }

    public int getTotalAlunos(Professor professor) {
        return getAlunosDoProfessor(professor).size();
    }
}
