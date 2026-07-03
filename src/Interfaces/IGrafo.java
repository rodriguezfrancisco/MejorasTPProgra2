package Interfaces;
import model.Usuario;


public interface IGrafo {

        void insertarVertice(Usuario vertice);
        void eliminarVertice(Usuario vertice);
        void insertarArista(Usuario origen, Usuario destino);
        void eliminarArista(Usuario origen, Usuario destino);
        boolean existeVertice(Usuario vertice);
        boolean existeArista(Usuario origen, Usuario destino);
        void mostrarMatriz();
        void mostrarVertices();

        void dfsAlcance(Usuario usuario);
        void bfsNiveles(Usuario usuario);
        void recomendarAmigos(Usuario usuario);
}
