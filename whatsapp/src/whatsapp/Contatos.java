
package whatsapp;

import java.util.ArrayList;

public class Contatos {
    
    private static ArrayList<Usuario>listaUsuarios = new ArrayList<>();

    public ArrayList<Usuario> getListaUsuarios() {
        return listaUsuarios;
    }
    
    public Usuario getUser(int i) {
        return listaUsuarios.get(i);
    }

    public void setListaUsuarios(ArrayList<Usuario> listaUsuarios) {
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
