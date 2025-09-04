package br.com.javafx.educalink.login;

import br.com.javafx.educalink.areaalu.AreaAluController;
import br.com.javafx.educalink.alunos.Aluno;
import br.com.javafx.educalink.database.DadosCompartilhados;
import br.com.javafx.educalink.professores.Professor;
import br.com.javafx.educalink.areaprof.AreaProfController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.event.ActionEvent;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import java.io.IOException;

public class LoginController {

    @FXML
    private TextField matricula;

    @FXML
    private PasswordField senha;

    @FXML
    private TextField senhaVisivel;

    @FXML
    private Button entrar;

    @FXML
    private Hyperlink esquecisenha;

    @FXML
    private ImageView iconSenha;

    private boolean senhaEstaVisivel = false;

    @FXML
    private void initialize() {
        senhaVisivel.setVisible(false);

        senhaVisivel.textProperty().addListener((obs, oldText, newText) -> senha.setText(newText));
        senha.textProperty().addListener((obs, oldText, newText) -> senhaVisivel.setText(newText));

        iconSenha.setStyle("-fx-cursor: hand;");

        entrar.setOnMouseEntered(e -> entrar.setStyle("-fx-background-color: #6b00b3; -fx-text-fill: white; -fx-background-radius: 20; -fx-cursor: hand;"));
        entrar.setOnMouseExited(e -> entrar.setStyle("-fx-background-color: #820AD1; -fx-text-fill: white; -fx-background-radius: 20;"));

        esquecisenha.setOnMouseEntered(e -> esquecisenha.setStyle("-fx-text-fill: #a53de0; -fx-underline: true; -fx-cursor: hand;"));
        esquecisenha.setOnMouseExited(e -> esquecisenha.setStyle("-fx-text-fill: #820AD1;"));

        // 🔥 Registrar todos os alunos e professores no singleton
        DadosCompartilhados dados = DadosCompartilhados.getInstancia();
        Map<String, Aluno> alunos = criarMapaAlunos();
        Map<String, Professor> professores = criarMapaProfessores();

        for (Aluno a : alunos.values()) {
            dados.cadastrarAluno(a);
            for (String idProf : a.getIdsProfessores()) {
                if (professores.containsKey(idProf)) {
                    dados.cadastrarProfessor(professores.get(idProf));
                }
            }
        }
    }

    public Map<String, Professor> criarMapaProfessores() {
        Map<String, Professor> professores = new HashMap<>();

        Professor prof1 = new Professor("Carlos Vieira", "admin","Matemática");
        professores.put("admin", prof1);

        Professor prof2 = new Professor("Luiz Gonzaga", "admin2", "Português");
        professores.put("admin2", prof2);

        return professores;
    }

    private Map<String, Aluno> criarMapaAlunos() {
        Map<String, Aluno> alunos = new HashMap<>();

        Aluno aluno1 = new Aluno("Leonardo Aguiar", "2025");
        aluno1.setCurso("Sistemas de Informação");
        aluno1.setEndereco("Rua Fioravante Guersoni");
        aluno1.setBairro("Cruzeiro");
        aluno1.setNumero("301");
        aluno1.setEmail("leonardo@email.com");
        aluno1.setIdsProfessores(List.of("admin", "admin2"));
        alunos.put("2025", aluno1);

        Aluno aluno2 = new Aluno("Lucas Rodrigues Xavier", "20251");
        aluno2.setCurso("Sistemas de Informação");
        aluno2.setEndereco("Rua das Andorinhas");
        aluno2.setBairro("Papagaio");
        aluno2.setNumero("679");
        aluno2.setEmail("lucas@email.com");
        aluno2.setIdsProfessores(List.of("admin", "admin2"));
        alunos.put("20251", aluno2);

        return alunos;
    }

    private Map<String, String> criarMapaSenhas() {
        Map<String, String> senhas = new HashMap<>();
        senhas.put("2025", "faitec25");    // Aluno
        senhas.put("20251", "faitec25");   // Aluno2
        senhas.put("admin", "admin"); //Professor
        senhas.put("admin2", "admin2"); // Professor2
        return senhas;
    }


    @FXML
    private void entrar(ActionEvent event) {
        String user = matricula.getText();
        String pass = senhaEstaVisivel ? senhaVisivel.getText() : senha.getText();

        Map<String, String> senhas = criarMapaSenhas();
        Map<String, Aluno> alunos = criarMapaAlunos();
        Map<String, Professor> professores = criarMapaProfessores();

        if (senhas.containsKey(user) && senhas.get(user).equals(pass)) {
            try {
                Stage stage = (Stage) entrar.getScene().getWindow();

                if (alunos.containsKey(user)) {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/br/com/javafx/educalink/areaalu/areaalu.fxml"));
                    Parent root = loader.load();

                    AreaAluController controller = loader.getController();
                    Aluno alunoLogado = alunos.get(user);

                    List<Professor> professoresDoAluno = new ArrayList<>();
                    for (String idProf : alunoLogado.getIdsProfessores()) {
                        Professor prof = professores.get(idProf);
                        if (prof != null) {
                            professoresDoAluno.add(prof);
                        }
                    }

                    controller.receberDadosAluno(alunoLogado);
                    controller.receberDadosProfessor(professoresDoAluno);

                    // 🔥 Atualiza singleton (DadosCompartilhados)
                    DadosCompartilhados dados = DadosCompartilhados.getInstancia();
                    dados.cadastrarAluno(alunoLogado);

                    for (Professor prof : professoresDoAluno) {
                        dados.cadastrarProfessor(prof);
                    }

                    DadosCompartilhados.getInstancia().setAreaProfController(null);

                    stage.setScene(new Scene(root, 800, 500));
                    stage.setTitle("EducaLink - Área do Aluno");

                } else if (professores.containsKey(user)) {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/br/com/javafx/educalink/areaprof/areaprof.fxml"));
                    Parent root = loader.load();

                    AreaProfController controller = loader.getController();
                    Professor professorLogado = professores.get(user);
                    controller.receberDadosProfessor(professorLogado);

                    DadosCompartilhados.getInstancia().setAreaProfController(controller);

                    stage.setScene(new Scene(root, 800, 500));
                    stage.setTitle("EducaLink - Área do Professor");
                    stage.show();
                }

                stage.setResizable(false);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            mostrarAlerta("Matrícula ou senha incorretos.");
        }
    }


    @FXML
    private void esquecisenha(MouseEvent event) {
        mostrarAlerta("Solicitação de redefinição de senha enviada!");
    }

    @FXML
    private void alternarSenha(MouseEvent event) {
        senhaEstaVisivel = !senhaEstaVisivel;

        if (senhaEstaVisivel) {
            senhaVisivel.setText(senha.getText());
            senhaVisivel.setVisible(true);
            senhaVisivel.setManaged(true);
            senha.setVisible(false);
            senha.setManaged(false);
            senhaVisivel.requestFocus();
            senhaVisivel.positionCaret(senhaVisivel.getText().length());
        } else {
            senha.setText(senhaVisivel.getText());
            senha.setVisible(true);
            senha.setManaged(true);
            senhaVisivel.setVisible(false);
            senhaVisivel.setManaged(false);
            senha.requestFocus();
            senha.positionCaret(senha.getText().length());
        }
    }

    private void mostrarAlerta(String mensagem) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Aviso");
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }
}
