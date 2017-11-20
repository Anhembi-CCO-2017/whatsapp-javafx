package whatsapp;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Whatsapp extends Application {
    
    // Ideias para o Seriallize
    // https://stackoverflow.com/questions/14187963/passing-parameters-javafx-fxml
    
    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader layout = new FXMLLoader(getClass().getResource("WhatsappInterface.fxml"));  

        Scene scene = new Scene(layout.load());
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
    
}
