package ar.edu.utn.frc.backend.services;

import ar.edu.utn.frc.backend.entities.MetodoPago;
import jakarta.persistence.EntityManager;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public class MetodoPagoService {
    private Map<Integer, MetodoPago> metodospago = new HashMap<>();
    private final EntityManager em;

    public MetodoPagoService(EntityManager em) {
        this.em = em;
    }

    public MetodoPago getOrCreateMetodoPago(Integer id, String nombre, String detalles, BigDecimal comision) {
        MetodoPago metodoPago = em.find(MetodoPago.class, id);
        if (metodoPago != null) {
            return metodoPago;
        }
        metodoPago = new MetodoPago(id, nombre, detalles, comision);
        em.getTransaction().begin();
        em.persist(metodoPago);
        em.getTransaction().commit();
        metodospago.put(id, metodoPago);
        return metodoPago;
    }

    public MetodoPago postMetodoPago(Integer id,String nombre, String detalles, BigDecimal comision) {
        MetodoPago metodoPago = new MetodoPago(id, nombre, detalles, comision);
        em.getTransaction().begin();
        em.persist(metodoPago); // Persist the MetodoPago entity to generate the ID
        em.getTransaction().commit();
        metodospago.put(id, metodoPago);
        return metodoPago;
    }

    public MetodoPago deleteMetodoPago(Integer id) {
        MetodoPago metodoPago = metodospago.get(id);
        if (metodoPago != null) {
            em.getTransaction().begin();
            em.remove(metodoPago);
            em.getTransaction().commit();
            metodospago.remove(id);
        }
        return metodoPago;
    }

    public MetodoPago updateMetodoPago(Integer id, String nombre, String detalles, BigDecimal comision) {
        MetodoPago metodoPago = metodospago.get(id);
        if (metodoPago != null) {
            metodoPago.setNombre(nombre);
            metodoPago.setDetalles(detalles);
            metodoPago.setComision(comision);
            em.getTransaction().begin();
            em.merge(metodoPago);
            em.getTransaction().commit();
        }
        return metodoPago;
    }
}