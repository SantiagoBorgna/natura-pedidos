package app.ui;

import app.App;
import app.dao.PedidoDAO;
import app.model.Pedido;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class MainWindow extends JFrame {

    private PedidoDAO pedidoDAO;
    private JTable tablaPedidos;
    private DefaultTableModel modeloTabla;
    private JLabel lblTotal;
    private JLabel lblGanancia;

    public MainWindow() {
        pedidoDAO = new PedidoDAO();
        setIconImage(App.logo);
        setTitle("Gestión de Pedidos Natura");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(1000, 700));
        setLocationRelativeTo(null);

        setExtendedState(JFrame.MAXIMIZED_BOTH);

        // Layout principal
        setLayout(new BorderLayout());

        //Header
        JLabel lblCiclo = new JLabel("Ciclo Actual: 12", SwingConstants.CENTER);
        lblCiclo.setFont(new Font("Arial", Font.BOLD, 18));
        add(lblCiclo, BorderLayout.NORTH);

        //Tabla de pedidos
        String[] columnas = {"id", "Cliente", "Producto", "Código", "Cantidad", "Precio Unitario", "Precio Total"};
        modeloTabla = new DefaultTableModel(columnas, 0);
        tablaPedidos = new JTable(modeloTabla);

        tablaPedidos.getColumnModel().getColumn(0).setMinWidth(0);
        tablaPedidos.getColumnModel().getColumn(0).setMaxWidth(0);
        tablaPedidos.getColumnModel().getColumn(0).setWidth(0);

        add(new JScrollPane(tablaPedidos), BorderLayout.CENTER);

        //Footer
        JPanel panelFooter = new JPanel(new GridLayout(1, 2));
        lblTotal = new JLabel("Total a cobrar: $0.00");
        lblGanancia = new JLabel("Ganancia: $0.00");
        panelFooter.add(lblTotal);
        panelFooter.add(lblGanancia);
        add(panelFooter, BorderLayout.SOUTH);

        //Panel de botones
        JPanel panelBotones = new JPanel();
        JButton btnAgregar = new JButton("Agregar Pedido");
        JButton btnEditar = new JButton("Editar Pedido");
        JButton btnEliminar = new JButton("Eliminar Pedido");
        panelBotones.add(btnAgregar);
        panelBotones.add(btnEditar);
        panelBotones.add(btnEliminar);
        add(panelBotones, BorderLayout.PAGE_END);

        //Cargar pedidos en la tabla
        cargarPedidos();

        //Funcionalidad botones
        btnAgregar.addActionListener(e -> {
            PedidoForm form = new PedidoForm(this, pedidoDAO);
            form.setVisible(true);
            cargarPedidos();
        });

        btnEditar.addActionListener(e -> {
            int fila = tablaPedidos.getSelectedRow();
            if (fila == -1) {
                JOptionPane.showMessageDialog(this, "Selecciona un pedido para editar.");
                return;
            }

            int id = (int) modeloTabla.getValueAt(fila, 0);
            String cliente = (String) modeloTabla.getValueAt(fila, 1);
            String producto = (String) modeloTabla.getValueAt(fila, 2);
            String codigo = (String) modeloTabla.getValueAt(fila, 3);
            int cantidad = (int) modeloTabla.getValueAt(fila, 4);
            double precioUnit = (double) modeloTabla.getValueAt(fila, 5);

            Pedido pedido = new Pedido(cliente, producto, codigo, cantidad, precioUnit);
            pedido.setId(id);

            // abrir formulario en modo edición
            PedidoForm form = new PedidoForm(this, pedidoDAO, pedido);
            form.setVisible(true);

            cargarPedidos();
        });

        btnEliminar.addActionListener(e -> {
            int fila = tablaPedidos.getSelectedRow();
            if (fila == -1) {
                JOptionPane.showMessageDialog(this, "Selecciona un pedido para eliminar.");
                return;
            }

            int id = (int) modeloTabla.getValueAt(fila, 0);
            String cliente = (String) modeloTabla.getValueAt(fila, 1);

            String[] opciones = {"Sí", "No"};
            int opcion = JOptionPane.showOptionDialog(
                    this,
                    "¿Segura que desea eliminar el pedido de " + cliente + "?",
                    "Confirmar eliminación",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    opciones,
                    opciones[1] // opción por defecto ("No")
            );

            if (opcion == 0) { // 0 = "Sí"
                pedidoDAO.eliminarPedido(id);
                cargarPedidos();
            }
        });

    }

    protected void cargarPedidos() {
        modeloTabla.setRowCount(0); // limpiar
        List<Pedido> pedidos = pedidoDAO.listarPedidos();

        double total = 0;
        double ganancia = 0;

        for (Pedido p : pedidos) {
            Object[] fila = {
                    p.getId(),
                    p.getCliente(),
                    p.getProducto(),
                    p.getCodigo(),
                    p.getCantidad(),
                    p.getPrecioUnitario(),
                    p.getPrecioTotal()
            };
            modeloTabla.addRow(fila);

            total += p.getPrecioTotal();
            ganancia += (p.getPrecioTotal() * 0.25);
        }

        lblTotal.setText("Total a cobrar: $" + total);
        lblGanancia.setText("Ganancia: $" + ganancia);
    }
}
