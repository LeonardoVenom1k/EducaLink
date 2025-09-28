package br.com.javafx.educalink.login;

import br.com.javafx.educalink.areaalu.AreaAluController;
import br.com.javafx.educalink.alunos.Aluno;
import br.com.javafx.educalink.areaprof.Material;
import br.com.javafx.educalink.database.DadosCompartilhados;
import br.com.javafx.educalink.professores.Professor;
import br.com.javafx.educalink.areaprof.AreaProfController;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.event.ActionEvent;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.time.LocalDateTime;
import java.util.*;
import java.io.IOException;

public class LoginController {

    @FXML
    private TextField matricula;
    @FXML
    private PasswordField senha;
    @FXML
    private TextField senhaVisivel;
    @FXML
    private Button entrar;
    @FXML
    private Hyperlink esquecisenha;
    @FXML
    private ImageView iconSenha;

    private boolean senhaEstaVisivel = false;

    @FXML
    private void initialize() {
        senhaVisivel.setVisible(false);

        // sincroniza campos de senha
        senhaVisivel.textProperty().addListener((obs, oldText, newText) -> senha.setText(newText));
        senha.textProperty().addListener((obs, oldText, newText) -> senhaVisivel.setText(newText));

        iconSenha.setStyle("-fx-cursor: hand;");

        entrar.setOnMouseEntered(e -> entrar.setStyle("-fx-background-color: #6b00b3; -fx-text-fill: white; -fx-background-radius: 20; -fx-cursor: hand;"));
        entrar.setOnMouseExited(e -> entrar.setStyle("-fx-background-color: #820AD1; -fx-text-fill: white; -fx-background-radius: 20;"));

        esquecisenha.setOnMouseEntered(e -> esquecisenha.setStyle("-fx-text-fill: #a53de0; -fx-underline: true; -fx-cursor: hand;"));
        esquecisenha.setOnMouseExited(e -> esquecisenha.setStyle("-fx-text-fill: #820AD1;"));

        // 🔥 Inicializa dados
        inicializarDados();
    }

    private void inicializarDados() {
        DadosCompartilhados dados = DadosCompartilhados.getInstancia();
        Map<String, Professor> professores = criarMapaProfessores();
        Map<String, Aluno> alunos = criarMapaAlunos();

        senhaVisivel.prefWidthProperty().bind(senha.widthProperty());
        senhaVisivel.prefHeightProperty().bind(senha.heightProperty());
        senhaVisivel.fontProperty().bind(senha.fontProperty());
        senhaVisivel.styleProperty().bind(senha.styleProperty());


        // Adiciona materiais e professores no singleton
        for (Professor prof : professores.values()) {
            dados.cadastrarProfessor(prof);
        }

        // Adiciona alunos no singleton
        for (Aluno aluno : alunos.values()) {
            dados.cadastrarAluno(aluno);
        }
    }

    public Map<String, Professor> criarMapaProfessores() {
        Map<String, Professor> professores = new HashMap<>();

        Professor prof1 = new Professor("Carlos Vieira", "admin","Matemática");
        prof1.adicionarMateria("Álgebra Linear");
        prof1.adicionarMateria("Cálculo I");
        professores.put("admin", prof1);

        Professor prof2 = new Professor("Luiz Gonzaga", "admin2", "Português");
        prof2.adicionarMateria("Gramática");
        prof2.adicionarMateria("Redação");
        professores.put("admin2", prof2);

        Professor prof3 = new Professor("Mariana Souza", "admin3", "Biologia");
        prof3.adicionarMateria("Genética");
        prof3.adicionarMateria("Ecologia");
        professores.put("admin3", prof3);

        Professor prof4 = new Professor("Ricardo Lima", "admin4", "História");
        prof4.adicionarMateria("História Geral");
        prof4.adicionarMateria("História do Brasil");
        professores.put("admin4", prof4);

        Professor prof5 = new Professor("Fernanda Alves", "admin5", "Física");
        prof5.adicionarMateria("Mecânica");
        prof5.adicionarMateria("Eletromagnetismo");
        professores.put("admin5", prof5);

        // NOVO PROFESSOR
        Professor prof6 = new Professor("Carla Monteiro", "admin6", "Química");
        prof6.adicionarMateria("Química Orgânica");
        prof6.adicionarMateria("Química Inorgânica");
        professores.put("admin6", prof6);

        return professores;
    }


