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
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

public class LancarMaterialController {

    private Professor professor;

    @FXML private HBox prazoBox;
    @FXML private DatePicker prazoData;
    @FXML private Spinner<Integer> horaSpinner;
    @FXML private Spinner<Integer> minutoSpinner;

    @FXML private ComboBox<String> tipoCombo;
    @FXML private ComboBox<String> materiaCombo;
    @FXML private TextField assuntoField;
    @FXML private TextArea anexosArea;

    @FXML private Button anexarBtn;
    @FXML private Button cadastrarBtn;
    @FXML private Button sair;

    @FXML
    public void initialize() {
        // Configura efeitos de hover nos botões
        aplicarEfeitoBotao(sair, "#820AD1", "#6b00b3", 20, 18, 100);
        aplicarEfeitoBotao(anexarBtn, "#820AD1", "#6b00b3", 20, 18, 200);
        aplicarEfeitoBotao(cadastrarBtn, "#820AD1", "#6b00b3", 20, 18, 100);

        // Configuração dos Spinners de hora/minuto
        horaSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 23, 12));
        minutoSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 59, 0, 5));

        // Listener para exibir/esconder o prazo
        tipoCombo.valueProperty().addListener((obs, oldVal, newVal) -> {
            boolean mostrarPrazo = "Atividade".equals(newVal);
            prazoBox.setVisible(mostrarPrazo);
            prazoBox.setManaged(mostrarPrazo);
        });
    }

    public void setProfessor(Professor professor) {
        this.professor = professor;
        if (!professor.getMaterias().isEmpty()) {
            materiaCombo.getItems().setAll(professor.getMaterias());
            materiaCombo.setValue(professor.getMaterias().get(0));
        } else {
            String materiaDefault = "Sem Matéria";
            professor.adicionarMateria(materiaDefault);
            materiaCombo.getItems().add(materiaDefault);
            materiaCombo.setValue(materiaDefault);
        }
    }

    @FXML
    private void clicouAnexar() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Selecionar arquivos de apoio");

        List<File> arquivosSelecionados = fileChooser.showOpenMultipleDialog(getStage());
        if (arquivosSelecionados != null) {
            for (File file : arquivosSelecionados) {
                try {
                    File pastaMateriais = new File("materiais");
                    if (!pastaMateriais.exists()) pastaMateriais.mkdirs();

                    File destino = new File(pastaMateriais, file.getName());
                    java.nio.file.Files.copy(file.toPath(), destino.toPath(),
                            java.nio.file.StandardCopyOption.REPLACE_EXISTING);

                    anexosArea.appendText(destino.getAbsolutePath() + "\n");

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @FXML
    private void clicouCadastrar() {
        String tipo = tipoCombo.getValue();
        String assunto = assuntoField.getText();
        String nomeMateria = materiaCombo.getValue();

        if (tipo == null || assunto.isEmpty() || nomeMateria == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Preencha todos os campos!", ButtonType.OK);
            alert.showAndWait();
            return;
        }

        LocalDateTime prazo = null;
        if ("Atividade".equals(tipo) && prazoData.getValue() != null) {
            prazo = LocalDateTime.of(
                    prazoData.getValue(),
                    java.time.LocalTime.of(horaSpinner.getValue(), minutoSpinner.getValue())
            );
        }

        Material material = new Material(tipo, assunto, nomeMateria, prazo, professor.getId());

        String[] anexos = anexosArea.getText().split("\n");
        if (anexos.length > 0 && !anexos[0].isBlank()) {
            material.setCaminhoArquivo(anexos[0]);
        }

        DadosCompartilhados.getInstancia().adicionarMaterial(material);

        Alert alert = new Alert(Alert.AlertType.INFORMATION, "Enviado com sucesso!", ButtonType.OK);
        alert.showAndWait();

        assuntoField.clear();
        anexosArea.clear();
    }

    public void carregarAlunos(List<Aluno> alunos) {
        System.out.println("Lista de alunos recebida: " + alunos.size());
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

    private Stage getStage() {
        return (Stage) sair.getScene().getWindow();
    }

    /**
     * Método para adicionar efeito hover a um botão.
     */
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