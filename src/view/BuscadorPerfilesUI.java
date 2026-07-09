package view;

import TDA.Arbol_Categoria;
import TDA.DiccionarioUsuarios;
import Interfaces.Componente;
import controller.BuscadorPerfiles;
import model.Usuario;

public class BuscadorPerfilesUI {

    /**
     * Menú interactivo de búsqueda de perfiles.
     * Se añade el parámetro del índice invertido para optimizar las búsquedas por habilidad pura.
     */
    public static void menuBuscar(
            java.util.Scanner scanner,
            DiccionarioUsuarios plataforma,
            Arbol_Categoria catalogoRaiz) {

        if (plataforma.getCantidadElementos() == 0) {
            System.out.println("\n No hay usuarios registrados en la plataforma.");
            return;
        }

        System.out.println("\n--- BUSCAR USUARIOS POR PERFIL ---");
        System.out.println("¿Qué criterios desea utilizar?");
        System.out.println("1. Por Especialidad");
        System.out.println("2. Por Especialidad + Subespecialidad");
        System.out.println("3. Por Especialidad + Subespecialidad + Habilidad");
        System.out.println("4. Solo por Habilidad");
        System.out.println("5. Por Subespecialidad");
        System.out.println("0. Cancelar");

        int opcion = SelectorPerfil.leerEnteroEnRango(scanner, "\nOpción: ", 0, 5);

        String especialidad = null;
        String subespecialidad = null;
        String habilidad = null;

        switch (opcion) {
            case 1:
                especialidad = seleccionarEspecialidad(scanner, catalogoRaiz);
                break;
            case 2:
                especialidad = seleccionarEspecialidad(scanner, catalogoRaiz);
                if (especialidad != null) {
                    subespecialidad = seleccionarSubespecialidad(scanner, catalogoRaiz, especialidad);
                }
                break;
            case 3:
                especialidad = seleccionarEspecialidad(scanner, catalogoRaiz);
                if (especialidad != null) {
                    subespecialidad = seleccionarSubespecialidad(scanner, catalogoRaiz, especialidad);
                    if (subespecialidad != null) {
                        habilidad = seleccionarHabilidad(scanner, catalogoRaiz, especialidad, subespecialidad);
                    }
                }
                break;
            case 4:
                habilidad = preguntarHabilidad(scanner);
                break;
            case 5:
                subespecialidad = preguntarSubespecialidad(scanner);
                break;
            case 0:
                System.out.println("Búsqueda cancelada.");
                return;
        }

        realizarBusqueda(plataforma, especialidad, subespecialidad, habilidad);
    }

    private static String seleccionarEspecialidad(java.util.Scanner scanner, Arbol_Categoria catalogoRaiz) {
        System.out.println("\nSeleccione una especialidad:");
        Componente[] especialidades = catalogoRaiz.getHijos();

        for (int i = 0; i < especialidades.length; i++) {
            System.out.println((i + 1) + ". " + especialidades[i].getNombre());
        }

        int opcion = SelectorPerfil.leerEnteroEnRango(scanner, "Opción: ", 1, especialidades.length);
        return especialidades[opcion - 1].getNombre();
    }

    /**
     * Selecciona una subespecialidad según la especialidad elegida
     */
    private static String seleccionarSubespecialidad(
            java.util.Scanner scanner,
            Arbol_Categoria catalogoRaiz,
            String nombreEspecialidad) {

        // Buscamos la especialidad en el árbol
        Componente[] especialidades = catalogoRaiz.getHijos();
        Arbol_Categoria especialidadElegida = null;

        for (int i = 0; i < especialidades.length; i++) {
            if (especialidades[i].getNombre().equals(nombreEspecialidad)) {
                especialidadElegida = (Arbol_Categoria) especialidades[i];
                break;
            }
        }

        if (especialidadElegida == null) {
            return null;
        }

        System.out.println("\nSeleccione una subespecialidad:");
        Componente[] subespecialidades = especialidadElegida.getHijos();

        for (int i = 0; i < subespecialidades.length; i++) {
            System.out.println((i + 1) + ". " + subespecialidades[i].getNombre());
        }

        int opcion = SelectorPerfil.leerEnteroEnRango(scanner, "Opción: ", 1, subespecialidades.length);
        return subespecialidades[opcion - 1].getNombre();
    }

