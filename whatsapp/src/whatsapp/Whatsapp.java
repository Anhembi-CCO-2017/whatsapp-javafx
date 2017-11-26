package whatsapp;

import java.util.ArrayList;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Whatsapp extends Application {
    
    @Override
    public void start(Stage stage) throws Exception {
        Database db = new Database(); // Carega e instancia Database
        
        Contatos cont = db.getContacts(); // Carrega contatos
        ArrayList<Conversa> conv = db.getConversa(); // Carrega conversas
        
        FXMLLoader layout = new FXMLLoader(getClass().getResource("WhatsappInterface.fxml"));  

        Scene scene = new Scene(layout.load()); // Define Scene do JavaFX com Layout
        stage.setScene(scene);
        
        // Instancia do controlador do WhatsappInterface
        WhatsappInterfaceController controller = layout.<WhatsappInterfaceController>getController();
        
        controller.setContacts(cont); // set contatos carregadas da db
        controller.setConversas(conv); // set conversas carregadas da db
        controller.setMySelf(db.mySelf); // define o usuario utilizador
        
        // Evento para salvar database e finalizar o WhatsApp
        stage.setOnHiding( event -> {
            //Contatos c, ArrayList<Conversas> cc
            db.save(controller.getContacts(), controller.getConversas());
            System.exit(0);
        });
        
        stage.show(); // Inicia a janela do whastapp
    }

    public static void main(String[] args) {
        launch(args);
    }
}
