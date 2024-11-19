package ar.edu.utn.frc.backend.repositories;

import ar.edu.utn.frc.backend.entities.Pago;
import jakarta.persistence.EntityManager;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public class PagoRepository {
    private final EntityManager em;

    public PagoRepository(EntityManager em) {
        this.em = em;
    }

    public void saveAll(List<Pago> pagos) {
        em.getTransaction().begin();
        for (Pago pago : pagos) {
            if (pago != null) {
                em.merge(pago);
            }
        }
        em.getTransaction().commit();
    }

    public void save(Pago pagoAGuardar) {
        begin();
        em.persist(pagoAGuardar);
        commit();
    }

    public void begin() {
        em.getTransaction().begin();
    }

    public void commit(){
        em.getTransaction().commit();
    }

    public Optional<Pago> findById(int id) {
        Pago pago = em.find(Pago.class, id);
        return Optional.ofNullable(pago);
    }

    public int count() {
        return ((Number) em.createQuery("select count(p) from Pago p").getSingleResult()).intValue();
    }
}