package whatsapp;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Whatsapp extends Application {
    
    private Contatos contact;
    private Conversa convers;
    
    // Ideias para o Seriallize
    // https://stackoverflow.com/questions/14187963/passing-parameters-javafx-fxml
    
    @Override
    public void start(Stage stage) throws Exception {
        Database db = new Database();
        
        Contatos cont = db.getContacts();
        ArrayList<Conversa> conv = db.getConversa();
        
        FXMLLoader layout = new FXMLLoader(getClass().getResource("WhatsappInterface.fxml"));  

        Scene scene = new Scene(layout.load());
        stage.setScene(scene);
        
        WhatsappInterfaceController controller = layout.<WhatsappInterfaceController>getController();
        controller.setContacts(cont);
        controller.setConversas(conv);
        
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
