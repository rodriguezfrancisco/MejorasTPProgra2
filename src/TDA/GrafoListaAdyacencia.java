package TDA;

import model.Usuario;
import Interfaces.IGrafo;

public class GrafoListaAdyacencia implements IGrafo {

    // Nodo interno para manejar la lista enlazada de conexiones de cada vértice
    private class NodoArista {
        int indiceDestino;
        NodoArista siguiente;

        public NodoArista(int indiceDestino) {
            this.indiceDestino = indiceDestino;
            this.siguiente = null;
        }
    }

    private Usuario[] vertices;
    private NodoArista[] listasAdyacencia; // Arreglo de listas enlazadas
    private int cantidad;
    private int capacidad;
    private boolean dirigido;

    public GrafoListaAdyacencia(int capacidad, boolean dirigido) {
        this.capacidad = capacidad;
        this.dirigido = dirigido;
        this.cantidad = 0;
        this.vertices = new Usuario[capacidad];
        this.listasAdyacencia = new NodoArista[capacidad];
    }

    @Override
    public void insertarVertice(Usuario vertice) {
        if (cantidad == capacidad) {
            System.out.println("La red social está llena.");
            return;
        }
        if (existeVertice(vertice)) {
            System.out.println("El usuario ya existe en la red.");
            return;
        }
        vertices[cantidad] = vertice;
        listasAdyacencia[cantidad] = null; // Inicializa la lista de amigos vacía
        cantidad++;
    }

    @Override
    public boolean existeVertice(Usuario vertice) {
        return obtenerIndice(vertice) != -1;
    }

