public class InfoCambio {
    private int cantidadDevolver;
    private int cantidadMonedasDif;
    private int[] tiposMonedas;
    private int[][] tabla;
    private int[] monedasDevueltas;
    private int numMinMonedasDevueltas;
    private String tipoMonedasDevueltas;
    private String trazaSolucion;
    private boolean mostrarTraza;
    private int n;
    
    public InfoCambio(int cantidadDevolver, int cantidadMonedas, int[] tiposMonedas, boolean mostrarTraza) {
        this.cantidadDevolver = cantidadDevolver;
        this.cantidadMonedasDif = cantidadMonedas;
        this.tiposMonedas = tiposMonedas;
        this.mostrarTraza = mostrarTraza;
        this.tabla = new int[tiposMonedas.length + 1][cantidadDevolver + 1];
        this.monedasDevueltas = new int[tiposMonedas.length];
        this.trazaSolucion = "";
        this.n = cantidadMonedas;
    }

   public void getCambio(Boolean traza) {
        if (mostrarTraza) {
            trazaSolucion += "La cantidad a devolver es: " + cantidadDevolver + System.getProperty("line.separator");
            trazaSolucion += "con " + cantidadMonedasDif + " tipos diferentes de monedas" + System.getProperty("line.separator");
        }

        for (int i = 0; i <= n; i++) {  // Ajustamos la condición de iteración a n
            for (int j = 0; j <= cantidadDevolver; j++) {
                if (traza) {
                    trazaSolucion += tabla[i][j] + " ";
                    if (j == cantidadDevolver) {
                        trazaSolucion += System.getProperty("line.separator");
                    }
                }

                if (i == 0) {
                    tabla[i][j] = j;
                } else if (tiposMonedas[i - 1] > j) {
                    tabla[i][j] = tabla[i - 1][j];
                } else {
                    tabla[i][j] = Math.min(tabla[i - 1][j], tabla[i][j - tiposMonedas[i - 1]] + 1);
                }
            }
        }

        numMinMonedasDevueltas = tabla[n][cantidadDevolver];  // Ajustamos la obtención del resultado
    }
    
    
    public String getTipoMonedas() {
    int cantidadRestante = cantidadDevolver;
    int i = tiposMonedas.length;
    int j = cantidadDevolver;
    tipoMonedasDevueltas = "";

    while (i > 0) {
        if (tabla[i][j] == tabla[i - 1][j]) {
            i = i - 1;
        } else {
            tipoMonedasDevueltas += tiposMonedas[i - 1] + " ";
            j = j - tiposMonedas[i - 1];
        }
    }
    return tipoMonedasDevueltas.trim();
    }

    
    public String getCambioSolucion() {
        return String.valueOf(numMinMonedasDevueltas);
    }
    
  public String getTrazaSolucion() {
        if (mostrarTraza) {
            StringBuilder trazaSolucion = new StringBuilder();

            for (int i = 0; i <= tiposMonedas.length; i++) {
                for (int j = 0; j <= cantidadDevolver; j++) {
                    trazaSolucion.append(String.format("t[%d,%d] = %d   ", i, j, tabla[i][j]));
                }
                trazaSolucion.append(System.lineSeparator());
            }

            return trazaSolucion.toString();
        } else {
            return "La opción de traza no está habilitada.";
        }
    }
    private void rellenarTraza() {
        for (int i = 0; i <= tiposMonedas.length; i++) {
            for (int j = 1; j <= cantidadDevolver; j++) {
                trazaSolucion += " " + tabla[i][j];
                if (j == cantidadDevolver) {
                    trazaSolucion += System.getProperty("line.separator");
                }
            }
        }
        trazaSolucion += System.getProperty("line.separator");
    }
    
        // Método getter para mostrarTraza
    public boolean isMostrarTraza() {
        return mostrarTraza;
    }
    }

