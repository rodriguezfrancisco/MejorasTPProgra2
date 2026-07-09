package controller;

import TDA.ColaPrioridadEstatica;
import model.SolicitudEmpleo;
import model.Usuario;
import view.SelectorPerfil;

public class GestorPostulaciones {

    // Instanciamos el TDA diseñado en clase
    private ColaPrioridadEstatica colaDeEspera;

    public GestorPostulaciones(int capacidadMaxima) {
        this.colaDeEspera = new ColaPrioridadEstatica(capacidadMaxima);
    }

    public void recibirSolicitud(SolicitudEmpleo solicitud) {
        Usuario candidato = solicitud.getPostulante();
        String nombrePuesto = solicitud.getPuesto().getNombrePuesto();

        // 1. BARRERA DE UNICIDAD: Consultamos al Conjunto en O(1)
        if (candidato.yaSePostulo(nombrePuesto)) {
            System.out.println("❌ Solicitud rechazada: El usuario '" + candidato.getNombre() +
                    "' ya se había postulado previamente a este puesto.");
            return; // Cortamos la ejecución, evitando duplicados en la cola
        }

        // 2. Si pasó la barrera, intentamos encolar
        if (!colaDeEspera.estaLlena()) {
            boolean exito = colaDeEspera.insertar(solicitud);
            if (exito) {
                // Si entró a la cola, lo registramos en su conjunto personal
                candidato.registrarPostulacion(nombrePuesto);
                System.out.println("✅ Solicitud registrada y encolada por relevancia: " + solicitud);
            }
        } else {
            System.out.println("❌ No se pudo registrar la solicitud, la cola de la empresa está llena.");
        }
    }

    public void procesarSiguienteSolicitud() {
        if (!colaDeEspera.estaVacia()) {
            SolicitudEmpleo proxima = colaDeEspera.eliminar();
            System.out.println("\n Procesando " + proxima + "...");
            System.out.println("   [Resultado]: Perfil evaluado con éxito.");
        } else {
            System.out.println("\n No hay solicitudes pendientes de procesamiento.");
        }
    }

    public void verProximaSolicitud() {
        SolicitudEmpleo proxima = colaDeEspera.frente();
        if (proxima != null) {
            System.out.println("\n Siguiente candidato recomendado por el algoritmo: " + proxima);
        } else {
            System.out.println("\n La cola de postulaciones está vacía.");
        }
    }

    public void evaluarSiguienteSolicitud(java.util.Scanner scanner) {
        if (!colaDeEspera.estaVacia()) {
            // Evaluamos al candidato con mayor Score (frente de la cola)
            SolicitudEmpleo proxima = colaDeEspera.frente();
            Usuario candidato = proxima.getPostulante();

            System.out.println("\n=============================================");
            System.out.println("📋 EVALUANDO CANDIDATO DE ALTA PRIORIDAD");
            System.out.println("=============================================");
            System.out.println("Puesto requerido: " + proxima.getPuesto().getNombrePuesto());
            System.out.println("Empresa: " + proxima.getPuesto().getEmpresa());
            System.out.println("Área requerida: " + proxima.getPuesto().getEspecialidadCategoria());
            System.out.println("---------------------------------------------");
            System.out.println("👤 Candidato: " + candidato.getNombre() + " (ID: " + candidato.getId() + ")");
            System.out.println("✉️ Mail: " + candidato.getMail());
            System.out.println("⭐ SCORE DE MATCHING: " + proxima.calcularScoreMatching() + " puntos");
            System.out.println("🛠️ Perfil del candidato:");
            System.out.println("   " + candidato.getPerfil().toString());
            System.out.println("=============================================\n");

            System.out.println("¿Qué decisión desea tomar con este candidato?");
            System.out.println("1. Aceptar postulación");
            System.out.println("2. Rechazar postulación");
            System.out.println("0. Cancelar revisión (dejar al candidato en la cima de la cola)");

            int decision = SelectorPerfil.leerEnteroEnRango(scanner, "Opción: ", 0, 2);

            if (decision == 1) {
                colaDeEspera.eliminar();
                System.out.println("✅ Postulación ACEPTADA. El candidato fue procesado.");
            } else if (decision == 2) {
                colaDeEspera.eliminar();
                System.out.println("❌ Postulación RECHAZADA. El candidato fue procesado.");
            } else {
                System.out.println("⚠️ Operación cancelada. La postulación sigue liderando la cola de prioridad.");
            }
        } else {
            System.out.println("No hay solicitudes pendientes en este momento.");
        }
    }
}