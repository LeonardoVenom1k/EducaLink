package br.com.javafx.educalink.areaalu;

import br.com.javafx.educalink.alunos.Aluno;
import br.com.javafx.educalink.areaprof.Material;
import br.com.javafx.educalink.database.DadosCompartilhados;
import br.com.javafx.educalink.professores.Professor;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.List;

public class MaterialEntregaController {

    @FXML private Label disciplinaLabel;
    @FXML private Label assuntoLabel;
    @FXML private Hyperlink materialLink;
    @FXML private VBox atividadePane;
    @FXML private TextArea atividadeArea;
    @FXML private Button anexarBtn;
    @FXML private Button enviarBtn;
    @FXML private Button voltar;

    private List<Professor> professores;
    private Material material; // referência do material/atividade exibido


    private File arquivoAnexado;        // arquivo selecionado pelo aluno para enviar
    private Aluno aluno;                // aluno logado
    private String materialId;          // opcional: id do material/atividade
    private String materialTipo;        // opcional: "Atividade" ou "Material"

    public void receberDadosAluno(Aluno aluno) {
        this.aluno = aluno;
    }

    public void receberDadosProfessor(List<Professor> professores) {
        this.professores = professores;
    }

    @FXML
    public void initialize() {
        // Inicialmente esconde o link de material (será mostrado em setMaterial caso exista arquivo)
        materialLink.setVisible(false);

        // configura botões caso FXML não tenha onAction
        anexarBtn.setOnAction(e -> anexarArquivo());
        enviarBtn.setOnAction(e -> enviarAtividade());
        materialLink.setOnAction(e -> abrirArquivoMaterial());

        // estilo do botão Voltar
        voltar.setOnMouseEntered(e -> voltar.setStyle(
                "-fx-background-color: #6b00b3; -fx-text-fill: white; -fx-background-radius: 20; -fx-font-size: 18px; -fx-pref-width: 100px; -fx-cursor: hand;"));
        voltar.setOnMouseExited(e -> voltar.setStyle(
                "-fx-background-color: #820AD1; -fx-text-fill: white; -fx-background-radius: 20; -fx-font-size: 18px; -fx-pref-width: 100px; -fx-cursor: hand;"));
    }

    /**
     * Popula a tela com os dados do material/atividade.
     */
    public void setMaterial(Material material) {
        if (material == null) return;

        this.material = material; // salva referência real para usar na entrega

        this.materialTipo = material.getTipo();
        this.materialId = material.getAssunto() + "_" + LocalDateTime.now();

        disciplinaLabel.setText(material.getMateria());
        assuntoLabel.setText(material.getAssunto());

        boolean isAtividade = "Atividade".equalsIgnoreCase(material.getTipo());
        atividadePane.setVisible(isAtividade);
        atividadePane.setManaged(isAtividade);

        if (material.getCaminhoArquivo() != null && !material.getCaminhoArquivo().isBlank()) {
            File f = new File(material.getCaminhoArquivo());
            materialLink.setText("Baixar: " + f.getName());
            materialLink.setUserData(f.getAbsolutePath());
            materialLink.setVisible(true);
        } else {
            materialLink.setVisible(false);
        }
    }


    public void setAluno(Aluno aluno) {
        this.aluno = aluno;
    }

    // ---------- Ações dos botões ----------

    private void anexarArquivo() {
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Anexar arquivo da atividade");
        File selecionado = chooser.showOpenDialog(voltar.getScene().getWindow());
        if (selecionado != null) {
            arquivoAnexado = selecionado;
            atividadeArea.appendText("\n[Arquivo anexado] " + selecionado.getName() + "\n");
        }
    }

    private void enviarAtividade() {
        if (!atividadePane.isVisible()) {
            Alert a = new Alert(Alert.AlertType.INFORMATION, "Esta tela é apenas para visualização do material.", ButtonType.OK);
            a.showAndWait();
            return;
        }

        try {
            File pastaBase = new File("entregas");
            if (!pastaBase.exists()) pastaBase.mkdirs();

            String sub = aluno != null ? aluno.getMatricula() : "anonimo";
            File pastaAluno = new File(pastaBase, sub);
            if (!pastaAluno.exists()) pastaAluno.mkdirs();

            // copia arquivo anexado
            if (arquivoAnexado != null && arquivoAnexado.exists()) {
                File destino = new File(pastaAluno, arquivoAnexado.getName());
                java.nio.file.Files.copy(arquivoAnexado.toPath(), destino.toPath(), java.nio.file.StandardCopyOption.REPLACE_EXISTING);
            }

            // salva texto da atividade
            String texto = atividadeArea.getText();
            if (texto != null && !texto.isBlank()) {
                String nomeEntrega = "entrega_" + System.currentTimeMillis() + ".txt";
                File arquivoTexto = new File(pastaAluno, nomeEntrega);
                try (FileWriter fw = new FileWriter(arquivoTexto, false)) {
                    fw.write("Aluno: " + (aluno != null ? aluno.getNome() + " (" + aluno.getMatricula() + ")" : "Desconhecido") + "\n");
                    fw.write("Material: " + assuntoLabel.getText() + "\n");
                    fw.write("Data envio: " + java.time.LocalDateTime.now() + "\n\n");
                    fw.write(texto);
                }
            }

            br.com.javafx.educalink.areaalu.Entrega entrega = new br.com.javafx.educalink.areaalu.Entrega(
                    aluno.getMatricula(),
                    aluno.getNome(),
                    this.material, // usa o material real
                    atividadeArea.getText(),
                    arquivoAnexado != null ? arquivoAnexado.getAbsolutePath() : null,
                    java.time.LocalDateTime.now()
            );

            DadosCompartilhados.registrarEntrega(entrega);

            Alert sucesso = new Alert(Alert.AlertType.INFORMATION, "Atividade enviada com sucesso!", ButtonType.OK);
            sucesso.showAndWait();

            // limpa campos
            atividadeArea.clear();
            arquivoAnexado = null;

        } catch (IOException ex) {
            ex.printStackTrace();
            Alert err = new Alert(Alert.AlertType.ERROR, "Erro ao enviar a atividade: " + ex.getMessage(), ButtonType.OK);
            err.showAndWait();
        }
    }

    private void abrirArquivoMaterial() {
        Object data = materialLink.getUserData();
        if (data instanceof String path) {
            try {
                java.awt.Desktop.getDesktop().open(new File(path));
            } catch (Exception e) {
                e.printStackTrace();
                Alert a = new Alert(Alert.AlertType.ERROR, "Não foi possível abrir o arquivo.", ButtonType.OK);
                a.showAndWait();
            }
        }
    }

    @FXML
    private void clicouVoltar() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/br/com/javafx/educalink/areaalu/areaalu.fxml"));
            Parent root = loader.load();

            // pega controller e passa os dados
            AreaAluController controller = loader.getController();
            if (this.aluno != null) {
                controller.receberDadosAluno(this.aluno);
            }
            if (this.professores != null) {
                controller.receberDadosProfessor(this.professores);
            }

            Stage stage = (Stage) voltar.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("EducaLink - Área do Aluno");
            stage.setMaximized(true);
            stage.setResizable(true);

        } catch (IOException e) {
            e.printStackTrace();
            Alert a = new Alert(Alert.AlertType.ERROR, "Erro ao voltar: " + e.getMessage(), ButtonType.OK);
            a.showAndWait();
        }
    }
}
