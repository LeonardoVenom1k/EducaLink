package br.com.javafx.educalink.areaalu;

import br.com.javafx.educalink.alunos.Aluno;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.input.MouseEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Optional;

public class AreaAluController {

    @FXML
    private Button sair;

    @FXML
    private Label materias;

    @FXML
    private Label atividades;

    @FXML
    private Label areadoAluno;

    // Armazena os dados do aluno que vieram do login
    private Aluno aluno;

    @FXML
    public void initialize() {
        String estiloLabelPadrao = "-fx-text-fill: #000000; -fx-underline: false; -fx-cursor: hand;";
        String estiloLabelHover = "-fx-text-fill: #6b00b3; -fx-underline: true; -fx-cursor: hand;";

        setupHover(materias, estiloLabelHover, estiloLabelPadrao);
        setupHover(atividades, estiloLabelHover, estiloLabelPadrao);
        setupHover(areadoAluno, estiloLabelHover, estiloLabelPadrao);

        sair.setOnMouseEntered(e -> sair.setStyle(
                "-fx-background-color: #6b00b3; -fx-text-fill: white; -fx-background-radius: 20; -fx-font-size: 14px; -fx-pref-width: 100px; -fx-cursor: hand;"));
        sair.setOnMouseExited(e -> sair.setStyle(
                "-fx-background-color: #820AD1; -fx-text-fill: white; -fx-background-radius: 20; -fx-font-size: 14px; -fx-pref-width: 100px; -fx-cursor: hand;"));
    }

    private void setupHover(Label label, String estiloHover, String estiloPadrao) {
        label.setStyle(estiloPadrao);
        label.setOnMouseEntered(e -> label.setStyle(estiloHover));
        label.setOnMouseExited(e -> label.setStyle(estiloPadrao));
    }

    public void receberDadosAluno(Aluno aluno) {
        this.aluno = aluno;  // Guarda o objeto na variável interna
        // Aqui pode atualizar labels da área do aluno, se quiser
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
    private void clicouMaterias(MouseEvent event) {
        mostrarAlerta("Você já está na seção Matérias.");
    }

    @FXML
    private void clicouAtividades(MouseEvent event) {
        mostrarAlerta("Você já está na seção Atividades.");
    }

    @FXML
    private void clicouAreaAluno(MouseEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/br/com/javafx/educalink/areaalu/perfil.fxml"));
            Parent root = loader.load();

            PerfilController perfilController = loader.getController();
            perfilController.receberDadosAluno(this.aluno);

            Stage stage = (Stage) sair.getScene().getWindow();
            stage.setScene(new Scene(root, 800, 500));
            stage.setTitle("Perfil do Aluno");
            stage.setResizable(false);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void mostrarAlerta(String mensagem) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Informação");
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }
}
