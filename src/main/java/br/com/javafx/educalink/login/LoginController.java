package br.com.javafx.educalink.login;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.event.ActionEvent;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

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

    @FXML
    private void entrar(ActionEvent event) {
        String user = matricula.getText();
        String pass = senhaEstaVisivel ? senhaVisivel.getText() : senha.getText();

        if ("2025".equals(user) && "faitec25".equals(pass)) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/br/com/javafx/educalink/areaalu/areaalu.fxml"));
                Parent root = loader.load();

                Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
                stage.setScene(new Scene(root));
                stage.setTitle("EducaLink - Área do Aluno");
                stage.setResizable(false);
                stage.show();

            } catch (IOException e) {
                e.printStackTrace();
                mostrarAlerta("Erro ao carregar a área do aluno.");
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
