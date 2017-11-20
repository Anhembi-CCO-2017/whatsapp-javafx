/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package whatsapp;

import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
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
import javafx.scene.input.KeyEvent;

/**
 *
 * @author googs
 */
public class WhatsappInterfaceController implements Initializable {

    @FXML
    private Button send;
    @FXML
    private TextArea msgTextArea;
    @FXML
    private ScrollPane contactScroll;
    @FXML
    private ScrollPane contactScrollPane;
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
    @FXML
    private TextField searchField;
    @FXML
    private HBox searchHBox;

    private Contatos contatos = new Contatos();
    private ArrayList<Conversa> conversas = new ArrayList<>();
    private Conversa activeConv;
    private boolean switchState = false;
    private boolean scrollActiveAction = false; // False = Lista de Conversas, True = Lista de contatos
    private Usuario selfUser;

    private StackPane genContactScrollList(Usuario usr, boolean event) {
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
        
        layout.setUserData(usr);
        
        //  Caso true, define Evento como ClickContact
        if(event)
            layout.onMouseClickedProperty().set(this::handleClickContact);
        else //  Caso false, define Evento como ClickAddContact
            layout.onMouseClickedProperty().set(this::handleClickAddContact);

        return layout;  
    }

    private StackPane genConversScrolllist(Conversa conv) {
        Usuario usr = conv.getUser(1);
        
        GridPane gppConv = new GridPane();
        gppConv.getStyleClass().add("conversa");

        GridPane gppConvText = new GridPane();
        gppConvText.getStyleClass().add("conversa-texts");

        /* Nome do usuário dentro de um Text */
        Text textConvName = new Text(usr.getNome());
        textConvName.getStyleClass().add("conversa-name");

        /* Ultima mensagem da conversa dentro de um Text */
        String textDataConvLast;

        if(conv.getListaMensagens().size() > 0)
            textDataConvLast = conv.retornarMensagemString(conv.getListaMensagens().size() - 1);
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
        layout.setUserData(conv);
        layout.onMouseClickedProperty().set(this::handleClickConv);

        return layout;
    }
    
    //  Renderiza lista de contatos
    private void renderContactsList(ArrayList<Usuario> data) {
        VBox content = new VBox();

        VBox scrollContactContent = new VBox();

        //Define como cancelar botão de janela
        buttonAddConv.setText("Cancelar");

        //Gambiarra para criar o novo contato.
        Usuario newContact = new Usuario("", "Novo contato");
        newContact.setImage("contact.jpg");

        scrollContactContent.getChildren().add(this.genContactScrollList(newContact, false));

        // Monta lista de usuarios
        for (int i = 0; i < data.size(); i++)
            scrollContactContent.getChildren().add(this.genContactScrollList(data.get(i), true));

        content.getChildren().add(scrollContactContent);

        contactScrollPane.setContent(content);
    }
    
    private VBox renderConvStructure(Conversa conv) {
        /* Arraylist com todas as mensagens */
        ArrayList<Mensagem> msgs = conv.getListaMensagens();

        VBox dataText = msgContent;
        dataText.getChildren().clear();
        dataText.setUserData(conv);

        /* mudar imagem, nome e status na conversa ativa*/
        topoNome.setText(conv.getUser(1).getNome());
        topoImage.setFill(new ImagePattern(conv.getUser(1).getImage(), 0, 0, 1, 1, true));
        labelStatus.setText(conv.getUser(1).getUltimaVezOnline());

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
            if(msg.getEmissor().equals(conv.getUser(0))){
                gppText.getStyleClass().add("mensagem-left");
                generalGrid.add(gppText, 0, 0);
            }else{
                gppText.getStyleClass().add("mensagem-right");
                generalGrid.add(gppText, 0, 1);
            }

            dataText.getChildren().add(gppText);
        }
        
