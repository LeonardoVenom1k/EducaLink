package br.com.javafx.educalink.areaprof;

import br.com.javafx.educalink.database.DadosCompartilhados;
import br.com.javafx.educalink.professores.Professor;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.event.ActionEvent;

import java.io.IOException;
import java.util.Optional;

public class AreaProfController {

    @FXML
    private Label lblTotalAlunos;

    @FXML
    private Button sair;

    private Professor professor;

    @FXML
    public void initialize() {
        // Estilo hover no botão Sair
        sair.setOnMouseEntered(e -> sair.setStyle(
                "-fx-background-color: #6b00b3; -fx-text-fill: white; -fx-background-radius: 20; -fx-font-size: 14px; -fx-pref-width: 100px; -fx-cursor: hand;"));
        sair.setOnMouseExited(e -> sair.setStyle(
                "-fx-background-color: #820AD1; -fx-text-fill: white; -fx-background-radius: 20; -fx-font-size: 14px; -fx-pref-width: 100px; -fx-cursor: hand;"));

        // Clique no botão
        sair.setOnAction(this::clicouSair);
    }

    @FXML
    private void clicouSair(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmação");
        alert.setHeaderText("Deseja realmente sair?");
        alert.setContentText("Você será redirecionado para a tela de login.");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                Parent root = FXMLLoader.load(getClass().getResource("/br/com/javafx/educalink/login/login.fxml"));
                Stage stage = (Stage) sair.getScene().getWindow();
                stage.setScene(new Scene(root, 800, 500));
                stage.setTitle("EducaLink - Login");
                stage.setResizable(false);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    private void abrirTelaAlunos() {
        // abrir nova tela ou exibir os alunos
    }

    @FXML
    private void abrirTelaPendentes() {
        // abrir atividades pendentes
    }

    @FXML
    private void abrirTelaLancar() {
        // abrir tela de envio de material
    }

    public void atualizarQtdAlunos(int total) {
        lblTotalAlunos.setText(String.valueOf(total));
    }

    public void receberDadosProfessor(Professor professor) {
        this.professor = professor;

        // Pega os alunos inscritos nesse professor
        int total = DadosCompartilhados.getInstancia().getTotalAlunos(professor);
        lblTotalAlunos.setText("Total de Alunos: " + total);
    }
}