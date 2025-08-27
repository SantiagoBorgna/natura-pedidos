package app;

import app.dao.DBConnection;

import java.sql.Connection;

public class App {

    //static String url = "jdbc:mysql://127.0.0.1:3306/arcadb";

    //static String usuario = "santi";

    //static String contrasena = "";

    //static Image logo = Toolkit.getDefaultToolkit().getImage(App.class.getResource("/logo arca.png"));

    //static JTable listTable;

    //static DecimalFormat formato1 = new DecimalFormat("#.00");

    public static void main(String[] args) {
        try (Connection conn = DBConnection.getConnection()) {
            if (conn != null) {
                System.out.println("Conexión exitosa a SQLite!");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}