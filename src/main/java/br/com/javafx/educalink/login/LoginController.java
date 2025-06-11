package br.com.javafx.educalink.login;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.PasswordField;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Button;
import javafx.event.ActionEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class LoginController {

    @FXML
    private TextField matricula;

    @FXML
    private PasswordField senha;

    @FXML
    private Button entrar;

    @FXML
    private Hyperlink esquecisenha;

    @FXML
    private void entrar(ActionEvent event) {
        String user = matricula.getText();
        String pass = senha.getText();

        // Aqui você vai validar o login. Por enquanto só exemplo:
        if ("1234".equals(user) && "admin".equals(pass)) {
            mostrarAlerta("Login realizado com sucesso!");
            // Navegar para próxima tela aqui
        } else {
            mostrarAlerta("Matrícula ou senha incorretos.");
        }
    }

    @FXML
    private void esquecisenha(MouseEvent event) {
        mostrarAlerta("Solicitação de redefinição de senha enviada!");
        // Pode abrir outra tela ou fazer algo mais aqui
    }

    private void mostrarAlerta(String mensagem) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Aviso");
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }
}
