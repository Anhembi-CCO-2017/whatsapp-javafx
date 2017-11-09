package whatsapp;
import java.text.DateFormat;

public class Usuario {

    private String nome;
    private String status;
    private String telefone;

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }
    private DateFormat ultimavezonline = DateFormat.getDateTimeInstance();

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

    public DateFormat getUltimavezonline() {
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