    private Map<String, Aluno> criarMapaAlunos() {
        Map<String, Aluno> alunos = new HashMap<>();

        Aluno aluno1 = new Aluno("Leonardo Aguiar", "2025");
        aluno1.setCurso("Sistemas de Informação");
        aluno1.setEndereco("Rua Fioravante Guersoni");
        aluno1.setBairro("Cruzeiro");
        aluno1.setNumero("301");
        aluno1.setEmail("leonardo@email.com");
        alunos.put("2025", aluno1);

        Aluno aluno2 = new Aluno("Lucas Rodrigues Xavier", "20251");
        aluno2.setCurso("Sistemas de Informação");
        aluno2.setEndereco("Rua das Andorinhas");
        aluno2.setBairro("Papagaio");
        aluno2.setNumero("679");
        aluno2.setEmail("lucas@email.com");
        alunos.put("20251", aluno2);

        Aluno aluno3 = new Aluno("Ana Beatriz", "20252");
        aluno3.setCurso("Ensino Médio - 2º Ano");
        aluno3.setEndereco("Rua das Flores");
        aluno3.setBairro("Centro");
        aluno3.setNumero("120");
        aluno3.setEmail("ana@email.com");
        alunos.put("20252", aluno3);

        Aluno aluno4 = new Aluno("Pedro Henrique", "20253");
        aluno4.setCurso("Ensino Médio - 3º Ano");
        aluno4.setEndereco("Av. Brasil");
        aluno4.setBairro("São José");
        aluno4.setNumero("45");
        aluno4.setEmail("pedro@email.com");
        alunos.put("20253", aluno4);

        Aluno aluno5 = new Aluno("Maria Eduarda", "20254");
        aluno5.setCurso("Ensino Médio - 1º Ano");
        aluno5.setEndereco("Rua das Palmeiras");
        aluno5.setBairro("Boa Vista");
        aluno5.setNumero("87");
        aluno5.setEmail("mariaeduarda@email.com");
        alunos.put("20254", aluno5);

        // NOVOS ALUNOS
        Aluno aluno6 = new Aluno("Gustavo Mendes", "20255");
        aluno6.setCurso("Sistemas de Informação");
        aluno6.setEndereco("Rua Nova Esperança");
        aluno6.setBairro("Jardim das Rosas");
        aluno6.setNumero("210");
        aluno6.setEmail("gustavo@email.com");
        alunos.put("20255", aluno6);

        Aluno aluno7 = new Aluno("Juliana Campos", "20256");
        aluno7.setCurso("Ensino Médio - 2º Ano");
        aluno7.setEndereco("Rua da Liberdade");
        aluno7.setBairro("Vila Nova");
        aluno7.setNumero("98");
        aluno7.setEmail("juliana@email.com");
        alunos.put("20256", aluno7);

        return alunos;
    }


    private Map<String, String> criarMapaSenhas() {
        Map<String, String> senhas = new HashMap<>();
        senhas.put("2025", "faitec25");
        senhas.put("20251", "faitec25");
        senhas.put("20252", "faitec25");
        senhas.put("20253", "faitec25");
        senhas.put("20254", "faitec25");
        senhas.put("20255", "faitec25"); // novo aluno
        senhas.put("20256", "faitec25"); // novo aluno
        senhas.put("admin", "admin");
        senhas.put("admin2", "admin2");
        senhas.put("admin3", "admin3");
        senhas.put("admin4", "admin4");
        senhas.put("admin5", "admin5");
        senhas.put("admin6", "admin6"); // novo professor
        return senhas;
    }

