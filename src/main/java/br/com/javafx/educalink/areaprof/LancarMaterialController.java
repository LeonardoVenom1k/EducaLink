package br.com.javafx.educalink.areaprof;

import br.com.javafx.educalink.alunos.Aluno;
import br.com.javafx.educalink.database.DadosCompartilhados;
import br.com.javafx.educalink.professores.Professor;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class LancarMaterialController {
    Professor professor;

    @FXML
    private ComboBox<String> tipoCombo;

    @FXML
    private TextField assuntoField;

    @FXML
    private TextArea anexosArea;

    @FXML
    private Button anexarBtn;

    @FXML
    private Button cadastrarBtn;

    @FXML
    private Button sair;

    @FXML
    public void initialize() {
        sair.setOnMouseEntered(e -> sair.setStyle(
                "-fx-background-color: #6b00b3; -fx-text-fill: white; -fx-background-radius: 20; -fx-font-size: 18px; -fx-pref-width: 100px; -fx-cursor: hand;"));
        sair.setOnMouseExited(e -> sair.setStyle(
                "-fx-background-color: #820AD1; -fx-text-fill: white; -fx-background-radius: 20; -fx-font-size: 18px; -fx-pref-width: 100px; -fx-cursor: hand;"));

        sair.setOnAction(this::clicouSair);
    }

    public void carregarAlunos(List<Aluno> alunos) {
    }

    public void setProfessor(Professor professor) {
        this.professor = professor;
    }

    // Método chamado ao clicar no botão "Anexar arquivos"
    @FXML
    private void clicouAnexar() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Selecionar arquivos de apoio");

        // Permitir qualquer tipo de arquivo (se quiser limitar, pode usar getExtensionFilters)
        List<File> arquivosSelecionados = fileChooser.showOpenMultipleDialog(getStage());

        if (arquivosSelecionados != null) {
            for (File file : arquivosSelecionados) {
                anexosArea.appendText(file.getAbsolutePath() + "\n");
            }
        }
    }

    // Método chamado ao clicar no botão "Cadastrar"
    @FXML
    private void clicouCadastrar() {
        String tipo = tipoCombo.getValue();
        String assunto = assuntoField.getText();
        String anexos = anexosArea.getText();

        if (tipo == null || assunto.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Preencha todos os campos!", ButtonType.OK);
            alert.showAndWait();
            return;
        }

        // Aqui você pode salvar no banco de dados ou processar como precisar
        System.out.println("Tipo: " + tipo);
        System.out.println("Assunto: " + assunto);
        System.out.println("Arquivos anexados: \n" + anexos);

        Alert alert = new Alert(Alert.AlertType.INFORMATION, "Cadastro realizado com sucesso!", ButtonType.OK);
        alert.showAndWait();

        // Limpar os campos
        assuntoField.clear();
        anexosArea.clear();
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
            stage.setScene(new Scene(root));
            stage.setTitle("EducaLink - Área do Professor");
            stage.setMaximized(true);
            stage.setResizable(true);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Utilitário para obter a Stage atual
    private Stage getStage() {
        return (Stage) sair.getScene().getWindow();
    }
}
