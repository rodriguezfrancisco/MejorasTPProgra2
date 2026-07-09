package TDA;

import model.Usuario;
import Interfaces.IGrafo;

public class GrafoListaAdyacencia implements IGrafo {

    //Nodo secundario: Representa una conexión (arista) en la lista de amigos.

    private class Arista {
        NodoVertice destino; // Puntero directo al objeto amigo
        Arista siguiente;

        public Arista(NodoVertice destino) {
            this.destino = destino;
            this.siguiente = null;
        }
    }

    // Nodo principal: Representa al Usuario y gestiona su propia lista de adyacencias.
    // También actúa como eslabón en la lista enlazada principal del Grafo.

    private class NodoVertice {
        Usuario usuario;

        Arista primerAdyacente;        // Cabeza de la sub-lista (amigos)
        NodoVertice siguienteVertice;  // Puntero al siguiente usuario de la plataforma

        // Atributos auxiliares para reemplazar a los arreglos en los algoritmos de grafos
        boolean visitado;
        int nivel;

        public NodoVertice(Usuario usuario) {
            this.usuario = usuario;
            this.primerAdyacente = null;
            this.siguienteVertice = null;
            this.visitado = false;
            this.nivel = -1;
        }

        // El propio vértice agrega un amigo a su lista enlazada (O(1))
        public void agregarAdyacencia(NodoVertice destino) {
            Arista nueva = new Arista(destino);
            nueva.siguiente = this.primerAdyacente;
            this.primerAdyacente = nueva;
        }

        // El propio vértice elimina un amigo de su lista enlazada (O(E))
        public void eliminarAdyacencia(NodoVertice destino) {
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

        public boolean tieneAdyacencia(NodoVertice destino) {
            Arista actual = this.primerAdyacente;
            while (actual != null) {
                if (actual.destino == destino) return true;
                actual = actual.siguiente;
            }
            return false;
        }
    }

    // ==========================================
    // ESTRUCTURA AUXILIAR PARA ALGORITMOS
    // ==========================================

    // Cola enlazada simple para el BFS (para no usar arreglos estáticos)
    private class ColaNodos {
        private class NodoCola {
            NodoVertice vertice;
            NodoCola siguiente;
            public NodoCola(NodoVertice v) { this.vertice = v; this.siguiente = null; }
        }
        private NodoCola frente, fin;

        public void encolar(NodoVertice v) {
            NodoCola nuevo = new NodoCola(v);
            if (frente == null) frente = fin = nuevo;
            else { fin.siguiente = nuevo; fin = nuevo; }
        }

        public NodoVertice desencolar() {
            if (frente == null) return null;
            NodoVertice v = frente.vertice;
            frente = frente.siguiente;
            if (frente == null) fin = null;
            return v;
        }

        public boolean estaVacia() { return frente == null; }
    }


    // ==========================================
    // LÓGICA PRINCIPAL DEL GRAFO
    // ==========================================

    private NodoVertice primerVertice; // Cabeza de la Lista Principal de Usuarios
    private boolean dirigido;


    public GrafoListaAdyacencia(boolean dirigido) {
        this.primerVertice = null;
        this.dirigido = dirigido;
    }

    @Override
    public void insertarVertice(Usuario usuario) {
        if (existeVertice(usuario)) {
            System.out.println("El usuario ya existe en la red.");
            return;
        }

        // Insertamos al principio de la lista principal (O(1))
        NodoVertice nuevoVertice = new NodoVertice(usuario);
        nuevoVertice.siguienteVertice = this.primerVertice;
        this.primerVertice = nuevoVertice;
    }

    @Override
    public boolean existeVertice(Usuario usuario) {
        return obtenerVertice(usuario) != null;
    }

    // Método privado para buscar un nodo atravesando la lista principal (O(V))
    private NodoVertice obtenerVertice(Usuario usuario) {
        NodoVertice actual = primerVertice;
        while (actual != null) {
            if (actual.usuario.esIgual(usuario)) {
                return actual;
            }
            actual = actual.siguienteVertice;
        }
        return null;
    }

    @Override
    public void insertarArista(Usuario origen, Usuario destino) {
        NodoVertice vOrigen = obtenerVertice(origen);
        NodoVertice vDestino = obtenerVertice(destino);

        if (vOrigen == null || vDestino == null) {
            System.out.println("Uno de los usuarios no existe.");
            return;
        }

        if (vOrigen.tieneAdyacencia(vDestino)) return;

        vOrigen.agregarAdyacencia(vDestino);

        if (!dirigido) {
            vDestino.agregarAdyacencia(vOrigen);
        }
    }

    @Override
    public void eliminarArista(Usuario origen, Usuario destino) {
        NodoVertice vOrigen = obtenerVertice(origen);
        NodoVertice vDestino = obtenerVertice(destino);

        if (vOrigen == null || vDestino == null) return;

        vOrigen.eliminarAdyacencia(vDestino);
        if (!dirigido) {
            vDestino.eliminarAdyacencia(vOrigen);
        }
    }

    @Override
    public boolean existeArista(Usuario origen, Usuario destino) {
        NodoVertice vOrigen = obtenerVertice(origen);
        NodoVertice vDestino = obtenerVertice(destino);

        if (vOrigen == null || vDestino == null) return false;
        return vOrigen.tieneAdyacencia(vDestino);
    }

