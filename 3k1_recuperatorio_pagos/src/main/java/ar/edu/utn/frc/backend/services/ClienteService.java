package ar.edu.utn.frc.backend.services;

import ar.edu.utn.frc.backend.entities.Cliente;
import jakarta.persistence.EntityManager;

import java.util.HashMap;
import java.util.Map;

public class ClienteService {
    private Map<Integer, Cliente> clientes = new HashMap<>();
    private final EntityManager em;

    public ClienteService(EntityManager em) {
        this.em = em;
    }
    public Cliente getOrCreateCliente(Integer id, String nombre, String email, String telefono, String direccion) {
        Cliente cliente = em.find(Cliente.class, id);
        if (cliente != null) {
            return cliente;
        }

        for (Cliente existingCliente : clientes.values()) {
            if ((existingCliente.getNombre().equals(nombre) && existingCliente.getEmail().equals(email)) &&
                    (existingCliente.getTelefono().equals(telefono) && existingCliente.getDireccion().equals(direccion))) {
                return existingCliente;
            }
        }

        Cliente newCliente = new Cliente(id, nombre, email, telefono, direccion);
        em.getTransaction().begin();
        em.persist(newCliente); // Persist the Cliente entity to generate the ID
        em.getTransaction().commit();
        clientes.put(id, newCliente);
        return newCliente;
    }

    /**
    public Cliente postCliente(Integer id, String nombre, String email, String telefono, String direccion) {
        Cliente cliente = new Cliente(id, nombre, email, telefono, direccion);
        em.getTransaction().begin();
        em.persist(cliente); // Persist the Cliente entity to generate the ID
        em.getTransaction().commit();
        clientes.put(id, cliente);
        return cliente;
    }

    public Cliente deleteCliente(Integer id) {
        Cliente cliente = clientes.get(id);
        if (cliente != null) {
            em.getTransaction().begin();
            em.remove(cliente);
            em.getTransaction().commit();
            clientes.remove(id);
        }
        return cliente;
    }

    public Cliente updateCliente(Integer id, String nombre, String email, String telefono, String direccion) {
        Cliente cliente = clientes.get(id);
        if (cliente != null) {
            cliente.setNombre(nombre);
            cliente.setEmail(email);
            cliente.setTelefono(telefono);
            cliente.setDireccion(direccion);
            em.getTransaction().begin();
            em.merge(cliente);
            em.getTransaction().commit();
        }
        return cliente;
    }
     **/
}
