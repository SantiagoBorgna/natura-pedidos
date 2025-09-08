package app.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    private static final String URL = "jdbc:mysql://127.0.0.1:3306/natura_pedidos";

    private static final String USUARIO = "santi";

    private static final String PASS = "";

    public static Connection getConnection() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            return DriverManager.getConnection(
                    URL,
                    USUARIO, PASS
            );
        } catch (ClassNotFoundException e) {
            throw new SQLException("No se encontró el driver MySQL", e);
        }
    }
}
