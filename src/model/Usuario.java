package model;
import TDA.ConjuntoCadenas;

public class Usuario implements Comparable<Usuario> {

    private int id;
    private String nombre;
    private String mail;
    private Perfil perfil;

    private ConjuntoCadenas postulacionesRealizadas;

    // Constructor completo
    public Usuario(String nombre, int id, String mail, Perfil perfil) {
        this.nombre = nombre;
        this.id = id;
        this.mail = mail;
        this.postulacionesRealizadas = new ConjuntoCadenas(20);

        if (perfil != null) {
            this.perfil = new Perfil(perfil); // Constructor copia
        } else {
            this.perfil = null;
        }
    }

    // Constructor copia para guardar el historial en la pila
    public Usuario(Usuario otro) {
        this.nombre = otro.nombre;
        this.id = otro.id;
        this.mail = otro.mail;

        // Mantenemos la referencia al mismo conjunto porque el historial
        // de deshacer solo afecta al perfil, no a los trabajos a los que ya aplicó.
        this.postulacionesRealizadas = otro.postulacionesRealizadas;

        if (otro.perfil != null) {
            this.perfil = new Perfil(otro.perfil);
        } else {
            this.perfil = null;
        }
    }

    // Constructor alternativo (sin perfil inicial)
    public Usuario(String nombre, int id, String mail) {
        this.nombre = nombre;
        this.id = id;
        this.mail = mail;
        this.perfil = null;
        this.postulacionesRealizadas = new ConjuntoCadenas(20);
    }

    // Getters
    public String getNombre() {
        return nombre;
    }

    public int getId() {
        return id;
    }

    public String getMail() {
        return mail;
    }

    public Perfil getPerfil() {
        if (this.perfil != null) {
            return new Perfil(this.perfil);
        } else {
            return null;
        }
    }

    // Setters
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public void setPerfil(Perfil perfil) {
        if (perfil != null) {
            this.perfil = new Perfil(perfil);
        } else {
            this.perfil = null;
        }
    }

    public boolean registrarPostulacion(String nombrePuesto) {
        return postulacionesRealizadas.agregar(nombrePuesto);
    }

    public boolean yaSePostulo(String nombrePuesto) {
        return postulacionesRealizadas.pertenece(nombrePuesto);
    }

    public boolean esIgual(Usuario otro) {
        if (otro == null) {
            return false;
        }
        return this.id == otro.getId();
    }

    @Override
    public int compareTo(Usuario otro) {
        if (this.id < otro.id) {
            return -1;
        } else if (this.id > otro.id) {
            return 1;
        } else {
            return 0;
        }
    }

    @Override
    public String toString() {
        String textoPerfil;

        if (this.perfil == null) {
            textoPerfil = "Sin perfil configurado";
        } else {
            textoPerfil = this.perfil.toString();
        }

        return "@" + nombre + " (ID: " + id + ") | Mail: " + mail + " | Perfil: " + textoPerfil;
    }
}