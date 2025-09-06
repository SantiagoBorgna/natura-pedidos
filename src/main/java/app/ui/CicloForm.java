package app.ui;

import app.dao.CicloDAO;
import app.model.Ciclo;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class CicloForm extends JDialog {

    private JTextField txtNumero;
    private JTextField txtFechaInicio;
    private JTextField txtFechaFin;
    private JButton btnGuardar;
    private JButton btnCancelar;

    private CicloDAO cicloDAO;
    private DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    // Constructor para crear
    public CicloForm(JFrame parent, CicloDAO cicloDAO) {
        this(parent, cicloDAO, null);
    }

    // Constructor para editar
    public CicloForm(JFrame parent, CicloDAO cicloDAO, Ciclo ciclo) {
        super(parent, true);
        this.cicloDAO = cicloDAO;

        setTitle(ciclo == null ? "Crear Ciclo" : "Editar Ciclo");
        setSize(450, 250);
        setLocationRelativeTo(parent);

        initUI();

        if (ciclo != null) {
            cargarDatosCiclo(ciclo);

            for (ActionListener al : btnGuardar.getActionListeners()) {
                btnGuardar.removeActionListener(al);
            }
            btnGuardar.addActionListener(e -> actualizarCiclo(ciclo));
        }
    }

    private void initUI() {
        JPanel panel = new JPanel(new GridLayout(4, 2, 15, 15));
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));

        Font labelFont = new Font("Arial", Font.PLAIN, 15);

        JLabel lblNumero = new JLabel("Número de ciclo:");
        lblNumero.setFont(labelFont);
        panel.add(lblNumero);
        txtNumero = new JTextField();
        txtNumero.setFont(labelFont);
        panel.add(txtNumero);

        JLabel lblInicio = new JLabel("Fecha inicio (dd/MM/aaaa):");
        lblInicio.setFont(labelFont);
        panel.add(lblInicio);
        txtFechaInicio = new JTextField();
        txtFechaInicio.setFont(labelFont);
        panel.add(txtFechaInicio);

        JLabel lblFin = new JLabel("Fecha fin (dd/MM/aaaa):");
        lblFin.setFont(labelFont);
        panel.add(lblFin);
        txtFechaFin = new JTextField();
        txtFechaFin.setFont(labelFont);
        panel.add(txtFechaFin);

        btnGuardar = new JButton("Guardar");
        btnCancelar = new JButton("Cancelar");

        aplicarEstiloBoton(btnGuardar);
        aplicarEstiloBoton(btnCancelar);

        panel.add(btnGuardar);
        panel.add(btnCancelar);

        btnGuardar.addActionListener(e -> guardarCiclo());
        btnCancelar.addActionListener(e -> dispose());

        add(panel);
    }

    private void cargarDatosCiclo(Ciclo ciclo) {
        txtNumero.setText(String.valueOf(ciclo.getNumero()));
        txtNumero.setEnabled(false);
        txtFechaInicio.setText(ciclo.getFechaInicio().format(dateFormatter));
        txtFechaFin.setText(ciclo.getFechaFin().format(dateFormatter));
    }

    private void guardarCiclo() {
        try {
            int numero = Integer.parseInt(txtNumero.getText());
            LocalDate inicio = LocalDate.parse(txtFechaInicio.getText(), dateFormatter);
            LocalDate fin = LocalDate.parse(txtFechaFin.getText(), dateFormatter);

            Ciclo ciclo = new Ciclo(numero, inicio, fin);
            cicloDAO.insertarCiclo(ciclo);
            dispose();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al guardar ciclo: " + e.getMessage());
        }
    }

    private void actualizarCiclo(Ciclo ciclo) {
        try {
            LocalDate inicio = LocalDate.parse(txtFechaInicio.getText(), dateFormatter);
            LocalDate fin = LocalDate.parse(txtFechaFin.getText(), dateFormatter);

            ciclo.setFechaInicio(inicio);
            ciclo.setFechaFin(fin);

            cicloDAO.actualizarCiclo(ciclo);
            dispose();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al actualizar ciclo: " + e.getMessage());
        }
    }

    private void aplicarEstiloBoton(JButton boton) {
        boton.setBackground(Color.WHITE);
        boton.setOpaque(true);
        boton.setBorder(new EmptyBorder(10, 15, 10, 15)); // margen interno
        boton.setFocusPainted(false);
        boton.setContentAreaFilled(true);

        boton.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                boton.setBackground(Color.LIGHT_GRAY);
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                boton.setBackground(Color.WHITE);
            }
        });
    }
}

