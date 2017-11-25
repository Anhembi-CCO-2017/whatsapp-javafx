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
import java.util.Date;
import java.util.GregorianCalendar;

public class Database {
    private Contatos contatos = new Contatos();
    private ArrayList<Conversa> conversas = new ArrayList<>();
    private Connection conn;
    
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
    
    private void loadUsers() {
        String sql = "SELECT id, nome, status, telefone, img, lasttime FROM usuario";
        
        try(Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)){
            while (rs.next()) {
                contatos.adicionarUsuario(new Usuario(rs.getString("nome"), rs.getString("status"), rs.getString("telefone"), rs.getString("img")));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    
    private void loadConv() {
        String sql = "SELECT id, usuario FROM conversas";
        Usuario mySelf = new Usuario("me", "to bem");
        
        try(Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)){
            while (rs.next()) {
               // AQUI CARREGA CONVERSA POR CONVERSA
               Conversa conv = new Conversa(mySelf, contatos.getUser(rs.getInt("usuario") - 1));
               
               String getMsgSQL = "SELECT id, texto, status, data, emissor FROM mensagem WHERE conv = ?";
               
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
                            emissor = contatos.getUser(rs.getInt("id") - 1);
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
            stmt.execute("TRUNCATE TABLE mensagem; TRUNCATE TABLE conversas; TRUNCATE TABLE usuario;");
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
