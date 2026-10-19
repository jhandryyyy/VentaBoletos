package boleteria;

/**
 * Clase Pasajeros
 * Guarda los datos de la persona que compra y cuantos boletos lleva.
 *
 * Integrantes:
 * - Jhandry Orozco
 * - Mateo Gaibor
 */
public class Pasajeros {

    // Atributos
    private String nombre;
    private String cedula;
    private String ruta;
    private int numeroBoletos;

    // Constructor
    public Pasajeros(String nombre, String cedula, String ruta, int numeroBoletos) {
        this.nombre = nombre;
        this.cedula = cedula;
        this.ruta = ruta;
        this.numeroBoletos = numeroBoletos;
    }

    // Get y Set
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCedula() {
        return cedula;
    }

    public void setCedula(String cedula) {
        this.cedula = cedula;
    }

    public String getRuta() {
        return ruta;
    }

    public void setRuta(String ruta) {
        this.ruta = ruta;
    }

    public int getNumeroBoletos() {
        return numeroBoletos;
    }

    public void setNumeroBoletos(int numeroBoletos) {
        this.numeroBoletos = numeroBoletos;
    }

    // toString
    @Override
    public String toString() {
        return "Ruta: " + ruta
                + " | Boletos: " + numeroBoletos
                + " | Pasajero: " + nombre
                + " | C.I.: " + cedula;
    }
}
