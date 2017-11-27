package whatsapp;

import java.util.Calendar;
import java.util.Date;

/**
 *
 * @author Lucas Golino, Thiago Almeida, Matheus Eli, Gabriel Henrique, Gabriel Forster
 */
public class Mensagem {

    private Usuario emissor;
    private String texto;
    private String status;
    private Calendar dataHora;
    private int statusIndex;

    public Mensagem(Usuario emissor, String texto, Calendar dataHora) {
        this.emissor = emissor;
        this.texto = texto;
        this.dataHora = dataHora;
        this.setStatus(0);
    }

    public Mensagem(Usuario emissor, String texto, Calendar dataHora, int status) {
        this.emissor = emissor;
        this.texto = texto;
        this.dataHora = dataHora;
        this.setStatus(status);
    }

    /**
     *  Set Status da Mensagem pelo Index
     * @param i   Index da mensagem
     */
    public void setStatus(int i){
        String status[] = {"Está sendo enviada","foi enviada",
            "Foi recebida","foi lida"};

        if(i>=0 && i<=3) {
            this.status=status[i];
            this.statusIndex = i;
        }
    }

    /**
     *  Get o emissor da mensagem
     * @return Usuario   Objeto usuario
     */
    public Usuario getEmissor() {
        return emissor;
    }

    /**
     *  Get Status com valor do Index
     * @return Int   index de status
     */
    public int getStatusIndex() {
        return statusIndex;
    }

    /**
     *  Get nome do Emissor
     * @return String   nome do emissor
     */
    public String getEmissorName() {
        return emissor.getNome();
    }

    /**
     *  Get Texto da Mensagem
     * @return String
     */
    public String getTexto() {
        return texto;
    }

    /**
     *  Get Mensagem de Status
     * @return String   Mensagem de Status
     */
    public String getStatus() {
        return status;
    }

    /**
     *  Get Data e Hora de envio da mensagem
     * @return Calendar
     */
    public Calendar getDataHora() {
        return dataHora;
    }
    
    /**
     *  Get Data pelo objeto Calendar
     * @return Date
     */
    public Date getData() {
        return dataHora.getTime();
    }

    /**
     *  Get Hora formatada de envio
     * @return String
     */
    public String getHora() {
        return dataHora.get(Calendar.HOUR) + ":" + dataHora.get(Calendar.MINUTE);
    }

}
