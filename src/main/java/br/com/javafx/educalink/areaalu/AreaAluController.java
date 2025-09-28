package br.com.javafx.educalink.areaalu;

import br.com.javafx.educalink.alunos.Aluno;
import br.com.javafx.educalink.areaprof.Material;
import br.com.javafx.educalink.database.DadosCompartilhados;
import br.com.javafx.educalink.professores.Professor;
import br.com.javafx.educalink.database.CorrecaoStorage;
import br.com.javafx.educalink.areaprof.Correcao;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.input.MouseEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.geometry.Rectangle2D;
import javafx.stage.Screen;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class AreaAluController {

    @FXML
    private VBox correcoesBox;

    @FXML
    private Button sair;

    @FXML
    private Label materias;

    @FXML
    private Label atividades;

    @FXML
    private Label areadoAluno;

    private Aluno aluno;
    private List<Professor> professores;

    @FXML
    private VBox atividadesBox;

    @FXML
    public void initialize() {
        String estiloLabelPadrao = "-fx-text-fill: #000000; -fx-underline: false; -fx-cursor: hand;";
        String estiloLabelHover = "-fx-text-fill: #6b00b3; -fx-underline: true; -fx-cursor: hand;";

        setupHover(materias, estiloLabelHover, estiloLabelPadrao);
        setupHover(atividades, estiloLabelHover, estiloLabelPadrao);
        setupHover(areadoAluno, estiloLabelHover, estiloLabelPadrao);

        sair.setOnMouseEntered(e -> sair.setStyle(
                "-fx-background-color: #6b00b3; -fx-text-fill: white; -fx-background-radius: 20; -fx-font-size: 18px; -fx-pref-width: 100px; -fx-cursor: hand;"));
        sair.setOnMouseExited(e -> sair.setStyle(
                "-fx-background-color: #820AD1; -fx-text-fill: white; -fx-background-radius: 20; -fx-font-size: 18px; -fx-pref-width: 100px; -fx-cursor: hand;"));
    }

    private void setupHover(Label label, String estiloHover, String estiloPadrao) {
        label.setStyle(estiloPadrao);
        label.setOnMouseEntered(e -> label.setStyle(estiloHover));
        label.setOnMouseExited(e -> label.setStyle(estiloPadrao));
    }

    public void setAluno(Aluno aluno) {
        this.aluno = aluno;
        carregarAtividades();
        carregarCorrecoes();
    }

    public void receberDadosAluno(Aluno aluno) {
        this.aluno = aluno;
        System.out.println("Recebido aluno: " + aluno.getNome());
        carregarAtividades();
        carregarCorrecoes();
    }

    public void receberDadosProfessor(List<Professor> professores) {
        this.professores = professores;
        for (Professor p : professores) {
            System.out.println("Recebido professor: " + p.getNome());
        }
    }

    @FXML
    private void clicouSair(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmação");
        alert.setHeaderText("Deseja realmente sair?");
        alert.setContentText("Você será redirecionado para a tela de login.");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                Parent root = FXMLLoader.load(getClass().getResource("/br/com/javafx/educalink/login/login.fxml"));
                Stage stage = (Stage) sair.getScene().getWindow();
                trocaCena(stage, root, "EducaLink - Login");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void carregarAtividades() {
        atividadesBox.getChildren().clear();

        if (aluno == null) {
            mostrarAlerta("Nenhum aluno logado.");
            return;
        }

        List<Material> materiais = DadosCompartilhados.getInstancia().getMateriais();
        boolean encontrou = false;

        for (Material m : materiais) {
            // 🔥 Busca o professor dono da matéria
            Professor dono = DadosCompartilhados.getInstancia().getProfessorPorMateria(m.getMateria());

            // Mostra só se o aluno estiver inscrito no professor dono
            if (dono != null && DadosCompartilhados.getInstancia().alunoEstaInscrito(dono, aluno)) {

                // 🔥 Verifica se o aluno já entregou esta atividade
                boolean jaEntregou = DadosCompartilhados.getEntregas().stream()
                        .anyMatch(e -> e.getAlunoMatricula().equals(aluno.getMatricula())
                                && e.getAtividade().getAssunto().equals(m.getAssunto())
                                && "Atividade".equalsIgnoreCase(e.getAtividade().getTipo()));


                if (!jaEntregou) {
                    encontrou = true;
                    HBox card = criarCardMaterial(m);
                    atividadesBox.getChildren().add(card);
                }
            }
        }

        if (!encontrou) {
            Label vazio = new Label("Nenhuma atividade disponível para suas inscrições.");
            vazio.setStyle("-fx-font-size: 16px; -fx-text-fill: #555;");
            atividadesBox.getChildren().add(vazio);
        }
    }

    // 🔥 Método auxiliar para criar o card do material
    private HBox criarCardMaterial(Material m) {
        HBox card = new HBox(10);
        card.setStyle("-fx-background-color: #FFFFFF; -fx-padding: 10; -fx-border-color: #DDD; "
                + "-fx-background-radius: 10; -fx-border-radius: 10; -fx-cursor: hand;");

        // Ícone
        ImageView icon = new ImageView(new Image(getClass().getResourceAsStream(
                "/br/com/javafx/educalink/img/areaalu/Pasteicon.png")));
        icon.setFitHeight(40);
        icon.setFitWidth(40);

        // Infos
        VBox info = new VBox(5);
        Label materiaLbl = new Label("Matéria: " + m.getMateria());
        materiaLbl.setStyle("-fx-font-weight: bold; -fx-font-size: 16px;");

        Label tipoLbl = new Label("Tipo: " + m.getTipo());
        Label assuntoLbl = new Label("Assunto: " + m.getAssunto());
        info.getChildren().addAll(materiaLbl, tipoLbl, assuntoLbl);

        // Prazo se atividade
        if ("Atividade".equalsIgnoreCase(m.getTipo()) && m.getPrazo() != null) {
            Label prazoLbl = new Label("Prazo: " + m.getPrazo().toString());
            info.getChildren().add(prazoLbl);
        }

        card.getChildren().addAll(icon, info);

        // ─── EFEITO DE HOVER ───────────────────────────────────────
        String estiloNormal = "-fx-background-color: #FFFFFF; -fx-padding: 10; -fx-border-color: #DDD; "
                + "-fx-background-radius: 10; -fx-border-radius: 10; -fx-cursor: hand;";
        String estiloHover = "-fx-background-color: #f0f0f0; -fx-padding: 10; -fx-border-color: #6b00b3; "
                + "-fx-background-radius: 10; -fx-border-radius: 10; -fx-cursor: hand; "
                + "-fx-effect: dropshadow(three-pass-box, rgba(107,0,179,0.4), 10, 0, 0, 0);";

        card.setStyle(estiloNormal);

        card.setOnMouseEntered(e -> card.setStyle(estiloHover));
        card.setOnMouseExited(e -> card.setStyle(estiloNormal));
        // ───────────────────────────────────────────────────────────

        // Clique abre tela de entrega
        card.setOnMouseClicked(event -> {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/br/com/javafx/educalink/areaalu/materialentrega.fxml"));
                Parent root = loader.load();

                MaterialEntregaController controller = loader.getController();
                controller.setMaterial(m);
                controller.setAluno(this.aluno);
                controller.receberDadosAluno(this.aluno);
                controller.receberDadosProfessor(this.professores);

                Stage stage = (Stage) sair.getScene().getWindow();
                trocaCena(stage, root, "Material/Atividade - " + m.getMateria());

            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        return card;
    }

    public void carregarCorrecoes() {
        correcoesBox.getChildren().clear();

        if (aluno == null) {
            mostrarAlerta("Nenhum aluno logado.");
            return;
        }

        boolean encontrou = false;

        List<Correcao> correcoes = CorrecaoStorage.carregar();

        for (Correcao correcao : correcoes) {
            if (correcao.getAlunoMatricula() != null &&
                    correcao.getAlunoMatricula().equals(aluno.getMatricula())) {

                encontrou = true;
                HBox card = criarCardCorrecao(correcao);
                correcoesBox.getChildren().add(card);
            }
        }

        if (!encontrou) {
            Label vazio = new Label("Nenhuma correção disponível ainda.");
            vazio.setStyle("-fx-font-size: 16px; -fx-text-fill: #555;");
            correcoesBox.getChildren().add(vazio);
        }
    }



    private HBox criarCardCorrecao(Correcao correcao) {
        HBox card = new HBox(10);
        card.setStyle("-fx-background-color: #FFFFFF; -fx-padding: 10; -fx-border-color: #DDD; "
                + "-fx-background-radius: 10; -fx-border-radius: 10; -fx-cursor: hand;");

        ImageView icon = new ImageView(new Image(getClass().getResourceAsStream(
                "/br/com/javafx/educalink/img/areaalu/Pasteicon.png")));
        icon.setFitHeight(40);
        icon.setFitWidth(40);

        VBox info = new VBox(5);

        // Buscar Material correspondente
        String materia = "Indisponível";
        String assunto = "Indisponível";
        List<Material> materiais = DadosCompartilhados.getInstancia().getMateriais();
        for (Material m : materiais) {
            if (m.getId() != null && m.getId().equals(correcao.getAtividadeId())) {
                materia = m.getMateria();
                assunto = m.getAssunto();
                break;
            }
        }

        Label tituloLbl = new Label("Correção");
        tituloLbl.setStyle("-fx-font-weight: bold; -fx-font-size: 16px;");

        Label materiaLbl = new Label("Matéria: " + materia);
        Label assuntoLbl = new Label("Assunto: " + assunto);

        info.getChildren().addAll(tituloLbl, materiaLbl, assuntoLbl);
        card.getChildren().addAll(icon, info);

        // ─── EFEITO DE HOVER ───────────────────────────────────────
        String estiloNormal = "-fx-background-color: #FFFFFF; -fx-padding: 10; -fx-border-color: #DDD; "
                + "-fx-background-radius: 10; -fx-border-radius: 10; -fx-cursor: hand;";
        String estiloHover = "-fx-background-color: #f0f0f0; -fx-padding: 10; -fx-border-color: #6b00b3; "
                + "-fx-background-radius: 10; -fx-border-radius: 10; -fx-cursor: hand; "
                + "-fx-effect: dropshadow(three-pass-box, rgba(107,0,179,0.4), 10, 0, 0, 0);";

        card.setStyle(estiloNormal);
        card.setOnMouseEntered(e -> card.setStyle(estiloHover));
        card.setOnMouseExited(e -> card.setStyle(estiloNormal));
        // ───────────────────────────────────────────────────────────

        card.setOnMouseClicked(event -> {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/br/com/javafx/educalink/areaalu/correcao.fxml"));
                Parent root = loader.load();

                CorrecaoController controller = loader.getController();
                controller.setCorrecao(correcao.getMateria(), correcao.getAssunto(), correcao.getCaminhoArquivo(), correcao.getComentario());
                controller.receberDadosAluno(this.aluno);
                controller.receberDadosProfessor(this.professores);

                Stage stage = (Stage) sair.getScene().getWindow();
                trocaCena(stage, root, "Correção");

            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        return card;
    }


    @FXML
    private void clicouMaterias(MouseEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/br/com/javafx/educalink/areaalu/inscricao.fxml"));
            Parent root = loader.load();

            InscricaoController controller = loader.getController();
            controller.receberDadosAluno(this.aluno);
            List<Professor> professores = new ArrayList<>(DadosCompartilhados.getInstancia().getProfessores().values());
            controller.receberDadosProfessor(professores);

            Stage stage = (Stage) materias.getScene().getWindow();
            trocaCena(stage, root, "Inscrição em Matérias");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void clicouAtividades(MouseEvent event) {
        mostrarAlerta("Você já está na seção Atividades.");
    }

    @FXML
    private void clicouAreaAluno(MouseEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/br/com/javafx/educalink/areaalu/perfil.fxml"));
            Parent root = loader.load();

            PerfilController perfilController = loader.getController();
            perfilController.receberDadosAluno(this.aluno);
            perfilController.receberDadosProfessor(this.professores);

            Stage stage = (Stage) sair.getScene().getWindow();
            trocaCena(stage, root, "Perfil do Aluno");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void mostrarAlerta(String mensagem) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Informação");
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }

    private void trocaCena(Stage stage, Parent root, String titulo) {
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle(titulo);

        // 🔥 sempre maximiza
        stage.setMaximized(true);
        stage.setResizable(true);

        // 🔄 se um dia não quiser maximizar, centraliza na tela
        if (!stage.isMaximized()) {
            Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
            stage.setX((screenBounds.getWidth() - stage.getWidth()) / 2);
            stage.setY((screenBounds.getHeight() - stage.getHeight()) / 2);
        }
    }
}