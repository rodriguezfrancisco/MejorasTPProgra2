package model;

import java.time.LocalDateTime;

public class SolicitudEmpleo implements Comparable<SolicitudEmpleo> {
    private static int contadorId = 1;

    private final int idSolicitud;
    private Usuario postulante;
    private Postulacion puesto;
    private LocalDateTime fechaLlegada;

    public SolicitudEmpleo(Usuario postulante, Postulacion puesto) {
        this.idSolicitud = contadorId++;
        this.postulante = postulante;
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

    /**
     * Calcula un puntaje de compatibilidad (Matching Score) entre el usuario y el puesto.
     * Esto actúa como el atributo "prioridad" de las diapositivas de clase.
     */
    public int calcularScoreMatching() {
        int score = 0;

        if (postulante.getPerfil() != null) {
            // 1. Validar coincidencia de rama principal (+50 puntos)
            if (postulante.getPerfil().getEspecialidad() != null &&
                    postulante.getPerfil().getEspecialidad().getNombre().equalsIgnoreCase(puesto.getEspecialidadCategoria())) {
                score += 50;
            }

            // 2. Bonificación por cantidad de habilidades (+10 puntos por cada una)
            if (postulante.getPerfil().getHabilidades() != null) {
                score += postulante.getPerfil().getHabilidades().length * 10;
            }
        }

        return score;
    }

    @Override
    public int compareTo(SolicitudEmpleo otra) {
        return Integer.compare(this.idSolicitud, otra.idSolicitud);
    }

    @Override
    public String toString() {
        int score = calcularScoreMatching();
        return "[Solicitud #" + idSolicitud + " | Score: " + score + "] " +
                postulante.getNombre() + " -> " + puesto.getNombrePuesto() + " en " + puesto.getEmpresa();
    }
}