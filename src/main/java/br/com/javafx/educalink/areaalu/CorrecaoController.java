package br.com.javafx.educalink.areaalu;

import br.com.javafx.educalink.alunos.Aluno;
import br.com.javafx.educalink.professores.Professor;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class CorrecaoController {

    @FXML
    private Hyperlink linkArquivo;

    @FXML
    private Label lblMateria;

    @FXML
    private Label lblAssunto;

    @FXML
    private ImageView iconArquivo;

    @FXML
    private TextArea txtCorrecao;

    @FXML
    private Button voltar;

    private Aluno aluno;
    private List<Professor> professores;

    public void receberDadosAluno(Aluno aluno) {
        this.aluno = aluno;
    }

    public void receberDadosProfessor(List<Professor> professores) {
        this.professores = professores;
    }

    /**
     * Preenche os dados da correção.
     *
     * @param materia         Nome da matéria
     * @param assunto         Assunto da atividade
     * @param caminhoArquivo  Caminho do arquivo corrigido
     * @param comentario      Comentário feito pelo professor
     */

    public void setCorrecao(String materia, String assunto, String caminhoArquivo, String comentario) {
        lblMateria.setText((materia != null ? materia : "Indisponível"));
        lblAssunto.setText((assunto != null ? assunto : "Indisponível"));

        if (caminhoArquivo != null && !caminhoArquivo.isBlank()) {
            File file = new File(caminhoArquivo);
            if (file.exists()) {
                InputStream is = getClass().getResourceAsStream("/br/com/javafx/educalink/img/ativmate/filelogo.png");
                if (is != null) {
                    iconArquivo.setImage(new Image(is));
                } else {
                    System.out.println("Ícone não encontrado!");
                }

                // Link exibindo caminho do arquivo
                linkArquivo.setText(file.getAbsolutePath());
                linkArquivo.setOnAction(event -> {
                    try {
                        java.awt.Desktop.getDesktop().open(file);
                    } catch (IOException ex) {
                        ex.printStackTrace();
                        Alert a = new Alert(Alert.AlertType.ERROR, "Erro ao abrir o arquivo: " + ex.getMessage(), ButtonType.OK);
                        a.showAndWait();
                    }
                });
            } else {
                linkArquivo.setText("Arquivo não encontrado");
                linkArquivo.setDisable(true);
            }
        } else {
            linkArquivo.setText("Arquivo não informado");
            linkArquivo.setDisable(true);
        }

        txtCorrecao.setText(comentario != null ? comentario : "Sem comentário disponível.");
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
            stage.setTitle("EducaLink - Atividades do Aluno");
            stage.setMaximized(true);
            stage.setResizable(true);

        } catch (IOException e) {
            e.printStackTrace();
            Alert a = new Alert(Alert.AlertType.ERROR, "Erro ao voltar: " + e.getMessage(), ButtonType.OK);
            a.showAndWait();
        }
    }

    @FXML
    private void initialize() {
        aplicarEfeitoBotao(voltar, "#820AD1", "#6b00b3", 20, 18, 100);
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
