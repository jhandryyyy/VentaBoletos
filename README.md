# Venta de Boletos · Rutas desde Quito

Taller de Programación III. Programa en Java (Swing + GUI Designer de IntelliJ) para vender boletos de furgoneta en tres rutas.

## Integrantes
- Jhandry Orozco
- Mateo Gaibor

## Rutas

| Ruta | Valor del boleto |
|---|---|
| QUITO - GUAYAQUIL | $10.50 |
| QUITO - CUENCA | $12.75 |
| QUITO - LOJA | $15.00 |

Cada furgoneta lleva **20 asientos** por viaje.

## Clases

- **`Pasajeros`**: guarda el nombre, la cédula, la ruta y el número de boletos. Tiene constructor, get, set y `toString`.
- **`Bus`**: representa la furgoneta de una ruta y guarda a sus pasajeros en una cola (`Queue` con `LinkedList`).
  - `registrar()`: al vender, el pasajero entra al final de la cola y se restan sus boletos de los asientos.
  - `embarcar()`: el primero de la cola sube y sale de la cola.
  - `verSiguiente()`: muestra quién sube después, sin sacarlo de la cola.
  - Constantes: `CAPACIDAD_MAXIMA = 20`, `MAX_BOLETOS_POR_PERSONA = 5`, las rutas y sus precios.
  - Variable `boletosDisponibles`: baja con cada venta.
- **`VentanaVenta`** (`.form` + `.java`): es la interfaz y realiza las validaciones.

## Qué hace la ventana

1. **Datos de la compra**: ComboBox con las 3 rutas, campos de cédula, nombre y número de boletos, y el botón **Comprar**.
2. **Validaciones al comprar**:
   - ningún campo puede quedar vacío;
   - la cédula debe tener 10 dígitos y ser ecuatoriana válida (provincia, tercer dígito y dígito verificador);
   - el nombre solo lleva letras, y una cédula siempre debe usar el mismo nombre;
   - se compran de 1 a 5 boletos por vez (no se aceptan texto, 0 ni negativos);
   - cada persona tiene como **máximo 5 boletos en total**, sumando sus compras anteriores. Si ya tiene 2 y pide 4, se le avisa que solo puede llevar 3 más;
   - no se venden más boletos de los asientos que quedan en el bus.
3. **TextArea**: por cada venta muestra la ruta, la cantidad de boletos, el nombre y el total pagado. También registra quién subió a cada bus.
4. **Labels**: uno por ruta, con los boletos vendidos y los disponibles, y otro con el total recaudado.
5. **Embarque**: los 3 buses son 3 objetos. Junto al label de cada ruta está su botón **Embarcar**, que saca al primero de la cola.

## Cómo ejecutar

1. En IntelliJ IDEA, ir a *File → Open* y elegir **esta carpeta**, la que tiene `src`, `.idea` y este README.
2. Arriba a la derecha ya aparece **VentanaVenta**. Presionar  (Run).

El proyecto ya trae la configuración de IntelliJ: la carpeta del código y la ejecución. Hay que ejecutarlo desde IntelliJ, porque es IntelliJ el que compila el `.form`. Si IntelliJ pide un JDK, elegir el que esté instalado (17 o más nuevo).

Cédulas válidas para probar: `1712345675`, `1723456784`, `0102030400`, `0908070600`, `1100220035`.
