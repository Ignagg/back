package ar.edu.utn.frc.backend;

import ar.edu.utn.frc.backend.services.*;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

public class Main {
    public static void main(String[] args) {
        System.out.println("Sistema de Gestión de Pagos y Facturación");

        EntityManagerFactory emf = Persistence.createEntityManagerFactory("PagosDB");
        EntityManager em = emf.createEntityManager();

        PagoService pagoService = new PagoService(em);

        pagoService.cargarPagos();
        pagoService.guardarEnBD();

        // PUNTO 2
        pagoService.calcularMontosTotalesPorClienteConEstadoFacturaEspecifico("Pagada");

        // PUNTO 3
        pagoService.generarReporteMetodosDePago();

        // PUNTO 4
        pagoService.mostrarClientesConFacturasPendientes();

        // PUNTO 5
        pagoService.generarReportePagosPorMes();

        // PUNTO 6
        pagoService.mostrarFacturasYMetodosDePagoAsociados();

        // PUNTO 9
        pagoService.mostrarReporteFacturasPorCliente();

        // PUNTO 10
        pagoService.listarPagosPorMetodoDePago("Tarjeta de Crédito");

        // PUNTO 11
        pagoService.listarFacturasPagadasConMetodoEspecifico("Transferencia Bancaria");

        // PUNTO 12
        pagoService.listarPagosPorCliente(1);

        // PUNTO 13
        pagoService.listarPagosPorFactura(101);

        // PUNTO 14
        pagoService.listarPagosPorMes(3);

        // PUNTO 15
        pagoService.reporteComisionesAcumuladasPorMetodoDePago();

        // PUNTO 16
        pagoService.listarMetodoDePagoMasUtilizadoPorCliente();
    }


}