    @FXML
    private void entrar(ActionEvent event) {
        String user = matricula.getText();
        String pass = senhaEstaVisivel ? senhaVisivel.getText() : senha.getText();

        Map<String, String> senhas = criarMapaSenhas();
        Map<String, Aluno> alunos = criarMapaAlunos();
        Map<String, Professor> professores = criarMapaProfessores();

        if (senhas.containsKey(user) && senhas.get(user).equals(pass)) {
            try {
                Stage stage = (Stage) entrar.getScene().getWindow();

                if (alunos.containsKey(user)) {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/br/com/javafx/educalink/areaalu/areaalu.fxml"));
                    Parent root = loader.load();

                    AreaAluController controller = loader.getController();
                    Aluno alunoLogado = alunos.get(user);

                    // Agora passa todos os professores
                    List<Professor> professoresDoAluno = new ArrayList<>(professores.values());

                    controller.receberDadosAluno(alunoLogado);
                    controller.receberDadosProfessor(professoresDoAluno);

                    Scene scene = new Scene(root);
                    stage.setScene(scene);
                    stage.setTitle("EducaLink - Área do Aluno");

                    Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
                    stage.setX(screenBounds.getMinX());
                    stage.setY(screenBounds.getMinY());
                    stage.setWidth(screenBounds.getWidth());
                    stage.setHeight(screenBounds.getHeight());

                    stage.setResizable(true);
                    stage.show();

                } else if (professores.containsKey(user)) {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/br/com/javafx/educalink/areaprof/areaprof.fxml"));
                    Parent root = loader.load();

                    AreaProfController controller = loader.getController();
                    Professor professorLogado = professores.get(user);
                    controller.receberDadosProfessor(professorLogado);

                    DadosCompartilhados.getInstancia().setAreaProfController(controller);

                    Scene scene = new Scene(root);
                    stage.setScene(scene);
                    stage.setTitle("EducaLink - Área do Professor");

                    Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
                    stage.setX(screenBounds.getMinX());
                    stage.setY(screenBounds.getMinY());
                    stage.setWidth(screenBounds.getWidth());
                    stage.setHeight(screenBounds.getHeight());

                    stage.setResizable(true);
                    stage.show();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            mostrarAlerta("Matrícula ou senha incorretos.");
        }
    }

    @FXML
    private void esquecisenha(MouseEvent event) {
        mostrarAlerta("Solicitação de redefinição de senha enviada!");
    }

    @FXML
    private void alternarSenha(MouseEvent event) {
        senhaEstaVisivel = !senhaEstaVisivel;

        if (senhaEstaVisivel) {
            int caretPos = senha.getCaretPosition();
            senhaVisivel.setText(senha.getText());
            senhaVisivel.setVisible(true);
            senhaVisivel.setManaged(true);
            senha.setVisible(false);
            senha.setManaged(false);

            Platform.runLater(() -> {
                senhaVisivel.requestFocus();
                Platform.runLater(() -> senhaVisivel.positionCaret(caretPos));
            });

            iconSenha.setImage(new Image(getClass().getResourceAsStream("/br/com/javafx/educalink/img/login/eye_view.png")));
        } else {
            int caretPos = senhaVisivel.getCaretPosition();
            senha.setText(senhaVisivel.getText());
            senha.setVisible(true);
            senha.setManaged(true);
            senhaVisivel.setVisible(false);
            senhaVisivel.setManaged(false);

            Platform.runLater(() -> {
                senha.requestFocus();
                Platform.runLater(() -> senha.positionCaret(caretPos));
            });

            iconSenha.setImage(new Image(getClass().getResourceAsStream("/br/com/javafx/educalink/img/login/eye_block.png")));
        }
    }



    private void mostrarAlerta(String mensagem) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Aviso");
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }
}