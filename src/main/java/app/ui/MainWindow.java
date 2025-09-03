package app.ui;

import app.App;
import app.dao.PedidoDAO;
import app.dao.CicloDAO;
import app.model.Pedido;
import app.model.Ciclo;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class MainWindow extends JFrame {

    private PedidoDAO pedidoDAO;
    private JTable tablaPedidos;
    private DefaultTableModel modeloTabla;
    private JLabel lblTotal;
    private JLabel lblGanancia;
    private CicloDAO cicloDAO;
    private Ciclo cicloActual;
    private JLabel lblCiclo;
    private JLabel lblFechaFin;
    private DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public MainWindow() {
        pedidoDAO = new PedidoDAO();
        cicloDAO = new CicloDAO();

        setIconImage(App.logo);
        setTitle("Gestión de Pedidos Natura");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(1000, 700));
        setLocationRelativeTo(null);

        setExtendedState(JFrame.MAXIMIZED_BOTH);

        // Layout principal
        setLayout(new BorderLayout());

        //Header
        JPanel panelHeader = new JPanel(new BorderLayout());

        JButton btnPrev = new JButton("<");
        JButton btnNext = new JButton(">");

        // Panel central con ciclo y fecha
        JPanel panelCentro = new JPanel();
        panelCentro.setLayout(new BoxLayout(panelCentro, BoxLayout.Y_AXIS));

        lblCiclo = new JLabel("Ciclo: -", SwingConstants.CENTER);
        lblCiclo.setFont(new Font("Arial", Font.BOLD, 18));

        lblFechaFin = new JLabel("Cierra: -", SwingConstants.RIGHT);

        panelCentro.add(lblCiclo);
        panelCentro.add(lblFechaFin);

        panelHeader.add(btnPrev, BorderLayout.WEST);
        panelHeader.add(panelCentro, BorderLayout.CENTER);
        panelHeader.add(btnNext, BorderLayout.EAST);

        add(panelHeader, BorderLayout.NORTH);

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
        JButton btnCrearCiclo = new JButton("Crear Ciclo");
        panelBotones.add(btnAgregar);
        panelBotones.add(btnEditar);
        panelBotones.add(btnEliminar);
        panelBotones.add(btnCrearCiclo);
        add(panelBotones, BorderLayout.PAGE_END);

        //Cargar pedidos en la tabla
        cargarPedidos();

        //Funcionalidad botones
        btnAgregar.addActionListener(e -> {
            if (cicloActual == null) {
                JOptionPane.showMessageDialog(this, "Primero cree un ciclo.");
                return;
            }
            PedidoForm form = new PedidoForm(this, pedidoDAO, cicloActual.getId());
            form.setVisible(true);
            cargarPedidos(); // refrescar
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
            pedido.setCicloId(cicloActual.getId());

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
            String producto = (String) modeloTabla.getValueAt(fila, 2);

            String[] opciones = {"Sí", "No"};
            int opcion = JOptionPane.showOptionDialog(
                    this,
                    "¿Segura que desea eliminar el pedido " + producto + " de " + cliente + "?",
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

        btnCrearCiclo.addActionListener(e -> {
            CicloForm form = new CicloForm(this, cicloDAO);
            form.setVisible(true);
            cargarCicloMasReciente();
        });

        btnPrev.addActionListener(e -> cambiarCiclo(-1));
        btnNext.addActionListener(e -> cambiarCiclo(1));

        cargarCicloMasReciente();
    }

    protected void cargarPedidos() {
        modeloTabla.setRowCount(0); // limpiar

        if (cicloActual == null) {
            lblTotal.setText("Total a cobrar: $0.00");
            lblGanancia.setText("Ganancia: $0.00");
            return;
        }

        List<Pedido> pedidos = pedidoDAO.listarPedidos(cicloActual.getId());

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

    private void cargarCicloMasReciente() {
        List<Ciclo> ciclos = cicloDAO.listarCiclos();
        if (!ciclos.isEmpty()) {
            cicloActual = ciclos.get(ciclos.size() - 1);
            actualizarHeader();
            cargarPedidos();
        }
    }

    private void cambiarCiclo(int direccion) {
        List<Ciclo> ciclos = cicloDAO.listarCiclos();

        for (Ciclo c : ciclos) {
            if (c.getNumero() == cicloActual.getNumero()){
                int index = ciclos.indexOf(c);
                System.out.println(index);
                int nuevoIndex = index + direccion;
                System.out.println(nuevoIndex);

                if (nuevoIndex >= 0 && nuevoIndex < ciclos.size()) {
                    cicloActual = ciclos.get(nuevoIndex);
                    actualizarHeader();
                    cargarPedidos();
                }
            }
        }
    }

    private void actualizarHeader() {
        if (cicloActual != null) {
            lblCiclo.setText("Ciclo: " + cicloActual.getNumero());

            if (cicloActual.getFechaFin() != null) {
                lblFechaFin.setText("Cierra: " + cicloActual.getFechaFin().format(dateFormatter));
            } else {
                lblFechaFin.setText("Cierra: -");
            }
        } else {
            lblCiclo.setText("Ciclo: -");
            lblFechaFin.setText("Cierra: -");
        }
    }
}
