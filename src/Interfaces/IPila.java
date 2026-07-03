package Interfaces;
public interface IPila<T extends Comparable<T>> {
    void apilar(T elemento);
    T desapilar();
    T cima();
    boolean estaVacia();
    boolean estaLlena();
    int tamano();
}