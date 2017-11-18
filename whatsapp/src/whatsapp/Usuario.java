package whatsapp;
import java.text.DateFormat;
import javafx.scene.image.Image;

public class Usuario {

    private String nome;
    private String status;
    private String telefone;
    private Image image;

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }
    private DateFormat ultimavezonline = DateFormat.getDateTimeInstance();

    public Usuario(String telefone) {
        this.telefone = telefone;
    }

    public Usuario(String nome, String status) {
        this.nome = nome;
        this.status = status;
    }

    public String getNome() {
        return nome;
    }
    
    public void setImage(String image) {
        this.image = new Image(getClass().getResourceAsStream(image));
    }
    
    public Image getImage() {
        return image;
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

    public DateFormat getUltimavezonline() {
        return ultimavezonline;
    }
    
    public DateFormat getFormatedLastOnline() {
        return ultimavezonline;
    }

    public void setUltimavezonline(DateFormat ultimavezonline) {
        this.ultimavezonline = ultimavezonline;
    }
    
    @Override
    public String toString(){
    
        return "Nome: "+nome+" - Status: "+status+" - Ultima vez Online: "+ultimavezonline;
    }
}
