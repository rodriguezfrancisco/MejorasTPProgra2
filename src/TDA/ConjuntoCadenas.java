package TDA;

public class ConjuntoCadenas {

    // Nodo interno para manejar colisiones
    private class Nodo {
        String valor;
        Nodo siguiente;

        public Nodo(String valor) {
            this.valor = valor;
            this.siguiente = null;
        }
    }

    private Nodo[] tabla;
    private int capacidad;
    private int cantidad;

    public ConjuntoCadenas(int capacidad) {
        this.capacidad = capacidad;
        this.tabla = new Nodo[capacidad];
        this.cantidad = 0;
    }

    // Función Hash para distribuir las cadenas
    private int hash(String valor) {
        int hash = 0;
        for (int i = 0; i < valor.length(); i++) {
            hash = (31 * hash + valor.charAt(i)) % capacidad;
        }
        return Math.abs(hash);
    }

    /**
     * Intenta agregar un valor al conjunto.
     * @return true si se agregó con éxito, false si ya existía (rechaza el duplicado).
     */
    public boolean agregar(String valor) {
        if (valor == null) return false;

        String valorNormalizado = valor.trim().toLowerCase();

        // 1. Verificamos pertenencia (Si ya existe, la regla del Conjunto lo rechaza)
        if (pertenece(valorNormalizado)) {
            return false;
        }

        // 2. Si no existe, lo insertamos
        int indice = hash(valorNormalizado);
        Nodo nuevo = new Nodo(valorNormalizado);
        nuevo.siguiente = tabla[indice];
        tabla[indice] = nuevo;

        cantidad++;
        return true;
    }


    //Verifica en tiempo constante O(1) si el elemento pertenece al conjunto.
    public boolean pertenece(String valor) {
        if (valor == null) return false;

        String valorNormalizado = valor.trim().toLowerCase();
        int indice = hash(valorNormalizado);
        Nodo actual = tabla[indice];

        while (actual != null) {
            if (actual.valor.equals(valorNormalizado)) {
                return true;
            }
            actual = actual.siguiente;
        }
        return false;
    }
}