package ar.edu.utn.frc.backend.entities;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Objects;

@Entity
@Table(name = "FACTURAS")
public class Factura {
    @Id
    private int ID;

    private BigDecimal monto_total;

    @Temporal(TemporalType.DATE)
    private Date fecha_emision;

    @Temporal(TemporalType.DATE)
    private Date fecha_vencimiento;

    private String descripcion;
    private String estado;

    // Constructor
    public Factura(int ID, BigDecimal monto_total, Date fecha_emision, Date fecha_vencimiento, String descripcion, String estado) {
        this.ID = ID;
        this.monto_total = monto_total;
        this.fecha_emision = fecha_emision;
        this.fecha_vencimiento = fecha_vencimiento;
        this.descripcion = descripcion;
        this.estado = estado;
    }

    public Factura() {
    }

    // Getters and Setters

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public BigDecimal getMonto_total() {
        return monto_total;
    }

    public void setMonto_total(BigDecimal monto_total) {
        this.monto_total = monto_total;
    }

    public Date getFecha_emision() {
        return fecha_emision;
    }

    public void setFecha_emision(Date fecha_emision) {
        this.fecha_emision = fecha_emision;
    }

    public Date getFecha_vencimiento() {
        return fecha_vencimiento;
    }

    public void setFecha_vencimiento(Date fecha_vencimiento) {
        this.fecha_vencimiento = fecha_vencimiento;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    // Equals, HashCode

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Factura factura = (Factura) o;
        return ID == factura.ID && Objects.equals(monto_total, factura.monto_total) && Objects.equals(fecha_emision, factura.fecha_emision) && Objects.equals(fecha_vencimiento, factura.fecha_vencimiento) && Objects.equals(descripcion, factura.descripcion) && Objects.equals(estado, factura.estado);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ID, monto_total, fecha_emision, fecha_vencimiento, descripcion, estado);
    }

    // ToString

    @Override
    public String toString() {
        return "Factura{" +
                "ID=" + ID +
                ", monto_total=" + monto_total +
                ", fecha_emision=" + fecha_emision +
                ", fecha_vencimiento=" + fecha_vencimiento +
                ", descripcion='" + descripcion + '\'' +
                ", estado='" + estado + '\'' +
                '}';
    }
}