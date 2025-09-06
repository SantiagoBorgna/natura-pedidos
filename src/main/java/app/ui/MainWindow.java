package app.ui;

import app.App;
import app.dao.PedidoDAO;
import app.dao.CicloDAO;
import app.model.Pedido;
import app.model.Ciclo;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
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
        panelHeader.setBackground(new Color(233, 212, 195));
        panelHeader.setBorder(null);

        JPanel panelCentro = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        panelCentro.setBackground(new Color(233, 212, 195));

        JButton btnPrev = new JButton("<");
        JButton btnNext = new JButton(">");

        // Panel central con ciclo y fecha
        JPanel panelCiclo = new JPanel();
        panelCiclo.setLayout(new BoxLayout(panelCiclo, BoxLayout.Y_AXIS));
        panelCiclo.setBackground(new Color(233, 212, 195));

        lblCiclo = new JLabel("Ciclo: -", SwingConstants.CENTER);
        lblCiclo.setFont(new Font("Roboto", Font.BOLD, 24));

        lblFechaFin = new JLabel("Cierra: -", SwingConstants.CENTER);

        panelCiclo.add(lblCiclo);
        panelCiclo.add(lblFechaFin);

        panelCentro.add(btnPrev);
        panelCentro.add(panelCiclo);
        panelCentro.add(btnNext);

        for (Component comp : panelCentro.getComponents()) {
            if (comp instanceof JButton boton) {
                boton.setBackground(Color.WHITE);
                boton.setOpaque(true);
                boton.setBorder(new EmptyBorder(5, 15, 5, 15)); // margen interno sin borde
                boton.setFocusPainted(false);
                boton.setContentAreaFilled(true);

                // Hover
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

        panelHeader.add(panelCentro, BorderLayout.CENTER);

        add(panelHeader, BorderLayout.NORTH);

        //Tabla de pedidos
        String[] columnas = {"id", "Cliente", "Producto", "Código", "Cantidad", "Precio Unitario", "Precio Total"};
        modeloTabla = new DefaultTableModel(columnas, 0);
        tablaPedidos = new JTable(modeloTabla);

        //Ocultar columna ID
        tablaPedidos.getColumnModel().getColumn(0).setMinWidth(0);
        tablaPedidos.getColumnModel().getColumn(0).setMaxWidth(0);
        tablaPedidos.getColumnModel().getColumn(0).setWidth(0);

        //Estilos header tabla
        JTableHeader header = tablaPedidos.getTableHeader();
        header.setFont(new Font("Roboto", Font.BOLD, 15));
        header.setPreferredSize(new Dimension(header.getWidth(), 30));
        header.setBackground(new Color(255, 159, 103));

        //Estilos tabla
        tablaPedidos.setBorder(null);
        tablaPedidos.setRowHeight(30);
        tablaPedidos.setFont(new Font("Arial", Font.PLAIN, 15));

        JScrollPane scrollPane = new JScrollPane(tablaPedidos);
        scrollPane.getViewport().setBackground(new Color(233, 212, 195));
        scrollPane.setBorder(null);
        scrollPane.getViewport().setBorder(null);

        add(scrollPane, BorderLayout.CENTER);

        //Footer
        JPanel panelSur = new JPanel(new BorderLayout());

        JPanel panelFooter = new JPanel(new GridLayout(2, 1));
        panelFooter.setBorder(new EmptyBorder(0, 0, 0, 20));

        lblTotal = new JLabel("Total: $0.00", SwingConstants.RIGHT);
        lblTotal.setFont(new Font("Arial", Font.BOLD, 16));
        lblGanancia = new JLabel("Ganancia: $0.00", SwingConstants.RIGHT);
        lblGanancia.setFont(new Font("Arial", Font.BOLD, 16));

        panelFooter.add(lblTotal);
        panelFooter.add(lblGanancia);

        //Panel de botones
        JPanel panelBotones = new JPanel();
        panelBotones.setBorder(new EmptyBorder(0, 180, 0, 0));

        JButton btnAgregar = new JButton("Agregar Pedido");
        JButton btnEditar = new JButton("Editar Pedido");
        JButton btnEliminar = new JButton("Eliminar Pedido");
        JButton btnCrearCiclo = new JButton("Crear Ciclo");
        JButton btnEditarCiclo = new JButton("Editar Ciclo");
        panelBotones.add(btnAgregar);
        panelBotones.add(btnEditar);
        panelBotones.add(btnEliminar);
        panelBotones.add(btnCrearCiclo);
        panelBotones.add(btnEditarCiclo);

        for (Component comp : panelBotones.getComponents()) {
            if (comp instanceof JButton boton) {
                boton.setBackground(Color.WHITE);
                boton.setOpaque(true);
                boton.setBorder(new EmptyBorder(5, 15, 5, 15)); // margen interno sin borde
                boton.setFocusPainted(false);
                boton.setContentAreaFilled(true);

                // Hover
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

        panelFooter.setBackground(new Color(233, 212, 195));
        panelBotones.setBackground(new Color(233, 212, 195));

        panelSur.add(panelBotones, BorderLayout.CENTER);
        panelSur.add(panelFooter, BorderLayout.EAST);
        panelSur.setBorder(null);

        add(panelSur, BorderLayout.SOUTH);

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

        btnEditarCiclo.addActionListener(e -> {
            if (cicloActual == null) {
                JOptionPane.showMessageDialog(this, "No hay un ciclo seleccionado para editar.");
                return;
            }

            CicloForm form = new CicloForm(this, cicloDAO, cicloActual);
            form.setVisible(true);

            cargarCicloMasReciente();
            cargarPedidos();
        });

        //Cargar pedidos en la tabla
        cargarPedidos();
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
                int nuevoIndex = index + direccion;

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
