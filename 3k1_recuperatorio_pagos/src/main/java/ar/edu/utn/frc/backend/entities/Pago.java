package ar.edu.utn.frc.backend.entities;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Objects;

@Entity
@Table(name = "PAGOS")
public class Pago {
    @Id
    private int ID;

    private BigDecimal monto;
    private String estado;

    @Temporal(TemporalType.DATE)
    private Date fecha_pago;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "METODO_PAGO_ID", referencedColumnName = "ID")
    private MetodoPago metodoPago;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "FACTURA_ID", referencedColumnName = "ID")
    private Factura factura;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "CLIENTE_ID", referencedColumnName = "ID")
    private Cliente cliente;

    // Constructor
    public Pago(int ID, BigDecimal monto, String estado, Date fecha_pago, MetodoPago metodoPago, Factura factura, Cliente cliente) {
        this.ID = ID;
        this.monto = monto;
        this.estado = estado;
        this.fecha_pago = fecha_pago;
        this.metodoPago = metodoPago;
        this.factura = factura;
        this.cliente = cliente;
    }

    public Pago() {
    }

    // Getters and Setters

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public BigDecimal getMonto() {
        return monto;
    }

    public void setMonto(BigDecimal monto) {
        this.monto = monto;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public Date getFecha_pago() {
        return fecha_pago;
    }

    public void setFecha_pago(Date fecha_pago) {
        this.fecha_pago = fecha_pago;
    }

    public MetodoPago getMetodoPago() {
        return metodoPago;
    }

    public void setMetodoPago(MetodoPago metodoPago) {
        this.metodoPago = metodoPago;
    }

    public Factura getFactura() {
        return factura;
    }

    public void setFactura(Factura factura) {
        this.factura = factura;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    // Equals, HashCode

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Pago pago = (Pago) o;
        return ID == pago.ID && Objects.equals(monto, pago.monto) && Objects.equals(estado, pago.estado) && Objects.equals(fecha_pago, pago.fecha_pago) && Objects.equals(metodoPago, pago.metodoPago) && Objects.equals(factura, pago.factura) && Objects.equals(cliente, pago.cliente);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ID, monto, estado, fecha_pago, metodoPago, factura, cliente);
    }

    // ToString

    @Override
    public String toString() {
        return "Pago{" +
                "ID=" + ID +
                ", monto=" + monto +
                ", estado='" + estado + '\'' +
                ", fecha_pago=" + fecha_pago +
                ", metodoPago=" + metodoPago +
                ", factura=" + factura +
                ", cliente=" + cliente +
                '}';
    }

    public Pago orElse(Object o) {
        return null;
    }
}