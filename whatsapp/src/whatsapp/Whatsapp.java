package whatsapp;

import java.util.ArrayList;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Whatsapp extends Application {
    
    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader layout = new FXMLLoader(getClass().getResource("FXMLDocument.fxml"));  

        Scene scene = new Scene(layout.load());
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
    
}
