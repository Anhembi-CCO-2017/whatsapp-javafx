package whatsapp;   

import java.util.Calendar;

public class Mensagem {

    private Usuario emissor;
    private String texto;
    private String status;
    private Calendar dataHora;

    public Mensagem(Usuario emissor, String texto, Calendar dataHora) {
        this.emissor = emissor;
        this.texto = texto;
        this.dataHora = dataHora;
        setStatus(0);
        
    }

    public void setStatus(int i){
        String status[] = {"Está sendo enviada","foi enviada",
            "Foi recebida","foi lida"};
        if(i>=0 && i<=3)
      this.status=status[i];
    }

    public Usuario getEmissor() {
        return emissor;
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


}
