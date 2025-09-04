package br.com.javafx.educalink.areaprof;

import br.com.javafx.educalink.database.DadosCompartilhados;
import br.com.javafx.educalink.professores.Professor;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import br.com.javafx.educalink.alunos.Aluno;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

public class AluInscritoController {
    Professor professor;

    @FXML
    private Button sair;

    @FXML
    private VBox listaalunos;

    @FXML
    public void initialize() {
        DadosCompartilhados.getInstancia().setAluInscritoController(this);
    }

    public void carregarAlunos(List<Aluno> alunos) {
        listaalunos.getChildren().clear();

        for (Aluno aluno : alunos) {
            HBox card = criarCardAluno(aluno.getNome(), aluno.getEmail());
            listaalunos.getChildren().add(card);
        }
    }

    private HBox criarCardAluno(String nome, String email) {
        HBox card = new HBox(10);
        card.setStyle("-fx-background-color: #820AD1; -fx-background-radius: 8;");
        card.setPadding(new Insets(10));
        card.setPrefHeight(60);

        ImageView icone = new ImageView(new Image(getClass().getResourceAsStream(
                "/br/com/javafx/educalink/img/aluinscrito/Vector.png")));
        icone.setFitHeight(40);
        icone.setFitWidth(40);

        Label lblNome = new Label(nome);
        lblNome.setStyle("-fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 14px;");

        Label lblEmail = new Label(email);
        lblEmail.setStyle("-fx-text-fill: white; -fx-font-size: 12px;");

        VBox dados = new VBox(lblNome, lblEmail);
        dados.setSpacing(2);

        card.getChildren().addAll(icone, dados);

        // 🔥 margem para separar cada card
        VBox.setMargin(card, new Insets(5, 0, 5, 0));

        return card;
    }

    @FXML
    void clicouSair(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/br/com/javafx/educalink/areaprof/areaprof.fxml"));
            Parent root = loader.load();

            // Usa o professor já salvo no controller
            AreaProfController areaProfController = loader.getController();
            areaProfController.receberDadosProfessor(this.professor);

            Stage stage = (Stage) sair.getScene().getWindow();
            stage.setScene(new Scene(root, 800, 500));
            stage.setTitle("EducaLink - Área do Professor");
            stage.setResizable(false);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setProfessor(Professor professor) {
        this.professor = professor;
    }
}