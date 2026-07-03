package Interfaces;

public interface ICola<T extends Comparable<T>> {
    void encolar(T elemento);
    T desencolar();
    T frente();
    boolean estaVacia();
    boolean estaLlena();
    int tamano();
}