    private static String seleccionarHabilidad(
            java.util.Scanner scanner,
            Arbol_Categoria catalogoRaiz,
            String nombreEspecialidad,
            String nombreSubespecialidad) {

        // Buscamos la especialidad
        Componente[] especialidades = catalogoRaiz.getHijos();
        Arbol_Categoria especialidadElegida = null;

        for (int i = 0; i < especialidades.length; i++) {
            if (especialidades[i].getNombre().equals(nombreEspecialidad)) {
                especialidadElegida = (Arbol_Categoria) especialidades[i];
                break;
            }
        }

        if (especialidadElegida == null) {
            return null;
        }

        // Buscamos la subespecialidad
        Componente[] subespecialidades = especialidadElegida.getHijos();
        Arbol_Categoria subespecialidadElegida = null;

        for (int i = 0; i < subespecialidades.length; i++) {
            if (subespecialidades[i].getNombre().equals(nombreSubespecialidad)) {
                subespecialidadElegida = (Arbol_Categoria) subespecialidades[i];
                break;
            }
        }

        if (subespecialidadElegida == null) {
            return null;
        }

        // Listamos las habilidades
        System.out.println("\nSeleccione una habilidad:");
        Componente[] habilidades = subespecialidadElegida.getHijos();

        for (int i = 0; i < habilidades.length; i++) {
            System.out.println((i + 1) + ". " + habilidades[i].getNombre());
        }

        int opcion = SelectorPerfil.leerEnteroEnRango(scanner, "Opción: ", 1, habilidades.length);
        return habilidades[opcion - 1].getNombre();
    }

    /**
     * Pregunta al usuario por una habilidad de texto libre
     */
    private static String preguntarHabilidad(java.util.Scanner scanner) {
        return SelectorPerfil.leerTexto(scanner, "Ingrese el nombre de la habilidad a buscar: ");
    }

    /**
     * Pregunta al usuario por una subespecialidad de texto libre
     */
    private static String preguntarSubespecialidad(java.util.Scanner scanner) {
        return SelectorPerfil.leerTexto(scanner, "Ingrese el nombre de la subespecialidad a buscar: ");
    }

    /**
     * Realiza la búsqueda secuencial tradicional e imprime los resultados
     */
    private static void realizarBusqueda(
            DiccionarioUsuarios plataforma,
            String especialidad,
            String subespecialidad,
            String habilidad) {

        Usuario[] todosUsuarios = plataforma.obtenerTodos();
        Usuario[] resultados = BuscadorPerfiles.buscarPorFiltros(
                todosUsuarios,
                especialidad,
                subespecialidad,
                habilidad
        );

        // Construir descripción de búsqueda para el usuario
        String descripcion = construirDescripcionBusqueda(especialidad, subespecialidad, habilidad);
        BuscadorPerfiles.imprimirResultados(resultados, descripcion);
    }

    /**
     * Construye una descripción legible de los filtros aplicados
     */
    private static String construirDescripcionBusqueda(
            String especialidad,
            String subespecialidad,
            String habilidad) {

        StringBuilder sb = new StringBuilder();

        if (especialidad != null) {
            sb.append("Especialidad: ").append(especialidad);
        }

        if (subespecialidad != null) {
            if (sb.length() > 0) {
                sb.append(" | ");
            }
            sb.append("Subespecialidad: ").append(subespecialidad);
        }

        if (habilidad != null) {
            if (sb.length() > 0) {
                sb.append(" | ");
            }
            sb.append("Habilidad: ").append(habilidad);
        }

        if (sb.length() == 0) {
            return "Sin filtros";
        }

        return sb.toString();
    }
}