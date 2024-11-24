import java.util.ArrayList;
import java.util.List;

public class Edificio {
    private char[][] matriz;
    private boolean[][] exploradas;
    private List<Casilla> solucion;
    private List<Casilla> caminoSeguido;

    public Edificio(char[][] matriz) {
        if (matriz == null || matriz.length == 0 || matriz[0].length == 0) {
            throw new IllegalArgumentException("La matriz no puede ser nula o vacía");
        }

        this.matriz = matriz;
        this.exploradas = new boolean[matriz.length][matriz[0].length];
        this.solucion = new ArrayList<>();
        this.caminoSeguido = new ArrayList<>();
    }

    public List<Casilla> getSolucion() {
        return solucion;
    }

    public List<Casilla> getCaminoSeguido() {
        return caminoSeguido;
    }

    public boolean buscarTornillo(boolean traza) {
        Casilla inicio = new Casilla(0, 0);
        boolean exito = buscarTornilloRecursivo(inicio, traza);
        return exito;
    }

    private boolean buscarTornilloRecursivo(Casilla casilla, boolean traza) {
        exploradas[casilla.x][casilla.y] = true;

        if (traza) {
            System.out.println("Explorando la casilla (" + casilla.x + "," + casilla.y + ")...");
        }

        if (matriz[casilla.x][casilla.y] == 'T') {
            solucion.add(new Casilla(casilla.x, casilla.y));

            if (traza) {
                System.out.println("¡Tornillo encontrado en la casilla (" + casilla.x + "," + casilla.y + ")!");
            }
            return true;
        }

        List<Casilla> hijos = caminos(casilla);

        for (Casilla hijo : hijos) {
            if (!exploradas[hijo.x][hijo.y]) {
                if (buscarTornilloRecursivo(hijo, traza)) {
                    solucion.add(new Casilla(casilla.x, casilla.y));
                    caminoSeguido.add(new Casilla(casilla.x, casilla.y));
                    return true;
                }
            }
        }

        return false;
    }

    private List<Casilla> caminos(Casilla casilla) {
        List<Casilla> hijos = new ArrayList<>();
        if (casilla.x + 1 < matriz.length && matriz[casilla.x + 1][casilla.y] != 'E') {
            hijos.add(new Casilla(casilla.x + 1, casilla.y));
        }
        if (casilla.x - 1 >= 0 && matriz[casilla.x - 1][casilla.y] != 'E') {
            hijos.add(new Casilla(casilla.x - 1, casilla.y));
        }
        if (casilla.y + 1 < matriz[0].length && matriz[casilla.x][casilla.y + 1] != 'E') {
            hijos.add(new Casilla(casilla.x, casilla.y + 1));
        }
        if (casilla.y - 1 >= 0 && matriz[casilla.x][casilla.y - 1] != 'E') {
            hijos.add(new Casilla(casilla.x, casilla.y - 1));
        }
        return hijos;
    }
}
