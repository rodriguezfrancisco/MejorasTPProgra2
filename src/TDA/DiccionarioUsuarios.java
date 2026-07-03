package TDA;

import model.Usuario;

public class DiccionarioUsuarios {

    // Nodo interno para manejar las colisiones mediante una lista enlazada simple
    private class Nodo {
        Usuario usuario;
        Nodo siguiente;

        public Nodo(Usuario usuario) {
            this.usuario = usuario;
            this.siguiente = null;
        }
    }

    private Nodo[] tabla;
    private int capacidad;
    private int cantidadElementos;

    // Constructor
    public DiccionarioUsuarios(int capacidad) {
        this.capacidad = capacidad;
        this.tabla = new Nodo[capacidad];
        this.cantidadElementos = 0;
    }

    // Función Hash: Convierte el ID del usuario en un índice válido del arreglo
    private int funcionHash(int id) {
        // Usamos Math.abs por si el ID llega a ser negativo, y el módulo para que encaje en el arreglo
        return Math.abs(id) % capacidad;
    }

    // Insertar un usuario en la tabla
    public boolean insertar(Usuario usuario) {
        if (usuario == null) {
            return false;
        }

        // 1. Verificamos si el ID ya existe en la plataforma
        if (buscar(usuario.getId()) != null) {
            return false; // El ID ya está en uso, rechazamos la inserción
        }

        // 2. Si no existe, procedemos a insertarlo normalmente
        int indice = funcionHash(usuario.getId());
        Nodo nuevoNodo = new Nodo(usuario);

        if (tabla[indice] == null) {
            tabla[indice] = nuevoNodo;
        } else {
            nuevoNodo.siguiente = tabla[indice];
            tabla[indice] = nuevoNodo;
        }

        cantidadElementos++;
        return true; // Guardado exitoso
    }

    // Buscar un usuario de forma instantánea usando su ID
    public Usuario buscar(int id) {
        int indice = funcionHash(id);
        Nodo actual = tabla[indice];

        // Recorremos la lista en ese índice (si hay colisión, serán unos pocos saltos)
        while (actual != null) {
            if (actual.usuario.getId() == id) {
                return actual.usuario; // ¡Usuario encontrado!
            }
            actual = actual.siguiente;
        }

        return null; // El usuario no existe en el sistema
    }

    // Eliminar un usuario por su ID
    public boolean eliminar(int id) {
        int indice = funcionHash(id);
        Nodo actual = tabla[indice];
        Nodo anterior = null;

        while (actual != null) {
            if (actual.usuario.getId() == id) {
                if (anterior == null) {
                    // Era el primer elemento de la lista
                    tabla[indice] = actual.siguiente;
                } else {
                    // Estaba en el medio o al final
                    anterior.siguiente = actual.siguiente;
                }
                cantidadElementos--;
                return true; // Eliminado con éxito
            }
            anterior = actual;
            actual = actual.siguiente;
        }
        return false; // No se encontró para eliminar
    }

    public int getCantidadElementos() {
        return cantidadElementos;
    }

    /**
     * Obtiene todos los usuarios en la plataforma.
     * Útil para realizar búsquedas complejas sin filtro de ID.
     * <p>
     * Complejidad: O(n) donde n es la cantidad de elementos
     *
     * @return Array de todos los usuarios registrados
     */
    public Usuario[] obtenerTodos() {
        if (cantidadElementos == 0) {
            return new Usuario[0]; // Retornamos un arreglo vacío si no hay usuarios
        }

        Usuario[] usuarios = new Usuario[cantidadElementos];
        int indice = 0;

        // Recorremos toda la tabla hash
        for (int i = 0; i < tabla.length; i++) {
            Nodo actual = tabla[i];

            // Si hay elementos en esta posición, los agregamos al arreglo
            while (actual != null) {
                usuarios[indice] = actual.usuario;
                indice++;
                actual = actual.siguiente;
            }
        }

        return usuarios;
    }

}
