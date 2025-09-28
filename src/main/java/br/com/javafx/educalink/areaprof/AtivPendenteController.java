package br.com.javafx.educalink.areaprof;

import br.com.javafx.educalink.areaalu.Entrega;
import br.com.javafx.educalink.areaprof.Correcao;
import br.com.javafx.educalink.database.CorrecaoStorage;
import br.com.javafx.educalink.database.DadosCompartilhados;
import br.com.javafx.educalink.professores.Professor;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class AtivPendenteController {

    @FXML private VBox atividadesBox;
    @FXML private Button sair;

    private Professor professor;
    private List<Entrega> entregasFiltradas = new ArrayList<>();

    public void setProfessor(Professor professor) {
        this.professor = professor;
        aplicarEfeitoBotao(sair, "#820AD1", "#6b00b3", 20, 18, 100);
        iniciarTela();
    }

    public void atualizarTela() {
        carregarAtividadesDoSistema();
    }

    private void iniciarTela() {
        carregarAtividadesDoSistema();
    }

    private void carregarAtividadesDoSistema() {
        if (professor == null) return;

        entregasFiltradas.clear();
        List<Entrega> entregas = DadosCompartilhados.getEntregas();
        List<Correcao> correcoes = CorrecaoStorage.carregar();

        List<AtividadeDTO> lista = new ArrayList<>();

        for (Entrega e : entregas) {
            if (e.getAtividade() != null &&
                    e.getAtividade().getProfessorId().equals(professor.getId()) &&
                    "Atividade".equalsIgnoreCase(e.getAtividade().getTipo())) {

                boolean jaCorrigida = correcoes.stream()
                        .anyMatch(c -> c.getAtividadeId().equals(e.getAtividade().getId()) &&
                                Objects.equals(c.getAlunoMatricula(), e.getAlunoMatricula()));

                if (!jaCorrigida) {
                    entregasFiltradas.add(e);
                    lista.add(new AtividadeDTO(
                            e.getAtividade().getAssunto(),
                            e.getAlunoNome(),
                            e.getArquivoPath(),
                            e.getDataEntrega().atZone(java.time.ZoneId.systemDefault()).toInstant().toEpochMilli()
                    ));
                }
            }
        }

        carregarAtividades(lista);
    }

    private void carregarAtividades(List<AtividadeDTO> atividades) {
        atividadesBox.getChildren().clear();

        if (atividades == null || atividades.isEmpty()) {
            Label vazio = new Label("Nenhuma atividade entregue ainda.");
            vazio.setStyle("-fx-font-size: 16px; -fx-text-fill: #666;");
            atividadesBox.getChildren().add(vazio);
            return;
        }

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

        for (AtividadeDTO ativ : atividades) {
            HBox card = new HBox(20);

            String estiloNormal = "-fx-background-color: #FFFFFF; -fx-padding: 15; -fx-border-color: #DDD; "
                    + "-fx-background-radius: 10; -fx-border-radius: 10; -fx-cursor: hand;";
            String estiloHover = "-fx-background-color: #f0f0f0; -fx-padding: 15; -fx-border-color: #6b00b3; "
                    + "-fx-background-radius: 10; -fx-border-radius: 10; -fx-cursor: hand; "
                    + "-fx-effect: dropshadow(three-pass-box, rgba(107,0,179,0.4), 10, 0, 0, 0);";

            card.setStyle(estiloNormal);
            card.setPrefHeight(80);

            VBox iconeBox = new VBox();
            iconeBox.setStyle("-fx-alignment: center;");
            ImageView icone = new ImageView(new Image(getClass().getResourceAsStream(
                    "/br/com/javafx/educalink/img/areaalu/Pasteicon.png")));
            icone.setFitHeight(40);
            icone.setFitWidth(40);
            iconeBox.getChildren().add(icone);

            VBox infos = new VBox(5);
            Label titulo = new Label("Assunto: " + ativ.getAssunto());
            titulo.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
            Label aluno = new Label("Aluno: " + ativ.getAlunoNome());
            aluno.setStyle("-fx-font-size: 14px;");
            Label dataEntrega = new Label("Entregue em: " + sdf.format(ativ.getTimestamp()));
            dataEntrega.setStyle("-fx-font-size: 14px; -fx-text-fill: #333;");
            infos.getChildren().addAll(titulo, aluno, dataEntrega);

            card.getChildren().addAll(iconeBox, infos);

            card.setOnMouseEntered(e -> card.setStyle(estiloHover));
            card.setOnMouseExited(e -> card.setStyle(estiloNormal));

            card.setOnMouseClicked(e -> {
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/br/com/javafx/educalink/areaprof/corrigirAt.fxml"));
                    Parent root = loader.load();

                    CorrigirAtController controller = loader.getController();
                    controller.setDados(entregasFiltradas.get(atividades.indexOf(ativ)), professor);
                    controller.setAtivPendenteController(this);

                    Stage stage = (Stage) atividadesBox.getScene().getWindow();
                    stage.setScene(new Scene(root));
                    stage.setTitle("EducaLink - Corrigir Atividade");
                    stage.setMaximized(true);
                    stage.setResizable(true);

                } catch (IOException ex) {
                    ex.printStackTrace();
                    Alert a = new Alert(Alert.AlertType.ERROR, "Erro ao abrir tela de correção: " + ex.getMessage(), ButtonType.OK);
                    a.showAndWait();
                }
            });

            atividadesBox.getChildren().add(card);
        }
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
            stage.setTitle("EducaLink - Painel do Professor");
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

    public static class AtividadeDTO {
        private final String assunto;
        private final String alunoNome;
        private final String caminhoArquivo;
        private final long timestamp;

        public AtividadeDTO(String assunto, String alunoNome, String caminhoArquivo, long timestamp) {
            this.assunto = assunto;
            this.alunoNome = alunoNome;
            this.caminhoArquivo = caminhoArquivo;
            this.timestamp = timestamp;
        }

        public String getAssunto() { return assunto; }
        public String getAlunoNome() { return alunoNome; }
        public String getCaminhoArquivo() { return caminhoArquivo; }
        public long getTimestamp() { return timestamp; }
    }
}
