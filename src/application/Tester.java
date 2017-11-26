package application;

import java.net.URL;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Tester extends Application {
    private Stage primaryStage;

    @Override
    public void start(Stage primaryStage) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("Tester.fxml"));
            this.primaryStage = primaryStage;
            this.primaryStage.setTitle("Tester pakietu");
            Scene scene = new Scene(root);
            URL url = this.getClass().getResource("application.css");
            if (url == null) {
                System.out.println("Nie znaleziono stylu. Anulowano.");
                System.exit(-1);
            }
            String css = url.toExternalForm(); 
            scene.getStylesheets().add(css);
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
