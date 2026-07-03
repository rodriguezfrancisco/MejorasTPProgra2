package TDA;

import model.SolicitudEmpleo;

public class ColaPrioridadEstatica {
    private SolicitudEmpleo[] cola;
    private int cantidad;
    private int max;

    public ColaPrioridadEstatica(int max) {
        this.max = max;
        this.cola = new SolicitudEmpleo[max];
        this.cantidad = 0;
    }

    public boolean insertar(SolicitudEmpleo solicitud) {
        // si cola está llena retornar falso
        if (cantidad == max) {
            return false;
        }

        // Calculamos la prioridad (Score de matching) que define su lugar en la fila
        int prioridadNuevo = solicitud.calcularScoreMatching();

        // i <- cantidad - 1
        int i = cantidad - 1;

        // mientras i >= 0 y cola[i].prioridad < prioridad hacer
        while (i >= 0 && cola[i].calcularScoreMatching() < prioridadNuevo) {
            // cola[i+1] <- cola[i]
            cola[i + 1] = cola[i];
            // i <- i - 1
            i--;
        }

        // cola[i+1] <- nuevo
        cola[i + 1] = solicitud;
        // cantidad <- cantidad + 1
        cantidad++;
        // retornar verdadero
        return true;
    }

    public SolicitudEmpleo eliminar() {
        // si cola está vacía retornar nulo
        if (cantidad == 0) {
            return null;
        }

        // eliminado <- cola[0]
        SolicitudEmpleo eliminado = cola[0];

        // para i desde 0 hasta cantidad - 2 hacer
        for (int i = 0; i <= cantidad - 2; i++) {
            // cola[i] <- cola[i+1]
            cola[i] = cola[i + 1];
        }

        // cantidad <- cantidad - 1
        cantidad--;
        // Limpiamos la última referencia para liberar memoria
        cola[cantidad] = null;

        // retornar eliminado
        return eliminado;
    }

    // Devuelve el frente de la cola sin eliminarlo
    public SolicitudEmpleo frente() {
        if (cantidad == 0) {
            return null;
        }
        return cola[0];
    }

    public boolean estaVacia() {
        return cantidad == 0;
    }

    public boolean estaLlena() {
        return cantidad == max;
    }
}
