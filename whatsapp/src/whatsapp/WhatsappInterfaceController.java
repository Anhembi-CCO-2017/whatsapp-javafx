package whatsapp;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
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
import javafx.stage.FileChooser;
import javafx.stage.Stage;


/**
 *
 * @author Lucas Golino, Thiago Almeida, Matheus Eli, Gabriel Henrique, Gabriel Forster
 */
public class WhatsappInterfaceController implements Initializable {
    
    // Elementos do UI-JavaFX
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
    @FXML
    private Label mySelfStatus;
    @FXML
    private Label mySelfName;

    private Contatos contatos = new Contatos(); // Contatos cadastrados
    private ArrayList<Conversa> conversas = new ArrayList<>(); // Conversas existentes
    private Conversa activeConv; // Conversa ativa no Menssage Box
    private boolean switchState = false; // Estado do emissor, usuario ou contato.
    private boolean scrollActiveAction = false; // False = Lista de Conversas, True = Lista de contatos
    private Usuario selfUser; // Usuario utilizador
    private File fileOpen; // auxiliar data para abrir arquivos.
    private Image defaultFoto = new Image(getClass().getResourceAsStream("placeholder.png")); // Foto padrão

    /**
     *  Gera ScrollList de contatos
     * @param usr   Usuario objeto de usuario
     * @param event Boolean de qual evento deve usar
     * @return StackPane  Objeto do javaFX
     */
    private StackPane genContactScrollList(Usuario usr, boolean event) {
        GridPane gppConv = new GridPane();
        gppConv.getStyleClass().add("conversa");

        GridPane gppConvText = new GridPane();
        gppConvText.getStyleClass().add("conversa-texts");

        /* Nome do usuário dentro de um Text */
        Text textConvName = new Text(usr.getNome());
        textConvName.getStyleClass().add("conversa-name");

        /* Status do contato dentro de um Text */
        Label textConvLast = new Label(usr.getStatus());
        textConvLast.getStyleClass().add("conversa-last");

        /* Pegando a imagem e colocando dentro de um circulo*/
        Circle foto = new Circle(40, 40, 20, Color.rgb(18,140,126));
        if(usr.getImage() == null)
            foto.setFill(new ImagePattern(this.defaultFoto, 0, 0, 1, 1, true));
        else
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

    /**
     *  Gera o ScrollList de conversas.
     * @param conv   Conversa objeto de Conversa
     * @return StackPane  Objeto do javaFX
     */
    private StackPane genConversScrolllist(Conversa conv) {
        Usuario usr = conv.getUser(1); // Pega o usuario contato
        
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
        if(usr.getImage() == null)
            foto.setFill(new ImagePattern(this.defaultFoto, 0, 0, 1, 1, true));
        else
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
    
    /**
     *  Renderiza lista de contatos
     * @param data   ArrayList<Usuario>
     */
    private void renderContactsList(ArrayList<Usuario> data) {
        VBox content = new VBox();

        VBox scrollContactContent = new VBox();

        //Define como cancelar botão de janela
        buttonAddConv.setText("cancelar");

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
    
    /**
     *  Renderizador de estrutura da conversa
     * @param conv   Conversa objeto
     * @return VBox Objeto do JavaFX
     */
    private VBox renderConvStructure(Conversa conv) {
        /* Arraylist com todas as mensagens */
        ArrayList<Mensagem> msgs = conv.getListaMensagens();

        VBox dataText = msgContent;
        dataText.getChildren().clear();
        dataText.setUserData(conv);

        /* mudar imagem, nome e status na conversa ativa*/
        topoNome.setText(conv.getUser(1).getNome());
        if(conv.getUser(1).getImage() == null)
            topoImage.setFill(new ImagePattern(defaultFoto, 0, 0, 1, 1, true));
        else
            topoImage.setFill(new ImagePattern(conv.getUser(1).getImage(), 0, 0, 1, 1, true));
        labelStatus.setText(conv.getUser(1).getUltimaVezOnline());

        for (int i = 0; i < msgs.size(); i++)
        {
            GridPane gppText = new GridPane();
            VBox vbxText = new VBox();
            Mensagem msg = msgs.get(i);

            Text textMsg = new Text(msg.getTexto());
            Text textStatus = new Text(msg.getHora());
            Text textStats = new Text(msg.getStatus());
            
            textMsg.wrappingWidthProperty();
            textStatus.getStyleClass().add("mensagem-hora");
            textStats.getStyleClass().add("mensagem-status");
            gppText.add(textMsg, 0, 0);
            gppText.add(textStatus, 0, 1);
            gppText.add(textStats, 1, 1);

            gppText.getStyleClass().add("mensagem");
            
            if(msg.getEmissor().equals(conv.getUser(0))){
                gppText.getStyleClass().add("mensagem-right");
                vbxText.setAlignment(Pos.TOP_RIGHT);
            }else{
                gppText.getStyleClass().add("mensagem-left");
                vbxText.setAlignment(Pos.TOP_LEFT);
            }
            
            
            vbxText.getChildren().add(gppText);
            dataText.getChildren().add(vbxText);
        }
        
        return dataText;
    }
    
    /**
     * Ordenacao de conversas pela ultima mensagem enviada.
     */
    private void bubbleSortForSortConvers() {
        
        int size = conversas.size();

        Conversa auxiliar;

        for (int i = 0; i < size; i++)
            for(int j=0; j < size-1; j++) {
                Calendar firstCalendar = conversas.get(j).retornaUltimaMensagem().getDataHora();
                Calendar nextCalendar = conversas.get(j+1).retornaUltimaMensagem().getDataHora();
                
                if(firstCalendar.compareTo(nextCalendar) < 0) {
                    auxiliar = conversas.get(j);
                    conversas.set(j, conversas.get(j+1));
                    conversas.set(j+1, auxiliar);
                }
            }
        
        // Reload no frame de conversas apos a re-ordem.
        this.loadConversas(this.conversas);
    }
    
    /**
     * Ordenacao de contatos pelo nome
     */
    private void bubbleSortForSortContact() {
        int size = contatos.getListaUsuarios().size();
        ArrayList<Usuario> outList = contatos.getListaUsuarios();
        
        Usuario auxiliar;

        for (int i = 0; i < size; i++)
            for(int j=0; j < size-1; j++) {
                String first = outList.get(j).getNome().toLowerCase();
                String next = outList.get(j+1).getNome().toLowerCase();
                
                if(next.compareTo(first) < 0) {
                    auxiliar = outList.get(j);
                    outList.set(j, outList.get(j+1));
                    outList.set(j+1, auxiliar);
                }
            }
        
        //  Atualizando nova lista de contatos
        contatos.setListaUsuarios(outList);
    }
    
    /**
     *  Carrega conversas
     * @param conv   ArrayList<Conversa>
     */
    private void loadConversas(ArrayList<Conversa> conv) {
        VBox box = new VBox();

        for (int i = 0; i < conv.size(); i++)
            box.getChildren().add(this.genConversScrolllist(conv.get(i)));

        contactScrollPane.setContent(box);
    }
    
    /**
     *  Evento do botão de troca de Usuario
     * @param event ActionEvent do JavaFX
     */
    @FXML
    private void handlerUserSwitch(ActionEvent event) {
        this.switchState = !this.switchState;
    }

    /**
     *  Evento do botão de adicionar contato
     * @param event ActionEvent do JavaFX
     */
    @FXML
    private void handleClickAddContact(MouseEvent event) {
        //  Area de Mensagem:
        VBox layout = msgContent;

        //  Limpando para renderizar box de contato;
        layout.getChildren().clear();
        
        //  Definindo componentes
        //  TextField
        TextField nameTF = new TextField();
        TextField telTF = new TextField();
        TextField statusTF = new TextField();
        //  Defs of TextField
        nameTF.setMaxWidth(250);
        telTF.setMaxWidth(250);
        statusTF.setMaxWidth(250);
        
        // apenas numeros no telefone e no maximo 11 caracteres 
        telTF.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue,
                    String newValue) {
                if (!newValue.matches("\\d*")) {
                    telTF.setText(newValue.replaceAll("[^\\d]", ""));
                } else if (telTF.getLength() >= 12 ){
                    telTF.setText(telTF.getText().substring(0, 11));
                 }
            }
           
        });

        //  FileChooser
        FileChooser.ExtensionFilter imageFilter = new FileChooser.ExtensionFilter("Image File *.jpg, *.jpeg, *.png", "*.jpg", "*.jpeg", "*.png");
        FileChooser fotoChoose = new FileChooser();
        fotoChoose.getExtensionFilters().add(imageFilter);
        fotoChoose.setTitle("Selecione uma foto");
        
        // Button & Grid
        HBox buttonGrid = new HBox();
        Button adduser = new Button("Adicionar");
        Button cancel = new Button("Cancelar");
        Button fotoOpen = new Button("Selecionar");
        //Adicionando no grid
        buttonGrid.getChildren().add(adduser);
        buttonGrid.getChildren().add(cancel);
        
        //Button Events
        fotoOpen.setOnAction((e) -> {
            this.fileOpen = fotoChoose.showOpenDialog(new Stage());
        });
        
        adduser.setOnAction((e) -> {
            String name = nameTF.getText();
            String tel = telTF.getText();
            String status = statusTF.getText();
            
            // Tratamento de informações
            if(name.isEmpty()) {
                nameTF.setStyle("-fx-background-color: firebrick;");
                return;
            } else if(tel.isEmpty()) {
                telTF.setStyle("-fx-background-color: firebrick;");
                return;
            } else if(status.isEmpty()) {
                statusTF.setStyle("-fx-background-color: firebrick;");
                return;                    
            }
            
            //add user
            Usuario newUser;
            
            if(this.fileOpen == null)
                newUser = new Usuario(name, tel, status, "");
            else
                newUser = new Usuario(name, tel, status, this.fileOpen.toURI().toString());
            
            this.contatos.adicionarUsuario(newUser);
            //Reordena e Reload Contact List
            bubbleSortForSortContact();
            this.renderContactsList(this.contatos.getListaUsuarios());
            //Clearbox
            layout.getChildren().clear();
        });
        
        cancel.setOnAction((e) -> {
           layout.getChildren().clear(); 
        });

        //Adicionando componentes
        layout.getChildren().add(new Label("Nome do Contato:"));
        layout.getChildren().add(nameTF);
        layout.getChildren().add(new Label("Telefone do Contato:"));
        layout.getChildren().add(telTF);
        layout.getChildren().add(new Label("Status do Contato:"));
        layout.getChildren().add(statusTF);
        layout.getChildren().add(new Label("Selecione uma foto para o contato:"));
        layout.getChildren().add(fotoOpen);
        layout.getChildren().add(new Label("")); //Criando espaco vazio
        layout.getChildren().add(buttonGrid);
        return;
    }
    
