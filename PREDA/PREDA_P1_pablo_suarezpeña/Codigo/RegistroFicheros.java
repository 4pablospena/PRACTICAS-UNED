import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Arrays;

class RegistroFicheros {
    private String[] args;
    private Boolean traza;
    private Boolean ayuda;
    private String ficheroEntrada;
    private String ficheroSalida;
    private int cantidadDevolver;

    public RegistroFicheros(String[] args) {
        this.args = args != null ? args : new String[0];
        this.traza = false;
        this.ayuda = false;
        this.ficheroEntrada = null;
        this.ficheroSalida = null;
    }

    public Boolean esOrdenCorrecto() {
        int idxT = obtenerIndiceArgumento("-t");
        int idxH = obtenerIndiceArgumento("-h");
        int idxEntrada = obtenerIndiceFicheroEntrada();
        int idxSalida = obtenerIndiceFicheroSalida();

        return (idxT <= idxH && idxH <= idxEntrada && idxEntrada <= idxSalida) ||
                (idxH <= idxT && idxT <= idxEntrada && idxEntrada <= idxSalida);
    }

    private int obtenerIndiceArgumento(String opcion) {
    for (int i = 0; i < this.args.length; i++) {
        if (this.args[i] != null && this.args[i].equals(opcion)) {
            return i;
        }
    }
    return -1; // Si no se encuentra la opción, devolver -1 o manejar de acuerdo a tu lógica.
    }

    private int obtenerIndiceFicheroEntrada() {
        return args.length > 0 ? 0 : args.length;
    }

    private int obtenerIndiceFicheroSalida() {
        return args.length > 1 ? 1 : args.length;
    }

    public void anotarArgumentos() {
        Set<String> opciones = new HashSet<>(Arrays.asList(args));

        traza = opciones.contains("-t");
        ayuda = opciones.contains("-h");

        if (esOrdenCorrecto()) {
            if (args.length > 0) {
                ficheroEntrada = args[obtenerIndiceFicheroEntrada()];
            }

            if (args.length > 1) {
                ficheroSalida = args[obtenerIndiceFicheroSalida()];
            }
        } else {
            errorArgumentos();
        }
    }

    public Boolean getMostrarAyuda() {
        return ayuda;
    }

    public void mostrarAyuda() {
        System.out.println("SINTAXIS: cambio-dinamica [-t] [-h] [fichero entrada] [fichero salida]");
        System.out.println("-t Traza el algoritmo");
        System.out.println("-h Muestra esta ayuda");
        System.out.println("[fichero_entrada] Nombre del fichero de entrada");
        System.out.println("[fichero_salida]  Nombre del fichero de salida");
    }

    public Boolean getTraza() {
        return traza;
    }

    public Boolean getAyuda() {
        return ayuda;
    }

    public String getFicheroEntrada() {
        return ficheroEntrada;
    }

    public String getFicheroSalida() {
        return ficheroSalida;
    }

    public int getCantidadDevolver() {
        return cantidadDevolver;
    }

    public void errorArgumentos() {
        System.out.println("Argumentos erróneos");
        this.mostrarAyuda();
        System.exit(0);
    }
}
