package br.com.javafx.educalink.areaprof;

import br.com.javafx.educalink.database.DadosCompartilhados;
import br.com.javafx.educalink.professores.Professor;
import br.com.javafx.educalink.alunos.Aluno;
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
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

public class AluInscritoController {

    private Professor professor;

    @FXML
    private Button sair;

    @FXML
    private VBox listaalunos;

    @FXML
    public void initialize() {
        aplicarEfeitoBotao(sair, "#820AD1", "#6b00b3", 20, 18, 100);
        sair.setOnAction(this::clicouSair);

        DadosCompartilhados.getInstancia().setAluInscritoController(this);
    }

    public void carregarAlunos(List<Aluno> alunos) {
        listaalunos.getChildren().clear();

        if (alunos == null || alunos.isEmpty()) {
            Label vazio = new Label("Nenhum aluno inscrito.");
            vazio.setStyle("-fx-font-size: 16px; -fx-text-fill: #666;");
            listaalunos.getChildren().add(vazio);
            return;
        }

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

        VBox dados = new VBox(lblNome, lblEmail);
        dados.setSpacing(7);
        dados.setAlignment(Pos.CENTER);

        HBox.setHgrow(dados, Priority.ALWAYS);

        card.getChildren().addAll(icone, dados);
        VBox.setMargin(card, new Insets(10, 0, 10, 0));

        return card;
    }

    @FXML
    void clicouSair(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/br/com/javafx/educalink/areaprof/areaprof.fxml"));
            Parent root = loader.load();

            AreaProfController areaProfController = loader.getController();
            areaProfController.receberDadosProfessor(this.professor);

            Stage stage = (Stage) sair.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("EducaLink - Área do Professor");
            stage.setMaximized(true);
            stage.setResizable(true);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setProfessor(Professor professor) {
        this.professor = professor;
    }

    private void aplicarEfeitoBotao(Button botao, String corNormal, String corHover, int raioBorda, int tamanhoFonte, int larguraPref) {
        botao.setStyle(String.format(
                "-fx-background-color: %s; -fx-text-fill: white; -fx-background-radius: %d; -fx-font-size: %dpx; -fx-pref-width: %dpx; -fx-cursor: hand;",
                corNormal, raioBorda, tamanhoFonte, larguraPref
        ));

        botao.setOnMouseEntered(e -> botao.setStyle(String.format(
                "-fx-background-color: %s; -fx-text-fill: white; -fx-background-radius: %d; -fx-font-size: %dpx; -fx-pref-width: %dpx; -fx-cursor: hand;",
                corHover, raioBorda, tamanhoFonte, larguraPref
        )));

        botao.setOnMouseExited(e -> botao.setStyle(String.format(
                "-fx-background-color: %s; -fx-text-fill: white; -fx-background-radius: %d; -fx-font-size: %dpx; -fx-pref-width: %dpx; -fx-cursor: hand;",
                corNormal, raioBorda, tamanhoFonte, larguraPref
        )));
    }
}
