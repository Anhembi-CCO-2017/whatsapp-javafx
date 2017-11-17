/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package whatsapp;

import java.net.URL;
import java.util.ArrayList;
import java.util.Random;
import java.util.ResourceBundle;
import java.util.UUID;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

/**
 *
 * @author googs
 */
public class FXMLDocumentController implements Initializable {
    
    @FXML
    private Button send;
    @FXML
    private TextArea msgTextArea;
    @FXML
    private ScrollPane contactScroll;
    @FXML
    private ScrollPane msgScroll;
    @FXML
    private VBox msgContent;
    @FXML
    private VBox content;
    
    private Contatos contatos;
    private ArrayList<Conversa> conversas = new ArrayList<>();
    private Conversa activeConv;
    
    @FXML
    private void handleButtonAction(ActionEvent event) {
        if(msgTextArea.getText().equals(""))
            return;
        
        activeConv.addMensagem(0, msgTextArea.getText());
        ArrayList<Mensagem> msgs = activeConv.getListaMensagens();

        GridPane gppText = new GridPane();
        Mensagem msg = msgs.get(msgs.size()-1);
        
        Text textMsg = new Text(msg.getTexto());
        Text textStatus = new Text(msg.getDataHora().getTime().toString());
            
        gppText.add(textMsg, 0, 0);
        gppText.add(textStatus, 0, 1);
            
        gppText.getStyleClass().add("mensagem");
            
        if(msg.getEmissor().equals(activeConv.getUser(0)))
            gppText.getStyleClass().add("mensagem-left");
        else
            gppText.getStyleClass().add("mensagem-right");
            
        msgContent.getChildren().add(gppText);
        msgTextArea.clear();
    }
    
    @FXML
    private void handleClickContact(MouseEvent event) {
        StackPane content = (StackPane) event.getSource();
        activeConv = (Conversa) content.getUserData();
        ArrayList<Mensagem> msgs = activeConv.getListaMensagens();

        VBox dataText = msgContent;
        dataText.getChildren().clear(); // DAR CLEAR SEMPRE QUE TROCAR DE MSG
        dataText.setUserData(activeConv);
        
        for (int i = 0; i < msgs.size(); i++)
        {
            GridPane gppText = new GridPane();
            Mensagem msg = msgs.get(i);
            
            Text textMsg = new Text(msg.getTexto());
            Text textStatus = new Text(msg.getDataHora().getTime().toString());
            
            gppText.add(textMsg, 0, 0);
            gppText.add(textStatus, 0, 1);
            
            gppText.getStyleClass().add("mensagem");
            
            if(msg.getEmissor().equals(activeConv.getUser(0)))
                gppText.getStyleClass().add("mensagem-left");
            else
                gppText.getStyleClass().add("mensagem-right");
            
            dataText.getChildren().add(gppText);
        }
        
        msgScroll.setContent(dataText);
    }
    
    //ESSA FUNCAO SO VAI EXISTIR POR ENQUANTO
    //DEPOIS QUE EXISTIR A OPCAO DE INICIAR CONVERSA ELA VAI PRO BELELEU
    private void genConversas() {
        Usuario me = new Usuario("Eu", "stackoverflow");
        Random r = new Random();
        
        for (int i = 0; i < 10; i++) {
            Usuario user = new Usuario("Carlos"+i, "cade 2 segunda chance");
            Conversa conv = new Conversa(me, user);
            
            boolean swtch = false;
                    
            for(int j = 0; i < r.nextInt(50); j++) {
                if(swtch)
                    conv.addMensagem(0, UUID.randomUUID().toString());
                else
                    conv.addMensagem(1, UUID.randomUUID().toString());
                
                swtch = !swtch;
            }
            
            conversas.add(conv);
            //contatos.adicionarUsuario(user); 
        }
    }
    
    private void loadConversas() {
        this.genConversas();
        
        VBox content = this.content;
        Random r = new Random();
                
        for (int i = 0; i < conversas.size(); i++)
        {
            Rectangle rect = new Rectangle(130, 50, Color.rgb(r.nextInt(255), r.nextInt(255), r.nextInt(255)));
            Text text = new Text(conversas.get(i).getUser(1).getNome());
            
            StackPane layout = new StackPane();
            layout.getChildren().addAll(rect, text);
            layout.setUserData(conversas.get(i));
            layout.onMouseClickedProperty().set(this::handleClickContact);

            content.getChildren().add(layout);
        }
        
        contactScroll.setContent(content);
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        send.setOnAction(this::handleButtonAction);
        this.loadConversas();
//        ImagePattern imgptr = new ImagePattern(img);
//        tt.setFill(imgptr);
//        String image = FXMLDocumentController.class.getResource("imgs/foto.jpg").toExternalForm();
//        tt.setStyle("-fx-background-image: url('" + image + "');");
    }
   
}
