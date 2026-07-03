package view;
import TDA.Arbol_Categoria;
import Interfaces.Componente;
import model.Habilidad;
import model.Perfil;

public class SelectorPerfil {

    public static String leerTexto(java.util.Scanner scanner, String mensaje) {
        String texto;

        do {
            System.out.print(mensaje);
            texto = scanner.nextLine();

            if (texto == null || texto.length() == 0) {
                System.out.println("El valor no puede estar vacio.");
            }

        } while (texto == null || texto.length() == 0);

        return texto;
    }

    public static int leerEntero(java.util.Scanner scanner, String mensaje) {
        int numero = 0;
        boolean valido = false;

        while (!valido) {
            try {
                System.out.print(mensaje);
                numero = Integer.parseInt(scanner.nextLine());
                valido = true;
            } catch (NumberFormatException e) {
                System.out.println("Debe ingresar un numero valido.");
            }
        }

        return numero;
    }

    public static int leerEnteroEnRango(java.util.Scanner scanner, String mensaje, int minimo, int maximo) {
        int numero;

        do {
            numero = leerEntero(scanner, mensaje);

            if (numero < minimo || numero > maximo) {
                System.out.println("Opcion invalida. Debe estar entre " + minimo + " y " + maximo + ".");
            }

        } while (numero < minimo || numero > maximo);

        return numero;
    }

    public static Perfil cargarPerfil(java.util.Scanner scanner, Arbol_Categoria catalogoRaiz) {

        // 1. ELEGIR ESPECIALIDAD (Nivel 1 del árbol)
        System.out.println("\nSeleccione una especialidad:");
        Componente[] especialidades = catalogoRaiz.getHijos();

        for (int i = 0; i < especialidades.length; i++) {
            System.out.println((i + 1) + ". " + especialidades[i].getNombre());
        }
        int opcEsp = leerEnteroEnRango(scanner, "Opcion: ", 1, especialidades.length);

        // Hacemos un "cast" seguro porque sabemos que el nivel 1 contiene Categorías
        Arbol_Categoria especialidadElegida = (Arbol_Categoria) especialidades[opcEsp - 1];

        // 2. ELEGIR SUBESPECIALIDAD (Nivel 2 del árbol)
        Arbol_Categoria subespecialidadElegida;
        Componente[] subespecialidades = especialidadElegida.getHijos();

        // === SI ELIGE LA OPCIÓN 9 O SE LLAMA "Otros", SALTEA LA UI ===
        if (opcEsp == 9 || especialidadElegida.getNombre().equalsIgnoreCase("Otros")) {
            subespecialidadElegida = (Arbol_Categoria) subespecialidades[0];
        } else {
            System.out.println("\nSeleccione una subespecialidad para " + especialidadElegida.getNombre() + ":");
            for (int i = 0; i < subespecialidades.length; i++) {
                System.out.println((i + 1) + ". " + subespecialidades[i].getNombre());
            }
            int opcSub = leerEnteroEnRango(scanner, "Opcion: ", 1, subespecialidades.length);
            subespecialidadElegida = (Arbol_Categoria) subespecialidades[opcSub - 1];
        }

        // 3. ELEGIR HABILIDADES (Nivel 3 del árbol - Hojas)
        System.out.println("\nHabilidades disponibles para " + subespecialidadElegida.getNombre() + ":");
        Componente[] habilidadesDisponibles = subespecialidadElegida.getHijos();

        for (int i = 0; i < habilidadesDisponibles.length; i++) {
            System.out.println((i + 1) + ". " + habilidadesDisponibles[i].getNombre());
        }
        System.out.println("-1. Finalizar carga de habilidades");

        // Lógica manual de arreglos para selección múltiple
        Habilidad[] seleccionadasTemporal = new Habilidad[habilidadesDisponibles.length];
        int cantidadSeleccionadas = 0;
        int opcion;

        do {
            opcion = leerEnteroEnRango(
                    scanner,
                    "Seleccione una habilidad o -1 para finalizar: ",
                    -1,
                    habilidadesDisponibles.length
            );

            if (opcion == 0) {
                System.out.println(" Numero invalido. Intente nuevamente.");
            }

            else if (opcion != -1) {
                Habilidad habilidadElegida = (Habilidad) habilidadesDisponibles[opcion - 1];

                if (yaFueElegida(seleccionadasTemporal, cantidadSeleccionadas, habilidadElegida)) {
                    System.out.println("Esa habilidad ya fue seleccionada.");
                } else {
                    seleccionadasTemporal[cantidadSeleccionadas] = habilidadElegida;
                    cantidadSeleccionadas++;
                    System.out.println("Habilidad agregada: " + habilidadElegida.getNombre());
                }
            }

        } while (opcion != -1 && cantidadSeleccionadas < habilidadesDisponibles.length);

        // Ajustamos el arreglo al tamaño exacto de las seleccionadas
        Habilidad[] habilidadesElegidas = new Habilidad[cantidadSeleccionadas];
        for (int i = 0; i < cantidadSeleccionadas; i++) {
            habilidadesElegidas[i] = seleccionadasTemporal[i];
        }

        return new Perfil(especialidadElegida, subespecialidadElegida, habilidadesElegidas);
    }

    private static boolean yaFueElegida(Habilidad[] seleccionadas, int cantidadSeleccionadas, Habilidad habilidad) {
        for (int i = 0; i < cantidadSeleccionadas; i++) {
            // Comparamos por referencia de memoria, es suficiente en este caso
            if (seleccionadas[i] == habilidad) {
                return true;
            }
        }
        return false;
    }
}