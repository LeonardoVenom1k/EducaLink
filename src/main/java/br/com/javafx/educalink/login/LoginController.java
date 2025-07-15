package br.com.javafx.educalink.login;

import br.com.javafx.educalink.areaalu.AreaAluController;
import br.com.javafx.educalink.alunos.Aluno;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.event.ActionEvent;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.util.HashMap;
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
    }

    private Map<String, Aluno> criarMapaAlunos() {
        Map<String, Aluno> alunos = new HashMap<>();

        Aluno aluno1 = new Aluno("Leonardo Aguiar", "2025");
        aluno1.setCurso("Sistemas de Informação");
        aluno1.setEndereco("Rua Fioravante Guersoni");
        aluno1.setBairro("Cruzeiro");
        aluno1.setNumero("301");
        alunos.put("2025", aluno1);

        Aluno aluno2 = new Aluno("Lucas Rodrigues Xavier", "20251");
        aluno2.setCurso("Sistemas de Informação");
        aluno2.setEndereco("Rua das Andorinhas");
        aluno2.setBairro("Papagaio");
        aluno2.setNumero("679");
        alunos.put("20251", aluno2);

        return alunos;
    }

    private Map<String, String> criarMapaSenhas() {
        Map<String, String> senhas = new HashMap<>();
        senhas.put("2025", "faitec25");
        senhas.put("20251", "faitec25");
        return senhas;
    }


    @FXML
    private void entrar(ActionEvent event) {
        String user = matricula.getText();
        String pass = senhaEstaVisivel ? senhaVisivel.getText() : senha.getText();

        Map<String, String> senhas = criarMapaSenhas();
        Map<String, Aluno> alunos = criarMapaAlunos();

        // Verificação
        if (senhas.containsKey(user) && senhas.get(user).equals(pass)) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/br/com/javafx/educalink/areaalu/areaalu.fxml"));
                Parent root = loader.load();

                AreaAluController controller = loader.getController();

                // Pega o aluno pelo user (matrícula)
                Aluno alunoLogado = alunos.get(user);
                controller.receberDadosAluno(alunoLogado);

                Stage stage = (Stage) entrar.getScene().getWindow();
                stage.setScene(new Scene(root, 800, 500));
                stage.setTitle("EducaLink - Área do Aluno");
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