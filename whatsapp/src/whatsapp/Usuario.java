package whatsapp;

import com.sun.javafx.fxml.builder.URLBuilder;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import javafx.scene.image.Image;

public class Usuario {

    private String nome;
    private String status;
    private String telefone;
    private Image image;
    private String imgURL;
    private Date ultimavezonline = new Date();

    public Usuario(String telefone) {
        this.telefone = telefone;
    }

    public Usuario(String nome, String status) {
        this.nome = nome;
        this.status = status;
    }
    
    public Usuario(String nome, String status, String image) {
        this.nome = nome;
        this.status = status;
        this.imgURL = image;
        this.image = new Image(getClass().getResourceAsStream(image));
    }
    
    public Usuario(String nome, String status, String telefone, String image) {
        this.nome = nome;   
        this.status = status;
        this.imgURL = image;
        if(!image.isEmpty()) this.image = new Image(image);
        this.telefone = telefone;
    }

    public String getNome() {
        return nome;
    }
    
    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setImage(String image) {
        this.image = new Image(getClass().getResourceAsStream(image));
    }
    
    public Image getImage() {
        return image;
    }
    
    public String getImageURL() {
        return imgURL;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public void setUltimaVezOnline(Date ultimavezonline) {
        
        this.ultimavezonline = ultimavezonline;
    }

    public String getUltimaVezOnline() {
        
        DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
        return dateFormat.format(ultimavezonline);
    }
    
    public Date getUltimaVezOnlineObject() {
        return ultimavezonline;
    }
    
    public Date getDateObject(){
    
        return this.ultimavezonline;
    }
    
    @Override
    public String toString(){
    
        return "Nome: "+nome+" - Status: "+status+" - Ultima vez Online: "+ultimavezonline;
    }
}
