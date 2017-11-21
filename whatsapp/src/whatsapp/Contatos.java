
package whatsapp;

import java.io.Serializable;
import java.util.ArrayList;

public class Contatos implements Serializable {
    
    private static ArrayList<Usuario>listaUsuarios = new ArrayList<>();

    public static ArrayList<Usuario> getListaUsuarios() {
        return listaUsuarios;
    }

    public static void setListaUsuarios(ArrayList<Usuario> listaUsuarios) {
        Contatos.listaUsuarios = listaUsuarios;
    }
    
    public void adicionarUsuario(Usuario usuario) {
        listaUsuarios.add(usuario);
    }
    
    public ArrayList<Usuario> getArrayListUsers() {
        return this.listaUsuarios;
    }
    
    public String listarUsuario(){
        String saida = "";

        for (Usuario usuario: listaUsuarios) {
            saida += usuario.toString()+"\n";
        }
        
        return saida;
    }
}