    @Override
    public void eliminarVertice(Usuario usuario) {
        NodoVertice verticeAEliminar = obtenerVertice(usuario);
        if (verticeAEliminar == null) return;

        // 1. Recorremos TODOS los usuarios y les pedimos que borren al usuario de sus sub-listas de amigos
        NodoVertice iterador = primerVertice;
        while (iterador != null) {
            iterador.eliminarAdyacencia(verticeAEliminar);
            iterador = iterador.siguienteVertice;
        }

        // 2. Eliminamos al usuario de la lista principal de vértices
        if (primerVertice == verticeAEliminar) {
            primerVertice = primerVertice.siguienteVertice;
            return;
        }

        iterador = primerVertice;
        while (iterador.siguienteVertice != null) {
            if (iterador.siguienteVertice == verticeAEliminar) {
                iterador.siguienteVertice = iterador.siguienteVertice.siguienteVertice;
                return;
            }
            iterador = iterador.siguienteVertice;
        }
    }

    @Override
    public void mostrarVertices() {
        System.out.print("Usuarios registrados: ");
        NodoVertice actual = primerVertice;
        while (actual != null) {
            System.out.print(actual.usuario + " ");
            actual = actual.siguienteVertice;
        }
        System.out.println();
    }

    @Override
    public void mostrarMatriz() {
        // En una "Lista de Listas" imprimir una matriz es costoso pero útil visualmente
        System.out.println("Matriz de Conexiones:");
        System.out.print("         ");

        NodoVertice col = primerVertice;
        while (col != null) {
            System.out.printf("%-9s", col.usuario.getNombre());
            col = col.siguienteVertice;
        }
        System.out.println();

        NodoVertice fila = primerVertice;
        while (fila != null) {
            System.out.printf("%-9s", fila.usuario.getNombre());
            col = primerVertice;
            while (col != null) {
                int conectado = fila.tieneAdyacencia(col) ? 1 : 0;
                System.out.printf("%-9d", conectado);
                col = col.siguienteVertice;
            }
            System.out.println();
            fila = fila.siguienteVertice;
        }
    }

    // ==========================================
    // ALGORITMOS (BFS / DFS)
    // ==========================================

    private void reiniciarEstadosGrafos() {
        NodoVertice actual = primerVertice;
        while (actual != null) {
            actual.visitado = false;
            actual.nivel = -1;
            actual = actual.siguienteVertice;
        }
    }

    @Override
    public void dfsAlcance(Usuario usuario) {
        NodoVertice inicio = obtenerVertice(usuario);
        if (inicio == null) {
            System.out.println("El usuario no existe.");
            return;
        }

        reiniciarEstadosGrafos(); // Limpiamos la bandera 'visitado' en todos los nodos
        System.out.print("Alcance de red de " + usuario + " (DFS): ");
        dfsRecursivo(inicio);
        System.out.println();
    }

    private void dfsRecursivo(NodoVertice actual) {
        actual.visitado = true;
        System.out.print(actual.usuario + " ");

        Arista aristaActual = actual.primerAdyacente;
        while (aristaActual != null) {
            if (!aristaActual.destino.visitado) {
                dfsRecursivo(aristaActual.destino);
            }
            aristaActual = aristaActual.siguiente;
        }
    }

    @Override
    public void bfsNiveles(Usuario usuario) {
        NodoVertice inicio = obtenerVertice(usuario);
        if (inicio == null) return;

        ejecutarBFS(inicio);

        System.out.println("Niveles de conexión para " + usuario);

        //Encontramos cuál fue el nivel máximo alcanzado en este recorrido
        int maxNivel = 0;
        NodoVertice actual = primerVertice;
        while (actual != null) {
            if (actual.nivel > maxNivel) {
                maxNivel = actual.nivel;
            }
            actual = actual.siguienteVertice;
        }

        // Imprimimos secuencialmente nivel por nivel
        for (int i = 1; i <= maxNivel; i++) {
            actual = primerVertice; // Volvemos al inicio de la lista
            while (actual != null) {
                if (actual.nivel == i) {
                    System.out.println(" - Nivel " + actual.nivel + " (" + getNivelDesc(actual.nivel) + "): " + actual.usuario);
                }
                actual = actual.siguienteVertice;
            }
        }
    }

    @Override
    public void recomendarAmigos(Usuario usuario) {
        NodoVertice inicio = obtenerVertice(usuario);
        if (inicio == null) return;

        ejecutarBFS(inicio);

        System.out.println("Recomendaciones de amistad para " + usuario + ":");
        boolean hayRecomendaciones = false;

        NodoVertice actual = primerVertice;
        while (actual != null) {
            if (actual.nivel == 2) { // Un nivel 2 significa que es "amigo de amigo"
                System.out.println(" - Conectar con: " + actual.usuario + " (Tienen amigos en común)");
                hayRecomendaciones = true;
            }
            actual = actual.siguienteVertice;
        }

        if (!hayRecomendaciones) {
            System.out.println(" - No hay recomendaciones en este momento.");
        }
    }

    private void ejecutarBFS(NodoVertice inicio) {
        reiniciarEstadosGrafos();

        ColaNodos cola = new ColaNodos();
        cola.encolar(inicio);
        inicio.nivel = 0;

        while (!cola.estaVacia()) {
            NodoVertice actual = cola.desencolar();

            Arista aristaActual = actual.primerAdyacente;
            while (aristaActual != null) {
                NodoVertice vecino = aristaActual.destino;

                // Si el nivel es -1, significa que no lo hemos visitado
                if (vecino.nivel == -1) {
                    vecino.nivel = actual.nivel + 1;
                    cola.encolar(vecino);
                }
                aristaActual = aristaActual.siguiente;
            }
        }
    }

    private String getNivelDesc(int nivel) {
        if (nivel == 1) return "Amigo directo";
        if (nivel == 2) return "Amigo de amigo";
        return "Conexión lejana";
    }
}