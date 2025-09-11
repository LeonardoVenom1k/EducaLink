package br.com.javafx.educalink.areaalu;

import br.com.javafx.educalink.alunos.Aluno;
import br.com.javafx.educalink.professores.Professor;
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
import javafx.geometry.Rectangle2D;
import javafx.stage.Screen;

import java.io.IOException;
import java.util.List;
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

    private Aluno aluno;
    private List<Professor> professores;

    @FXML
    public void initialize() {
        String estiloLabelPadrao = "-fx-text-fill: #000000; -fx-underline: false; -fx-cursor: hand;";
        String estiloLabelHover = "-fx-text-fill: #6b00b3; -fx-underline: true; -fx-cursor: hand;";

        setupHover(materias, estiloLabelHover, estiloLabelPadrao);
        setupHover(atividades, estiloLabelHover, estiloLabelPadrao);
        setupHover(areadoAluno, estiloLabelHover, estiloLabelPadrao);

        sair.setOnMouseEntered(e -> sair.setStyle(
                "-fx-background-color: #6b00b3; -fx-text-fill: white; -fx-background-radius: 20; -fx-font-size: 18px; -fx-pref-width: 100px; -fx-cursor: hand;"));
        sair.setOnMouseExited(e -> sair.setStyle(
                "-fx-background-color: #820AD1; -fx-text-fill: white; -fx-background-radius: 20; -fx-font-size: 18px; -fx-pref-width: 100px; -fx-cursor: hand;"));
    }

    private void setupHover(Label label, String estiloHover, String estiloPadrao) {
        label.setStyle(estiloPadrao);
        label.setOnMouseEntered(e -> label.setStyle(estiloHover));
        label.setOnMouseExited(e -> label.setStyle(estiloPadrao));
    }

    public void receberDadosAluno(Aluno aluno) {
        this.aluno = aluno;
        System.out.println("Recebido aluno: " + aluno.getNome());
    }

    public void receberDadosProfessor(List<Professor> professores) {
        this.professores = professores;
        for (Professor p : professores) {
            System.out.println("Recebido professor: " + p.getNome());
        }
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
                trocaCena(stage, root, "EducaLink - Login");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    private void clicouMaterias(MouseEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/br/com/javafx/educalink/areaalu/inscricao.fxml"));
            Parent root = loader.load();

            InscricaoController controller = loader.getController();
            controller.receberDadosAluno(this.aluno);
            controller.receberDadosProfessor(this.professores);

            Stage stage = (Stage) materias.getScene().getWindow();
            trocaCena(stage, root, "Inscrição em Matérias");
        } catch (IOException e) {
            e.printStackTrace();
        }
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
            perfilController.receberDadosProfessor(this.professores);

            Stage stage = (Stage) sair.getScene().getWindow();
            trocaCena(stage, root, "Perfil do Aluno");
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

    private void trocaCena(Stage stage, Parent root, String titulo) {
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle(titulo);

        // 🔥 sempre maximiza
        stage.setMaximized(true);
        stage.setResizable(true);

        // 🔄 se um dia não quiser maximizar, centraliza na tela
        if (!stage.isMaximized()) {
            Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
            stage.setX((screenBounds.getWidth() - stage.getWidth()) / 2);
            stage.setY((screenBounds.getHeight() - stage.getHeight()) / 2);
        }
    }
}
