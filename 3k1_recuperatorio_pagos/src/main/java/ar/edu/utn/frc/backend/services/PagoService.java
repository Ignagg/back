package ar.edu.utn.frc.backend.services;

import ar.edu.utn.frc.backend.entities.Cliente;
import ar.edu.utn.frc.backend.entities.Factura;
import ar.edu.utn.frc.backend.entities.MetodoPago;
import ar.edu.utn.frc.backend.entities.Pago;
import ar.edu.utn.frc.backend.repositories.PagoRepository;
import jakarta.persistence.EntityManager;

import java.io.*;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class PagoService {
    private final PagoRepository repository;
    private final ClienteService clienteServices;
    private final FacturaService facturaServices;
    private final MetodoPagoService metodoPagoServices;
    private final List<Pago> pagos;
    private final EntityManager em;

    public PagoService(EntityManager em) {
        this.repository = new PagoRepository(em);
        this.clienteServices = new ClienteService(em);
        this.facturaServices = new FacturaService(em);
        this.metodoPagoServices = new MetodoPagoService(em);
        this.pagos = new ArrayList<>();
        this.em = em;
    }

    private final String DIRECCION_CSV = "C:\\Mio\\Facultad - UTN\\3er_anio\\Backend_de_Aplicaciones\\3k1_recuperatorio_pagos\\3k1_recuperatorio_pagos\\pagos.csv";

    public void cargarPagos() {
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(DIRECCION_CSV))) {
            String line = bufferedReader.readLine(); // Leer la cabecera
            while ((line = bufferedReader.readLine()) != null) {
                Pago pago = lineaPago(line);
                // Agregar el pago a la lista en memoria
                pagos.add(pago);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Pago lineaPago(String linea) {
        String[] campos = linea.split("\\|");
        int pago_id = Integer.parseInt(campos[0]);
        Date pago_fecha = parseFecha(campos[1]);
        BigDecimal pago_monto = parseMonto(campos[2]);
        String pago_estado = campos[3];
        int cliente_id = Integer.parseInt(campos[4]);
        String cliente_nombre = campos[5];
        String cliente_email = campos[6];
        String cliente_telefono = campos[7];
        String cliente_direccion = campos[8];
        int factura_id = Integer.parseInt(campos[9]);
        BigDecimal factura_monto_total = parseMonto(campos[10]);
        Date factura_fecha_emision = parseFecha(campos[11]);
        Date factura_fecha_vencimiento = parseFecha(campos[12]);
        String factura_descripcion = campos[13];
        String factura_estado = campos[14];
        int metodo_pago_id = Integer.parseInt(campos[15]);
        String metodo_pago_nombre = campos[16];
        String metodo_pago_detalles = campos[17];
        BigDecimal metodo_pago_comision = parseMonto(campos[18]);

        Cliente cliente = clienteServices.getOrCreateCliente(cliente_id, cliente_nombre, cliente_email, cliente_telefono, cliente_direccion);
        Factura factura = facturaServices.getOrCreateFactura(factura_id, factura_monto_total, factura_fecha_emision, factura_fecha_vencimiento, factura_descripcion, factura_estado);
        MetodoPago metodoPago = metodoPagoServices.getOrCreateMetodoPago(metodo_pago_id, metodo_pago_nombre, metodo_pago_detalles, metodo_pago_comision);
        Pago pago = new Pago(pago_id, pago_monto, pago_estado, pago_fecha, metodoPago, factura, cliente);
        return pago;
    }

    public Date parseFecha(String fechaTexto) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        try {
            Date fecha = formatter.parse(fechaTexto);
            return fecha;
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    public BigDecimal parseMonto(String monto) {
        try {
            return new BigDecimal(monto);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return null;
        }
    }

    // 2GPT -  Cálculo de montos totales por cliente con estado de factura específico
    //Determinar el monto total pagado por cada cliente, considerando únicamente los pagos relacionados con facturas en estado "Pagada".

    public void calcularMontosTotalesPorClienteConEstadoFacturaEspecifico(String estadoFactura) {
        System.out.println("PUNTO 2");
        Map<Cliente, BigDecimal> montosTotalesPorCliente = new HashMap<>();
        for (Pago pago : pagos) {
            if (pago.getFactura().getEstado().equals(estadoFactura)) {
                Cliente cliente = pago.getCliente();
                BigDecimal montoTotal = montosTotalesPorCliente.getOrDefault(cliente, BigDecimal.ZERO);
                montoTotal = montoTotal.add(pago.getMonto());
                montosTotalesPorCliente.put(cliente, montoTotal);
            }
        }
        for (Map.Entry<Cliente, BigDecimal> entry : montosTotalesPorCliente.entrySet()) {
            System.out.println("Cliente: " + entry.getKey().getNombre() + " Monto total: " + entry.getValue());
        }
    }


    //3GPT - Reporte de métodos de pago
    //Generar un archivo de texto separado por comas (reporte_metodos_pago.csv) que incluya el nombre del cliente,
    // el metodo de pago utilizado y el monto total pagado agrupado por metodo de pago.
    public void generarReporteMetodosDePago() {
        System.out.println("PUNTO 3");
        Map<MetodoPago, Map<Cliente, BigDecimal>> montosTotalesPorMetodoPago = new HashMap<>();

        for (Pago pago : pagos) {
            MetodoPago metodoPago = pago.getMetodoPago();
            Cliente cliente = pago.getCliente();
            montosTotalesPorMetodoPago.putIfAbsent(metodoPago, new HashMap<>());
            Map<Cliente, BigDecimal> montosPorCliente = montosTotalesPorMetodoPago.get(metodoPago);
            BigDecimal montoTotal = montosPorCliente.getOrDefault(cliente, BigDecimal.ZERO);
            montoTotal = montoTotal.add(pago.getMonto());
            montosPorCliente.put(cliente, montoTotal);
        }

        try {
            String direccion = "C:\\Mio\\Facultad - UTN\\3er_anio\\Backend_de_Aplicaciones\\3k1_recuperatorio_pagos\\3k1_recuperatorio_pagos\\reporte_metodos_pago.csv";
            FileWriter fileWriter = new FileWriter(direccion);
            PrintWriter printWriter = new PrintWriter(fileWriter);
            for (Map.Entry<MetodoPago, Map<Cliente, BigDecimal>> entry : montosTotalesPorMetodoPago.entrySet()) {
                MetodoPago metodoPago = entry.getKey();
                for (Map.Entry<Cliente, BigDecimal> clienteEntry : entry.getValue().entrySet()) {
                    Cliente cliente = clienteEntry.getKey();
                    BigDecimal montoTotal = clienteEntry.getValue();
                    printWriter.println(cliente.getNombre() + "," + metodoPago.getNombre() + "," + montoTotal);
                }
            }
            printWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /** ESTE GENERA UN CSV CON EL METODO DE PAGO Y CUANTO SE PAGO CON ESE METODO
     public void generarReporteMetodosDePago() {
     System.out.println("PUNTO 3");
     Map<MetodoPago, BigDecimal> montosTotalesPorMetodoPago = new HashMap<>();
     for (Pago pago : pagos) {
     MetodoPago metodoPago = pago.getMetodoPago();
     BigDecimal montoTotal = montosTotalesPorMetodoPago.getOrDefault(metodoPago, BigDecimal.ZERO);
     montoTotal = montoTotal.add(pago.getMonto());
     montosTotalesPorMetodoPago.put(metodoPago, montoTotal);
     }
     try {
     String direccion = "C:\\Mio\\Facultad - UTN\\3er_anio\\Backend_de_Aplicaciones\\3k1_recuperatorio_pagos\\3k1_recuperatorio_pagos\\reporte_metodos_pago.csv";
     FileWriter fileWriter = new FileWriter(direccion);
     PrintWriter printWriter = new PrintWriter(fileWriter);
     for (Map.Entry<MetodoPago, BigDecimal> entry : montosTotalesPorMetodoPago.entrySet()) {
     printWriter.println(entry.getKey().getNombre() + "," + entry.getValue());
     }
     printWriter.close();
     } catch (IOException e) {
     e.printStackTrace();
     }
     }
     **/
    //4GPT - Clientes con facturas pendientes
    //Mostrar por pantalla la lista de clientes con al menos una factura en estado "Emitida".
    // La lista debe incluir el nombre del cliente, el total de montos emitidas y debe estar ordenada de forma descendente por dicho total.

    public void mostrarClientesConFacturasPendientes() {
        System.out.println("PUNTO 4");
        Map<Cliente, BigDecimal> montosPendientesPorCliente = new HashMap<>();
        for (Pago pago : pagos) {
            if (pago.getFactura().getEstado().equals("Emitida")) {
                Cliente cliente = pago.getCliente();
                BigDecimal montoPendiente = montosPendientesPorCliente.getOrDefault(cliente, BigDecimal.ZERO);
                montoPendiente = montoPendiente.add(pago.getMonto());
                montosPendientesPorCliente.put(cliente, montoPendiente);
            }
        }
        List<Map.Entry<Cliente, BigDecimal>> lista = new ArrayList<>(montosPendientesPorCliente.entrySet());
        lista.sort((o1, o2) -> o2.getValue().compareTo(o1.getValue()));
        for (Map.Entry<Cliente, BigDecimal> entry : lista) {
            System.out.println("Cliente: " + entry.getKey().getNombre() + " Monto pendiente: " + entry.getValue());
        }
    }

    // 5GPT - Pagos por mes
    //Crear un reporte que muestre, para cada mes, el total de pagos realizados tomando como fecha el PAGO_FECHA.
    // El reporte debe incluir el mes, el monto total y la cantidad de pagos.

    public void generarReportePagosPorMes() {
        System.out.println("PUNTO 5");
        Map<Integer, BigDecimal> montosTotalesPorMes = new HashMap<>();
        Map<Integer, Integer> cantidadPagosPorMes = new HashMap<>();
        for (Pago pago : pagos) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(pago.getFecha_pago()); // Asumiendo que getFecha_pago() devuelve un objeto Date
            int mes = calendar.get(Calendar.MONTH) + 1; // Sumando 1 al mes para que sea 1-based (Enero = 1, Diciembre = 12)
            BigDecimal montoTotal = montosTotalesPorMes.getOrDefault(mes, BigDecimal.ZERO);
            montoTotal = montoTotal.add(pago.getMonto());
            montosTotalesPorMes.put(mes, montoTotal);
            int cantidadPagos = cantidadPagosPorMes.getOrDefault(mes, 0);
            cantidadPagos++;
            cantidadPagosPorMes.put(mes, cantidadPagos);
        }
        for (Map.Entry<Integer, BigDecimal> entry : montosTotalesPorMes.entrySet()) {
            System.out.println("Mes: " + entry.getKey() + " Monto total: " + entry.getValue() + " Cantidad de pagos: " +
                    cantidadPagosPorMes.get(entry.getKey()));
        }
    }

    // 6GPT - Facturas y métodos de pago asociados
    //Generar una lista que muestre el ID de cada factura junto con el metodo de pago utilizado, su comisión,
    // y el monto pagado. Ordenar los resultados por el monto pagado en orden descendente.

    public void mostrarFacturasYMetodosDePagoAsociados() {
        System.out.println("PUNTO 6");
        List<Pago> pagosOrdenadosPorMonto = new ArrayList<>(pagos);
        pagosOrdenadosPorMonto.sort((o1, o2) -> o2.getMonto().compareTo(o1.getMonto()));
        for (Pago pago : pagosOrdenadosPorMonto) {
            System.out.println("ID Factura: " + pago.getFactura().getID() + " Metodo de pago: " + pago.getMetodoPago().getNombre() +
                    " Comision: " + pago.getMetodoPago().getComision() + " Monto pagado: " + pago.getMonto());
        }
    }

    /**

     // 7GPT -Crea un procedimiento para registrar un nuevo pago en la tabla Pagos. Al registrar el pago:
     //Verifica si el monto total de la factura ha sido pagado completamente.
     //Si el monto total ha sido cubierto, actualiza el estado de la factura a "Pagada".

     public void registrarNuevoPago(int pago_id, BigDecimal pago_monto, String pago_estado, Date pago_fecha, int cliente_id, int factura_id, int metodo_pago_id) {
     Cliente cliente = clienteServices.getOrCreateCliente(cliente_id, null, null, null, null);
     Factura factura = facturaServices.getOrCreateFactura(factura_id, null, null, null, null, null);
     MetodoPago metodoPago = metodoPagoServices.getOrCreateMetodoPago(metodo_pago_id, null, null, null);

     Pago nuevoPago = new Pago(pago_id, pago_monto, pago_estado, pago_fecha, metodoPago, factura, cliente);
     pagos.add(nuevoPago);

     em.getTransaction().begin();
     em.persist(nuevoPago);
     em.getTransaction().commit();

     // Check if the total amount of the invoice has been fully paid
     BigDecimal totalPagado = pagos.stream()
     .filter(p -> p.getFactura().getID() == factura_id)
     .map(Pago::getMonto)
     .reduce(BigDecimal.ZERO, BigDecimal::add);

     if (totalPagado.compareTo(factura.getMonto_total()) >= 0) {
     factura.setEstado("Pagada");
     em.getTransaction().begin();
     em.merge(factura);
     em.getTransaction().commit();
     }
     }

     // 8GPT - Inserta un nuevo registro en la tabla Pagos con la información correspondiente al cliente, factura y
     // metodo de pago.

     public void insertarNuevoPago(int pago_id, BigDecimal pago_monto, String pago_estado, Date pago_fecha, int cliente_id, int factura_id, int metodo_pago_id) {
     Cliente cliente = clienteServices.getOrCreateCliente(cliente_id, null, null, null, null);
     Factura factura = facturaServices.getOrCreateFactura(factura_id, null, null, null, null, null);
     MetodoPago metodoPago = metodoPagoServices.getOrCreateMetodoPago(metodo_pago_id, null, null, null);

     Pago nuevoPago = new Pago(pago_id, pago_monto, pago_estado, pago_fecha, metodoPago, factura, cliente);
     pagos.add(nuevoPago);

     em.getTransaction().begin();
     em.persist(nuevoPago);
     em.getTransaction().commit();
     }
     **/

    // 9GPT - Crea una consulta que muestre:
    //El nombre del cliente.
    //El número de facturas en estado "Pagada".
    //El número de facturas en estado "Cancelada".
    //El reporte debe estar agrupado por cliente.

    public void mostrarReporteFacturasPorCliente() {
        Map<Cliente, Map<String, Long>> reporte = new HashMap<>();
        System.out.println("PUNTO 9");

        for (Pago pago : pagos) {
            Cliente cliente = pago.getCliente();
            String estadoFactura = pago.getFactura().getEstado();

            reporte.putIfAbsent(cliente, new HashMap<>());
            Map<String, Long> estados = reporte.get(cliente);
            estados.put(estadoFactura, estados.getOrDefault(estadoFactura, 0L) + 1);
        }

        for (Map.Entry<Cliente, Map<String, Long>> entry : reporte.entrySet()) {
            Cliente cliente = entry.getKey();
            Map<String, Long> estados = entry.getValue();
            long pagadas = estados.getOrDefault("Pagada", 0L);
            long pendientes = estados.getOrDefault("Cancelada", 0L);

            System.out.println("Cliente: " + cliente.getNombre() + " - Facturas Pagadas: " + pagadas + " - Facturas Canceladas: " + pendientes);
        }
    }

    // 10GPT - Listado de pagos por metodo de pago
    //Genera una consulta que muestre:
    //El nombre del cliente.
    //El monto total pagado.
    //La fecha del último pago realizado.
    //Filtra los resultados para incluir solo los pagos realizados con un metodo de pago específico (por ejemplo, "Tarjeta de Crédito").

    public void listarPagosPorMetodoDePago(String metodoPagoEspecifico) {
        System.out.println("PUNTO 10");
        Map<Cliente, Pago> ultimoPagoPorCliente = new HashMap<>();
        Map<Cliente, BigDecimal> montoTotalPorCliente = new HashMap<>();

        for (Pago pago : pagos) {
            if (pago.getMetodoPago().getNombre().equals(metodoPagoEspecifico)) {
                Cliente cliente = pago.getCliente();
                montoTotalPorCliente.put(cliente, montoTotalPorCliente.getOrDefault(cliente, BigDecimal.ZERO).add(pago.getMonto()));

                Pago ultimoPago = ultimoPagoPorCliente.get(cliente);
                if (ultimoPago == null || pago.getFecha_pago().after(ultimoPago.getFecha_pago())) {
                    ultimoPagoPorCliente.put(cliente, pago);
                }
            }
        }

        for (Map.Entry<Cliente, Pago> entry : ultimoPagoPorCliente.entrySet()) {
            Cliente cliente = entry.getKey();
            Pago ultimoPago = entry.getValue();
            BigDecimal montoTotal = montoTotalPorCliente.get(cliente);

            System.out.println("Cliente: " + cliente.getNombre() + " - Monto Total Pagado: " + montoTotal + " - Fecha del Último Pago: " + ultimoPago.getFecha_pago());
        }
    }

    //11GPT - Facturas pagadas con un metodo específico
    //Diseña un listado que muestre:
    //El ID de las facturas.
    //La descripción de cada factura.
    //El monto total de cada factura.
    //Incluye únicamente las facturas cuyo estado sea "Pagada" y que hayan sido saldadas utilizando un
    // metodo de pago determinado (por ejemplo, "Transferencia Bancaria").

    public void listarFacturasPagadasConMetodoEspecifico(String metodoPagoEspecifico) {
        System.out.println("PUNTO 11");
        for (Pago pago : pagos) {
            if (pago.getFactura().getEstado().equals("Pagada") && pago.getMetodoPago().getNombre().equals(metodoPagoEspecifico)) {
                System.out.println("ID Factura: " + pago.getFactura().getID() + " - Descripción: " + pago.getFactura().getDescripcion() + " - Monto Total: " + pago.getFactura().getMonto_total());
            }
        }
    }

    // 12GPT - Listado de pagos por cliente

    public void listarPagosPorCliente(int cliente_id) {
        System.out.println("PUNTO 12");
        for (Pago pago : pagos) {
            if (pago.getCliente().getID() == cliente_id) {
                System.out.println("ID Pago: " + pago.getID() + " - Monto: " + pago.getMonto() + " - Estado: " + pago.getEstado());
            }
        }
    }

    // 13GPT - Listado de pagos por factura

    public void listarPagosPorFactura(int factura_id) {
        System.out.println("PUNTO 13");
        for (Pago pago : pagos) {
            if (pago.getFactura().getID() == factura_id) {
                System.out.println("ID Pago: " + pago.getID() + " - Monto: " + pago.getMonto() + " - Estado: " + pago.getEstado());
            }
        }
    }

    // 14GPT - Listado de pagos por mes

    public void listarPagosPorMes(int mes) {
        System.out.println("PUNTO 14");
        for (Pago pago : pagos) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(pago.getFecha_pago());
            if (calendar.get(Calendar.MONTH) + 1 == mes) {
                System.out.println("ID Pago: " + pago.getID() + " - Monto: " + pago.getMonto() + " - Estado: " + pago.getEstado());
            }
        }
    }

    // 15GPT - Reporte de comisiones acumuladas por metodo de pago
    //Genera un listado que incluya:
    //El nombre del metodo de pago.
    //La comisión total acumulada generada por ese metodo.

    public void reporteComisionesAcumuladasPorMetodoDePago() {
        System.out.println("PUNTO 15");
        Map<MetodoPago, BigDecimal> comisionesAcumuladasPorMetodoPago = new HashMap<>();
        for (Pago pago : pagos) {
            MetodoPago metodoPago = pago.getMetodoPago();
            BigDecimal comisionAcumulada = comisionesAcumuladasPorMetodoPago.getOrDefault(metodoPago, BigDecimal.ZERO);
            comisionAcumulada = comisionAcumulada.add(pago.getMonto().multiply(metodoPago.getComision()));
            comisionesAcumuladasPorMetodoPago.put(metodoPago, comisionAcumulada);
        }
        for (Map.Entry<MetodoPago, BigDecimal> entry : comisionesAcumuladasPorMetodoPago.entrySet()) {
            System.out.println("Metodo de pago: " + entry.getKey().getNombre() + " - Comisión acumulada: " + entry.getValue());
        }
    }

    // 16GPT -
    //Generar un listado que muestre:
    //
    //El nombre del cliente.
    //El metodo de pago que más utiliza.
    //Filtra los clientes que han realizado al menos 5 pagos.

    public void listarMetodoDePagoMasUtilizadoPorCliente() {
        System.out.println("PUNTO 16");
        Map<Cliente, Map<MetodoPago, Integer>> cantidadPagosPorMetodoPago = new HashMap<>();
        for (Pago pago : pagos) {
            Cliente cliente = pago.getCliente();
            cantidadPagosPorMetodoPago.putIfAbsent(cliente, new HashMap<>());
            Map<MetodoPago, Integer> cantidadPagos = cantidadPagosPorMetodoPago.get(cliente);
            cantidadPagos.put(pago.getMetodoPago(), cantidadPagos.getOrDefault(pago.getMetodoPago(), 0) + 1);
        }
        for (Map.Entry<Cliente, Map<MetodoPago, Integer>> entry : cantidadPagosPorMetodoPago.entrySet()) {
            Cliente cliente = entry.getKey();
            Map<MetodoPago, Integer> cantidadPagos = entry.getValue();
            if (cantidadPagos.size() >= 1) {
                MetodoPago metodoPagoMasUtilizado = cantidadPagos.entrySet().stream()
                        .max(Comparator.comparingInt(Map.Entry::getValue))
                        .map(Map.Entry::getKey)
                        .orElse(null);
                System.out.println("Cliente: " + cliente.getNombre() + " - Metodo de pago más utilizado: " + metodoPagoMasUtilizado.getNombre());
            }
        }
    }


    // funcion para guardar en la base de datos
    public void guardarEnBD() {
        // si la base de datos NO esta vacia, no guardo nada
        repository.saveAll(pagos); // Guardo todos los pagos en la base de datos
    }


}