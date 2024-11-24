import java.io.*;
import java.util.List;
import java.util.Scanner;
public class Robot {
    public static void main(String[] args) {
        try {
            String archivoEntrada = null;
            String archivoSalida = "salida.txt";
            boolean traza = false;

            // Procesar los argumentos
            boolean mostrarAyuda = false;
            boolean ordenCorrecto = true; // Flag para verificar el orden correcto
            for (int i = 0; i < args.length; i++) {
                String arg = args[i];
                switch (arg) {
                    case "-t":
                        traza = true;
                        break;
                    case "-h":
                        mostrarAyuda();
                        return; // Salir después de mostrar la ayuda
                    default:
                        if (archivoEntrada == null) {
                            archivoEntrada = arg;
                        } else if (archivoSalida.equals("salida.txt")) {
                            archivoSalida = arg;
                        } else {
                            // Argumentos fuera de orden
                            ordenCorrecto = false;
                        }
                        break;
                }
            }

            // Verificar el orden de los argumentos
            if (!ordenCorrecto) {
                System.out.println("Error: Argumentos en orden incorrecto.");
                mostrarAyuda();
                return;
            }

            // Mostrar la ayuda si se solicita
            if (mostrarAyuda) {
                mostrarAyuda();
            } else {
                // Resto del código sin cambios...

                char[][] edificio;

                if (archivoEntrada != null) {
                    // Leer el edificio desde el archivo de entrada
                    try {
                        edificio = leerEdificio(archivoEntrada);
                    } catch (FileNotFoundException e) {
                        // El archivo de entrada no existe, mostrar mensaje y salir
                        System.out.println("Error: El archivo de entrada no existe.");
                        return;
                    } catch (IOException e) {
                        // Otro error al leer el archivo de entrada, mostrar mensaje y salir
                        System.out.println("Error: No se pudo leer el archivo de entrada.");
                        return;
                    }
                } else {
                    // Introducir los datos por la entrada estándar.
                    System.out.println("Introduce el número de filas: ");
                    int filas = leerEntero();

                    System.out.println("Introduce el número de columnas: ");
                    int columnas = leerEntero();

                    edificio = new char[filas][columnas];

                    System.out.println("Introduce la matriz (L: paso libre, E: paso estrecho, T: tornillo):");
                    for (int i = 0; i < filas; i++) {
                        for (int j = 0; j < columnas; j++) {
                            System.out.println("Fila " + (i + 1) + ", Columna " + (j + 1) + ": ");
                            edificio[i][j] = leerCaracter();
                        }
                    }
                }

                // Resto del código sin cambios...
                Edificio laberinto = new Edificio(edificio);

                boolean exito = laberinto.buscarTornillo(traza);

                if (exito) {
                    List<Casilla> solucion = laberinto.getSolucion();
                    List<Casilla> caminoSeguido = laberinto.getCaminoSeguido();

                    // Mostrar por la salida estándar el resultado de la ejecución del programa.
                    mostrarSolucion(solucion);

                    // Manejar la creación y posible sobreescritura del archivo de salida
                    File fileSalida = new File(archivoSalida);
                    if (fileSalida.exists() && fileSalida.isFile()) {
                        // Si salida.txt ya existe: se sobrescribirá el fichero de salida existente.
                        escribirSolucion(solucion, caminoSeguido, archivoSalida);
                    } else {
                        // Si salida.txt no existe se genera el fichero salida.txt
                        escribirSolucion(solucion, caminoSeguido, archivoSalida);
                    }
                } else {
                    System.out.println("No se encontró el tornillo. Fin de la exploración.");
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private static int leerEntero() {
        Scanner scanner = new Scanner(System.in);
        return scanner.nextInt();
    }

    private static char leerCaracter() {
        Scanner scanner = new Scanner(System.in);
        return scanner.next().charAt(0);
    }


    private static char[][] procesarEntradaEstándar() {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Introduce el número de filas: ");
        int filas = scanner.nextInt();

        System.out.print("Introduce el número de columnas: ");
        int columnas = scanner.nextInt();

        char[][] edificio = new char[filas][columnas];

        System.out.println("Introduce la matriz (L: paso libre, E: paso estrecho, T: tornillo):");
        for (int i = 0; i < filas; i++) {
            for (int j = 0; j < columnas; j++) {
                edificio[i][j] = scanner.next().charAt(0);
            }
        }

        return edificio;
    }

    private static char[][] leerEdificio(String fileName) throws IOException {
        BufferedReader reader = null;

        try {
            reader = new BufferedReader(new FileReader(fileName));

            // Lee el número de filas y columnas
            int filas = Integer.parseInt(reader.readLine());
            int columnas = Integer.parseInt(reader.readLine());

            char[][] edificio = new char[filas][columnas];

            // Lee la matriz desde el archivo
            for (int i = 0; i < filas; i++) {
                String linea = reader.readLine();
                for (int j = 0; j < columnas; j++) {
                    edificio[i][j] = linea.charAt(j * 2);  // Salta el espacio en blanco
                }
            }

            return edificio;
        } finally {
            if (reader != null) {
                reader.close();
            }
        }
    }

    private static void escribirSolucion(List<Casilla> solucion, List<Casilla> caminoSeguido, String fileName) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            writer.write("Solución:\n");
            for (Casilla casilla : solucion) {
                writer.write("(" + (casilla.x + 1) + "," + (casilla.y + 1) + ")\n");
            }

            writer.write("\nCamino Seguido:\n");
            for (Casilla casilla : caminoSeguido) {
                writer.write("(" + (casilla.x + 1) + "," + (casilla.y + 1) + ")\n");
            }
        }
    }

    private static void mostrarSolucion(List<Casilla> solucion) {
        for (int i = solucion.size() - 1; i >= 0; i--) {
            Casilla casilla = solucion.get(i);
            System.out.println("(" + (casilla.x + 1) + "," + (casilla.y + 1) + ")");
        }
    }
    private static void mostrarAyuda() {
        System.out.println("SINTAXIS: java -jar robot.jar [-t] [-h] [fichero_entrada] [fichero_salida]");
        System.out.println("-t: traza el algoritmo utilizado.");
        System.out.println("-h: muestra una ayuda.");
        System.out.println("[fichero_entrada] Nombre del fichero de entrada.");
        System.out.println("[fichero_salida] Nombre del fichero de salida.");
    }
  
}

