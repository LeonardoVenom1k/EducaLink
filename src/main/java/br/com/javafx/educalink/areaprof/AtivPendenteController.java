package br.com.javafx.educalink.areaprof;

import br.com.javafx.educalink.alunos.Aluno;
import br.com.javafx.educalink.areaalu.Entrega;
import br.com.javafx.educalink.database.DadosCompartilhados;
import br.com.javafx.educalink.professores.Professor;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class AtivPendenteController {

    @FXML
    private VBox atividadesBox;

    @FXML
    private Button sair;

    private Professor professor;

    public void setProfessor(Professor professor) {
        this.professor = professor;
        iniciarTela();
    }

    public void carregarAlunos(List<Aluno> alunos) {}

    private List<Entrega> entregasFiltradas = new ArrayList<>();

    private void iniciarTela() {
        carregarAtividadesDoSistema();
    }

    private void carregarAtividadesDoSistema() {
        if (professor == null) return;

        entregasFiltradas.clear();

        List<Entrega> entregas = DadosCompartilhados.getEntregas();

        List<AtividadeDTO> lista = new ArrayList<>();

        for (Entrega e : entregas) {
            if (e.getAtividade() != null &&
                    e.getAtividade().getProfessorId().equals(professor.getId()) &&
                    "Atividade".equalsIgnoreCase(e.getAtividade().getTipo())) {

                entregasFiltradas.add(e); // Guardando referência

                lista.add(new AtividadeDTO(
                        e.getAtividade().getAssunto(),
                        e.getAlunoNome(),
                        e.getArquivoPath(),
                        e.getDataEntrega().atZone(java.time.ZoneId.systemDefault()).toInstant().toEpochMilli()
                ));
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
            card.setStyle("-fx-background-color: white; -fx-padding: 15; -fx-background-radius: 10; -fx-border-color: #ddd; -fx-border-radius: 10;");
            card.setPrefHeight(80);

            VBox infos = new VBox(5);
            Label titulo = new Label("Assunto: " + ativ.getAssunto());
            titulo.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
            Label aluno = new Label("Aluno: " + ativ.getAlunoNome());
            aluno.setStyle("-fx-font-size: 14px;");

            Label dataEntrega = new Label("Entregue em: " + sdf.format(ativ.getTimestamp()));
            dataEntrega.setStyle("-fx-font-size: 14px; -fx-text-fill: #333;");

            // Botão Corrigir
            Button corrigir = new Button("Corrigir");
            corrigir.setOnAction(e -> {
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/br/com/javafx/educalink/areaprof/corrigirAt.fxml"));
                    Parent root = loader.load();

                    CorrigirAtController controller = loader.getController();
                    controller.setDados(entregasFiltradas.get(atividades.indexOf(ativ)), professor);

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

            infos.getChildren().addAll(titulo, aluno, dataEntrega, corrigir);
            card.getChildren().add(infos);
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
