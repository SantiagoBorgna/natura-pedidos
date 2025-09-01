package app;

import app.dao.DBConnection;

import java.sql.Connection;

public class App {
    //static Image logo = Toolkit.getDefaultToolkit().getImage(App.class.getResource("/logo arca.png"));

    //static JTable listTable;

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