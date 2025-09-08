package app.dao;

import app.model.Pedido;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PedidoDAO {

    public void agregarPedido(Pedido pedido) {
        String sql = "INSERT INTO pedidos (cliente, producto, codigo, cantidad, precio_unitario, precio_total, puntos, ciclo_id) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, pedido.getCliente());
            pstmt.setString(2, pedido.getProducto());
            pstmt.setString(3, pedido.getCodigo());
            pstmt.setInt(4, pedido.getCantidad());
            pstmt.setDouble(5, pedido.getPrecioUnitario());
            pstmt.setDouble(6, pedido.getPrecioTotal());
            pstmt.setInt(7, pedido.getPuntos());
            pstmt.setInt(8, pedido.getCicloId());

            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Pedido> listarPedidos(int cicloId) {
        List<Pedido> lista = new ArrayList<>();
        String sql = "SELECT * FROM pedidos WHERE ciclo_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, cicloId);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                Pedido p = new Pedido(
                        rs.getString("cliente"),
                        rs.getString("producto"),
                        rs.getString("codigo"),
                        rs.getInt("cantidad"),
                        rs.getDouble("precio_unitario"),
                        rs.getInt("puntos")
                );
                p.setId(rs.getInt("id"));
                p.setCicloId(rs.getInt("ciclo_id"));
                lista.add(p);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    public double calcularTotal() {
        String sql = "SELECT SUM(precio_total) AS total FROM pedidos";
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            return rs.getDouble("total");

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public void actualizarPedido(Pedido pedido) {
        String sql = "UPDATE pedidos SET cliente=?, producto=?, codigo=?, cantidad=?, precio_unitario=?, precio_total=?, puntos=?, ciclo_id=? WHERE id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, pedido.getCliente());
            pstmt.setString(2, pedido.getProducto());
            pstmt.setString(3, pedido.getCodigo());
            pstmt.setInt(4, pedido.getCantidad());
            pstmt.setDouble(5, pedido.getPrecioUnitario());
            pstmt.setDouble(6, pedido.getPrecioTotal());
            pstmt.setInt(7, pedido.getPuntos());
            pstmt.setInt(8, pedido.getCicloId());
            pstmt.setInt(9, pedido.getId());

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
