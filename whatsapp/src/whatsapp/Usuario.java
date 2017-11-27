package whatsapp;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import javafx.scene.image.Image;

/**
 *
 * @author Lucas Golino, Thiago Almeida, Matheus Eli, Gabriel Henrique, Gabriel Forster
 */
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
    
    public Usuario(String nome, String status, String telefone, String image) {
        this.nome = nome;   
        this.status = status;
        this.imgURL = image;
        if(!image.isEmpty()) this.image = new Image(image);
        this.telefone = telefone;
    }

    /**
     *  Get nome do Usuario
     * @return String   Nome do Usuario
     */
    public String getNome() {
        return nome;
    }
    
    /**
     *  Set nome do Usuario
     * @param nome   String Nome do Usuario
     */
    public void setNome(String nome) {
        this.nome = nome;
    }

    /**
     *  Get Status do Usuario
     * @return String   Mensagem de Status
     */
    public String getStatus() {
        return status;
    }

    /**
     *  Get Status do Usuario
     * @param String   Mensagem de Status
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     *  Set imagem do usuario
     * @param String   Image URL
     */
    public void setImage(String image) {
        this.image = new Image(getClass().getResourceAsStream(image));
    }
    
    /**
     *  Get Status do Usuario
     * @return Image   Objeto imagem do usuario
     */
    public Image getImage() {
        return image;
    }
    
    /**
     *  Get Imagem URL
     * @return String   Url de imagem
     */
    public String getImageURL() {
        return imgURL;
    }

    /**
     *  Get Telefone o Usuario
     * @return String
     */
    public String getTelefone() {
        return telefone;
    }

    /**
     *  Set telefone do usuario
     * @param telefone   String telefone do usuario
     */
    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    /**
     *  Set ultima vez online
     * @param ultimavezonline   Objeto Date
     */
    public void setUltimaVezOnline(Date ultimavezonline) {
        
        this.ultimavezonline = ultimavezonline;
    }

    /**
     *  Get String formatada da ultima vez Online
     * @return String   Ultima vez Online
     */
    public String getUltimaVezOnline() {
        
        DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
        return dateFormat.format(ultimavezonline);
    }
    
    /**
     *  Get Objeto Date do ultima vez online
     * @return Date
     */
    public Date getUltimaVezOnlineObject() {
        return ultimavezonline;
    }
}
