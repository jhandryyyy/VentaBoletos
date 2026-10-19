package boleteria;

import java.util.LinkedList;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

/**
 * Ventana de venta de boletos.
 * El diseno esta en VentanaVenta.form (GUI Designer de IntelliJ).
 *
 * Integrantes:
 * - Jhandry Orozco
 * - Mateo Gaibor
 */
public class VentanaVenta extends JFrame {

    // componentes del .form
    private JPanel panelPrincipal;
    private JComboBox<Bus> cmbRuta;
    private JTextField txtCedula;
    private JTextField txtNombre;
    private JTextField txtBoletos;
    private JButton btnComprar;
    private JTextArea txtDetalle;
    private JLabel lblGuayaquil;
    private JLabel lblCuenca;
    private JLabel lblLoja;
    private JLabel lblTotal;
    private JButton btnEmbarcarGye;
    private JButton btnEmbarcarCue;
    private JButton btnEmbarcarLoj;

    // un bus (objeto) por cada ruta
    private Bus busGuayaquil = new Bus(Bus.RUTA_GUAYAQUIL, Bus.PRECIO_GUAYAQUIL);
    private Bus busCuenca = new Bus(Bus.RUTA_CUENCA, Bus.PRECIO_CUENCA);
    private Bus busLoja = new Bus(Bus.RUTA_LOJA, Bus.PRECIO_LOJA);

    // todas las ventas: sirve para contar los boletos de cada cedula
    // (al embarcar el pasajero sale de la cola, pero sus boletos siguen contando)
    private LinkedList<Pasajeros> historialVentas = new LinkedList<>();

    private double totalRecaudado = 0;   // se suma con cada venta

    public VentanaVenta() {
        setTitle("Venta de boletos");
        setContentPane(panelPrincipal);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        cmbRuta.addItem(busGuayaquil);
        cmbRuta.addItem(busCuenca);
        cmbRuta.addItem(busLoja);

        btnComprar.addActionListener(e -> comprar());
        btnEmbarcarGye.addActionListener(e -> embarcar(busGuayaquil));
        btnEmbarcarCue.addActionListener(e -> embarcar(busCuenca));
        btnEmbarcarLoj.addActionListener(e -> embarcar(busLoja));

        refrescarLabels();
        pack();
        setLocationRelativeTo(null);
    }

    // ======================== BOTON COMPRAR ========================
    private void comprar() {
        Bus bus = (Bus) cmbRuta.getSelectedItem();
        String cedula = txtCedula.getText().trim();
        String nombre = txtNombre.getText().trim();
        String cantidadTexto = txtBoletos.getText().trim();

        // 1. que no falte ningun dato
        if (cedula.isEmpty() || nombre.isEmpty() || cantidadTexto.isEmpty()) {
            avisar("Complete la cédula, el nombre y el número de boletos.");
            return;
        }

        // 2. cedula: 10 digitos y ecuatoriana valida
        if (cedula.length() != 10 || !soloDigitos(cedula)) {
            avisar("La cédula debe tener 10 dígitos.");
            return;
        }
        if (!esCedulaEcuatoriana(cedula)) {
            avisar("La cédula " + cedula + " no es válida en Ecuador.");
            return;
        }

        // 3. nombre: solo letras, y si la cedula ya compro, el mismo nombre
        if (!nombre.matches("[a-zA-ZáéíóúÁÉÍÓÚñÑ ]+")) {
            avisar("El nombre solo puede llevar letras.");
            return;
        }
        String nombreRegistrado = nombreDeCedula(cedula);
        if (nombreRegistrado != null && !nombreRegistrado.equalsIgnoreCase(nombre)) {
            avisar("Esa cédula ya compró a nombre de " + nombreRegistrado + ".");
            return;
        }

        // 4. numero de boletos: entero entre 1 y 5
        int cantidad;
        try {
            cantidad = Integer.parseInt(cantidadTexto);
        } catch (NumberFormatException ex) {
            avisar("En número de boletos escriba un número entero.");
            return;
        }
        if (cantidad < 1 || cantidad > Bus.MAX_BOLETOS_POR_PERSONA) {
            avisar("Por compra se permiten de 1 a " + Bus.MAX_BOLETOS_POR_PERSONA + " boletos.");
            return;
        }

        // 5. maximo 5 boletos por persona, contando lo que ya compro antes
        int compradosAntes = boletosDeCedula(cedula);
        int leQuedan = Bus.MAX_BOLETOS_POR_PERSONA - compradosAntes;
        if (leQuedan == 0) {
            avisar("Esta persona ya tiene " + Bus.MAX_BOLETOS_POR_PERSONA + " boletos y no puede comprar más.");
            return;
        }
        if (cantidad > leQuedan) {
            avisar("Esta persona ya compró " + compradosAntes + " boleto(s); solo puede llevar " + leQuedan + " más.");
            return;
        }

        // 6. capacidad del bus (20 asientos)
        if (bus.getBoletosDisponibles() == 0) {
            avisar("No quedan asientos en el bus " + bus.getRuta() + ".");
            return;
        }
        if (cantidad > bus.getBoletosDisponibles()) {
            avisar("En el bus " + bus.getRuta() + " quedan solo " + bus.getBoletosDisponibles() + " asiento(s).");
            return;
        }

        // todo esta bien: el pasajero entra a la cola del bus
        Pasajeros pasajero = new Pasajeros(nombre, cedula, bus.getRuta(), cantidad);
        bus.registrar(pasajero);
        historialVentas.add(pasajero);

        double total = bus.calcularTotal(pasajero);
        totalRecaudado += total;

        // detalle en el TextArea: ruta, cantidad de boletos, nombre y precio total
        txtDetalle.append(String.format("%s | %d boleto(s) | %s | $%.2f%n",
                bus.getRuta(), cantidad, nombre, total));

        refrescarLabels();
        limpiar();
    }

