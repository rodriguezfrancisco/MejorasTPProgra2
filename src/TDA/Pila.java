package TDA;

import Interfaces.IPila;

public class Pila<T extends Comparable<T>> implements IPila<T> {
    private T[] datos;
    private int tope;
    private int tamanoMax;

    @SuppressWarnings("unchecked")
    public Pila(int tamanoMax) {
        this.tamanoMax = tamanoMax;
        // Al igual que en la cola, instanciamos como Comparable y casteamos a T
        this.datos = (T[]) new Comparable[tamanoMax];
        this.tope = 0; // 'tope' indica la próxima posición libre
    }

    @Override
    public boolean estaVacia() {
        return this.tope == 0;
    }

    @Override
    public boolean estaLlena() {
        return this.tope == this.tamanoMax;
    }

    @Override
    public void apilar(T elemento) {
        if (estaLlena()) {
            System.out.println("Pila llena");
        } else {
            datos[tope] = elemento;
            tope++;
        }
    }

    @Override
    public T desapilar() {
        if (estaVacia()) {
            return null; // Devolvemos null en lugar de un código de error como -1
        } else {
            tope--;
            T elemento = datos[tope];
            datos[tope] = null; // Liberamos la referencia para el Garbage Collector
            return elemento;
        }
    }

    @Override
    public T cima() {
        if (estaVacia()) {
            return null;
        } else {
            // El último elemento ingresado está en tope - 1
            return datos[tope - 1];
        }
    }

    @Override
    public int tamano() {
        return this.tope; // La cantidad de elementos coincide con el índice del tope
    }
}