package ar.edu.utn.frc.backend.entities;

import jakarta.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "CLIENTES")
public class Cliente {
    @Id
    private int ID;

    private String nombre;
    private String email;
    private String telefono;
    private String direccion;

    // Constructor
    public Cliente(int ID, String nombre, String email, String telefono, String direccion) {
        this.ID = ID;
        this.nombre = nombre;
        this.email = email;
        this.telefono = telefono;
        this.direccion = direccion;
    }

    public Cliente() {
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    // Equals, HashCode

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Cliente cliente = (Cliente) o;
        return ID == cliente.ID && Objects.equals(nombre, cliente.nombre) && Objects.equals(email, cliente.email) && Objects.equals(telefono, cliente.telefono) && Objects.equals(direccion, cliente.direccion);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ID, nombre, email, telefono, direccion);
    }

    // ToString

    @Override
    public String toString() {
        return "Cliente{" +
                "ID=" + ID +
                ", nombre='" + nombre + '\'' +
                ", email='" + email + '\'' +
                ", telefono='" + telefono + '\'' +
                ", direccion='" + direccion + '\'' +
                '}';
    }
}