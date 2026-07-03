package controller;

import TDA.Arbol_Categoria;
import TDA.DiccionarioUsuarios;
import TDA.HistorialPerfil;
import model.Perfil;
import model.Usuario;
import view.SelectorPerfil;

public class GestorPerfil {

    private HistorialPerfil historialPerfil;
    private Arbol_Categoria catalogoRaiz;


    public GestorPerfil(int tamanoHistorial, Arbol_Categoria catalogoRaiz) {
        this.historialPerfil = new HistorialPerfil(tamanoHistorial);
        this.catalogoRaiz = catalogoRaiz;
    }

    // Le agregamos la TablaHashUsuario como parámetro
    public Usuario completarUsuario(java.util.Scanner scanner, DiccionarioUsuarios plataforma) {
        System.out.println("\n--- COMPLETAR USUARIO ---");

        String nombre;
        boolean tieneNumeros;

        do {
            nombre = SelectorPerfil.leerTexto(scanner, "Ingrese nombre: ");
            tieneNumeros = nombre.matches(".*\\d.*");
            if (tieneNumeros) {
                System.out.println("El nombre no puede contener numeros. Intente nuevamente.");
            }

        } while (tieneNumeros);

        int id;
        while (true) {
            id = SelectorPerfil.leerEntero(scanner, "Ingrese numero de DNI: ");

            // Verificamos en la plataforma si ya existe
            if (plataforma.buscar(id) != null) {
                System.out.println("-> Error: El DNI " + id + " ya esta registrado. Intente con otro.");
            } else {
                break; // El ID está libre, salimos del bucle
            }
        }

        String mail = SelectorPerfil.leerTexto(scanner, "Ingrese mail: ");
        Perfil perfil = SelectorPerfil.cargarPerfil(scanner, catalogoRaiz);

        Usuario usuario = new Usuario(nombre, id, mail, perfil);

        System.out.println("\nUsuario creado correctamente.");
        return usuario;
    }

    public void editarUsuario(java.util.Scanner scanner, Usuario usuario) {
        if (usuario == null) {
            System.out.println("Primero debe completar un usuario.");
            return;
        }

        int opcion;

        do {
            System.out.println("\n--- EDITAR USUARIO ---");
            System.out.println("Usuario actual:");
            System.out.println(usuario);

            System.out.println("\n1. Editar nombre");
            System.out.println("2. Editar mail");
            System.out.println("3. Editar/Actualizar perfil");
            System.out.println("4. Editar todo");
            System.out.println("0. Salir");

            opcion = SelectorPerfil.leerEnteroEnRango(scanner, "Opcion: ", 0, 4);

            switch (opcion) {
                case 1:
                    editarNombre(scanner, usuario);
                    break;
                case 2:
                    editarMail(scanner, usuario);
                    break;
                case 3:
                    editarPerfil(scanner, usuario);
                    break;
                case 4:
                    editarTodo(scanner, usuario);
                    break;
                case 0:
                    System.out.println("Edicion finalizada.");
                    break;
            }

        } while (opcion != 0);
    }

    private void editarNombre(java.util.Scanner scanner, Usuario usuario) {
        if (!historialPerfil.guardarEstado(usuario)) {
            return;
        }

        String nuevoNombre = SelectorPerfil.leerTexto(scanner, "Ingrese nuevo nombre: ");
        usuario.setNombre(nuevoNombre);

        System.out.println("Nombre actualizado.");
    }

    private void editarMail(java.util.Scanner scanner, Usuario usuario) {
        if (!historialPerfil.guardarEstado(usuario)) {
            return;
        }

        String nuevoMail = SelectorPerfil.leerTexto(scanner, "Ingrese nuevo mail: ");
        usuario.setMail(nuevoMail);

        System.out.println("Mail actualizado.");
    }

    private void editarPerfil(java.util.Scanner scanner, Usuario usuario) {
        if (!historialPerfil.guardarEstado(usuario)) {
            return;
        }

        // Simplemente pisamos el perfil viejo con uno nuevo
        Perfil nuevoPerfil = SelectorPerfil.cargarPerfil(scanner, catalogoRaiz);
        usuario.setPerfil(nuevoPerfil);

        System.out.println("Perfil actualizado.");
    }

    private void editarTodo(java.util.Scanner scanner, Usuario usuario) {
        if (!historialPerfil.guardarEstado(usuario)) {
            return;
        }

        String nuevoNombre = SelectorPerfil.leerTexto(scanner, "Ingrese nuevo nombre: ");
        String nuevoMail = SelectorPerfil.leerTexto(scanner, "Ingrese nuevo mail: ");
        Perfil nuevoPerfil = SelectorPerfil.cargarPerfil(scanner, catalogoRaiz);

        usuario.setNombre(nuevoNombre);
        usuario.setMail(nuevoMail);
        usuario.setPerfil(nuevoPerfil);

        System.out.println("Usuario actualizado completamente.");
    }

    public void deshacerCambio(Usuario usuario) {
        historialPerfil.deshacer(usuario);
    }
}