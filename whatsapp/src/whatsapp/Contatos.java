package whatsapp;

import java.util.ArrayList;

/**
 *
 * @author Lucas Golino, Thiago Almeida, Matheus Eli, Gabriel Henrique, Gabriel Forster
 */
public class Contatos {
    
    private ArrayList<Usuario>listaUsuarios = new ArrayList<>();

    /**
     *  Get a lista de usuarios nos Contatos
     * @return ArrayList<Usuario>   arraylist com todos Usuarios.
     */
    public ArrayList<Usuario> getListaUsuarios() {
        return listaUsuarios;
    }
    
    /**
     *  Get um usuario especifico
     * @param i Index do usuario na lista
     * @return Usuario  objeto do Usuario.
     */
    public Usuario getUser(int i) {
        return listaUsuarios.get(i);
    }

    /**
     *  Set lista de Usuario
     * @param listaUsuarios ArrayList<Usuario>
     */
    public void setListaUsuarios(ArrayList<Usuario> listaUsuarios) {
        this.listaUsuarios = listaUsuarios;
    }
    
    /**
     *  Adiciona um usuario ao ArrayList
     * @param usuario   Object Usuario
     */
    public void adicionarUsuario(Usuario usuario) {
        listaUsuarios.add(usuario);
    }
    
    /**
     * Gera uma String com informações de usuario.
     * @return String   string formatada com todos usuarios
     */
    public String listarUsuario(){
        String saida = "";

        for (Usuario usuario: listaUsuarios) {
            saida += usuario.toString()+"\n";
        }
        
        return saida;
    }
}
