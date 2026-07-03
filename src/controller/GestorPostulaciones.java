package controller;

import TDA.Cola;
import model.SolicitudEmpleo;
import model.Usuario;
import view.SelectorPerfil;

public class GestorPostulaciones {
    private Cola<SolicitudEmpleo> colaDeEspera;


    public GestorPostulaciones(int capacidadMaxima) {
        this.colaDeEspera = new Cola<>(capacidadMaxima);
    }


    public void recibirSolicitud(SolicitudEmpleo solicitud) {
        if (!colaDeEspera.estaLlena()) {
            colaDeEspera.encolar(solicitud);
            System.out.println("Solicitud registrada con éxito: " + solicitud);
        } else {
            System.out.println("No se pudo registrar la solicitud, intente nuevamente.");
        }
    }


    public void procesarSiguienteSolicitud() {
        if (!colaDeEspera.estaVacia()) {
            SolicitudEmpleo proxima = colaDeEspera.desencolar();
            System.out.println("\n Procesando " + proxima + "...");
            System.out.println("   [Resultado]: Perfil evaluado con éxito.");
        } else {
            System.out.println("\n No hay solicitudes pendientes de procesamiento.");
        }
    }


    public void verProximaSolicitud() {
        SolicitudEmpleo proxima = colaDeEspera.frente();
        if (proxima != null) {
            System.out.println(" Siguiente en la fila de espera: " + proxima);
        } else {
            System.out.println("La cola está vacía.");
        }
    }

    public void evaluarSiguienteSolicitud(java.util.Scanner scanner) {
        if (!colaDeEspera.estaVacia()) {
            SolicitudEmpleo proxima = colaDeEspera.frente();
            Usuario candidato = proxima.getPostulante();

            System.out.println("\n=============================================");
            System.out.println("📋 EVALUANDO POSTULACIÓN");
            System.out.println("=============================================");
            System.out.println("Puesto requerido: " + proxima.getPuesto().getNombrePuesto());
            System.out.println("Empresa: " + proxima.getPuesto().getEmpresa());
            System.out.println("Área requerida: " + proxima.getPuesto().getEspecialidadCategoria());
            System.out.println("---------------------------------------------");
            System.out.println("👤 Candidato: " + candidato.getNombre() + " (ID: " + candidato.getId() + ")");
            System.out.println("✉️ Mail: " + candidato.getMail());
            System.out.println("🛠️ Perfil del candidato:");
            System.out.println("   " + candidato.getPerfil().toString());
            System.out.println("=============================================\n");

            System.out.println("¿Qué decisión desea tomar con este candidato?");
            System.out.println("1. Aceptar postulación");
            System.out.println("2. Rechazar postulación");
            System.out.println("0. Cancelar revisión (dejar al candidato primero en la fila)");

            int decision = SelectorPerfil.leerEnteroEnRango(scanner, "Opción: ", 0, 2);

            if (decision == 1) {
                colaDeEspera.desencolar();
                System.out.println("✅ Postulación ACEPTADA. El candidato fue procesado.");
            } else if (decision == 2) {
                colaDeEspera.desencolar();
                System.out.println("❌ Postulación RECHAZADA. El candidato fue procesado.");
            } else {
                System.out.println("⚠️ Operación cancelada. La postulación sigue esperando en la fila.");
            }
        } else {
            System.out.println("No hay solicitudes pendientes en este momento.");
        }
    }
}