package whatsapp;

import java.util.Calendar;
import java.util.Date;

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
        setStatus(0);
    }

    public Mensagem(Usuario emissor, String texto, Calendar dataHora, int status) {
        this.emissor = emissor;
        this.texto = texto;
        this.dataHora = dataHora;
        setStatus(status);
    }

    public void setStatus(int i){
        String status[] = {"Está sendo enviada","foi enviada",
            "Foi recebida","foi lida"};

        if(i>=0 && i<=3) {
            this.status=status[i];
            this.statusIndex = i;
        }
    }

    public Usuario getEmissor() {
        return emissor;
    }

    public int getStatusIndex() {
        return statusIndex;
    }


    public String getEmissorName() {
        return emissor.getNome();
    }

    public String getTexto() {
        return texto;
    }

    public String getStatus() {
        return status;
    }

    public Calendar getDataHora() {
        return dataHora;
    }
    
    public Date getData() {
        return dataHora.getTime();
    }

    public String getHora() {
        return dataHora.get(Calendar.HOUR) + ":" + dataHora.get(Calendar.MINUTE);
    }

}
