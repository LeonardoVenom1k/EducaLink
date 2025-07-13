package br.com.javafx.educalink.login;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.event.ActionEvent;

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
        // Oculta o campo de senha visível no início
        senhaVisivel.setVisible(false);

        // Sincroniza os campos quando o usuário digita
        senhaVisivel.textProperty().addListener((obs, oldText, newText) -> senha.setText(newText));
        senha.textProperty().addListener((obs, oldText, newText) -> senhaVisivel.setText(newText));
    }

    @FXML
    private void entrar(ActionEvent event) {
        String user = matricula.getText();
        String pass = senhaEstaVisivel ? senhaVisivel.getText() : senha.getText();

        if ("2025".equals(user) && "faitec25".equals(pass)) {
            mostrarAlerta("Login realizado com sucesso!");
            // Aqui você pode carregar outra tela
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
