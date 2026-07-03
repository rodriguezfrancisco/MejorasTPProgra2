
package TDA;
import Interfaces.Componente;

public class Arbol_Categoria implements Componente {
    private String nombre;
    private Componente[] hijos;
    private int cantidadHijos;

    public Arbol_Categoria(String nombre) {
        this.nombre = nombre;
        this.hijos = new Componente[5]; // Capacidad inicial arbitraria
        this.cantidadHijos = 0;
    }

    public void agregar(Componente Componente) {
        // Si el arreglo está lleno, duplicamos su tamaño
        if (cantidadHijos == hijos.length) {
            redimensionarArreglo();
        }

        hijos[cantidadHijos] = Componente;
        cantidadHijos++;
    }

    // Lógica manual de "ArrayList"
    private void redimensionarArreglo() {
        Componente[] nuevoArreglo = new Componente[hijos.length * 2];
        for (int i = 0; i < hijos.length; i++) {
            nuevoArreglo[i] = hijos[i];
        }
        hijos = nuevoArreglo; // Reemplazamos la referencia
    }

    public void eliminar(Componente Componente) {
        for (int i = 0; i < cantidadHijos; i++) {
            if (hijos[i] == Componente) {
                // Desplazamos todos los elementos posteriores hacia la izquierda
                for (int j = i; j < cantidadHijos - 1; j++) {
                    hijos[j] = hijos[j + 1];
                }
                hijos[cantidadHijos - 1] = null; // Limpiamos la última referencia
                cantidadHijos--;
                break;
            }
        }
    }

    @Override
    public String getNombre() {
        return nombre;
    }

    public Componente[] getHijos() {
        // Retornamos un arreglo exacto para no exponer espacios vacíos (nulls)
        Componente[] hijosReales = new Componente[cantidadHijos];
        for (int i = 0; i < cantidadHijos; i++) {
            hijosReales[i] = hijos[i];
        }
        return hijosReales;
    }

    @Override
    public void imprimir(String prefijo) {
        System.out.println(prefijo + "📁 " + nombre);
        for (int i = 0; i < cantidadHijos; i++) {
            hijos[i].imprimir(prefijo + "   ");
        }
    }
}