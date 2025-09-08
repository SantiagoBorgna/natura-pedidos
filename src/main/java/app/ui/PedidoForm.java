package app.ui;

import app.dao.PedidoDAO;
import app.model.Pedido;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class PedidoForm extends JDialog {

    private JTextField txtCliente;
    private JTextField txtProducto;
    private JTextField txtCodigo;
    private JTextField txtCantidad;
    private JTextField txtPrecioUnitario;
    private JTextField txtPuntos;
    private JButton btnGuardar;
    private JButton btnCancelar;

    private PedidoDAO pedidoDAO;
    private Pedido pedido;
    private Integer cicloId;

    //Agregar
    public PedidoForm(JFrame parent, PedidoDAO pedidoDAO, int cicloId) {
        this(parent, pedidoDAO, null);
        this.cicloId = cicloId;
    }

    //Editar
    public PedidoForm(JFrame parent, PedidoDAO pedidoDAO, Pedido pedido) {
        super(parent, true);
        this.pedidoDAO = pedidoDAO;
        this.pedido = pedido;
        this.cicloId = (pedido != null ? pedido.getCicloId() : null);

        setTitle(pedido == null ? "Agregar Pedido" : "Editar Pedido");
        setSize(400, 360);
        setLocationRelativeTo(parent);

        initUI(); // construís la interfaz
        if (pedido != null) {
            cargarDatosPedido(pedido); // precargar valores en los campos
        }
    }

    private void initUI() {
        JPanel panel = new JPanel(new GridLayout(7, 2, 15, 15));
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));

        Font labelFont = new Font("Arial", Font.PLAIN, 15);

        JLabel lblCliente = new JLabel("Cliente:");
        lblCliente.setFont(labelFont);
        panel.add(lblCliente);
        txtCliente = new JTextField();
        txtCliente.setFont(labelFont);
        panel.add(txtCliente);

        JLabel lblProducto = new JLabel("Producto:");
        lblProducto.setFont(labelFont);
        panel.add(lblProducto);
        txtProducto = new JTextField();
        txtProducto.setFont(labelFont);
        panel.add(txtProducto);

        JLabel lblCodigo = new JLabel("Codigo:");
        lblCodigo.setFont(labelFont);
        panel.add(lblCodigo);
        txtCodigo = new JTextField();
        txtCodigo.setFont(labelFont);
        panel.add(txtCodigo);

        JLabel lblCantidad = new JLabel("Cantidad:");
        lblCantidad.setFont(labelFont);
        panel.add(lblCantidad);
        txtCantidad = new JTextField();
        txtCantidad.setFont(labelFont);
        panel.add(txtCantidad);

        JLabel lblPrecioUnit = new JLabel("Precio unitario:");
        lblPrecioUnit.setFont(labelFont);
        panel.add(lblPrecioUnit);
        txtPrecioUnitario = new JTextField();
        txtPrecioUnitario.setFont(labelFont);
        panel.add(txtPrecioUnitario);

        JLabel lblPuntos = new JLabel("Puntos:");
        lblPuntos.setFont(labelFont);
        panel.add(lblPuntos);
        txtPuntos = new JTextField();
        txtPuntos.setFont(labelFont);
        panel.add(txtPuntos);

        btnGuardar = new JButton("Guardar");
        btnCancelar = new JButton("Cancelar");

        aplicarEstiloBoton(btnGuardar);
        aplicarEstiloBoton(btnCancelar);

        panel.add(btnGuardar);
        panel.add(btnCancelar);

        btnGuardar.addActionListener(e -> guardarPedido());
        btnCancelar.addActionListener(e -> dispose());

        add(panel);
    }

    private void cargarDatosPedido(Pedido p) {
        txtCliente.setText(p.getCliente());
        txtProducto.setText(p.getProducto());
        txtCodigo.setText(p.getCodigo());
        txtCantidad.setText(String.valueOf(p.getCantidad()));
        txtPrecioUnitario.setText(String.valueOf(p.getPrecioUnitario()));
        txtPuntos.setText(String.valueOf(p.getPuntos()));
    }

    private void guardarPedido() {
        try {
            String cliente = txtCliente.getText();
            String producto = txtProducto.getText();
            String codigo = txtCodigo.getText();
            int cantidad = Integer.parseInt(txtCantidad.getText());
            double precioUnit = Double.parseDouble(txtPrecioUnitario.getText());
            int puntos = Integer.parseInt(txtPuntos.getText());

            if (cantidad < 0 || precioUnit < 0) {
                JOptionPane.showMessageDialog(this, "La cantidad y el precio deben ser mayores o iguales a 0.");
                return;
            }

            if (pedido == null) {
                //Agregar
                Pedido nuevo = new Pedido(cliente, producto, codigo, cantidad, precioUnit, puntos);
                nuevo.setCicloId(cicloId);
                pedidoDAO.agregarPedido(nuevo);
            } else {
                //Editar
                pedido.setCliente(cliente);
                pedido.setProducto(producto);
                pedido.setCodigo(codigo);
                pedido.setCantidad(cantidad);
                pedido.setPrecioUnitario(precioUnit);
                pedido.setPrecioTotal(cantidad * precioUnit);
                pedido.setPuntos(puntos);
                pedido.setCicloId(cicloId);

                pedidoDAO.actualizarPedido(pedido);
            }
            dispose();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Cantidad y precio deben ser numéricos");
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
