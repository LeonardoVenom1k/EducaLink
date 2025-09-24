package br.com.javafx.educalink.database;

import br.com.javafx.educalink.alunos.Aluno;
import br.com.javafx.educalink.areaprof.AluInscritoController;
import br.com.javafx.educalink.areaprof.AreaProfController;
import br.com.javafx.educalink.areaprof.Material;
import br.com.javafx.educalink.professores.Professor;

import java.util.*;
import java.io.*;

public class DadosCompartilhados {
    private List<Material> materiais;
    private static List<br.com.javafx.educalink.areaalu.Entrega> entregas = new ArrayList<>();
    private AreaProfController areaProfController;
    private AluInscritoController aluInscritoController;
    private static DadosCompartilhados instancia;

    private Map<String, Professor> professores = new HashMap<>();
    private Map<String, Aluno> alunos = new HashMap<>();
    private Map<String, List<String>> inscricoesJson; // ID professor → list<matrículas>


    private DadosCompartilhados() {
        inscricoesJson = InscricaoStorage.carregar();
        materiais = MaterialStorage.carregar(); // carregar do arquivo usando MaterialStorage
    }

    public static DadosCompartilhados getInstancia() {
        if (instancia == null) {
            instancia = new DadosCompartilhados();
        }
        return instancia;
    }

    public static void registrarEntrega(br.com.javafx.educalink.areaalu.Entrega entrega) {
        entregas.add(entrega);
    }

    public static List<br.com.javafx.educalink.areaalu.Entrega> getEntregas() {
        return entregas;
    }

    // para filtrar entregas de uma atividade específica
    public static List<br.com.javafx.educalink.areaalu.Entrega> getEntregasPorAtividade(Material atividade) {
        return entregas.stream()
                .filter(e -> e.getAtividade().equals(atividade))
                .toList();
    }

    // -------------------------
    // Controllers
    // -------------------------
    public void setAreaProfController(AreaProfController controller) {
        this.areaProfController = controller;
    }

    public AreaProfController getAreaProfController() {
        return this.areaProfController;
    }

    public void setAluInscritoController(AluInscritoController controller) {
        this.aluInscritoController = controller;
    }

    public AluInscritoController getAluInscritoController() {
        return aluInscritoController;
    }

    // -------------------------
    // Cadastro de entidades
    // -------------------------
    public void cadastrarProfessor(Professor prof) {
        professores.put(prof.getId(), prof);
    }

    public void cadastrarAluno(Aluno aluno) {
        alunos.put(aluno.getMatricula(), aluno);
    }

    // -------------------------
    // Inscrição e desinscrição
    // -------------------------
    public void inscreverAluno(Professor professor, Aluno aluno) {
        String idProf = professor.getId();
        inscricoesJson.putIfAbsent(idProf, new ArrayList<>());

        if (!inscricoesJson.get(idProf).contains(aluno.getMatricula())) {
            inscricoesJson.get(idProf).add(aluno.getMatricula());
            salvarInscricoes();

            AreaProfController controller = getAreaProfController();
            if (controller != null && controller.getProfessor().getId().equals(idProf)) {
                int total = getTotalAlunos(professor);
                controller.atualizarQtdAlunos(total);
            }
        }
    }

    public void desinscreverAluno(Professor professor, Aluno aluno) {
        String idProf = professor.getId();
        if (inscricoesJson.containsKey(idProf)) {
            inscricoesJson.get(idProf).remove(aluno.getMatricula());
            salvarInscricoes();

            AreaProfController controller = getAreaProfController();
            if (controller != null && controller.getProfessor().getId().equals(idProf)) {
                int total = getTotalAlunos(professor);
                controller.atualizarQtdAlunos(total);

                AluInscritoController aluController = controller.getAluInscritoController();
                if (aluController != null) {
                    aluController.carregarAlunos(getAlunosInscritos(professor));
                }
            }
        }
    }

    // -------------------------
    // Consultas
    // -------------------------
    public boolean alunoEstaInscrito(Professor professor, Aluno aluno) {
        return inscricoesJson.getOrDefault(professor.getId(), Collections.emptyList())
                .contains(aluno.getMatricula());
    }

    public int getTotalAlunos(Professor professor) {
        return inscricoesJson.getOrDefault(professor.getId(), Collections.emptyList()).size();
    }

    public List<Aluno> getAlunosInscritos(Professor professor) {
        List<String> matriculas = inscricoesJson.getOrDefault(professor.getId(), Collections.emptyList());
        List<Aluno> alunosInscritos = new ArrayList<>();
        for (String matricula : matriculas) {
            if (alunos.containsKey(matricula)) {
                alunosInscritos.add(alunos.get(matricula));
            }
        }
        return alunosInscritos;
    }

    // -------------------------
    // Materiais
    // -------------------------
    public List<Material> getMateriais() {
        return materiais;
    }

    public void adicionarMaterial(Material m) {
        materiais.add(m);
        MaterialStorage.salvar(materiais); // salva usando MaterialStorage
    }

    // -------------------------
    // Professores e alunos
    // -------------------------
    public Map<String, Professor> getProfessores() {
        return professores;
    }

    public Map<String, Aluno> getAlunos() {
        return alunos;
    }

    public Professor getProfessorPorMateria(String nomeMateria) {
        String matLower = nomeMateria.trim().toLowerCase();
        return professores.values().stream()
                .filter(p -> p.getMaterias().stream().anyMatch(m -> m.trim().toLowerCase().equals(matLower)))
                .findFirst()
                .orElse(null);
    }

    // -------------------------
    // Persistência
    // -------------------------
    private void salvarInscricoes() {
        InscricaoStorage.salvar(inscricoesJson);
    }
}