package TDA;

import model.Usuario;

public class IndiceInvertidoHabilidades {

    // Lista enlazada simple para guardar a los usuarios que comparten una habilidad
    private class NodoUsuario {
        Usuario usuario;
        NodoUsuario siguiente;

        public NodoUsuario(Usuario usuario) {
            this.usuario = usuario;
            this.siguiente = null;
        }
    }

    // Nodo de la tabla Hash: guarda el nombre de la habilidad y la cabeza de la lista de usuarios
    private class NodoIndice {
        String nombreHabilidad;
        NodoUsuario cabezaUsuarios;
        NodoIndice siguiente;

        public NodoIndice(String nombreHabilidad) {
            this.nombreHabilidad = nombreHabilidad.toLowerCase().trim();
            this.cabezaUsuarios = null;
            this.siguiente = null;
        }
    }

    private NodoIndice[] tabla;
    private int capacidad;

    public IndiceInvertidoHabilidades(int capacidad) {
        this.capacidad = capacidad;
        this.tabla = new NodoIndice[capacidad];
    }

    // Función Hash simple para strings
    private int hash(String clave) {
        int hash = 0;
        for (int i = 0; i < clave.length(); i++) {
            hash = (31 * hash + clave.charAt(i)) % capacidad;
        }
        return Math.abs(hash);
    }

    // Este método extrae las habilidades del usuario y las indexa
    public void indexarUsuario(Usuario usuario) {
        if (usuario == null || usuario.getPerfil() == null || usuario.getPerfil().getHabilidades() == null) {
            return;
        }

        for (int i = 0; i < usuario.getPerfil().getHabilidades().length; i++) {
            String habilidad = usuario.getPerfil().getHabilidades()[i].getNombre();
            insertarEnIndice(habilidad, usuario);
        }
    }

    private void insertarEnIndice(String habilidad, Usuario usuario) {
        String habNormalizada = habilidad.toLowerCase().trim();
        int indice = hash(habNormalizada);
        NodoIndice actual = tabla[indice];

        // Buscamos si la habilidad ya existe en esta posición (manejo de colisiones)
        while (actual != null) {
            if (actual.nombreHabilidad.equals(habNormalizada)) {
                // La habilidad existe: agregamos al usuario al principio de su lista
                NodoUsuario nuevoUsuario = new NodoUsuario(usuario);
                nuevoUsuario.siguiente = actual.cabezaUsuarios;
                actual.cabezaUsuarios = nuevoUsuario;
                return;
            }
            actual = actual.siguiente;
        }

        // Si la habilidad no existe, creamos un nuevo nodo en el índice
        NodoIndice nuevoNodoIndice = new NodoIndice(habNormalizada);
        nuevoNodoIndice.cabezaUsuarios = new NodoUsuario(usuario);

        nuevoNodoIndice.siguiente = tabla[indice];
        tabla[indice] = nuevoNodoIndice;
    }

    // Búsqueda instantánea O(1) + O(K) (donde K son los usuarios con esa habilidad)
    public Usuario[] buscarPorHabilidad(String habilidad) {
        if (habilidad == null || habilidad.trim().isEmpty()) {
            return new Usuario[0];
        }

        String habNormalizada = habilidad.toLowerCase().trim();
        int indice = hash(habNormalizada);
        NodoIndice actual = tabla[indice];

        while (actual != null) {
            if (actual.nombreHabilidad.equals(habNormalizada)) {
                return extraerUsuariosComoArreglo(actual.cabezaUsuarios);
            }
            actual = actual.siguiente;
        }

        return new Usuario[0]; // Retorna arreglo vacío si nadie tiene la habilidad
    }

    // Convierte la lista enlazada interna en un arreglo estándar para la UI
    private Usuario[] extraerUsuariosComoArreglo(NodoUsuario cabeza) {
        int contador = 0;
        NodoUsuario temp = cabeza;
        while (temp != null) {
            contador++;
            temp = temp.siguiente;
        }

        Usuario[] resultados = new Usuario[contador];
        temp = cabeza;
        for (int i = 0; i < contador; i++) {
            resultados[i] = temp.usuario;
            temp = temp.siguiente;
        }

        return resultados;
    }
}