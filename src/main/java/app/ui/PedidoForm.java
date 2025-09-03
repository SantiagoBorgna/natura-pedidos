package app.ui;

import app.dao.PedidoDAO;
import app.model.Pedido;

import javax.swing.*;
import java.awt.*;

public class PedidoForm extends JDialog {

    private JTextField txtCliente;
    private JTextField txtProducto;
    private JTextField txtCodigo;
    private JTextField txtCantidad;
    private JTextField txtPrecioUnitario;

    private PedidoDAO pedidoDAO;
    private Pedido pedido; // puede ser nuevo o existente

    public PedidoForm(JFrame parent, PedidoDAO pedidoDAO) {
        this(parent, pedidoDAO, null); // constructor para "Agregar"
    }

    public PedidoForm(JFrame parent, PedidoDAO pedidoDAO, Pedido pedido) {
        super(parent, true);
        this.pedidoDAO = pedidoDAO;
        this.pedido = pedido;

        setTitle(pedido == null ? "Agregar Pedido" : "Editar Pedido");
        setSize(400, 300);
        setLocationRelativeTo(parent);

        initUI();
        if (pedido != null) {
            cargarDatosPedido(pedido);
        }
    }

    private void initUI() {
        setLayout(new GridLayout(6, 2, 10, 10));

        add(new JLabel("Cliente:"));
        txtCliente = new JTextField();
        add(txtCliente);

        add(new JLabel("Producto:"));
        txtProducto = new JTextField();
        add(txtProducto);

        add(new JLabel("Código:"));
        txtCodigo = new JTextField();
        add(txtCodigo);

        add(new JLabel("Cantidad:"));
        txtCantidad = new JTextField();
        add(txtCantidad);

        add(new JLabel("Precio Unitario:"));
        txtPrecioUnitario = new JTextField();
        add(txtPrecioUnitario);

        JButton btnGuardar = new JButton("Guardar");
        btnGuardar.addActionListener(e -> guardarPedido());
        add(btnGuardar);

        JButton btnCancelar = new JButton("Cancelar");
        btnCancelar.addActionListener(e -> dispose());
        add(btnCancelar);
    }

    private void cargarDatosPedido(Pedido p) {
        txtCliente.setText(p.getCliente());
        txtProducto.setText(p.getProducto());
        txtCodigo.setText(p.getCodigo());
        txtCantidad.setText(String.valueOf(p.getCantidad()));
        txtPrecioUnitario.setText(String.valueOf(p.getPrecioUnitario()));
    }

    private void guardarPedido() {
        try {
            String cliente = txtCliente.getText();
            String producto = txtProducto.getText();
            String codigo = txtCodigo.getText();
            int cantidad = Integer.parseInt(txtCantidad.getText());
            double precioUnit = Double.parseDouble(txtPrecioUnitario.getText());

            if (pedido == null) {
                // nuevo
                Pedido nuevo = new Pedido(cliente, producto, codigo, cantidad, precioUnit);
                pedidoDAO.agregarPedido(nuevo);
            } else {
                // edición
                pedido.setCliente(cliente);
                pedido.setProducto(producto);
                pedido.setCodigo(codigo);
                pedido.setCantidad(cantidad);
                pedido.setPrecioUnitario(precioUnit);
                pedido.setPrecioTotal(cantidad * precioUnit);

                pedidoDAO.actualizarPedido(pedido);
            }
            dispose();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Cantidad y precio deben ser numéricos");
        }
    }
}
