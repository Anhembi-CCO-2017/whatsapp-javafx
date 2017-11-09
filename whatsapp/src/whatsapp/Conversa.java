package whatsapp;

import java.util.ArrayList;

public class Conversa {
    
    private static ArrayList<Mensagem>listaMensagens = new ArrayList<>();
    private String contato;
    private Usuario user1;
    private Usuario user2;
    private String onlineUltimaVez;

    public Conversa(String contato, String onlineUltimaVez) {
        this.contato = contato;
        this.onlineUltimaVez = onlineUltimaVez;
    }

    public Conversa() {
    }
    

    public static ArrayList<Mensagem> getListaMensagens() {
        return listaMensagens;
    }

    public static void setListaMensagens(ArrayList<Mensagem> listaMensagens) {
        Conversa.listaMensagens = listaMensagens;
    }

    public String getContato() {
        return contato;
    }

    public void setContato(String contato) {
        this.contato = contato;
    }

    public String getOnlineUltimaVez() {
        return onlineUltimaVez;
    }

    public void setOnlineUltimaVez(String onlineUltimaVez) {
        this.onlineUltimaVez = onlineUltimaVez;
    }
    
    
    public void addMensagem(ArrayList<Mensagem>listaMensagens, String autor, Mensagem mensagem){
    
        listaMensagens.add(mensagem);
    }
    
    public String buscarMensagem(ArrayList<Mensagem>listaMensagens, String palavra){
    
        String saida = "";
        for (Mensagem msg : listaMensagens) {
            String [] tokens = msg.getTexto().split(" ");
            if (tokens.equals(msg)) 
                saida = saida +msg.getTexto()+"\n";
            
        }
        
        return saida;
    }
    public String retornarMensagens(){
    
        String saida = "";
        
        for (Mensagem msg : listaMensagens) {
        
            saida = saida + msg.getTexto()+"\n";
        }
        
        return saida;
    }
}