    /**
     *  Evento da caixa de texto de busca
     * @param event KeyEvent do JavaFX
     */
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
                this.renderContactsList(contatos.getListaUsuarios());
                return;
            }
            
            for (int i = 0; i < contatos.getListaUsuarios().size(); i++) {
                Usuario user = contatos.getListaUsuarios().get(i);

                //Adiciona no ArrayList<Conversa> foundConv
                if(user.getNome().toLowerCase().contains(searchString)) 
                    foundUser.add(user);
            }
            
            this.renderContactsList(foundUser);
        }
    }

    /**
     *  Evento do botão de abrir janela de contatos
     * @param event ActionEvent do JavaFX
     */
    @FXML
    private void handlerButtonOpenContacts(ActionEvent event) {
        //  Faz com que o botão de adicionar conversa vire de cancelar e voltar.
        if(scrollActiveAction) {
            scrollActiveAction = !scrollActiveAction;
            buttonAddConv.setText("Começar nova conversa");
            this.loadConversas(conversas);

            return;
        } else scrollActiveAction = !scrollActiveAction;
        
        //  Renderiza Lista de Contatos
        this.renderContactsList(contatos.getListaUsuarios());
    }

    /**
     *  Evento do botão de enviar mensagem
     * @param event ActionEvent do JavaFX
     */
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
        
        for (int i = 0; i <= 4; i++) {
            Timer timer = new Timer();
            Mensagem refMsg = activeConv.retornaUltimaMensagem();
            TimerTask changeStatus = new TimerTask() {
                public void run() {
                    //The task you want to do       
                    refMsg.setStatus(refMsg.getStatusIndex() + 1);
                }
            };
            
            int tim = 500 * i;
            
            timer.schedule(changeStatus,tim);
        }
        
        msgContent.getChildren().clear();
        VBox struct = this.renderConvStructure(activeConv);
        msgScroll.setContent(struct);
        msgScroll.vvalueProperty().bind(struct.heightProperty());
        msgTextArea.clear();
        
        bubbleSortForSortConvers();
    }

    /**
     *  Evento de Clicar no contato
     * @param event ActionEvent do JavaFX
     */
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
        
        // Caso não, inicia conversa no ponto 0 para abrir no topo da lista de conversas. E renderiza.
        conversas.add(0, new Conversa(this.selfUser, (Usuario) DOM.getUserData()));
        activeConv = conversas.get(0);
        msgScroll.setContent(this.renderConvStructure(conversas.get(0)));
        
        // Reseta janela lateral para lista de conversas
        this.handlerButtonOpenContacts(new ActionEvent()); //Inicia o evento para alterar e renderizar o correto.
    }

    /**
     *  Evento de acão do botão quando o Conversa é clicada. Insere todas as mensagens da conversa no container de mensagens
     * @param event MouseEvent do JavaFX
     */
    @FXML
    private void handleClickConv(MouseEvent event) {
        /* slecionando o conyainer de conteúdo*/
        StackPane content = (StackPane) event.getSource();

        /* selecionando informacoes da conversa e coloca como coversa atual*/
        activeConv = (Conversa) content.getUserData();
        
        VBox struct = this.renderConvStructure(activeConv);
        msgScroll.setContent(struct);
        msgScroll.vvalueProperty().bind(struct.heightProperty());
    }
    
    /*
     *                  _|_
     *                   |
     *               .-'''''-.
     *            .-'    '-.
     *         .-'  :::::_:::::  '-.
     *     ___/ ==:...:::-:::...:== \___
     *    /_____________________________\
     *  ':'-._________________________.-'_
     *   ':::\ @-,`-[-][-^-][-]-`,-@ / _| |_
     *    '::| .-------------------. ||_ @ _|
     *     ::|=|*                 *|=|'.| |
     *     ':| |'  jaz aqui nossa '| |::.^|
     *     _:|=|'  querida função '|=|::::::.
     *   _| || |'  genConversas() '| |:::::::.
     *  |_   |=|'16eb5af _ 00f07f2'|=|':::::.
     *    | ||=|'      _( )_      '|=|':::::.
     *    | || |' (   (_ ~ _)   ) '| | ':::'
     *    |^||=|*  )    (_)    (  *|=| '::'
     *       | '-------------------' .::::'
     *       |_____________________.::::::'
     *     .'___________________.::::::''
     *     |_______________.::::'':::'''
     *   .'_____________.::::::''::::''
     *              .:::'''' ... .'::::'
     *           .:::::''':.   .:::::'
     */

    /**
     *  Inicializador do Controllador e do Stage do JavaFx
     * @param url   URL do arquivo de Interface FXML carregado
     * @param rb    ResourceBundle do javaFX para manipulação do stagio
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Action da Troca de Usuario
        userSwitch.setOnAction(this::handlerUserSwitch);

        //  Action do botão de Envio
        send.setOnAction(this::handleButtonSendMsg);

        // Action de Adicionar novo Contato/Conversa.
        buttonAddConv.setOnAction(this::handlerButtonOpenContacts);

        //  Action de Buscar em Conversas;
        searchField.setOnKeyReleased(this::handlerButtonSearch);

        //  Definições de Inicio

        /* Preenchimento de foto default para o topo */
        //topoImage.setFill(new ImagePattern(defaultFoto, 0, 0, 1, 1, true));
    }
    
    /**
     *  Set Objetos de contato
     * @param cont Contatos objeto
     */
    public void setContacts(Contatos cont) {
        this.contatos = cont;
    }
    
    /**
     *  Set ArrayList de conversas
     * @param conv  ArrayList<Conversa>
     */
    public void setConversas(ArrayList<Conversa> conv) {
        this.conversas = conv;
        
        //  Organiza lista de mensagens em relação a hora da msg
        bubbleSortForSortConvers();
        this.loadConversas(this.conversas);
    }
    
    /**
     *  Get Objetos de contato
     * @return Contatos
     */
    public Contatos getContacts() {
        return this.contatos;
    }
    
    /**
     *  Get Lista de Conversas
     * @return ArrayList<Conversa>
     */
    public ArrayList<Conversa> getConversas() {
        return this.conversas;
    }
    
    /**
     *  Set MySelf usuario utilizador
     * @param user  Usuario object
     */
    public void setMySelf(Usuario user) {
        this.selfUser = user;
        
        // Dados do usuario
        mySelfStatus.setText(user.getStatus());
        mySelfName.setText(user.getNome());
    }
}
