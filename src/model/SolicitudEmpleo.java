package model;

import java.time.LocalDateTime;

public class SolicitudEmpleo implements Comparable<SolicitudEmpleo> {
    private static int contadorId = 1;

    private final int idSolicitud;
    private Usuario postulante;
    private Postulacion puesto;
    private LocalDateTime fechaLlegada;

    public SolicitudEmpleo(Usuario Postulante, Postulacion puesto) {
        this.idSolicitud = contadorId++;
        this.postulante = Postulante;
        this.puesto = puesto;
        this.fechaLlegada = LocalDateTime.now();
    }

    public int getIdSolicitud() {
        return idSolicitud;
    }

    public Usuario getPostulante() {
        return postulante;
    }

    public Postulacion getPuesto() {
        return puesto;
    }

    public LocalDateTime getFechaLlegada() {
        return fechaLlegada;
    }


    @Override
    public int compareTo(SolicitudEmpleo otra) {
        return Integer.compare(this.idSolicitud, otra.idSolicitud);
    }

    @Override
    public String toString() {
        return "[Solicitud #" + idSolicitud + "] " + postulante.getNombre() + " -> " + puesto.getNombrePuesto() + " en " + puesto.getEmpresa();    }
}