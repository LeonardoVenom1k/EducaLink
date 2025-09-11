package br.com.javafx.educalink.areaprof;

import br.com.javafx.educalink.database.DadosCompartilhados;
import br.com.javafx.educalink.professores.Professor;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
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
        sair.setOnMouseEntered(e -> sair.setStyle(
                "-fx-background-color: #6b00b3; -fx-text-fill: white; -fx-background-radius: 20; -fx-font-size: 18px; -fx-pref-width: 100px; -fx-cursor: hand;"));
        sair.setOnMouseExited(e -> sair.setStyle(
                "-fx-background-color: #820AD1; -fx-text-fill: white; -fx-background-radius: 20; -fx-font-size: 18px; -fx-pref-width: 100px; -fx-cursor: hand;"));

        sair.setOnAction(this::clicouSair);

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
        card.setPrefHeight(80);

        ImageView icone = new ImageView(new Image(getClass().getResourceAsStream(
                "/br/com/javafx/educalink/img/aluinscrito/Vector.png")));
        icone.setFitHeight(70);
        icone.setFitWidth(70);

        Label lblNome = new Label(nome);
        lblNome.setStyle("-fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 25px;");

        Label lblEmail = new Label(email);
        lblEmail.setStyle("-fx-text-fill: white; -fx-font-size: 20px;");

        // VBox para empilhar nome e email
        VBox dados = new VBox(lblNome, lblEmail);
        dados.setSpacing(7);
        dados.setAlignment(Pos.CENTER); // centraliza dentro do VBox

        // 🔥 Faz o VBox ocupar todo o espaço disponível no HBox
        HBox.setHgrow(dados, Priority.ALWAYS);

        card.getChildren().addAll(icone, dados);

        // 🔥 margem para separar cada card
        VBox.setMargin(card, new Insets(10, 0, 10, 0));

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