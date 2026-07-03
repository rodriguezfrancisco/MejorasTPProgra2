package TDA;

import model.Usuario;

public class HistorialPerfil {

    private Pila<Usuario> historial;

    public HistorialPerfil(int tamanoMaximo) {
        historial = new Pila<Usuario>(tamanoMaximo);
    }

    public boolean guardarEstado(Usuario usuario) {
        if (usuario == null) {
            System.out.println("No hay usuario para guardar en historial.");
            return false;
        }

        if (historial.estaLlena()) {
            System.out.println("Historial lleno. No se puede guardar el cambio.");
            return false;
        }

        historial.apilar(new Usuario(usuario));
        return true;
    }

    public boolean puedeDeshacer() {
        return !historial.estaVacia();
    }

    public void deshacer(Usuario usuario) {
        if (usuario == null) {
            System.out.println("No hay usuario para restaurar.");
            return;
        }

        if (historial.estaVacia()) {
            System.out.println("No hay cambios para deshacer.");
            return;
        }

        Usuario anterior = historial.desapilar();

        usuario.setNombre(anterior.getNombre());
        usuario.setMail(anterior.getMail());

        // ESTO CAMBIA: de getPerfiles() a getPerfil()
        usuario.setPerfil(anterior.getPerfil());

        System.out.println("Se restauro el cambio anterior.");
    }
}
