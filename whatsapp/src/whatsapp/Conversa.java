package whatsapp;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class Conversa {
    
    // primeiro usuario SEMPRE definido como "VOCE"
    private ArrayList<Usuario> user = new ArrayList<>();
    private ArrayList<Mensagem>listaMensagens = new ArrayList<>();

    public Conversa(Usuario user1, Usuario user2) {
        user.add(user1);
        user.add(user2);
    }
    
    public Usuario getUser(int index) {
        return user.get(index);
    }

    public ArrayList<Mensagem> getListaMensagens() {
        return listaMensagens;
    }
    
    public void addMensagem(int userIndex, String mensagem){
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(new Date());
        
        Mensagem msg = new Mensagem(user.get(userIndex), mensagem, calendar);
        listaMensagens.add(msg);
    }
    
    public ArrayList<Mensagem> buscarMensagem(String busca){
        ArrayList<Mensagem>returnData = new ArrayList<>();
        
        String[] buscaData = busca.split(" ");
        
        for (Mensagem msg : listaMensagens) {
            String[] msgData = msg.getTexto().split(" ");
            
            if(searchWords(msgData, buscaData))
                returnData.add(msg);
        }
        
        return returnData;
    }
    
    private static boolean searchWords(String[] msgData, String[] buscaData) {
        for (String word : buscaData)
            for (String data : msgData)
                if (data.equals(word) || data.contains(word))
                    return true;
        
        return false;
    }

    public Mensagem retornarMensagem(int index){
        return listaMensagens.get(index);
    }
    
    public String retornarMensagemString(int index){
        return listaMensagens.get(index).getTexto();
    }
}
