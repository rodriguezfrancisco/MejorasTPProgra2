package model;

import TDA.Arbol_Categoria;

public class Perfil {

    private Arbol_Categoria especialidad;
    private Arbol_Categoria subespecialidad;
    private Habilidad[] habilidades;

    public Perfil(Arbol_Categoria especialidad, Arbol_Categoria subespecialidad, Habilidad[] habilidades) {
        this.especialidad = especialidad;
        this.subespecialidad = subespecialidad;
        this.habilidades = copiarHabilidades(habilidades);
    }

    public Perfil(Perfil otro) {
        this.especialidad = otro.especialidad;
        this.subespecialidad = otro.subespecialidad;
        this.habilidades = copiarHabilidades(otro.habilidades);
    }


    private Habilidad[] copiarHabilidades(Habilidad[] origen) {
        if (origen == null) {
            return null;
        }

        Habilidad[] copia = new Habilidad[origen.length];

        for (int i = 0; i < origen.length; i++) {
            copia[i] = origen[i];
        }

        return copia;
    }

    private String habilidadesComoTexto() {
        if (habilidades == null || habilidades.length == 0) {
            return "Sin habilidades";
        }

        String texto = "";

        for (int i = 0; i < habilidades.length; i++) {
            texto += habilidades[i].getNombre();

            if (i < habilidades.length - 1) {
                texto += ", ";
            }
        }

        return texto;
    }

    public Arbol_Categoria getEspecialidad() {
        return especialidad;
    }

    public Arbol_Categoria getSubespecialidad() {
        return subespecialidad;
    }

    public Habilidad[] getHabilidades() {
        return copiarHabilidades(habilidades);
    }

    @Override
    public String toString() {
        String nombreEsp = (especialidad != null) ? especialidad.getNombre() : "Sin Especialidad";
        String nombreSub = (subespecialidad != null) ? subespecialidad.getNombre() : "Sin Subespecialidad";

        return nombreEsp + " > " + nombreSub + " > " + habilidadesComoTexto();
    }
}