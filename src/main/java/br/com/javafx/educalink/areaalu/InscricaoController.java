package br.com.javafx.educalink.areaalu;

import br.com.javafx.educalink.alunos.Aluno;
import br.com.javafx.educalink.areaprof.AreaProfController;
import br.com.javafx.educalink.database.DadosCompartilhados;
import br.com.javafx.educalink.professores.Professor;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class InscricaoController {

    @FXML
    private VBox listamaterias;

    @FXML
    private Button sair;

    @FXML
    private Label materias, atividades, areadoAluno;

    private Aluno aluno;
    private List<Professor> professores;
    private Map<Professor, Boolean> statusInscricao = new HashMap<>();

    public void receberDadosAluno(Aluno aluno) {
        this.aluno = aluno;
    }

    public void receberDadosProfessor(List<Professor> professores) {
        this.professores = professores;
        for (Professor p : professores) {
            adicionarMateria(p.getCurso(), p.getNome(), p);
        }
    }

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

    private void adicionarMateria(String nomeMateria, String nomeProfessor, Professor professor) {
        // garante espaçamento entre cards
        listamaterias.setSpacing(15);

        // container principal (o card)
        VBox container = new VBox(10);
        container.setStyle("-fx-background-color: #8000c8; -fx-background-radius: 8; -fx-padding: 15;");

        // labels
        Label materiaLabel = new Label("MATÉRIA: " + nomeMateria.toUpperCase());
        materiaLabel.setStyle("-fx-text-fill: white; -fx-font-size: 18px; -fx-font-weight: bold;");

        Label professorLabel = new Label("PROFESSOR: " + nomeProfessor.toUpperCase());
        professorLabel.setStyle("-fx-text-fill: white; -fx-font-size: 18px; -fx-font-weight: bold;");

        VBox textos = new VBox(5, materiaLabel, professorLabel);

        // botão imagem
        ImageView imagemBotao = new ImageView(new Image(getClass().getResourceAsStream(
                "/br/com/javafx/educalink/img/mtdp/inscrever-se.png")));
        imagemBotao.setFitWidth(165);
        imagemBotao.setFitHeight(45);
        imagemBotao.setPreserveRatio(true);

        Button botaoInscricao = new Button();
        botaoInscricao.setStyle("-fx-background-color: transparent; -fx-cursor: hand;");

        botaoInscricao.setOnMouseEntered(e -> imagemBotao.setStyle("-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.4), 8, 0.0, 0, 2);"));
        botaoInscricao.setOnMouseExited(e -> imagemBotao.setStyle("-fx-effect: none;"));

        boolean jaInscrito = DadosCompartilhados.getInstancia().alunoEstaInscrito(professor, aluno);

        if (jaInscrito) {
            imagemBotao.setImage(new Image(getClass().getResourceAsStream("/br/com/javafx/educalink/img/mtdp/inscrito.png")));
        } else {
            imagemBotao.setImage(new Image(getClass().getResourceAsStream("/br/com/javafx/educalink/img/mtdp/inscrever-se.png")));
        }

        botaoInscricao.setGraphic(imagemBotao);

        botaoInscricao.setOnAction(event -> {
            boolean estaInscrito = DadosCompartilhados.getInstancia().alunoEstaInscrito(professor, aluno);

            if (!estaInscrito) {
                DadosCompartilhados.getInstancia().inscreverAluno(professor, aluno);
                imagemBotao.setImage(new Image(getClass().getResourceAsStream("/br/com/javafx/educalink/img/mtdp/inscrito.png")));
            } else {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Desinscrição");
                alert.setHeaderText("Deseja se desinscrever?");
                alert.setContentText("Você perderá acesso à disciplina.");

                ButtonType sim = new ButtonType("Sim");
                ButtonType nao = new ButtonType("Não", ButtonType.CANCEL.getButtonData());
                alert.getButtonTypes().setAll(sim, nao);

                Optional<ButtonType> result = alert.showAndWait();
                DadosCompartilhados.getInstancia().desinscreverAluno(professor, aluno);
                imagemBotao.setImage(new Image(getClass().getResourceAsStream("/br/com/javafx/educalink/img/mtdp/inscrever-se.png")));

                AreaProfController controller = DadosCompartilhados.getInstancia().getAreaProfController();
                if (controller != null) {
                    int totalAtual = DadosCompartilhados.getInstancia().getTotalAlunos(professor);
                    controller.atualizarQtdAlunos(totalAtual);

                }
            }
        });

        // linha com textos à esquerda e botão à direita, centralizado verticalmente
        HBox linha = new HBox();
        linha.setSpacing(20);
        linha.setAlignment(Pos.CENTER_LEFT);
        HBox.setHgrow(textos, Priority.ALWAYS); // empurra o botão para a direita
        linha.getChildren().addAll(textos, botaoInscricao);

        container.getChildren().add(linha);
        listamaterias.getChildren().add(container);
    }




    @FXML
    private void clicouSair() {
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
                stage.setMaximized(true);
                stage.setResizable(true);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    private void clicouMaterias(MouseEvent event) { mostrarAlerta("Você já está em matérias.");}

    private void mostrarAlerta(String msg) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Informação");
            alert.setHeaderText(null);
            alert.setContentText(msg);
            alert.showAndWait();
        }

    @FXML
    private void clicouAtividades(MouseEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/br/com/javafx/educalink/areaalu/areaalu.fxml"));
            Parent root = loader.load();

            AreaAluController controller = loader.getController();
            controller.receberDadosAluno(this.aluno);
            controller.receberDadosProfessor(this.professores);

            Stage stage = (Stage) atividades.getScene().getWindow();
            stage.setScene(new Scene(root, 800, 500));
            stage.setTitle("Área do Aluno");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void clicouAreaAluno(MouseEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/br/com/javafx/educalink/areaalu/perfil.fxml"));
            Parent root = loader.load();

            PerfilController controller = loader.getController();
            controller.receberDadosAluno(this.aluno);
            controller.receberDadosProfessor(this.professores);

            Stage stage = (Stage) areadoAluno.getScene().getWindow();
            stage.setScene(new Scene(root, 800, 500));
            stage.setTitle("Perfil do Aluno");
            stage.setMaximized(true);
            stage.setResizable(true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}