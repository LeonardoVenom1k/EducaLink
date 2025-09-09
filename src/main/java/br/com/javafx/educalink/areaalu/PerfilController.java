package br.com.javafx.educalink.areaalu;

import br.com.javafx.educalink.alunos.Aluno;
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
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public class PerfilController {

    @FXML private TextField campoNome;
    @FXML private TextField campoMatricula;
    @FXML private TextField campoCurso;
    @FXML private TextField campoEndereco;
    @FXML private TextField campoBairro;
    @FXML private TextField campoNumero;
    @FXML private TextField campoEmail;

    @FXML private ImageView fotoPerfil;
    @FXML private Circle circleClip;

    @FXML private Button sair;
    @FXML private Label materias;
    @FXML private Label atividades;
    @FXML private Label areadoAluno;

    private Aluno aluno;

    private List<Professor> professores;

    @FXML
    public void initialize() {
        if (fotoPerfil != null) {
            Circle clip = new Circle(fotoPerfil.getFitWidth()/2, fotoPerfil.getFitHeight()/2, fotoPerfil.getFitWidth()/2);
            fotoPerfil.setClip(clip);
        }

        String estiloPadrao = "-fx-text-fill: #000000; -fx-underline: false; -fx-cursor: hand;";
        String estiloHover = "-fx-text-fill: #6b00b3; -fx-underline: true; -fx-cursor: hand;";

        setupHover(materias, estiloHover, estiloPadrao);
        setupHover(atividades, estiloHover, estiloPadrao);
        setupHover(areadoAluno, estiloHover, estiloPadrao);

        sair.setOnMouseEntered(e -> sair.setStyle("-fx-background-color: #6b00b3; -fx-text-fill: white; -fx-background-radius: 20; -fx-font-size: 18px; -fx-pref-width: 100px; -fx-cursor: hand;"));
        sair.setOnMouseExited(e -> sair.setStyle("-fx-background-color: #820AD1; -fx-text-fill: white; -fx-background-radius: 20; -fx-font-size: 18px; -fx-pref-width: 100px;"));
    }

    public void receberDadosProfessor(List<Professor> professores) {
        this.professores = professores;
        for (Professor p : professores) {
            System.out.println("Recebido professor: " + p.getNome());
        }
    }

    public void receberDadosAluno(Aluno aluno) {
        this.aluno = aluno;

        if (aluno != null) {
            campoNome.setText(aluno.getNome());
            campoMatricula.setText(aluno.getMatricula());
            campoCurso.setText(aluno.getCurso() != null ? aluno.getCurso() : "");
            campoEndereco.setText(aluno.getEndereco() != null ? aluno.getEndereco() : "");
            campoBairro.setText(aluno.getBairro() != null ? aluno.getBairro() : "");
            campoNumero.setText(aluno.getNumero() != null ? aluno.getNumero() : "");
            campoEmail.setText(aluno.getEmail() != null ? aluno.getEmail() : "");

            String caminhoImagem = "/br/com/javafx/educalink/img/perfil/" + aluno.getMatricula() + ".png";

            try {
                Image imagem = new Image(getClass().getResourceAsStream(caminhoImagem));
                fotoPerfil.setImage(imagem);
            } catch (Exception e) {
                Image imagemDefault = new Image(getClass().getResourceAsStream("/br/com/javafx/educalink/img/perfil/default.png"));
                fotoPerfil.setImage(imagemDefault);
            }
        }
    }

    private void setupHover(Label label, String hover, String padrao) {
        label.setStyle(padrao);
        label.setOnMouseEntered(e -> label.setStyle(hover));
        label.setOnMouseExited(e -> label.setStyle(padrao));
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
                stage.setMaximized(true);
                stage.setResizable(false);
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

            InscricaoController inscricaoController = loader.getController();
            inscricaoController.receberDadosAluno(this.aluno);
            inscricaoController.receberDadosProfessor(this.professores);

            Stage stageAtual = (Stage) sair.getScene().getWindow();
            stageAtual.close();

            Stage novoStage = new Stage();
            novoStage.setScene(new Scene(root, 800, 500));
            novoStage.setTitle("Inscrição em Matérias");
            novoStage.setMaximized(true);
            novoStage.setResizable(false);
            novoStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void clicouAtividades(MouseEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/br/com/javafx/educalink/areaalu/areaalu.fxml"));
            Parent root = loader.load();

            AreaAluController areaAluController = loader.getController();
            areaAluController.receberDadosAluno(this.aluno);
            areaAluController.receberDadosProfessor(this.professores);

            Stage stageAtual = (Stage) sair.getScene().getWindow();
            stageAtual.close();

            Stage novoStage = new Stage();
            novoStage.setScene(new Scene(root, 800, 500));
            novoStage.setTitle("EducaLink - Área do Aluno");
            novoStage.setMaximized(true);
            novoStage.setResizable(false);
            novoStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void clicouAreaAluno(MouseEvent event) {
        mostrarAlerta("Você já está na área do aluno.");
    }

    private void mostrarAlerta(String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Informação");
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}