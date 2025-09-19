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
        // Configuração botão sair
        sair.setOnMouseEntered(e -> sair.setStyle(
                "-fx-background-color: #6b00b3; -fx-text-fill: white; -fx-background-radius: 20; -fx-font-size: 18px; -fx-pref-width: 100px; -fx-cursor: hand;"));
        sair.setOnMouseExited(e -> sair.setStyle(
                "-fx-background-color: #820AD1; -fx-text-fill: white; -fx-background-radius: 20; -fx-font-size: 18px; -fx-pref-width: 100px; -fx-cursor: hand;"));
        sair.setOnAction(this::clicouSair);

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
        // Popular ComboBox de matérias do professor
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

    // Método chamado ao clicar no botão "Anexar arquivos"
    @FXML
    private void clicouAnexar() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Selecionar arquivos de apoio");

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

        // Criar material vinculado à matéria selecionada
        Material material = new Material(tipo, assunto, nomeMateria, prazo, professor.getId());

        // Salvar no singleton
        DadosCompartilhados.getInstancia().adicionarMaterial(material);

        System.out.println("Material enviado:");
        System.out.println("Tipo: " + tipo);
        System.out.println("Assunto: " + assunto);
        System.out.println("Matéria: " + nomeMateria);
        System.out.println("Arquivos anexados: \n" + anexos);

        Alert alert = new Alert(Alert.AlertType.INFORMATION, "Cadastro realizado com sucesso!", ButtonType.OK);
        alert.showAndWait();

        // Limpar campos
        assuntoField.clear();
        anexosArea.clear();
    }

    public void carregarAlunos(List<Aluno> alunos) {
        // Aqui você pode popular uma lista de checkboxes, TableView ou outra UI se quiser.
        // Por enquanto, apenas registra no console
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
}