        return dataText;
    }

    @FXML
    private void handlerUserSwitch(ActionEvent event) {
        this.switchState = !this.switchState;
    }

    @FXML
    private void handleClickAddContact(MouseEvent event) {
        return;
    }
    
    @FXML
    private void handlerButtonSearch(KeyEvent event) {
        String searchString = searchField.getText();
        
        //  Busca caso Lista de Conversa
        if(!scrollActiveAction) {
            ArrayList<Conversa> foundConv = new ArrayList<>();
            
            if(searchString.isEmpty()) {
                this.loadConversas(conversas);
                return;
            }
            
            for (int i = 0; i < conversas.size(); i++) {
                Conversa conv = conversas.get(i);
                ArrayList<Mensagem> find = conv.buscarMensagem(searchString);

                //Adiciona no ArrayList<Conversa> foundConv
                if(find.size() > 0) foundConv.add(conv);
            }
            
            this.loadConversas(foundConv);
        } else { //  Busca caso Lista de Contatos
            ArrayList<Usuario> foundUser = new ArrayList<>();
            
            if(searchString.isEmpty()) {
                this.renderContactsList(contatos.getArrayListUsers());
                return;
            }
            
            for (int i = 0; i < contatos.getArrayListUsers().size(); i++) {
                Usuario user = contatos.getArrayListUsers().get(i);

                //Adiciona no ArrayList<Conversa> foundConv
                if(user.getNome().contains(searchString)) 
                    foundUser.add(user);
            }
            
            this.renderContactsList(foundUser);
        }
    }

    @FXML
    private void handlerButtonOpenContacts(ActionEvent event) {
        //  Faz com que o botão de adicionar conversa vire de cancelar e voltar.
        if(scrollActiveAction) {
            scrollActiveAction = !scrollActiveAction;
            buttonAddConv.setText("add conversa");
            this.loadConversas(conversas);

            return;
        } else scrollActiveAction = !scrollActiveAction;
        
        //  Renderiza Lista de Contatos
        this.renderContactsList(contatos.getArrayListUsers());
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
            activeConv.getUser(1).setUltimaVezOnline(new Date(System.currentTimeMillis()));
            labelStatus.setText(activeConv.getUser(1).getUltimaVezOnline());
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

    @FXML
    private void handleClickContact(MouseEvent event) {
        StackPane DOM = (StackPane) event.getSource();
        
        //  Verifica se já existe conversa iniciada com contato, se sim renderiza conversa.
        for (int i = 0; i < conversas.size(); i++)
            if(conversas.get(i).getUser(1).equals((Usuario) DOM.getUserData())) {
                msgScroll.setContent(this.renderConvStructure(conversas.get(i)));
                activeConv = conversas.get(i);
                
                // Reseta janela lateral para lista de conversas
                this.handlerButtonOpenContacts(new ActionEvent()); //Inicia o evento para alterar e renderizar o correto.

                return;
            }
        
        // Caso não, inicia conversa. E renderiza.
        conversas.add(new Conversa(this.selfUser, (Usuario) DOM.getUserData()));
        activeConv = conversas.get(conversas.size() - 1);
        msgScroll.setContent(this.renderConvStructure(conversas.get(conversas.size() - 1)));
        
        // Reseta janela lateral para lista de conversas
        this.handlerButtonOpenContacts(new ActionEvent()); //Inicia o evento para alterar e renderizar o correto.
    }

    /*  Acão do botão quando o Conversa é clicada.
            Insere todas as mensagens da conversa no container de mensagens
    */
    @FXML
    private void handleClickConv(MouseEvent event) {
        /* slecionando o conyainer de conteúdo*/
        StackPane content = (StackPane) event.getSource();

        /* selecionando informacoes da conversa e coloca como coversa atual*/
        activeConv = (Conversa) content.getUserData();
        
        msgScroll.setContent(this.renderConvStructure(activeConv));
    }

    //ESSA FUNCAO SO VAI EXISTIR POR ENQUANTO
    //DEPOIS QUE EXISTIR A OPCAO DE INICIAR CONVERSA ELA VAI PRO BELELEU
    private void genConversas() {
        this.selfUser = new Usuario("Eu", "stackoverflow");
        Random r = new Random();
        
        this.contatos.adicionarUsuario(new Usuario("Carlinhos", "cade 2 segunda chance", "foto8.jpg"));
        this.contatos.adicionarUsuario(new Usuario("AMulherDosDragao", "cade 2 segunda chance", "foto7.jpg"));
        
        for (int i = 1; i < 8; i++) {
            Usuario user = new Usuario("Carlos"+i, "cade 2 segunda chance");
            Conversa conv = new Conversa(this.selfUser, user);

            user.setUltimaVezOnline(new Date(System.currentTimeMillis()));
            user.setImage("foto"+i+".jpg");

            this.contatos.adicionarUsuario(user);

            //  switcher para alterar de quem é a mensagem gerada.
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

    private void loadConversas(ArrayList<Conversa> conv) {
        VBox content = new VBox();

        for (int i = 0; i < conv.size(); i++)
            content.getChildren().add(this.genConversScrolllist(conv.get(i)));

        contactScrollPane.setContent(content);
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        // Action da Troca de Usuario
        userSwitch.setOnAction(this::handlerUserSwitch);

        //  Action do botão de Envio
        send.setOnAction(this::handleButtonSendMsg);

        // Action de Adicionar novo Contato/Conversa.
        buttonAddConv.setOnAction(this::handlerButtonOpenContacts);

        //  Action de Buscar em Conversas;
        searchField.setOnKeyPressed(this::handlerButtonSearch);

        //  Definições de Inicio

        /* Preenchimento de foto default para o topo*/
        Image tfImage = new Image(getClass().getResourceAsStream("foto.jpg"));
        topoImage.setFill(new ImagePattern(tfImage, 0, 0, 1, 1, true));

        // Carrega Conversas Existentes (Gera enquanto não implementado totalmente novos contatos.)
        this.genConversas();
        this.loadConversas(this.conversas);
    }

}
