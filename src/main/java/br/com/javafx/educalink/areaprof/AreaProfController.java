package br.com.javafx.educalink.areaprof;

import br.com.javafx.educalink.database.DadosCompartilhados;
import br.com.javafx.educalink.professores.Professor;
import javafx.animation.ScaleTransition;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import javafx.util.Duration;

import java.io.IOException;
import java.util.Optional;

public class AreaProfController {

    @FXML
    private VBox cardAlunos;

    @FXML
    private VBox cardPendentes;

    @FXML
    private VBox cardLancar;

    @FXML
    private AluInscritoController aluInscritoController;

    @FXML
    private Label lblTotalAlunos;

    @FXML
    private Button sair;

    private Professor professor;

    @FXML
    public void initialize() {
        // Estilo hover no botão Sair
        sair.setOnMouseEntered(e -> sair.setStyle(
                "-fx-background-color: #6b00b3; -fx-text-fill: white; -fx-background-radius: 20; -fx-font-size: 18px; -fx-pref-width: 100px; -fx-cursor: hand;"));
        sair.setOnMouseExited(e -> sair.setStyle(
                "-fx-background-color: #820AD1; -fx-text-fill: white; -fx-background-radius: 20; -fx-font-size: 18px; -fx-pref-width: 100px; -fx-cursor: hand;"));

        // Clique no botão
        sair.setOnAction(this::clicouSair);

        aplicarAnimacaoCard(cardAlunos);
        aplicarAnimacaoCard(cardPendentes);
        aplicarAnimacaoCard(cardLancar);
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
                stage.setScene(new Scene(root));
                stage.setTitle("EducaLink - Login");
                stage.setMaximized(true);
                stage.setResizable(true);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    private void abrirTelaAlunos() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/br/com/javafx/educalink/areaprof/aluinscrito.fxml"));
            Parent root = loader.load();

            // Pega o controller da tela de alunos inscritos
            AluInscritoController controller = loader.getController();

            // Envia o professor logado para que ele carregue a lista
            controller.carregarAlunos(DadosCompartilhados.getInstancia().getAlunosInscritos(professor));

            AluInscritoController aluController = loader.getController();
            aluController.setProfessor(this.professor); // passa o professor logado

            // Troca a cena no mesmo Stage
            Stage stage = (Stage) lblTotalAlunos.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("EducaLink - Alunos Inscritos");
            stage.setResizable(false);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void abrirTelaPendentes(MouseEvent event) {
        mostrarAlerta("Tela em desenvolvimento...");
    }

    @FXML
    private void abrirTelaLancar(MouseEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/br/com/javafx/educalink/areaprof/lancarmaterial.fxml"));
            Parent root = loader.load();

            // Pega o controller da tela de alunos inscritos
            LancarMaterialController controller = loader.getController();

            // Envia o professor logado para que ele carregue a lista
            controller.carregarAlunos(DadosCompartilhados.getInstancia().getAlunosInscritos(professor));

            LancarMaterialController lancarmaterial = loader.getController();
            lancarmaterial.setProfessor(this.professor); // passa o professor loga

            Stage stage = (Stage) lblTotalAlunos.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("EducaLink - Lançar Material/Atividade");
            stage.setMaximized(true);
            stage.setResizable(true);

        } catch (IOException e) {
            e.printStackTrace();
            mostrarAlerta("Erro ao abrir a tela de lançamento!");
        }
    }


    public AluInscritoController getAluInscritoController() {
        return aluInscritoController;
    }

    private void mostrarAlerta(String mensagem) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Informação");
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }

    public Professor getProfessor() {
        return professor;
    }

    public void atualizarQtdAlunos(int total) {
        lblTotalAlunos.setText(String.valueOf(total));
    }

    public void receberDadosProfessor(Professor professor) {
        this.professor = professor;
        DadosCompartilhados.getInstancia().setAreaProfController(this);

        int total = DadosCompartilhados.getInstancia().getTotalAlunos(professor);
        lblTotalAlunos.setText(String.valueOf(total));
    }

    private void aplicarAnimacaoCard(VBox card) {
        ScaleTransition stEnter = new ScaleTransition(Duration.millis(150), card);
        stEnter.setToX(1.05);
        stEnter.setToY(1.05);

        ScaleTransition stExit = new ScaleTransition(Duration.millis(150), card);
        stExit.setToX(1);
        stExit.setToY(1);

        String estiloOriginal = card.getStyle(); // pega o estilo atual

        card.setOnMouseEntered(e -> {
            stEnter.playFromStart();
            card.setStyle(estiloOriginal + "; -fx-cursor: hand;"); // mantém o original e adiciona cursor
        });

        card.setOnMouseExited(e -> {
            stExit.playFromStart();
            card.setStyle(estiloOriginal); // volta ao estilo original
        });
    }

}