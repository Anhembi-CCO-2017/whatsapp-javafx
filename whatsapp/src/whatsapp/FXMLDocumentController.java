/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package whatsapp;

import java.net.URL;
import java.util.Random;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;

/**
 *
 * @author googs
 */
public class FXMLDocumentController implements Initializable {
    
    @FXML
    private Button send;
    @FXML
    private ScrollPane contactScroll;
    @FXML
    private ScrollPane msgScroll;
    
    @FXML
    private VBox content;
    @FXML
    private VBox msgContent;

    @FXML
    private void handleButtonAction(ActionEvent event) {
        Random r = new Random();
        GridPane gpp = new GridPane();
        gpp.setStyle("-fx-border-width: 0 0 1 0 solid; -fx-border-color: #ccc; -fx-background-color:rgb("+ r.nextInt(255)+"," + r.nextInt(255) + "," + r.nextInt(255)+"); -fx-min-height: 50; ");
        msgContent.getChildren().add(gpp);
    }
    
    @FXML
    private void addMsg(ActionEvent event) {
        System.out.println("You clicked me!");
        Random r = new Random();
        content.getChildren().add(new Rectangle(130, 50, Color.rgb(r.nextInt(255), r.nextInt(255), r.nextInt(255))));
    }

    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        send.setOnAction(this::handleButtonAction);
       
        msgScroll.setContent(msgContent);
       
            
//        ImagePattern imgptr = new ImagePattern(img);
//        tt.setFill(imgptr);
//        String image = FXMLDocumentController.class.getResource("imgs/foto.jpg").toExternalForm();
//        tt.setStyle("-fx-background-image: url('" + image + "');");
    }    
    
}
