package br.com.javafx.educalink.database;

import br.com.javafx.educalink.alunos.Aluno;
import br.com.javafx.educalink.areaprof.AluInscritoController;
import br.com.javafx.educalink.areaprof.AreaProfController;
import br.com.javafx.educalink.professores.Professor;

import java.util.*;

public class DadosCompartilhados {
    private AreaProfController areaProfController;
    private AluInscritoController aluInscritoController;
    private static DadosCompartilhados instancia;

    private Map<String, Professor> professores = new HashMap<>();
    private Map<String, Aluno> alunos = new HashMap<>();
    private Map<String, List<String>> inscricoesJson; // ID professor → list<matrículas>

    private DadosCompartilhados() {
        inscricoesJson = InscricaoStorage.carregar();
    }

    public static DadosCompartilhados getInstancia() {
        if (instancia == null) {
            instancia = new DadosCompartilhados();
        }
        return instancia;
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
            salvar();

            // Atualizar UI se o professor correspondente estiver logado
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
            salvar();

            // 🔥 Atualizar a UI do professor em tempo real
            AreaProfController controller = getAreaProfController();
            if (controller != null && controller.getProfessor().getId().equals(idProf)) {
                int total = getTotalAlunos(professor);
                controller.atualizarQtdAlunos(total);

                // Só tenta recarregar os cards se a tela de alunos inscritos estiver aberta
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
    // Persistência
    // -------------------------
    private void salvar() {
        InscricaoStorage.salvar(inscricoesJson);
    }
}