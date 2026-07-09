package TDA;

import model.Usuario;
import Interfaces.IGrafo;

public class GrafoListaAdyacencia implements IGrafo {


    //Clase interna que representa una conexión (arista) en la lista enlazada.

    private class Arista {
        Vertice destino; // Apunta directamente al objeto, no a un índice numérico
        Arista siguiente;

        public Arista(Vertice destino) {
            this.destino = destino;
            this.siguiente = null;
        }
    }


     //Clase interna que encapsula a un Usuario y se hace cargo de gestionar
     //su propia lista de conexiones.

    private class Vertice {
        Usuario usuario;
        Arista primerAdyacente; // Cabeza de la lista enlazada de amigos

        public Vertice(Usuario usuario) {
            this.usuario = usuario;
            this.primerAdyacente = null;
        }

        // El propio vértice sabe cómo agregar un amigo a su lista (O(1))
        public void agregarAdyacencia(Vertice destino) {
            Arista nueva = new Arista(destino);
            nueva.siguiente = this.primerAdyacente;
            this.primerAdyacente = nueva;
        }

        // El propio vértice sabe cómo eliminar a un amigo de su lista
        public void eliminarAdyacencia(Vertice destino) {
            if (this.primerAdyacente == null) return;

            if (this.primerAdyacente.destino == destino) {
                this.primerAdyacente = this.primerAdyacente.siguiente;
                return;
            }

            Arista actual = this.primerAdyacente;
            while (actual.siguiente != null) {
                if (actual.siguiente.destino == destino) {
                    actual.siguiente = actual.siguiente.siguiente;
                    return;
                }
                actual = actual.siguiente;
            }
        }

        // El propio vértice sabe si es amigo de alguien
        public boolean tieneAdyacencia(Vertice destino) {
            Arista actual = this.primerAdyacente;
            while (actual != null) {
                if (actual.destino == destino) return true;
                actual = actual.siguiente;
            }
            return false;
        }
    }

    // ==========================================
    // LÓGICA PRINCIPAL DEL GRAFO
    // ==========================================

    private Vertice[] vertices;
    private int cantidad;
    private int capacidad;
    private boolean dirigido;

    public GrafoListaAdyacencia(int capacidad, boolean dirigido) {
        this.capacidad = capacidad;
        this.dirigido = dirigido;
        this.cantidad = 0;
        this.vertices = new Vertice[capacidad];
    }

    // Método para crecer infinitamente
    private void redimensionar() {
        int nuevaCapacidad = capacidad * 2;
        Vertice[] nuevosVertices = new Vertice[nuevaCapacidad];
        for (int i = 0; i < cantidad; i++) {
            nuevosVertices[i] = vertices[i];
        }
        this.vertices = nuevosVertices;
        this.capacidad = nuevaCapacidad;
    }

    @Override
    public void insertarVertice(Usuario usuario) {
        if (cantidad == capacidad) {
            redimensionar();
        }
        if (existeVertice(usuario)) {
            System.out.println("El usuario ya existe en la red.");
            return;
        }
        // Creamos el Objeto Vértice que ahora contiene al usuario y su lista vacía
        vertices[cantidad] = new Vertice(usuario);
        cantidad++;
    }

    @Override
    public boolean existeVertice(Usuario usuario) {
        return obtenerIndice(usuario) != -1;
    }

    // Devuelve la posición en el arreglo (necesaria para el DFS y BFS)
    private int obtenerIndice(Usuario usuario) {
        for (int i = 0; i < cantidad; i++) {
            if (vertices[i] != null && vertices[i].usuario.esIgual(usuario)) {
                return i;
            }
        }
        return -1;
    }

    // Método auxiliar para recuperar el objeto Vertice completo
    private Vertice obtenerVertice(Usuario usuario) {
        int index = obtenerIndice(usuario);
        return (index != -1) ? vertices[index] : null;
    }

    @Override
    public void insertarArista(Usuario origen, Usuario destino) {
        Vertice vOrigen = obtenerVertice(origen);
        Vertice vDestino = obtenerVertice(destino);

        if (vOrigen == null || vDestino == null) {
            System.out.println("Uno de los usuarios no existe.");
            return;
        }

        if (vOrigen.tieneAdyacencia(vDestino)) {
            return;
        }

        // Delegamos la lógica al objeto
        vOrigen.agregarAdyacencia(vDestino);

        if (!dirigido) {
            vDestino.agregarAdyacencia(vOrigen);
        }
    }

    @Override
    public void eliminarArista(Usuario origen, Usuario destino) {
        Vertice vOrigen = obtenerVertice(origen);
        Vertice vDestino = obtenerVertice(destino);

        if (vOrigen == null || vDestino == null) return;

        vOrigen.eliminarAdyacencia(vDestino);
        if (!dirigido) {
            vDestino.eliminarAdyacencia(vOrigen);
        }
    }

    @Override
    public boolean existeArista(Usuario origen, Usuario destino) {
        Vertice vOrigen = obtenerVertice(origen);
        Vertice vDestino = obtenerVertice(destino);

        if (vOrigen == null || vDestino == null) return false;

        return vOrigen.tieneAdyacencia(vDestino);
    }