    // ======================== BOTONES EMBARCAR ========================
    // el primero de la cola sube al bus y sale de la cola
    private void embarcar(Bus bus) {
        Pasajeros sube = bus.embarcar();
        if (sube == null) {
            avisar("Nadie está esperando el bus " + bus.getRuta() + ".");
            return;
        }
        txtDetalle.append("Embarcó en " + bus.getRuta() + ": " + sube.getNombre() + "\n");
    }

    // ======================== LABELS ========================
    private void refrescarLabels() {
        lblGuayaquil.setText(textoRuta(busGuayaquil));
        lblCuenca.setText(textoRuta(busCuenca));
        lblLoja.setText(textoRuta(busLoja));
        lblTotal.setText(String.format("Total recaudado: $%.2f", totalRecaudado));
    }

    private String textoRuta(Bus bus) {
        return bus.getRuta() + ":  vendidos " + bus.getBoletosVendidos()
                + "  |  disponibles " + bus.getBoletosDisponibles();
    }

    // ======================== AYUDAS ========================

    // boletos que ya compro una cedula en las 3 rutas
    private int boletosDeCedula(String cedula) {
        int suma = 0;
        for (Pasajeros p : historialVentas) {
            if (p.getCedula().equals(cedula)) {
                suma += p.getNumeroBoletos();
            }
        }
        return suma;
    }

    // nombre con el que compro antes una cedula (null si nunca compro)
    private String nombreDeCedula(String cedula) {
        for (Pasajeros p : historialVentas) {
            if (p.getCedula().equals(cedula)) {
                return p.getNombre();
            }
        }
        return null;
    }

    private boolean soloDigitos(String texto) {
        return texto.matches("\\d+");
    }

    /*
     * Cedula ecuatoriana (modulo 10):
     *  - los dos primeros numeros son la provincia: 01 a 24, o 30 (exterior)
     *  - el tercer numero es menor que 6
     *  - a los 9 primeros se los multiplica por 2 y 1 alternadamente (2,1,2,1...),
     *    si un resultado pasa de 9 se le resta 9, y se suma todo
     *  - el decimo numero debe ser lo que le falta a esa suma para la siguiente decena
     */
    private boolean esCedulaEcuatoriana(String cedula) {
        int provincia = Integer.parseInt(cedula.substring(0, 2));
        if (!((provincia >= 1 && provincia <= 24) || provincia == 30)) {
            return false;
        }
        if (cedula.charAt(2) - '0' > 5) {
            return false;
        }
        int suma = 0;
        for (int i = 0; i < 9; i++) {
            int n = cedula.charAt(i) - '0';
            if (i % 2 == 0) {
                n *= 2;
                if (n > 9) {
                    n -= 9;
                }
            }
            suma += n;
        }
        int esperado = (suma % 10 == 0) ? 0 : 10 - (suma % 10);
        return esperado == cedula.charAt(9) - '0';
    }

    private void avisar(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje);
    }

    private void limpiar() {
        txtCedula.setText("");
        txtNombre.setText("");
        txtBoletos.setText("");
    }

    public static void main(String[] args) {
        new VentanaVenta().setVisible(true);
    }
}
