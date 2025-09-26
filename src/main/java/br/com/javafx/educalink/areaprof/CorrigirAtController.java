package br.com.javafx.educalink.areaprof;

import br.com.javafx.educalink.areaalu.Entrega;
import br.com.javafx.educalink.database.EntregaStorage;
import br.com.javafx.educalink.professores.Professor;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.util.List;

public class CorrigirAtController {

    @FXML private Button btnAnexar;
    @FXML private Button btnEnviar;
    @FXML private Button sair;

    @FXML private ListView<HBox> listaAluno;
    @FXML private VBox listaCorrigidos;
    @FXML private TextField txtAluno;
    @FXML private TextField txtAtividade;
    @FXML private TextArea txtComentario;

    private Professor professor;
    private File arquivoCorrecao;
    private List<Entrega> entregas;

    @FXML
    public void initialize() {
        aplicarEfeitoBotao(sair, "#820AD1", "#6b00b3", 20, 18, 100);
        aplicarEfeitoBotao(btnAnexar, "#820AD1", "#6b00b3", 20, 18, 200);
        aplicarEfeitoBotao(btnEnviar, "#820AD1", "#6b00b3", 20, 18, 100);

        carregueEntregas();
    }

    private void carregueEntregas() {
        entregas = EntregaStorage.carregar();

        if (entregas.isEmpty()) {
            Alert alerta = new Alert(Alert.AlertType.INFORMATION, "Não há entregas para corrigir.", ButtonType.OK);
            alerta.showAndWait();
            return;
        }

        setDados(entregas.get(0), professor);
    }

    public void setDados(Entrega entrega, Professor professor) {
        if (entrega == null) return;

        txtAluno.setText(entrega.getAlunoNome());
        txtAtividade.setText(entrega.getAtividade().getAssunto());
        this.professor = professor;

        listaAluno.getItems().clear();

        if (entrega.temArquivoAnexado()) {
            File arquivoEntrega = new File(entrega.getArquivoPath());
            if (arquivoEntrega.exists()) {
                Button abrirBtn = new Button("Abrir");
                aplicarEfeitoBotao(abrirBtn, "#2196F3", "#1976D2", 15, 14, 80);
                abrirBtn.setOnAction(ev -> abrirArquivo(arquivoEntrega));

                HBox linha = new HBox(10, new Label(arquivoEntrega.getName()), abrirBtn);
                listaAluno.getItems().add(linha);
            }
        }

        if (entrega.getRespostaTexto() != null && !entrega.getRespostaTexto().isBlank()) {
            listaAluno.getItems().add(new HBox(new Label("Resposta escrita: " + entrega.getRespostaTexto())));
        }
    }

    @FXML
    void clicouAnexar(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Selecionar Arquivo de Correção");
        this.arquivoCorrecao = fileChooser.showOpenDialog(btnAnexar.getScene().getWindow());

        if (arquivoCorrecao != null) {
            Label arquivoLabel = new Label("Corrigido: " + arquivoCorrecao.getName());
            listaCorrigidos.getChildren().add(arquivoLabel);
        }
    }

    @FXML
    void clicouEnviar(ActionEvent event) {
        String comentario = txtComentario.getText();

        if (arquivoCorrecao == null && (comentario == null || comentario.isBlank())) {
            Alert alerta = new Alert(Alert.AlertType.WARNING,
                    "Adicione um comentário ou anexe um arquivo antes de enviar.",
                    ButtonType.OK);
            alerta.showAndWait();
            return;
        }

        Alert alerta = new Alert(Alert.AlertType.INFORMATION, "Correção enviada com sucesso!", ButtonType.OK);
        alerta.showAndWait();

        voltarTelaAnterior();
    }

    @FXML
    void clicouSair(ActionEvent event) {
        voltarTelaAnterior();
    }

    private void voltarTelaAnterior() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/br/com/javafx/educalink/areaprof/ativpendente.fxml"));
            Parent root = loader.load();

            AtivPendenteController controller = loader.getController();
            controller.setProfessor(this.professor);

            Stage stage = (Stage) sair.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("EducaLink - Atividades Pendentes");
            stage.setMaximized(true);
            stage.setResizable(true);

        } catch (IOException e) {
            e.printStackTrace();
            Alert alerta = new Alert(Alert.AlertType.ERROR, "Erro ao voltar: " + e.getMessage(), ButtonType.OK);
            alerta.showAndWait();
        }
    }

    private void abrirArquivo(File arquivo) {
        try {
            Desktop.getDesktop().open(arquivo);
        } catch (Exception e) {
            e.printStackTrace();
            Alert alerta = new Alert(Alert.AlertType.ERROR, "Não foi possível abrir o arquivo.", ButtonType.OK);
            alerta.showAndWait();
        }
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
