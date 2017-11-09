package whatsapp;

import java.util.Calendar;

public class Mensagem {

private String emissor;
private String texto;
private String [] status = {"Está sendo enviado","Foi enviada","Foi recebida","Foi lida"};
Calendar dataHora = Calendar.getInstance();

    public Mensagem(String emissor, String texto, Calendar dataHora) {
        this.emissor = emissor;
        this.texto = texto;
        this.dataHora = dataHora;
    }

    public void setStatus(String[] status) {
        this.status = status;
    }

    public String getEmissor() {
        return emissor;
    }

    public String getTexto() {
        return texto;
    }

    public String[] getStatus() {
        return status;
    }

    public Calendar getDataHora() {
        return dataHora;
    }


}
