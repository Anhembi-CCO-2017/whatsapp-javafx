/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package whatsapp;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
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
    private Rectangle tt;
    
    @FXML
    private void handleButtonAction(ActionEvent event) {
        System.out.println("You clicked me!");
    } 
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        send.setOnAction(this::handleButtonAction);
        Image img = new Image("foto.jpg");
        tt.setFill(new ImagePattern(img));

//        ImagePattern imgptr = new ImagePattern(img);
//        tt.setFill(imgptr);
//        String image = FXMLDocumentController.class.getResource("imgs/foto.jpg").toExternalForm();
//        tt.setStyle("-fx-background-image: url('" + image + "');");
    }    
    
}
