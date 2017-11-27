package whatsapp;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 *
 * @author Lucas Golino, Thiago Almeida, Matheus Eli, Gabriel Henrique, Gabriel Forster
 */
public class Conversa {
    
    // primeiro usuario SEMPRE definido como "VOCE"
    private ArrayList<Usuario> user = new ArrayList<>();
    private ArrayList<Mensagem>listaMensagens = new ArrayList<>();
    
    public Conversa(Usuario user1, Usuario user2) {
        this.user.add(user1);
        this.user.add(user2);
    }
    
    /**
     *  Get um usuario da conversa pelo index
     * @param i Index do usuario na lista da conversa
     * @return Usuario  objeto do Usuario.
     */
    public Usuario getUser(int index) {
        return this.user.get(index);
    }

    /**
     *  Get a lista de mensagens da Conversa
     * @return ArrayList<Mensagem>  ArrayList de mensagens.
     */
    public ArrayList<Mensagem> getListaMensagens() {
        return this.listaMensagens;
    }
    
    /**
     *  Set da lista de mensagens da conversa
     * @param msg ArrayList de Mensagens
     */
    public void setListaMensagens(ArrayList<Mensagem> msg) {
        this.listaMensagens = msg;
    }
    
    /**
     *  Adiciona uma mensagem a lista de mensagens da conversa.
     * @param userIndex index do usuario emissor da mensagem
     * @param mensagem  String da mensagem enviada.
     */
    public void addMensagem(int userIndex, String mensagem){
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(new Date());
        
        Mensagem msg = new Mensagem(this.user.get(userIndex), mensagem, calendar);
        this.listaMensagens.add(msg);
    }
    
    /**
     *  Adiciona uma mensagem a lista de mensagens da conversa.
     * @param msg Objeto mensagem
     */
    public void addMensagem(Mensagem msg){
        this.listaMensagens.add(msg);
    }
    
    /**
     *  Busca mensagens na conversa.
     * @param busca String de busca de mensagem
     * @return ArrayList<Mensagem>  ArrayList de mensagens contendo a busca.
     */
    public ArrayList<Mensagem> buscarMensagem(String busca){
        ArrayList<Mensagem>returnData = new ArrayList<>();
        
        String[] buscaData = busca.toLowerCase().split(" ");
        
        for (Mensagem msg : this.listaMensagens) {
            String[] msgData = msg.getTexto().toLowerCase().split(" ");
            
            if(searchWords(msgData, buscaData))
                returnData.add(msg);
        }
        
        return returnData;
    }
    
    /**
     *  Metodo de busca de palavras na String.
     * @param msgData String da mensagem a ser buscada
     * @param buscaData  String da busca para encontrar
     * @return Boolean  Booleano caso encontre ou não
     */
    private static boolean searchWords(String[] msgData, String[] buscaData) {
        for (String word : buscaData)
            for (String data : msgData)
                if (data.equals(word) || data.contains(word))
                    return true;
        
        return false;
    }

    /**
     *  Retorna um Objeto Mensagem da lista pelo Index.
     * @param index index da mensagem no ArrayList
     * @return Mensagem objeto da mensagem no index
     */
    public Mensagem retornarMensagem(int index){
        return this.listaMensagens.get(index);
    }
    
    /**
     *  Retorna o ultimo Objeto Mensagem da ArrayList.
     * @return Mensagem objeto da ultima mensagem no Arraylist
     */
    public Mensagem retornaUltimaMensagem(){
        if(this.listaMensagens.isEmpty()) {
            GregorianCalendar calendar = new GregorianCalendar();
            calendar.setTime(new Date(1, 1, 1));

            return new Mensagem(this.user.get(0), "", calendar);
        }
        
        return this.listaMensagens.get(this.listaMensagens.size() - 1);
    }
    
    /**
     *  Retorna o texto de uma Mensagem do ArrayList
     * @param index index da mensagem no ArrayList
     * @return String   texto da mensagem.
     */
    public String retornarMensagemString(int index){
        return listaMensagens.get(index).getTexto();
    }
}
