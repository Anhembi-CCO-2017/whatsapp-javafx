package whatsapp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Usuario {

    private String nome;
    private String status;
    private String telefone;
    private Date ultimavezonline = new Date();
    
    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }
    
    public Usuario() {
    }

    public Usuario(String nome, String status) {
        this.nome = nome;
        this.status = status;
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

    public void setUltimavezonline(Date ultimavezonline) {
        
        this.ultimavezonline = ultimavezonline;
    }

    public String getUltimavezonline() {
        
        DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
        return dateFormat.format(ultimavezonline);
    }
    
    public Date GetDateObject(){
    
        return this.ultimavezonline;
    }
    
    @Override
    public String toString(){
    
        return "Nome: "+nome+" - Status: "+status+" - Ultima vez Online: "+ultimavezonline;
    }
}
