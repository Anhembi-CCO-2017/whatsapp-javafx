/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package whatsapp;

import java.net.URL;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Random;
import java.util.ResourceBundle;
import java.util.UUID;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.HPos;
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
import javafx.scene.text.Text;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.scene.paint.ImagePattern;

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
    private Button userSwitch;
    @FXML
    private Button buttonAddConv;
    @FXML
    private Label labelStatus;
    
    private Contatos contatos = new Contatos();
    private ArrayList<Conversa> conversas = new ArrayList<>();
    private Conversa activeConv;
    private boolean switchState = false;
    
    @FXML
    private StackPane genScrollList(Usuario usr) {
        GridPane gppConv = new GridPane();
        gppConv.getStyleClass().add("conversa");

        GridPane gppConvText = new GridPane();
        gppConvText.getStyleClass().add("conversa-texts");

        /* Nome do usuário dentro de um Text */
        Text textConvName = new Text(usr.getNome());
        textConvName.getStyleClass().add("conversa-name");

        /* Status do contato dentro de um Text */
        Text textConvLast = new Text(usr.getStatus());
        textConvLast.getStyleClass().add("conversa-last");

        /* Pegando a imagem e colocando dentro de um circulo*/
        Circle foto = new Circle(40, 40, 20, Color.rgb(18,140,126));
        foto.setFill(new ImagePattern(usr.getImage(), 0, 0, 1, 1, true));

        gppConvText.add(textConvName, 1, 0);
        gppConvText.add(textConvLast, 1, 1);

        gppConv.add(gppConvText, 1, 0);
        gppConv.add(foto, 0, 0);

        StackPane layout = new StackPane();
        layout.getChildren().addAll(gppConv);
        //layout.setUserData(conversas.get(i));
        //layout.onMouseClickedProperty().set(this::handleClickContact);

        return layout;
    }
    
    @FXML
    private void handlerUserSwitch(ActionEvent event) {
        this.switchState = !this.switchState;
    }
    
    @FXML
    private void handlerButtonOpenContacts(ActionEvent event) {
        
        VBox content = this.content;

        GridPane upSide = new GridPane();
        upSide.add(new Text("\t"), 0, 0);
        upSide.add(new Text("New Chat\t     "), 1, 0);
        Button btCancel = new Button("Cancelar");
        btCancel.setStyle("user");
        upSide.add(btCancel, 2, 0);
        
        HBox downSide = new HBox();
        TextField searchBox = new TextField("Search\t\t\t\t");
        downSide.getChildren().add(new Button("..."));
        downSide.getChildren().add(searchBox);

        VBox scrollContactContent = new VBox();
        ArrayList<Usuario> data = contatos.getArrayListUsers();
        
        //Gambiarra para criar o novo contato.
        Usuario newContact = new Usuario("", "Novo contato");
        newContact.setImage("contact.jpg");
        
        scrollContactContent.getChildren().add(this.genScrollList(newContact));
        
        // Monta lista de usuarios
        for (int i = 0; i < data.size(); i++)
            scrollContactContent.getChildren().add(this.genScrollList(data.get(i)));

        content.getChildren().clear();
        content.getChildren().add(upSide);
        content.getChildren().add(downSide);
        content.getChildren().add(scrollContactContent);

        contactScroll.setContent(content);
    }

    
    /*Acão do botão para Enviar msg */
    @FXML
    private void handleButtonSendMsg(ActionEvent event) {
        if(msgTextArea.getText().equals(""))
            return;
        
        int userID = (switchState) ? 1 : 0;
        activeConv.addMensagem(userID, msgTextArea.getText());
        ArrayList<Mensagem> msgs = activeConv.getListaMensagens();
        
        //Caso Contato envie msg, atualiza o UltimaVezOnline
        if(switchState) {
            activeConv.getUser(1).setUltimavezonline(DateFormat.getDateTimeInstance());
            //labelStatus.setText(activeConv.getUser(1).getUltimavezonline().getCalendar().getDisplayName(35, Calendar.SHORT_FORMAT, Locale.ENGLISH));
        }
        
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
    
    /*  Acão do botão quando o contato é clicado.
            Insere todas as mensagens da conversa no container de mensagens
    */
    @FXML
    private void handleClickContact(MouseEvent event) {
        /* slecionando o conyainer de conteúdo*/ 
        StackPane content = (StackPane) event.getSource();
        
        /* selecionando informacoes da conversa e coloca como coversa atual*/
        activeConv = (Conversa) content.getUserData();
        
        /* Arraylist com todas as mensagens */
        ArrayList<Mensagem> msgs = activeConv.getListaMensagens();

        
        VBox dataText = msgContent;
        dataText.getChildren().clear();
        dataText.setUserData(activeConv);
        
        /* mudar imagem e nome na conversa ativa*/
        topoNome.setText(activeConv.getUser(1).getNome());
        topoImage.setFill(new ImagePattern(activeConv.getUser(1).getImage(), 0, 0, 1, 1, true));
        
        GridPane generalGrid = new GridPane();
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
                generalGrid.add(gppText, 0, 0);
            }else{
                gppText.getStyleClass().add("mensagem-right");
                generalGrid.add(gppText, 0, 1);
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
        userSwitch.setOnAction(this::handlerUserSwitch);
        send.setOnAction(this::handleButtonSendMsg);
        buttonAddConv.setOnAction(this::handlerButtonOpenContacts);

        this.loadConversas();
    }
   
}
