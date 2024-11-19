package ar.edu.utn.frc.backend.entities;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.util.Objects;

@Entity
@Table(name = "METODOS_PAGO")
public class MetodoPago {
    @Id
    private int ID;

    private String nombre;
    private String detalles;
    private BigDecimal comision;

    // Constructor
    public MetodoPago(int ID, String nombre, String detalles, BigDecimal comision) {
        this.ID = ID;
        this.nombre = nombre;
        this.detalles = detalles;
        this.comision = comision;
    }

    public MetodoPago() {
    }

    // Getters and Setters

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDetalles() {
        return detalles;
    }

    public void setDetalles(String detalles) {
        this.detalles = detalles;
    }

    public BigDecimal getComision() {
        return comision;
    }

    public void setComision(BigDecimal comision) {
        this.comision = comision;
    }

    // Equals, HashCode

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MetodoPago that = (MetodoPago) o;
        return ID == that.ID && Objects.equals(nombre, that.nombre) && Objects.equals(detalles, that.detalles) && Objects.equals(comision, that.comision);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ID, nombre, detalles, comision);
    }

    // ToString

    @Override
    public String toString() {
        return "MetodoPago{" +
                "ID=" + ID +
                ", nombre='" + nombre + '\'' +
                ", detalles='" + detalles + '\'' +
                ", comision=" + comision +
                '}';
    }
}