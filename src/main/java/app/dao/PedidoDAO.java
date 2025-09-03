package app.dao;

import app.model.Pedido;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PedidoDAO {
    private static final String URL = "jdbc:sqlite:database/natura_pedidos.db";

    public void agregarPedido(Pedido pedido) {
        String sql = "INSERT INTO pedidos (cliente, producto, codigo, cantidad, precio_unitario, precio_total) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setString(1, pedido.getCliente());
            pstmt.setString(2, pedido.getProducto());
            pstmt.setString(3, pedido.getCodigo());
            pstmt.setInt(4, pedido.getCantidad());
            pstmt.setDouble(5, pedido.getPrecioUnitario());
            pstmt.setDouble(6, pedido.getPrecioTotal());

            pstmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Pedido> listarPedidos() {
        List<Pedido> pedidos = new ArrayList<>();
        String sql = "SELECT * FROM pedidos";

        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Pedido pedido = new Pedido(
                        rs.getString("Cliente"),
                        rs.getString("producto"),
                        rs.getString("codigo"),
                        rs.getInt("cantidad"),
                        rs.getDouble("precio_unitario")
                );
                pedido.setId(rs.getInt("id")); // seteamos el ID aparte
                pedidos.add(pedido);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return pedidos;
    }

    public double calcularTotal() {
        String sql = "SELECT SUM(precio_total) AS total FROM pedidos";
        try (Connection conn = DriverManager.getConnection(URL);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            return rs.getDouble("total");

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public void actualizarPedido(Pedido pedido) {
        String sql = "UPDATE pedidos SET cliente=?, producto=?, codigo=?, cantidad=?, precio_unitario=?, precio_total=? WHERE id=?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, pedido.getCliente());
            pstmt.setString(2, pedido.getProducto());
            pstmt.setString(3, pedido.getCodigo());
            pstmt.setInt(4, pedido.getCantidad());
            pstmt.setDouble(5, pedido.getPrecioUnitario());
            pstmt.setDouble(6, pedido.getPrecioTotal());
            pstmt.setInt(7, pedido.getId());

            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void eliminarPedido(int id) {
        String sql = "DELETE FROM pedidos WHERE id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


}