    private int obtenerIndice(Usuario vertice) {
        for (int i = 0; i < cantidad; i++) {
            if (vertices[i] != null && vertices[i].esIgual(vertice)) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public void insertarArista(Usuario origen, Usuario destino) {
        int posOrigen = obtenerIndice(origen);
        int posDestino = obtenerIndice(destino);

        if (posOrigen == -1 || posDestino == -1) {
            System.out.println("Uno de los usuarios no existe.");
            return;
        }

        if (existeArista(origen, destino)) {
            return; // Conexión ya existente
        }

        // Insertar al inicio de la lista del nodo origen
        NodoArista nuevoNodo = new NodoArista(posDestino);
        nuevoNodo.siguiente = listasAdyacencia[posOrigen];
        listasAdyacencia[posOrigen] = nuevoNodo;

        // Si no es dirigido, se inserta la relación inversa
        if (!dirigido) {
            NodoArista nuevoNodoVuelta = new NodoArista(posOrigen);
            nuevoNodoVuelta.siguiente = listasAdyacencia[posDestino];
            listasAdyacencia[posDestino] = nuevoNodoVuelta;
        }
    }

    @Override
    public void eliminarArista(Usuario origen, Usuario destino) {
        int posOrigen = obtenerIndice(origen);
        int posDestino = obtenerIndice(destino);

        if (posOrigen == -1 || posDestino == -1) {
            return;
        }

        listasAdyacencia[posOrigen] = eliminarNodoDeLista(listasAdyacencia[posOrigen], posDestino);

        if (!dirigido) {
            listasAdyacencia[posDestino] = eliminarNodoDeLista(listasAdyacencia[posDestino], posOrigen);
        }
    }

    // Método auxiliar para remover un destino específico de una lista enlazada
    private NodoArista eliminarNodoDeLista(NodoArista cabeza, int destino) {
        if (cabeza == null) return null;

        if (cabeza.indiceDestino == destino) {
            return cabeza.siguiente;
        }

        NodoArista actual = cabeza;
        while (actual.siguiente != null) {
            if (actual.siguiente.indiceDestino == destino) {
                actual.siguiente = actual.siguiente.siguiente;
                break;
            }
            actual = actual.siguiente;
        }
        return cabeza;
    }

    @Override
    public boolean existeArista(Usuario origen, Usuario destino) {
        int posOrigen = obtenerIndice(origen);
        int posDestino = obtenerIndice(destino);

        if (posOrigen == -1 || posDestino == -1) {
            return false;
        }

        NodoArista actual = listasAdyacencia[posOrigen];
        while (actual != null) {
            if (actual.indiceDestino == posDestino) {
                return true;
            }
            actual = actual.siguiente;
        }
        return false;
    }

    @Override
    public void eliminarVertice(Usuario vertice) {
        int pos = obtenerIndice(vertice);
        if (pos == -1) return;

        // 1. Limpiar referencias y reajustar índices internos de las aristas remanentes
        for (int i = 0; i < cantidad; i++) {
            // Eliminar cualquier enlace directo al nodo que deja de existir
            listasAdyacencia[i] = eliminarNodoDeLista(listasAdyacencia[i], pos);

            // Decrementar los índices mayores a 'pos' porque los elementos del arreglo se van a desplazar
            NodoArista actual = listasAdyacencia[i];
            while (actual != null) {
                if (actual.indiceDestino > pos) {
                    actual.indiceDestino--;
                }
                actual = actual.siguiente;
            }
        }

        // 2. Desplazar los arreglos principales hacia la izquierda
        for (int i = pos; i < cantidad - 1; i++) {
            vertices[i] = vertices[i + 1];
            listasAdyacencia[i] = listasAdyacencia[i + 1];
        }

        cantidad--;
        vertices[cantidad] = null;
        listasAdyacencia[cantidad] = null;
    }

    @Override
    public void mostrarVertices() {
        System.out.print("Usuarios registrados: ");
        for (int i = 0; i < cantidad; i++) {
            System.out.print(vertices[i] + " ");
        }
        System.out.println();
    }

    @Override
    public void mostrarMatriz() {
        // Mantiene el contrato visual de la interfaz imprimiendo el estado como matriz simétrica
        System.out.println("Matriz de Conexiones:");
        System.out.print("         ");
        for (int i = 0; i < cantidad; i++) {
            System.out.printf("%-9s", vertices[i].getNombre());
        }
        System.out.println();

        for (int i = 0; i < cantidad; i++) {
            System.out.printf("%-9s", vertices[i].getNombre());
            for (int j = 0; j < cantidad; j++) {
                int conectado = 0;
                NodoArista actual = listasAdyacencia[i];
                while (actual != null) {
                    if (actual.indiceDestino == j) {
                        conectado = 1;
                        break;
                    }
                    actual = actual.siguiente;
                }
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

    private void dfsRecursivo(int v, boolean[] visitados) {
        visitados[v] = true;
        System.out.print(vertices[v] + " ");

        NodoArista actual = listasAdyacencia[v];
        while (actual != null) {
            if (!visitados[actual.indiceDestino]) {
                dfsRecursivo(actual.indiceDestino, visitados);
            }
            actual = actual.siguiente;
        }
    }

    @Override
    public void bfsNiveles(Usuario usuario) {
        int[] niveles = calcularNivelesBFS(usuario);
        if (niveles == null) return;

        System.out.println("Niveles de conexión para " + usuario);
        for (int i = 0; i < cantidad; i++) {
            if (niveles[i] > 0) {
                System.out.println(" - Nivel " + niveles[i] + " (" + getNivelDesc(niveles[i]) + "): " + vertices[i]);
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
                System.out.println(" - Conectar con: " + vertices[i] + " (Tienen amigos en común)");
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
            niveles[i] = -1; // -1 define que el vértice no fue visitado aún
        }

        // Simulación de Cola estática usando un arreglo nativo
        int[] cola = new int[capacidad];
        int frente = 0;
        int fin = 0;

        cola[fin] = inicio;
        fin++;
        niveles[inicio] = 0;

        while (frente < fin) {
            int actual = cola[frente];
            frente++;

            // Recorrer los vecinos a través de la lista de adyacencia del nodo actual
            NodoArista vecino = listasAdyacencia[actual];
            while (vecino != null) {
                if (niveles[vecino.indiceDestino] == -1) {
                    niveles[vecino.indiceDestino] = niveles[actual] + 1;
                    cola[fin] = vecino.indiceDestino;
                    fin++;
                }
                vecino = vecino.siguiente;
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