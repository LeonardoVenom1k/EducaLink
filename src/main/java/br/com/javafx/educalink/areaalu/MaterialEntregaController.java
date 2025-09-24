package br.com.javafx.educalink.areaalu;

import br.com.javafx.educalink.alunos.Aluno;
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

    private File arquivoAnexado;        // arquivo selecionado pelo aluno para enviar
    private Aluno aluno;                // aluno logado
    private String materialId;          // opcional: id do material/atividade
    private String materialTipo;        // opcional: "Atividade" ou "Material"

    public void receberDadosAluno(Aluno aluno) {
        this.aluno = aluno;
    }

    public void receberDadosProfessor(List<Professor> professores) {
        this.professores = professores;
        for (Professor p : professores) {}
    }

    @FXML
    public void initialize() {
        String estiloLabelPadrao = "-fx-text-fill: #000000; -fx-underline: false; -fx-cursor: hand;";
        String estiloLabelHover = "-fx-text-fill: #6b00b3; -fx-underline: true; -fx-cursor: hand;";

        // Inicialmente esconde o link de material (será mostrado em setMaterial caso exista arquivo)
        materialLink.setVisible(false);

        // configura botões caso FXML não tenha onAction
        anexarBtn.setOnAction(e -> anexarArquivo());
        enviarBtn.setOnAction(e -> enviarAtividade());
        materialLink.setOnAction(e -> abrirArquivoMaterial());

        voltar.setOnMouseEntered(e -> voltar.setStyle(
                "-fx-background-color: #6b00b3; -fx-text-fill: white; -fx-background-radius: 20; -fx-font-size: 18px; -fx-pref-width: 100px; -fx-cursor: hand;"));
        voltar.setOnMouseExited(e -> voltar.setStyle(
                "-fx-background-color: #820AD1; -fx-text-fill: white; -fx-background-radius: 20; -fx-font-size: 18px; -fx-pref-width: 100px; -fx-cursor: hand;"));
    }

    /**
     * Chamar este método para popular a tela com os dados do material/atividade
     * (o Material aqui é genérico — adapte ao teu model)
     */
    public void setMaterial(br.com.javafx.educalink.areaprof.Material material) {
        if (material == null) return;

        this.materialTipo = material.getTipo();
        this.materialId = material.getAssunto() + "_" + LocalDateTime.now().toString(); // exemplo

        disciplinaLabel.setText(material.getMateria());
        assuntoLabel.setText(material.getAssunto());

        // mostra ou esconde painel de entrega quando for atividade
        boolean isAtividade = "Atividade".equalsIgnoreCase(material.getTipo());
        atividadePane.setVisible(isAtividade);
        atividadePane.setManaged(isAtividade);

        // se existir arquivo de apoio, exibe link para abrir
        if (material.getCaminhoArquivo() != null && !material.getCaminhoArquivo().isBlank()) {
            File f = new File(material.getCaminhoArquivo());
            materialLink.setText("Baixar: " + f.getName());
            materialLink.setUserData(f.getAbsolutePath());
            materialLink.setVisible(true);
        } else {
            materialLink.setVisible(false);
        }
    }

    // Permite que a tela receba o aluno logado antes de abrir
    public void setAluno(Aluno aluno) {
        this.aluno = aluno;
    }

    // ---------- Ações dos botões ----------

    // Anexa arquivo (FileChooser)
    private void anexarArquivo() {
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Anexar arquivo da atividade");
        File selecionado = chooser.showOpenDialog(voltar.getScene().getWindow());
        if (selecionado != null) {
            arquivoAnexado = selecionado;
            atividadeArea.appendText("\n[Arquivo anexado] " + selecionado.getName() + "\n");
        }
    }

    // Envia a atividade (exemplo: copia arquivo para pasta 'entregas' e grava texto)
    private void enviarAtividade() {
        if (!atividadePane.isVisible()) {
            // não há envio quando é apenas material
            Alert a = new Alert(Alert.AlertType.INFORMATION, "Esta tela é apenas para visualização do material.", ButtonType.OK);
            a.showAndWait();
            return;
        }

        try {
            // Pasta base de entregas
            File pastaBase = new File("entregas");
            if (!pastaBase.exists()) pastaBase.mkdirs();

            // criar subpasta por materia/atividade opcionalmente (aqui por aluno)
            String sub = aluno != null ? aluno.getMatricula() : "anonimo";
            File pastaAluno = new File(pastaBase, sub);
            if (!pastaAluno.exists()) pastaAluno.mkdirs();

            // se houver arquivo anexado, copia para a pasta
            if (arquivoAnexado != null && arquivoAnexado.exists()) {
                File destino = new File(pastaAluno, arquivoAnexado.getName());
                Files.copy(arquivoAnexado.toPath(), destino.toPath(), StandardCopyOption.REPLACE_EXISTING);
            }

            // salva também o texto da atividade como arquivo .txt (exemplo)
            String texto = atividadeArea.getText();
            if (texto != null && !texto.isBlank()) {
                String nomeEntrega = "entrega_" + System.currentTimeMillis() + ".txt";
                File arquivoTexto = new File(pastaAluno, nomeEntrega);
                try (FileWriter fw = new FileWriter(arquivoTexto, false)) {
                    fw.write("Aluno: " + (aluno != null ? aluno.getNome() + " (" + aluno.getMatricula() + ")" : "Desconhecido") + "\n");
                    fw.write("Material: " + (assuntoLabel.getText()) + "\n");
                    fw.write("Data envio: " + LocalDateTime.now().toString() + "\n\n");
                    fw.write(texto);
                }
            }

            Alert sucesso = new Alert(Alert.AlertType.INFORMATION, "Atividade enviada com sucesso!", ButtonType.OK);
            sucesso.showAndWait();

            // limpa campos ou marca como enviado
            atividadeArea.clear();
            arquivoAnexado = null;

        } catch (IOException ex) {
            ex.printStackTrace();
            Alert err = new Alert(Alert.AlertType.ERROR, "Erro ao enviar a atividade: " + ex.getMessage(), ButtonType.OK);
            err.showAndWait();
        }
    }

    // Abre o arquivo de material/link (se existir)
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

    // Botão Voltar: volta para a Área do Aluno (areaalu.fxml) e preserva o aluno
    @FXML
    private void clicouVoltar() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/br/com/javafx/educalink/areaalu/areaalu.fxml"));
            Parent root = loader.load();

            // pega o controller da área do aluno e passa o aluno atualmente logado
            br.com.javafx.educalink.areaalu.AreaAluController ctrl = loader.getController();
            if (this.aluno != null) {
                ctrl.receberDadosAluno(this.aluno);
                // se você tiver os professores também, chame ctrl.receberDadosProfessor(...)
            }

            AreaAluController controller = loader.getController();
            controller.receberDadosAluno(this.aluno);
            controller.receberDadosProfessor(this.professores);

            Stage stage = (Stage) voltar.getScene().getWindow();
            stage.setScene(new Scene(root, 800, 500));
            stage.setTitle("EducaLink - Área do Aluno");
            stage.setResizable(false);

        } catch (IOException e) {
            e.printStackTrace();
            Alert a = new Alert(Alert.AlertType.ERROR, "Erro ao voltar: " + e.getMessage(), ButtonType.OK);
            a.showAndWait();
        }
    }
}
