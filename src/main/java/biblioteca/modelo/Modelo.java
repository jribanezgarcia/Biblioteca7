package biblioteca.modelo;

import biblioteca.modelo.dominio.Libro;
import biblioteca.modelo.dominio.Prestamo;
import biblioteca.modelo.dominio.Usuario;
import biblioteca.modelo.negocio.Libros;
import biblioteca.modelo.negocio.Prestamos;
import biblioteca.modelo.negocio.Usuarios;
import biblioteca.modelo.negocio.mysql.Conexion;

import java.time.LocalDate;
import java.util.List;

public class Modelo {

    private Prestamos prestamos;
    private Libros libros;
    private Usuarios usuarios;

    public Modelo() {
    }

    public void comenzar() {
        this.prestamos = Prestamos.getPrestamos();
        this.libros = Libros.getLibros();
        this.usuarios = Usuarios.getUsuarios();
    }

    public void terminar() { //No esta claro el uso de este método.
        //cerramos la conexion a la base de datos aqui, al cerrar el programa.
        Conexion.cerrarConexion();
        System.out.println("Termina Modelo");
    }

    public void alta(Libro libro) throws Exception {

        libros.alta(libro);
    }

    public boolean baja(Libro libro) throws Exception {

        return libros.bajaLibro(libro);
    }

    public Libro buscar(Libro libro) throws Exception {
        return libros.buscar(libro); //devolvemos una nueva instancia del elemento encontrado.
    }

    public List<Libro> listadoLibros() throws Exception {
        return libros.todos();
    }

    public void alta(Usuario usuario) throws Exception {

        usuarios.alta(usuario);
    }

    public boolean baja(Usuario usuario) throws Exception {

        return usuarios.baja(usuario);
    }

    public Usuario buscar(Usuario usuario) throws Exception {
        return usuarios.buscar(usuario); //devolvemos una copia
    }


    public List<Usuario> listadoUsuario() throws Exception {
        return usuarios.todos();
    }

    public void prestar(Libro libro, Usuario usuario, LocalDate fecha) throws Exception {

        prestamos.prestar(libro, usuario, fecha);
    }

    public boolean devolver(Libro libro, Usuario usuario, LocalDate fecha) throws Exception {

        return prestamos.devolver(libro, usuario, fecha);
    }

    public List<Prestamo> listadoPrestamos(Usuario usuario) throws Exception {

        return prestamos.todos(usuario);
    }

    public List<Prestamo> listadoPrestamos() throws Exception {
        return prestamos.todos();
    }
}

