package app.dao;

import app.model.Ciclo;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class CicloDAO {

    public void insertarCiclo(Ciclo ciclo) {
        String sql = "INSERT INTO ciclos (numero, fecha_inicio, fecha_fin) VALUES (?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setInt(1, ciclo.getNumero());
            pstmt.setString(2, ciclo.getFechaInicio().toString());
            pstmt.setString(3, ciclo.getFechaFin().toString());
            pstmt.executeUpdate();

            try (ResultSet rs = pstmt.getGeneratedKeys()) {
                if (rs.next()) {
                    ciclo.setId(rs.getInt(1));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Ciclo> listarCiclos() {
        List<Ciclo> lista = new ArrayList<>();
        String sql = "SELECT * FROM ciclos ORDER BY numero";
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Ciclo ciclo = new Ciclo(
                        rs.getInt("numero"),
                        LocalDate.parse(rs.getString("fecha_inicio")),
                        LocalDate.parse(rs.getString("fecha_fin"))
                );
                ciclo.setId(rs.getInt("id"));
                lista.add(ciclo);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    public Ciclo obtenerPorNumero(int numero) {
        String sql = "SELECT * FROM ciclos WHERE numero = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, numero);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                Ciclo ciclo = new Ciclo(
                        rs.getInt("numero"),
                        LocalDate.parse(rs.getString("fecha_inicio")),
                        LocalDate.parse(rs.getString("fecha_fin"))
                );
                ciclo.setId(rs.getInt("id"));
                return ciclo;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void actualizarCiclo(Ciclo ciclo) {
        String sql = "UPDATE ciclos SET fecha_inicio = ?, fecha_fin = ? WHERE id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, ciclo.getFechaInicio().toString());
            pstmt.setString(2, ciclo.getFechaFin().toString());
            pstmt.setInt(3, ciclo.getId());

            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
