package app.ui;

import app.dao.CicloDAO;
import app.model.Ciclo;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class CicloForm extends JDialog {

    private JTextField txtNumero;
    private JTextField txtFechaInicio;
    private JTextField txtFechaFin;
    private CicloDAO cicloDAO;
    private DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public CicloForm(JFrame parent, CicloDAO cicloDAO) {
        super(parent, true);
        this.cicloDAO = cicloDAO;

        setTitle("Crear Ciclo");
        setSize(350, 200);
        setLocationRelativeTo(parent);
        setLayout(new GridLayout(4, 2, 5, 5));

        add(new JLabel("Número de ciclo:"));
        txtNumero = new JTextField();
        add(txtNumero);

        add(new JLabel("Fecha inicio (dd/MM/yyyy):"));
        txtFechaInicio = new JTextField();
        add(txtFechaInicio);

        add(new JLabel("Fecha fin (dd/MM/yyyy):"));
        txtFechaFin = new JTextField();
        add(txtFechaFin);

        JButton btnGuardar = new JButton("Guardar");
        btnGuardar.addActionListener(e -> guardarCiclo());
        add(btnGuardar);

        JButton btnCancelar = new JButton("Cancelar");
        btnCancelar.addActionListener(e -> dispose());
        add(btnCancelar);
    }

    // Constructor edición
    public CicloForm(JFrame parent, CicloDAO cicloDAO, Ciclo ciclo) {
        super(parent, true);
        this.cicloDAO = cicloDAO;

        setTitle("Editar Ciclo");
        setSize(350, 250);
        setLocationRelativeTo(parent);
        setLayout(new GridLayout(4, 2, 5, 5));

        add(new JLabel("Número de ciclo:"));
        txtNumero = new JTextField();
        add(txtNumero);

        add(new JLabel("Fecha inicio (dd/MM/yyyy):"));
        txtFechaInicio = new JTextField();
        add(txtFechaInicio);

        add(new JLabel("Fecha fin (dd/MM/yyyy):"));
        txtFechaFin = new JTextField();
        add(txtFechaFin);

        JButton btnGuardar = new JButton("Guardar");
        btnGuardar.addActionListener(e -> guardarCiclo());
        add(btnGuardar);

        JButton btnCancelar = new JButton("Cancelar");
        btnCancelar.addActionListener(e -> dispose());
        add(btnCancelar);

        // Cargar datos en los campos
        txtNumero.setText(String.valueOf(ciclo.getNumero()));
        txtNumero.setEnabled(false);
        txtFechaInicio.setText(ciclo.getFechaInicio().format(dateFormatter));
        txtFechaFin.setText(ciclo.getFechaFin().format(dateFormatter));

        // Sobrescribir acción guardar
        for (ActionListener al : btnGuardar.getActionListeners()) {
            btnGuardar.removeActionListener(al);
        }
        btnGuardar.addActionListener(e -> actualizarCiclo(ciclo));
    }

    private void actualizarCiclo(Ciclo ciclo) {
        try {
            LocalDate fechaInicio = LocalDate.parse(txtFechaInicio.getText().trim(), dateFormatter);
            LocalDate fechaFin = LocalDate.parse(txtFechaFin.getText().trim(), dateFormatter);

            ciclo.setFechaInicio(fechaInicio);
            ciclo.setFechaFin(fechaFin);
            cicloDAO.actualizarCiclo(ciclo);

            JOptionPane.showMessageDialog(this, "Ciclo actualizado con éxito.");
            dispose();
        } catch (DateTimeParseException ex) {
            JOptionPane.showMessageDialog(this, "Las fechas deben tener el formato dd/MM/yyyy.");
        }
    }

    private void guardarCiclo() {
        try {
            int numero = Integer.parseInt(txtNumero.getText().trim());
            LocalDate fechaInicio = LocalDate.parse(txtFechaInicio.getText().trim(), dateFormatter);
            LocalDate fechaFin = LocalDate.parse(txtFechaFin.getText().trim(), dateFormatter);

            Ciclo ciclo = new Ciclo(numero, fechaInicio, fechaFin);
            cicloDAO.insertarCiclo(ciclo);

            JOptionPane.showMessageDialog(this, "Ciclo guardado con éxito.");
            dispose();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "El número de ciclo debe ser un número entero.");
        } catch (DateTimeParseException ex) {
            JOptionPane.showMessageDialog(this, "Las fechas deben tener el formato dd/MM/yyyy.");
        }
    }
}
