package model;
import Interfaces.Componente;

public class Habilidad implements Componente {
    private String nombre;
    private boolean esBlanda;

    public Habilidad(String nombre, boolean esBlanda) {
        this.nombre = nombre;
        this.esBlanda = esBlanda;
    }

    @Override
    public String getNombre() {
        return nombre;
    }

    public boolean esBlanda() {
        return esBlanda;
    }

    public boolean esTecnica() {
        return !esBlanda;
    }

    @Override
    public void imprimir(String prefijo) {
        String tipo = esBlanda ? "(Blanda)" : "(Técnica)";
        System.out.println(prefijo + "📄 " + nombre + " " + tipo);
    }

    @Override
    public String toString() {
        return nombre;
    }
}