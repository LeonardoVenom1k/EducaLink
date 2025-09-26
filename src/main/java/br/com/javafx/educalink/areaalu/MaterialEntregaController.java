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
    private Material material;

    private File arquivoAnexado;
    private Aluno aluno;

    public void receberDadosAluno(Aluno aluno) {
        this.aluno = aluno;
    }

    public void receberDadosProfessor(List<Professor> professores) {
        this.professores = professores;
    }

    @FXML
    public void initialize() {
        materialLink.setVisible(false);

        anexarBtn.setOnAction(e -> anexarArquivo());
        enviarBtn.setOnAction(e -> enviarAtividade());
        materialLink.setOnAction(e -> abrirArquivoMaterial());

        // Aplicar efeitos aos botões
        aplicarEfeitoBotao(voltar, "#820AD1", "#6b00b3", 20, 18, 100);
        aplicarEfeitoBotao(anexarBtn, "#820AD1", "#6b00b3", 20, 18, 200);
        aplicarEfeitoBotao(enviarBtn, "#820AD1", "#6b00b3", 20, 18, 100);
    }

    public void setMaterial(Material material) {
        if (material == null) return;

        this.material = material;

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

        // ALERTA DE CONFIRMAÇÃO
        Alert confirmacao = new Alert(Alert.AlertType.CONFIRMATION);
        confirmacao.setTitle("Confirmação de envio");
        confirmacao.setHeaderText("Revise sua atividade antes de enviar!");
        confirmacao.setContentText("Ao enviar, você não terá mais acesso a essa atividade até que o professor devolva a correção.\nDeseja prosseguir?");
        ButtonType btnSim = new ButtonType("Sim", ButtonBar.ButtonData.YES);
        ButtonType btnNao = new ButtonType("Não", ButtonBar.ButtonData.NO);
        confirmacao.getButtonTypes().setAll(btnSim, btnNao);

        confirmacao.showAndWait().ifPresent(resposta -> {
            if (resposta == btnSim) {
                try {
                    File pastaBase = new File("entregas");
                    if (!pastaBase.exists()) pastaBase.mkdirs();

                    String sub = aluno != null ? aluno.getMatricula() : "anonimo";
                    File pastaAluno = new File(pastaBase, sub);
                    if (!pastaAluno.exists()) pastaAluno.mkdirs();

                    if (arquivoAnexado != null && arquivoAnexado.exists()) {
                        File destino = new File(pastaAluno, arquivoAnexado.getName());
                        Files.copy(arquivoAnexado.toPath(), destino.toPath(), StandardCopyOption.REPLACE_EXISTING);
                    }

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
                            this.material,
                            atividadeArea.getText(),
                            arquivoAnexado != null ? arquivoAnexado.getAbsolutePath() : null,
                            java.time.LocalDateTime.now()
                    );

                    DadosCompartilhados.registrarEntrega(entrega);

                    Alert sucesso = new Alert(Alert.AlertType.INFORMATION, "Atividade enviada com sucesso!", ButtonType.OK);
                    sucesso.showAndWait();

                    atividadeArea.clear();
                    arquivoAnexado = null;

                    // VOLTAR AUTOMATICAMENTE
                    voltarTelaAnterior();

                } catch (IOException ex) {
                    ex.printStackTrace();
                    Alert err = new Alert(Alert.AlertType.ERROR, "Erro ao enviar a atividade: " + ex.getMessage(), ButtonType.OK);
                    err.showAndWait();
                }
            }
        });
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

            AreaAluController controller = loader.getController();
            if (this.aluno != null) controller.receberDadosAluno(this.aluno);
            if (this.professores != null) controller.receberDadosProfessor(this.professores);

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

    private void voltarTelaAnterior() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/br/com/javafx/educalink/areaalu/areaalu.fxml"));
            Parent root = loader.load();

            AreaAluController controller = loader.getController();
            if (this.aluno != null) controller.receberDadosAluno(this.aluno);
            if (this.professores != null) controller.receberDadosProfessor(this.professores);

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

    public void setAluno(Aluno aluno) {
    }
}
