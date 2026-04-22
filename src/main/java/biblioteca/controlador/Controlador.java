package biblioteca.controlador;

import biblioteca.modelo.Modelo;
import biblioteca.modelo.dominio.Libro;
import biblioteca.modelo.dominio.Prestamo;
import biblioteca.modelo.dominio.Usuario;
import biblioteca.vista.Consola;
import biblioteca.vista.Vista;

import java.time.LocalDate;
import java.util.List;

public class Controlador {

    private Modelo modelo;
    private Vista vista;

    public Controlador(Modelo modelo, Vista vista) throws Exception {
        if (modelo == null) {
            throw new IllegalArgumentException("ERROR: El modelo no puede ser nulo.");
        }
        if (vista == null) {
            throw new IllegalArgumentException("ERROR: La vista no puede ser nula.");
        }
        this.modelo = modelo;
        this.vista =vista;
        this.vista.setControlador(this);
    }
    public void comenzar(){
        modelo.comenzar();
        vista.comenzar();

    }
    public void terminar(){
        Consola.terminar(); //este metodo no existe en el diagrama!!!! pero si se explicaba en la tarea.
        vista.terminar();
        modelo.terminar(); //como aparece en el diagrama lo usamos, aunque no es relevante en este proyecto
        System.out.println("Termina Controlador");



    }
    public void alta(Libro libro) throws Exception {
        modelo.alta(libro);
    }
    public boolean baja(Libro libro) throws Exception {

        return modelo.baja(libro);
    }
    public Libro buscar(Libro libro) throws Exception {
        return modelo.buscar(libro);
    }
    public List<Libro> listadoLibros() throws Exception {
        return modelo.listadoLibros();
    }
    public void alta(Usuario usuario) throws Exception {
        modelo.alta(usuario);
    }
    public boolean baja(Usuario usuario) throws Exception {
        return modelo.baja(usuario);
    }
    public Usuario buscar(Usuario usuario) throws Exception {
        return modelo.buscar(usuario);
    }
    public List<Usuario> listadoUsuario() throws Exception {
        return modelo.listadoUsuario();
    }
    public void prestar(Libro libro, Usuario usuario, LocalDate fecha) throws Exception {
        modelo.prestar(libro,usuario,fecha);
    }
    public boolean devolver(Libro libro,Usuario usuario, LocalDate fecha) throws Exception {
        return modelo.devolver(libro,usuario,fecha);
    }
    public List<Prestamo> listadoPrestamos(Usuario usuario) throws Exception {

        return modelo.listadoPrestamos(usuario);
    }

    public List<Prestamo> listadoPrestamos() throws Exception {
        return modelo.listadoPrestamos();
    }
}
