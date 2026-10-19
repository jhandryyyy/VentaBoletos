package boleteria;

import java.util.LinkedList;
import java.util.Queue;

/**
 * Clase Bus
 * Representa la furgoneta de una ruta. Cada ruta es un objeto Bus distinto
 * y guarda a sus pasajeros en una cola: el primero en comprar es el primero en subir.
 *
 * Integrantes:
 * - Jhandry Orozco
 * - Mateo Gaibor
 */
public class Bus {

    // Asientos de cada furgoneta. Es constante: si cambia, se modifica solo aqui
    public static final int CAPACIDAD_MAXIMA = 20;

    // Maximo de boletos que puede tener una misma persona
    public static final int MAX_BOLETOS_POR_PERSONA = 5;

    // Rutas y precios (constantes)
    public static final String RUTA_GUAYAQUIL = "QUITO - GUAYAQUIL";
    public static final String RUTA_CUENCA = "QUITO - CUENCA";
    public static final String RUTA_LOJA = "QUITO - LOJA";

    public static final double PRECIO_GUAYAQUIL = 10.50;
    public static final double PRECIO_CUENCA = 12.75;
    public static final double PRECIO_LOJA = 15.00;

    // Atributos
    private String ruta;
    private double precio;
    private int boletosDisponibles;     // baja cada vez que se vende
    private Queue<Pasajeros> cola;      // pasajeros esperando subir

    // Constructor: el bus empieza vacio, con todos sus asientos libres
    public Bus(String ruta, double precio) {
        this.ruta = ruta;
        this.precio = precio;
        this.boletosDisponibles = CAPACIDAD_MAXIMA;
        this.cola = new LinkedList<>();
    }

    /**
     * Se llama al vender: pone al pasajero al final de la cola
     * y descuenta sus boletos de los asientos libres.
     * Devuelve false si ya no alcanzan los asientos.
     */
    public boolean registrar(Pasajeros pasajero) {
        int pedidos = pasajero.getNumeroBoletos();
        if (pedidos > boletosDisponibles) {
            return false;
        }
        cola.offer(pasajero);
        boletosDisponibles -= pedidos;
        return true;
    }

    /** Se llama al embarcar: saca de la cola al primer pasajero. Si no hay nadie devuelve null. */
    public Pasajeros embarcar() {
        return cola.poll();
    }

    /** Muestra quien es el proximo en subir, sin sacarlo de la cola. Si no hay nadie devuelve null. */
    public Pasajeros verSiguiente() {
        return cola.peek();
    }

    /** Lo que paga un pasajero segun el precio de este bus. */
    public double calcularTotal(Pasajeros pasajero) {
        return pasajero.getNumeroBoletos() * precio;
    }

    public boolean colaVacia() {
        return cola.isEmpty();
    }

    public int personasEnCola() {
        return cola.size();
    }

    // Get
    public String getRuta() {
        return ruta;
    }

    public double getPrecio() {
        return precio;
    }

    public int getBoletosDisponibles() {
        return boletosDisponibles;
    }

    public int getBoletosVendidos() {
        return CAPACIDAD_MAXIMA - boletosDisponibles;
    }

    public Queue<Pasajeros> getCola() {
        return cola;
    }

    // Asi se ve el bus dentro del ComboBox
    @Override
    public String toString() {
        return ruta;
    }
}
