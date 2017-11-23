package application;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
    private Stage primaryStage;

    @Override
    public void start(Stage primaryStage) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("testerPakietu.fxml"));
            this.primaryStage = primaryStage;
            this.primaryStage.setTitle("Tester AZZ08");
            primaryStage.setScene(new Scene(root/*, 800, 600*/));
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}