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
        if (cantidad == max) {
            return false;
        }

        int prioridadNuevo = solicitud.calcularScoreMatching();

        int i = cantidad - 1;

        while (i >= 0 && cola[i].calcularScoreMatching() < prioridadNuevo) {
            cola[i + 1] = cola[i];
            i--;
        }

        cola[i + 1] = solicitud;
        cantidad++;
        return true;
    }

    public SolicitudEmpleo eliminar() {
        if (cantidad == 0) {
            return null;
        }

        SolicitudEmpleo eliminado = cola[0];

        for (int i = 0; i <= cantidad - 2; i++) {
            cola[i] = cola[i + 1];
        }

        cantidad--;
        cola[cantidad] = null;

        return eliminado;
    }

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