    @Override
    public void eliminarVertice(Usuario usuario) {
        int pos = obtenerIndice(usuario);
        if (pos == -1) return;

        Vertice verticeAEliminar = vertices[pos];

        // 1. Recorrer todos los vértices del sistema y pedirles que borren al usuario de sus listas
        for (int i = 0; i < cantidad; i++) {
            vertices[i].eliminarAdyacencia(verticeAEliminar);
        }

        // 2. Desplazar el arreglo general para tapar el hueco
        for (int i = pos; i < cantidad - 1; i++) {
            vertices[i] = vertices[i + 1];
        }

        cantidad--;
        vertices[cantidad] = null;
    }

    @Override
    public void mostrarVertices() {
        System.out.print("Usuarios registrados: ");
        for (int i = 0; i < cantidad; i++) {
            System.out.print(vertices[i].usuario + " ");
        }
        System.out.println();
    }

    @Override
    public void mostrarMatriz() {
        System.out.println("Matriz de Conexiones:");
        System.out.print("         ");
        for (int i = 0; i < cantidad; i++) {
            System.out.printf("%-9s", vertices[i].usuario.getNombre());
        }
        System.out.println();

        for (int i = 0; i < cantidad; i++) {
            System.out.printf("%-9s", vertices[i].usuario.getNombre());
            for (int j = 0; j < cantidad; j++) {
                // Consultamos directamente al objeto si tiene la conexión
                int conectado = vertices[i].tieneAdyacencia(vertices[j]) ? 1 : 0;
                System.out.printf("%-9d", conectado);
            }
            System.out.println();
        }
    }

    @Override
    public void dfsAlcance(Usuario usuario) {
        int inicio = obtenerIndice(usuario);
        if (inicio == -1) {
            System.out.println("El usuario no existe.");
            return;
        }

        boolean[] visitados = new boolean[cantidad];
        System.out.print("Alcance de red de " + usuario + " (DFS): ");
        dfsRecursivo(inicio, visitados);
        System.out.println();
    }

    private void dfsRecursivo(int vIndex, boolean[] visitados) {
        visitados[vIndex] = true;
        Vertice actual = vertices[vIndex];
        System.out.print(actual.usuario + " ");

        Arista aristaActual = actual.primerAdyacente;
        while (aristaActual != null) {
            int destinoIndex = obtenerIndice(aristaActual.destino.usuario);
            if (destinoIndex != -1 && !visitados[destinoIndex]) {
                dfsRecursivo(destinoIndex, visitados);
            }
            aristaActual = aristaActual.siguiente;
        }
    }

    @Override
    public void bfsNiveles(Usuario usuario) {
        int[] niveles = calcularNivelesBFS(usuario);
        if (niveles == null) return;

        System.out.println("Niveles de conexión para " + usuario);
        for (int i = 0; i < cantidad; i++) {
            if (niveles[i] > 0) {
                System.out.println(" - Nivel " + niveles[i] + " (" + getNivelDesc(niveles[i]) + "): " + vertices[i].usuario);
            }
        }
    }

    @Override
    public void recomendarAmigos(Usuario usuario) {
        int[] niveles = calcularNivelesBFS(usuario);
        if (niveles == null) return;

        System.out.println("Recomendaciones de amistad para " + usuario + ":");
        boolean hayRecomendaciones = false;

        for (int i = 0; i < cantidad; i++) {
            if (niveles[i] == 2) {
                System.out.println(" - Conectar con: " + vertices[i].usuario + " (Tienen amigos en común)");
                hayRecomendaciones = true;
            }
        }

        if (!hayRecomendaciones) {
            System.out.println(" - No hay recomendaciones en este momento.");
        }
    }

    private int[] calcularNivelesBFS(Usuario origen) {
        int inicio = obtenerIndice(origen);
        if (inicio == -1) {
            System.out.println("El usuario no existe.");
            return null;
        }

        int[] niveles = new int[cantidad];
        for (int i = 0; i < cantidad; i++) {
            niveles[i] = -1;
        }

        int[] cola = new int[capacidad];
        int frente = 0;
        int fin = 0;

        cola[fin] = inicio;
        fin++;
        niveles[inicio] = 0;

        while (frente < fin) {
            int indexActual = cola[frente];
            frente++;

            Vertice verticeActual = vertices[indexActual];
            Arista aristaActual = verticeActual.primerAdyacente;

            while (aristaActual != null) {
                int destinoIndex = obtenerIndice(aristaActual.destino.usuario);
                if (destinoIndex != -1 && niveles[destinoIndex] == -1) {
                    niveles[destinoIndex] = niveles[indexActual] + 1;
                    cola[fin] = destinoIndex;
                    fin++;
                }
                aristaActual = aristaActual.siguiente;
            }
        }

        return niveles;
    }

    private String getNivelDesc(int nivel) {
        if (nivel == 1) return "Amigo directo";
        if (nivel == 2) return "Amigo de amigo";
        return "Conexión lejana";
    }
}