package app.model;

public class Pedido {
    private int id;
    private String cliente;
    private String producto;
    private String codigo;
    private int cantidad;
    private double precioUnitario;
    private double precioTotal;
    private Integer cicloId;

    // Constructor
    public Pedido(String cliente, String producto, String codigo, int cantidad, double precioUnitario) {
        this.cliente = cliente;
        this.producto = producto;
        this.codigo = codigo;
        this.cantidad = cantidad;
        this.precioUnitario = precioUnitario;
        this.precioTotal = cantidad * precioUnitario;
    }

    // Getters y Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCliente() {
        return cliente;
    }

    public void setCliente(String cliente) {
        this.cliente = cliente;
    }

    public String getProducto() {
        return producto;
    }

    public void setProducto(String producto) {
        this.producto = producto;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public double getPrecioUnitario() {
        return precioUnitario;
    }

    public void setPrecioUnitario(double precioUnitario) {
        this.precioUnitario = precioUnitario;
    }

    public double getPrecioTotal() {
        return precioTotal;
    }

    public void setPrecioTotal(double precioTotal) {
        this.precioTotal = precioTotal;
    }

    public Integer getCicloId() {
        return cicloId;
    }

    public void setCicloId(Integer cicloId) {
        this.cicloId = cicloId;
    }
}