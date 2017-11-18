/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package whatsapp;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Random;
import java.util.ResourceBundle;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.ImagePattern;
import javafx.stage.Stage;

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
    @FXML
    private Circle topoImage;
    @FXML
    private Label topoNome;
    @FXML
    private Button buttonAddConv;
    
    private Contatos contatos = new Contatos();
    private ArrayList<Conversa> conversas = new ArrayList<>();
    private Conversa activeConv;
    
    @FXML
    private void handlerButtonAddConv(ActionEvent event) {
        
        VBox content = new VBox();
        GridPane grid = new GridPane();

        GridPane upSide = new GridPane();
        upSide.add(new Text(""), 0, 0);
        upSide.add(new Text(""), 1, 0);
        Button btCancel = new Button("Cancelar");
        upSide.add(btCancel, 2, 0);

        GridPane gridNewContact = new GridPane();
        gridNewContact.add(new Circle(18), 0, 0);
        gridNewContact.add(new Text("Novo Contato"), 1, 0);
        
        GridPane downSide = new GridPane();
        TextField searchBox = new TextField("Search");
        downSide.add(searchBox, 0, 0);
        downSide.add(gridNewContact, 0, 1);
        ScrollPane scrollContact = new ScrollPane();
        
        VBox scrollContactContent = new VBox();
        ArrayList<Usuario> data = contatos.getArrayListUsers();
        for (int i = 0; i < data.size(); i++)
        {
            Usuario usr = data.get(i);
            
            GridPane gppConv = new GridPane();
            gppConv.getStyleClass().add("conversa");
            
            GridPane gppConvText = new GridPane();
            gppConvText.getStyleClass().add("conversa-texts");
            
            /* Nome do usuário dentro de um Text */
            Text textConvName = new Text(usr.getNome());
            textConvName.getStyleClass().add("conversa-name");
            
            /* Ultima mensagem da conversa dentro de um Text */
            Text textConvLast = new Text("ultima msg");
            textConvLast.getStyleClass().add("conversa-last");
            
            /* Pegando a imagem e colocando dentro de um circulo*/
            Circle foto = new Circle(40, 40, 20, Color.BLUE);
            foto.setFill(new ImagePattern(usr.getImage(), 0, 0, 1, 1, true));

            gppConvText.add(textConvName, 1, 0);
            gppConvText.add(textConvLast, 1, 1);
            
            gppConv.add(gppConvText, 1, 0);
            gppConv.add(foto, 0, 0);

            StackPane layout = new StackPane();
            layout.getChildren().addAll(gppConv);
            layout.setUserData(conversas.get(i));
            //layout.onMouseClickedProperty().set(this::handleClickContact);
            
            scrollContactContent.getChildren().add(layout);
        }
        
        scrollContact.setContent(scrollContactContent);
        downSide.add(scrollContact, 0, 1);
        
        grid.add(upSide, 0, 0);
        grid.add(downSide, 0, 1);

        content.getChildren().clear();
        content.getChildren().add(grid);

        contactScroll.setContent(content);
    }

    
    @FXML
    private void handleButtonAction(ActionEvent event) {
        if(msgTextArea.getText().equals(""))
            return;
        
        activeConv.addMensagem(0, msgTextArea.getText());
        ArrayList<Mensagem> msgs = activeConv.getListaMensagens();

        GridPane gppText = new GridPane();
        Mensagem msg = msgs.get(msgs.size()-1);
        
        Text textMsg = new Text(msg.getTexto());
        Text textStatus = new Text(msg.getHora());
        
        textStatus.getStyleClass().add("mensagem-hora");
        gppText.add(textMsg, 0, 0);
        gppText.add(textStatus, 0, 1);
            
        gppText.getStyleClass().add("mensagem");
            
        if(msg.getEmissor().equals(activeConv.getUser(0))){
            gppText.setHalignment(textStatus, HPos.CENTER);
            gppText.getStyleClass().add("mensagem-left");
        }else{
            gppText.setHalignment(textStatus, HPos.CENTER);
            gppText.getStyleClass().add("mensagem-right");
                }
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
        topoNome.setText(activeConv.getUser(1).getNome());
        topoImage.setFill(new ImagePattern(activeConv.getUser(1).getImage(), 0, 0, 1, 1, true));
        for (int i = 0; i < msgs.size(); i++)
        {
            GridPane gppText = new GridPane();
            Mensagem msg = msgs.get(i);
            
            Text textMsg = new Text(msg.getTexto());
            Text textStatus = new Text(msg.getHora());
            textStatus.getStyleClass().add("mensagem-hora");
            gppText.add(textMsg, 0, 0);
            gppText.add(textStatus, 0, 1);
            
            gppText.getStyleClass().add("mensagem");
            if(msg.getEmissor().equals(activeConv.getUser(0))){
                gppText.getStyleClass().add("mensagem-left");
            }else{
                gppText.getStyleClass().add("mensagem-right");
            }
            dataText.getChildren().add(gppText);
        }
        
        msgScroll.setContent(dataText);
    }
    
    //ESSA FUNCAO SO VAI EXISTIR POR ENQUANTO
    //DEPOIS QUE EXISTIR A OPCAO DE INICIAR CONVERSA ELA VAI PRO BELELEU
    private void genConversas() {
        Usuario me = new Usuario("Eu", "stackoverflow");
        Random r = new Random();
        
        for (int i = 1; i < 8; i++) {
            Usuario user = new Usuario("Carlos"+i, "cade 2 segunda chance");
            Conversa conv = new Conversa(me, user);
            user.setImage("foto"+i+".jpg");
            this.contatos.adicionarUsuario(user);
            boolean swtch = false;
                    
            for(int j = 0; i < r.nextInt(50); j++) {
                if(swtch)
                    conv.addMensagem(0, UUID.randomUUID().toString());
                else
                    conv.addMensagem(1, UUID.randomUUID().toString());
                
                swtch = !swtch;
            }
            
            conversas.add(conv);
        }
    }
    
    private void loadConversas() {
        this.genConversas();
        
        VBox content = this.content;                
        
        /* Preenchimento de foto default para o topo*/
        Image tfImage = new Image(getClass().getResourceAsStream("foto.jpg")); 
        topoImage.setFill(new ImagePattern(tfImage, 0, 0, 1, 1, true));
        
        for (int i = 0; i < conversas.size(); i++)
        {
            Usuario usr = conversas.get(i).getUser(1);
            
            GridPane gppConv = new GridPane();
            gppConv.getStyleClass().add("conversa");
            
            GridPane gppConvText = new GridPane();
            gppConvText.getStyleClass().add("conversa-texts");
            
            /* Nome do usuário dentro de um Text */
            Text textConvName = new Text(usr.getNome());
            textConvName.getStyleClass().add("conversa-name");
            
            /* Ultima mensagem da conversa dentro de um Text */
            String textDataConvLast;
                    
            if(conversas.get(i).getListaMensagens().size() > 0)
                textDataConvLast = conversas.get(i).retornarMensagemString(conversas.get(i).getListaMensagens().size() - 1);
            else
                textDataConvLast = "";
            
            Text textConvLast = new Text(""+textDataConvLast);
            textConvLast.getStyleClass().add("conversa-last");
            
            
            /* Pegando a imagem e colocando dentro de um circulo*/
            Circle foto = new Circle(40, 40, 20, Color.BLUE);
            foto.setFill(new ImagePattern(usr.getImage(), 0, 0, 1, 1, true));

            gppConvText.add(textConvName, 1, 0);
            gppConvText.add(textConvLast, 1, 1);
            
            gppConv.add(gppConvText, 1, 0);
            gppConv.add(foto, 0, 0);
            
            
            StackPane layout = new StackPane();
            layout.getChildren().addAll(gppConv);
            layout.setUserData(conversas.get(i));
//            topoNome.setText(conversas.get(i).getUser(1).getNome());
            layout.onMouseClickedProperty().set(this::handleClickContact);

            content.getChildren().add(layout);
        }
        
        contactScroll.setContent(content);
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        send.setOnAction(this::handleButtonAction);
        buttonAddConv.setOnAction(this::handlerButtonAddConv);
        this.loadConversas();

    }
   
}
