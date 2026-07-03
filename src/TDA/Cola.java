package TDA;

import Interfaces.ICola;

public class Cola<T extends Comparable<T>> implements ICola<T> {
    private T[] datos;
    private int fin;
    private int frente;
    private int tamano;

    @SuppressWarnings("unchecked")
    public Cola(int tamano){
        this.tamano = tamano;
        // En Java no se pueden instanciar arreglos genéricos directamente.
        // Se instancia como Comparable y se castea.
        this.datos = (T[]) new Comparable[tamano];
        this.frente = 0;
        this.fin = 0;
    }

    @Override
    public boolean estaVacia() {
        return this.fin <= this.frente;
    }

    @Override
    public boolean estaLlena() {
        return this.tamano == this.fin;
    }

    @Override
    public void encolar(T elemento) {
        if (estaLlena()){
            System.out.println("TDA.Cola llena");
        } else {
            datos[fin] = elemento;
            fin++;
        }
    }

    @Override
    public T desencolar() {
        if (estaVacia()){
            return null; // En vez de -1, retornamos null
        } else {
            T x = datos[frente];
            datos[frente] = null; // Liberamos la referencia para el Garbage Collector
            frente++;
            return x;
        }
    }

    @Override
    public T frente() {
        if (estaVacia()){
            return null;
        } else {
            return datos[frente];
        }
    }

    @Override
    public int tamano() {
        return this.tamano;
    }
}
