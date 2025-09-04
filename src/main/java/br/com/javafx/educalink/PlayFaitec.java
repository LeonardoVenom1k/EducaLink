package br.com.javafx.educalink;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.Parent;
import javafx.stage.Stage;

public class PlayFaitec extends Application {

    @Override
    public void start(Stage primaryStage) {
        try {
            //Tela 1
            FXMLLoader loaderProfessor = new FXMLLoader(getClass().getResource("/br/com/javafx/educalink/login/login.fxml"));
            Parent rootProfessor = loaderProfessor.load();
            Scene sceneProfessor = new Scene(rootProfessor, 800, 500);
            Stage stageProfessor = new Stage();
            stageProfessor.setTitle("EducaLink - Login");
            stageProfessor.setScene(sceneProfessor);
            stageProfessor.setResizable(false);
            stageProfessor.setX(stageProfessor.getX() - 500);
            stageProfessor.show();

            // Tela 2
            FXMLLoader loaderAluno = new FXMLLoader(getClass().getResource("/br/com/javafx/educalink/login/login.fxml"));
            Parent rootAluno = loaderAluno.load();
            Scene sceneAluno = new Scene(rootAluno, 800, 500);
            Stage stageAluno = new Stage();
            stageAluno.setTitle("EducaLink - Login");
            stageAluno.setScene(sceneAluno);
            stageAluno.setResizable(false);
            stageAluno.setX(stageProfessor.getX() + 500);
            stageAluno.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    }
