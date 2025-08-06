package br.com.javafx.educalink.areaalu;

import br.com.javafx.educalink.alunos.Aluno;
import br.com.javafx.educalink.database.DadosCompartilhados;
import br.com.javafx.educalink.professores.Professor;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;
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
    private boolean estaInscrito = false;

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
                "-fx-background-color: #6b00b3; -fx-text-fill: white; -fx-background-radius: 20; -fx-font-size: 14px; -fx-pref-width: 100px; -fx-cursor: hand;"));
        sair.setOnMouseExited(e -> sair.setStyle(
                "-fx-background-color: #820AD1; -fx-text-fill: white; -fx-background-radius: 20; -fx-font-size: 14px; -fx-pref-width: 100px; -fx-cursor: hand;"));
    }

    private void setupHover(Label label, String estiloHover, String estiloPadrao) {
        label.setStyle(estiloPadrao);
        label.setOnMouseEntered(e -> label.setStyle(estiloHover));
        label.setOnMouseExited(e -> label.setStyle(estiloPadrao));
    }

        private void adicionarMateria(String nomeMateria, String nomeProfessor, Professor professor){
        VBox container = new VBox(5);
        container.setStyle("-fx-background-color: #8000c8; -fx-background-radius: 12; -fx-padding: 20;");

        Label materiaLabel = new Label("MATÉRIA: " + nomeMateria);
        materiaLabel.setStyle("-fx-text-fill: white; -fx-font-size: 14px;");

        Label professorLabel = new Label("PROFESSOR: " + nomeProfessor);
        professorLabel.setStyle("-fx-text-fill: white; -fx-font-size: 14px;");

        ImageView imagemBotao = new ImageView(new Image(getClass().getResourceAsStream(
                "/br/com/javafx/educalink/img/mtdp/inscrever-se.png")));
        imagemBotao.setFitWidth(150);
        imagemBotao.setFitHeight(40);
        imagemBotao.setPreserveRatio(true);

        Button botaoInscricao = new Button();
        botaoInscricao.setStyle("-fx-background-color: transparent;");
        botaoInscricao.setGraphic(imagemBotao);

            botaoInscricao.setOnAction(event -> {
                if (!estaInscrito) {
                    estaInscrito = true;
                    imagemBotao.setImage(new Image(getClass().getResourceAsStream(
                            "/br/com/javafx/educalink/img/mtdp/inscrito.png")));

                    DadosCompartilhados.getInstancia().inscreverAluno(professor, aluno);

                } else {
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setTitle("Desinscrição");
                    alert.setHeaderText("Deseja se desinscrever?");
                    alert.setContentText("Você perderá acesso à disciplina.");

                    ButtonType sim = new ButtonType("Sim");
                    ButtonType nao = new ButtonType("Não", ButtonType.CANCEL.getButtonData());
                    alert.getButtonTypes().setAll(sim, nao);

                    Optional<ButtonType> result = alert.showAndWait();
                    if (result.isPresent() && result.get() == sim) {
                        estaInscrito = false;
                        imagemBotao.setImage(new Image(getClass().getResourceAsStream(
                                "/br/com/javafx/educalink/img/mtdp/inscrever-se.png")));
                        // aqui pode chamar o método para desinscrever se você tiver
                    }
                }
            });

            container.getChildren().addAll(materiaLabel, professorLabel, botaoInscricao);
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
                stage.setResizable(false);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    private void clicouMaterias(MouseEvent event) {
        // já está nessa tela
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
            stage.setResizable(false);
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
            stage.setResizable(false);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
