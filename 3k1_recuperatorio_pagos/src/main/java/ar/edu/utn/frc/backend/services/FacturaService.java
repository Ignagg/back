package ar.edu.utn.frc.backend.services;

import ar.edu.utn.frc.backend.entities.Factura;
import jakarta.persistence.EntityManager;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class FacturaService {
    private Map<Integer, Factura> facturas = new HashMap<>();
    private final EntityManager em;

    public FacturaService(EntityManager em) {
        this.em = em;
    }

    public Factura getOrCreateFactura(Integer id, BigDecimal monto_total, Date fecha_emision, Date fecha_vencimiento, String descripcion, String estado) {
        Factura factura = em.find(Factura.class, id);
        if (factura != null) {
            return factura;
        }
        factura = new Factura(id, monto_total, fecha_emision, fecha_vencimiento, descripcion, estado);
        em.getTransaction().begin();
        em.persist(factura);
        em.getTransaction().commit();
        facturas.put(id, factura);
        return factura;
    }

    public Factura postFactura(Integer id,BigDecimal monto_total, Date fecha_emision, Date fecha_vencimiento, String descripcion, String estado) {
        Factura factura = new Factura(id, monto_total, fecha_emision, fecha_vencimiento, descripcion, estado);
        em.getTransaction().begin();
        em.persist(factura); // Persist the Factura entity to generate the ID
        em.getTransaction().commit();
        facturas.put(id, factura);
        return factura;
    }

    public Factura deleteFactura(Integer id) {
        Factura factura = facturas.get(id);
        if (factura != null) {
            em.getTransaction().begin();
            em.remove(factura);
            em.getTransaction().commit();
            facturas.remove(id);
        }
        return factura;
    }

    public Factura updateFactura(Integer id,BigDecimal monto_total, Date fecha_emision, Date fecha_vencimiento, String descripcion, String estado) {
        Factura factura = facturas.get(id);
        if (factura != null) {
            factura.setMonto_total(monto_total);
            factura.setFecha_emision(fecha_emision);
            factura.setFecha_vencimiento(fecha_vencimiento);
            factura.setDescripcion(descripcion);
            factura.setEstado(estado);
            em.getTransaction().begin();
            em.merge(factura);
            em.getTransaction().commit();
        }
        return factura;
    }
}