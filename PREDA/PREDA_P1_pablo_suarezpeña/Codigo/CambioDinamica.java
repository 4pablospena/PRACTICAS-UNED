import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.io.File;
import java.io.IOException;

public class CambioDinamica {

    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("Error por falta de parámetros");
            solicitarEntradaUsuario();
        } else {
            ejecutarCaso(args);
        }
    }

  private static void ejecutarCaso(String[] args) {
    boolean traza = false;
    String ficheroEntrada = null;
    String ficheroSalida = null;
    boolean mostrarAyuda = false;

    for (int i = 0; i < args.length; i++) {
        switch (args[i]) {
            case "-t":
                traza = true;
                break;
            case "-h":
                validarOpcionAyuda(args);
                return; // Terminar la ejecución después de mostrar ayuda
            default:
                 if ("-h".equals(args[i])) {
                    mostrarAyuda = true;
                } else if (ficheroEntrada == null) {
                    ficheroEntrada = args[i];
                } else if (ficheroSalida == null) {
                    ficheroSalida = args[i];
                } else {
                    manejarErrorArgumentos();
                    return;
                }
                break;
        }
    }
    
      if (mostrarAyuda) {
        validarOpcionAyuda(args);
        return;
    }

    if (ficheroEntrada == null) {
        manejarErrorArgumentos();
        return;
    }

    if (ficheroSalida == null) {
        ficheroSalida = "salida.txt";
    }

    validarFichero(ficheroEntrada, ficheroSalida, traza);
}

    private static void manejarErrorArgumentos() {
        System.out.println("SINTAXIS: cambio-dinamica.jar [-t] [-h] [fichero_entrada] [fichero_salida]");
        System.out.println("[-t] Traza.");
        System.out.println("[-h] Muestra la ayuda y la sintaxis del comando.");
        System.out.println("[fichero_entrada] Nombre del fichero de entrada.");
        System.out.println("[fichero_salida]  Nombre del fichero de salida.");
        System.exit(1);
    }
    
    private static void ejecutarLogica(String ficheroEntrada, String ficheroSalida, boolean traza) {
        try {
            System.out.println("Leyendo contenido del archivo de entrada...");
            List<String> contenidoFichero = OperadorFicheros.leerFichero(ficheroEntrada);
            System.out.println("Contenido del archivo leído exitosamente.");

            if (contenidoFichero.size() != 3) {
                System.out.println("Error en el formato del fichero de entrada. Verifica los argumentos.");
                solicitarEntradaUsuario();
            }

            int n = Integer.parseInt(contenidoFichero.get(0));

            if (n <= 0) {
                System.out.println("Error: El número de monedas debe ser un entero mayor que 0.");
                solicitarEntradaUsuario();
            }

            String[] monedasString = contenidoFichero.get(1).split(" ");

            if (monedasString.length != n) {
                System.out.println("Error: El número de tipos de monedas no coincide con el número de monedas especificado.");
                solicitarEntradaUsuario();
            }

            int[] monedas = new int[n];

            for (int i = 0; i < n; i++) {
                monedas[i] = Integer.parseInt(monedasString[i]);

                if (monedas[i] <= 0 || (i > 0 && monedas[i] <= monedas[i - 1])) {
                    System.out.println("Error: El valor de las monedas debe ser mayor que 0 y consecutivo estrictamente creciente.");
                    solicitarEntradaUsuario();
                }
            }

            int cantidadDevolver = Integer.parseInt(contenidoFichero.get(2));

            if (cantidadDevolver < 0 || cantidadDevolver < monedas[0]) {
                System.out.println("Error: La cantidad a devolver debe ser mayor o igual a alguno de los valores del tipo de cambio.");
                solicitarEntradaUsuario();
            }

            InfoCambio infoCambio = new InfoCambio(cantidadDevolver, n, monedas, traza);
            infoCambio.getCambio(traza);

            if (traza) {
                System.out.println("Mostrando traza del algoritmo...");
                System.out.println(infoCambio.getTrazaSolucion());
            }

            System.out.println("Número mínimo de monedas: " + infoCambio.getCambioSolucion());
            System.out.println("Tipos de monedas devueltas: " + infoCambio.getTipoMonedas());

            if (ficheroSalida != null) {
                String contenidoGuardar1 = infoCambio.getCambioSolucion();
                String contenidoGuardar2 = infoCambio.getTipoMonedas();
                String contenidoGuardar = contenidoGuardar1 + System.getProperty("line.separator") +
                        contenidoGuardar2;
                OperadorFicheros.guardarFichero(ficheroSalida, contenidoGuardar);
                System.out.println("Resultado guardado en el archivo de salida: " + ficheroSalida);
            }
        } catch (NumberFormatException | IndexOutOfBoundsException e) {
            System.out.println("Error en el formato del fichero de entrada. Verifica los argumentos.");
            solicitarEntradaUsuario();
        } catch (IOException e) {
            System.out.println("Error al leer el fichero de entrada.");
            e.printStackTrace();
        }
    }
    private static void validarFichero(String rutaFicheroEntrada, String rutaFicheroSalida, boolean traza) {
    File ficheroEntrada = new File(rutaFicheroEntrada);

    if (!ficheroEntrada.exists() || !ficheroEntrada.isFile()) {
        // Si es la opción -t, simplemente indicamos que la traza está activada y continuamos
        if (traza) {
            ejecutarLogica(rutaFicheroEntrada, rutaFicheroSalida, traza);
            return;
        }

        System.out.println("Error: El fichero de entrada no existe o no es válido.");
        System.exit(1);
    }

    // Si el fichero de salida es null, se utiliza la ruta del fichero de entrada con un nuevo nombre
    String ficheroSalida = (rutaFicheroSalida != null) ? rutaFicheroSalida : "salida.txt";

    // Llamar a ejecutarLogica con las rutas válidas
    ejecutarLogica(ficheroEntrada.getAbsolutePath(), ficheroSalida, traza);
    }

     private static void validarOpcionAyuda(String[] args) {
        if (args.length == 2 || args.length == 4) {
            ejecutarAyuda();
        } else {
            manejarErrorArgumentos();
        }
    }

    private static void ejecutarAyuda() {
        System.out.println("SINTAXIS: cambio-dinamica.jar [-t] [-h] [fichero_entrada] [fichero_salida]");
        System.out.println("[-t] Traza.");
        System.out.println("[-h] Muestra la ayuda y la sintaxis del comando.");
        System.out.println("[fichero_entrada] Nombre del fichero de entrada.");
        System.out.println("[fichero_salida]  Nombre del fichero de salida.");
        System.exit(0);
    }

    private static void solicitarEntradaUsuario() {
        Scanner scanner = new Scanner(System.in);

        try {
            System.out.println("Introduce el número de monedas:");
            int n = scanner.nextInt();

            if (n <= 0) {
                throw new IllegalArgumentException("Error: El número de monedas debe ser un entero mayor que 0.");
            }

            int[] monedas = new int[n];
            for (int i = 0; i < n; i++) {
                System.out.println("Introduce el valor de la moneda " + (i + 1) + ":");
                monedas[i] = scanner.nextInt();

                if (monedas[i] <= 0 || (i > 0 && monedas[i] <= monedas[i - 1])) {
                    throw new IllegalArgumentException("Error: El valor de las monedas debe ser mayor que 0 y consecutivo estrictamente creciente.");
                }
            }

            System.out.println("Introduce el cambio:");
            int cantidadDevolver = scanner.nextInt();

            if (cantidadDevolver < 0 || cantidadDevolver < monedas[0]) {
                throw new IllegalArgumentException("Error: La cantidad a devolver debe ser mayor o igual a alguno de los valores del tipo de cambio.");
            }

            InfoCambio infoCambio = new InfoCambio(cantidadDevolver, n, monedas, true);
            infoCambio.getCambio(true); // Mostrar traza

            System.out.println("Número mínimo de monedas: " + infoCambio.getCambioSolucion());
            System.out.println("Tipos de monedas devueltas: " + infoCambio.getTipoMonedas());

        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
            solicitarEntradaUsuario(); // Si hay un error, solicitar la entrada nuevamente
        } finally {
            scanner.close();
        }
    }
   }
