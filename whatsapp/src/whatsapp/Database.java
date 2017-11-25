package whatsapp;

import java.io.File;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Calendar;
import java.sql.Date;
import java.util.GregorianCalendar;

public class Database {
    private Contatos contatos = new Contatos();
    private ArrayList<Conversa> conversas = new ArrayList<>();
    private Connection conn;
    public Usuario mySelf = new Usuario("me", "to bem");
    
    public Database() {
        Database.createDatabase();
        
        this.conn = this.connect();
        this.loadUsers();
        this.loadConv();
    }
    
    private Connection connect() {
        // SQLite connection string
        String url = "jdbc:sqlite:./database.db";
        Connection conn = null;
        
        try {
            conn = DriverManager.getConnection(url);
            System.out.println("Sucessfull connection on DB");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        
        return conn;
    }
    
    public void save(Contatos cnt, ArrayList<Conversa> cnv) {
        Database.truncateTables(this.conn);
        this.contatos = cnt;
        this.conversas = cnv;
        
        this.saveUsers();
        this.saveConv();
    }
    
    private void saveUsers() {
        ArrayList<Usuario> users = this.contatos.getArrayListUsers();
        for (int i = 0; i < users.size(); i++) {
            Usuario alvo = users.get(i);
            
            this.queryUser(alvo.getNome(), alvo.getStatus(), alvo.getTelefone(), alvo.getImageURL(), alvo.getUltimaVezOnlineObject().getTime());
        }
    }
    
    private void queryUser(String nome, String status, String telefone, String img, long lastTime) {
        String sql = "INSERT INTO usuario(nome, status, telefone, img, lasttime) VALUES(?,?,?,?,?)";
 
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, nome);
            pstmt.setString(2, status);
            pstmt.setString(3, telefone);
            pstmt.setString(4, img);            
            pstmt.setDate(5, new Date(lastTime));

            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    
    private void saveConv() {
        for (int i = 0; i < this.conversas.size(); i++) {
            Conversa conv = this.conversas.get(i);
            this.queryConv(i, conv);
        }
    }
    
    private void queryConv(int index, Conversa conv) {
        String sql = "INSERT INTO conversas(usuario) VALUES(?)";
 
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            for (int i = 0; i < this.contatos.getArrayListUsers().size(); i++) {
                if (conv.getUser(1).equals(this.contatos.getArrayListUsers().get(i))) {
                    pstmt.setInt(1, i);
                    break;
                }
            }

            pstmt.executeUpdate();
            
            for (int i = 0; i < conv.getListaMensagens().size(); i++) {
                Mensagem m = conv.getListaMensagens().get(i);
                int emissor = 0;
                
                if (m.getEmissor().hashCode() == conv.getUser(1).hashCode())
                    emissor = 1;
                
                this.queryMsg(index+1, m.getTexto(), m.getStatusIndex(), new Date(m.getData().getTime()), emissor);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    
    private void queryMsg(int convID, String texto, int status, Date data, int emissor) {
        String sql = "INSERT INTO mensagem(texto, status, data, conv, emissor) VALUES(?,?,?,?,?)";
 
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, texto);
            pstmt.setInt(2, status);
            pstmt.setDate(3, data);
            pstmt.setInt(4, convID);     
            pstmt.setInt(5, emissor);

            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    
    private void loadUsers() {
        String sql = "SELECT id, nome, status, telefone, img, lasttime FROM usuario ORDER BY id ASC";
        
        try(Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)){
            while (rs.next()) {
                contatos.adicionarUsuario(new Usuario(rs.getString("nome"), rs.getString("status"), rs.getString("telefone"), rs.getString("img")));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    
    private void loadConv() {
        String sql = "SELECT id, usuario FROM conversas ORDER BY id ASC";
        
        try(Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)){
            while (rs.next()) {
               // AQUI CARREGA CONVERSA POR CONVERSA
               Conversa conv = new Conversa(mySelf, contatos.getUser(rs.getInt("usuario")));
               
               String getMsgSQL = "SELECT id, texto, status, data, emissor FROM mensagem WHERE conv = ? ORDER BY id ASC";
               
               try(Statement stmt2 = conn.createStatement(); PreparedStatement pstmt = conn.prepareStatement(getMsgSQL)) {
                    pstmt.setInt(1, rs.getInt("id"));
                    
                    ResultSet msg = pstmt.executeQuery();
                    while (msg.next()) {
                        Calendar calendar = new GregorianCalendar();
                        calendar.setTime(msg.getDate("data"));
                        Usuario emissor;

                        // Usuario que enviou mySelf ou outro
                        if (msg.getInt("emissor") == 0) {
                            emissor = mySelf; 
                        } else {
                            emissor = contatos.getUser(rs.getInt("id"));
                        }

                        conv.addMensagem(new Mensagem(emissor, msg.getString("texto"), calendar, msg.getInt("status")));
                    }
                    
                    this.conversas.add(conv);
               } catch (SQLException e) {
                System.out.println(e.getMessage());
               }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    
    public Contatos getContacts() {
        return this.contatos;
    }
    
    public ArrayList<Conversa> getConversa() {
        return this.conversas;
    }
    
    
    
    
    
    
    
    
    
    
    
    
    public static void createDatabase() {
        String url = "jdbc:sqlite:./database.db";
        File f = new File("database.db");

        if (!f.exists()) {
            try (Connection conn = DriverManager.getConnection(url)) {
                if (conn != null) {
                    DatabaseMetaData meta = conn.getMetaData();
                    Database.createDefaultTables(conn);
                    System.out.println("The driver name is " + meta.getDriverName());
                    System.out.println("A new database has been created.");
                }
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    public static void truncateTables(Connection conn) {
        try(Statement stmt = conn.createStatement()) {
            stmt.execute("DELETE FROM mensagem;");
            stmt.execute("DELETE FROM conversas;");
            stmt.execute("DELETE FROM usuario;");
            stmt.execute("DELETE FROM sqlite_sequence;");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void createDefaultTables(Connection conn) {
        String sql =    "CREATE TABLE usuario (" +
                            "id integer PRIMARY KEY AUTOINCREMENT," +
                            "nome varchar," +
                            "status text," +
                            "telefone text," +
                            "img text," +
                            "lasttime datetime" +
                        ");";
        String sql2 =   "CREATE TABLE conversas (" +
                            "id integer PRIMARY KEY AUTOINCREMENT," +
                            "usuario integer" +
                        ");";
        String sql3 =   "CREATE TABLE mensagem (" +
                            "id integer PRIMARY KEY AUTOINCREMENT," +
                            "texto text," +
                            "status integer," +
                            "data datetime," +
                            "conv integer," +
                            "emissor integer" +
                        ");";

        try(Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
            stmt.execute(sql2);
            stmt.execute(sql3);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}
