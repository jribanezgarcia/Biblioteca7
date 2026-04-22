package biblioteca.modelo.negocio;

import biblioteca.modelo.dominio.*;
import biblioteca.modelo.negocio.mysql.Conexion;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Prestamos {
    private static Prestamos prestamos = null;

    private Prestamos(){

    }
    public static Prestamos getPrestamos() {
        //creamos un metodo static para devolver el libro para usarlo.
        if (prestamos == null) {
            prestamos = new Prestamos();
        }
        return prestamos;
    }

    public Prestamo prestar(Libro libro,Usuario usuario,LocalDate fecha) throws Exception {
        if(libro==null){
            throw new Exception("ERROR, el libro no puede ser nulo");
        }
        if(usuario==null){
            throw new Exception("ERROR, el usuario no puede ser nulo");
        }
        if(fecha==null){
            throw new Exception("ERROR, la fecha no puede ser nula");
        }
        //buscar lo valida ya la vista al introducir los datos del usuario y del libro.
        //MySQL impide insertar un prestamo con usuario o libro que no este en ella
        //hay dos columnas que tienen valores por defecto en la BD, devuelto que sera 0 y fDevolucion null
        //estos valores no se insertan porque deben estar asi al realizar el prestamo.
        Prestamo prestamoNuevo = new Prestamo(libro,usuario,fecha);
        String sqlPrestamo= "insert into prestamo (dni, isbn, fInicio, fLimite)"+
                " values (?, ?, ?, ?)";
        Connection con = Conexion.establecerConexion();
        try(PreparedStatement psPrestar = con.prepareStatement(sqlPrestamo)){
            psPrestar.setString(1,usuario.getDni());
            psPrestar.setString(2,libro.getIsbn());
            psPrestar.setDate(3, Date.valueOf(prestamoNuevo.getfInicio()));
            psPrestar.setDate(4,Date.valueOf(prestamoNuevo.getfLimite()));
            int filas = psPrestar.executeUpdate();
            if (filas != 1) {
                throw new Exception("Error al insertar el préstamo");
            }
        }catch (SQLException e){
            throw new Exception("ERROR MySQL: "+e.getMessage());
        }
        return prestamoNuevo;
    }

    public boolean devolver(Libro libro, Usuario usuario, LocalDate fechaDevolucion) throws Exception {
        if (libro == null) {
            throw new Exception("ERROR, el libro no puede ser nulo para devolverlo");
        }
        if (usuario == null) {
            throw new Exception("ERROR, el usuario no puede ser nulo para hacer la devolucion");
        }
        if (fechaDevolucion == null) {
            throw new Exception("ERROR, la fecha no puede ser nula");
        }
        Connection conexion = Conexion.establecerConexion();
        String sqlDevolver="UPDATE prestamo SET devuelto = 1, fDevolucion = ?"+
                " WHERE dni = ? AND isbn = ? AND devuelto = 0";
        try(PreparedStatement psDevolverPrestamo= conexion.prepareStatement(sqlDevolver)){
            psDevolverPrestamo.setDate(1, Date.valueOf(fechaDevolucion));
            psDevolverPrestamo.setString(2,usuario.getDni());
            psDevolverPrestamo.setString(3, libro.getIsbn());
            int filas =psDevolverPrestamo.executeUpdate();
            if (filas==0){
                throw new Exception("Error al devolver el prestamo de "+libro.getTitulo()+" y "+usuario.getNombre());
            }

        }catch (SQLException e){
            throw new Exception("ERROR MySQL "+e.getMessage());
        }

        return true;


    }

    public List<Prestamo> todos(Usuario usuario) throws Exception {
        if (usuario == null) throw new Exception("ERROR, el usuario no puede ser nulo");

        List<Prestamo> prestamosUsuario = new ArrayList<>();
        String sql = "SELECT isbn, fInicio, fDevolucion FROM prestamo WHERE dni = ?";

        Connection con = Conexion.establecerConexion();
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, usuario.getDni());
            try (ResultSet filas = ps.executeQuery()) {
                while (filas.next()) {
                    String isbn = filas.getString("isbn");
                    LocalDate fInicio = filas.getDate("fInicio").toLocalDate();
                    LocalDate fDevolucion = null;
                    Date date = filas.getDate("fDevolucion");
                    if (date != null) fDevolucion = date.toLocalDate();
                    //Usamos el Singleton para obtener el libro.
                    Libro libro = Libros.getLibros().buscar(new Libro(isbn, "dummy", 2000, Categoria.OTROS));
                    Prestamo p = new Prestamo(libro, usuario, fInicio);
                    if (fDevolucion != null) p.marcarDevuelto(fDevolucion);
                    prestamosUsuario.add(p);
                }
            }
        } catch (SQLException e) {
            throw new Exception("ERROR MySQL: " + e.getMessage(), e);
        }
        return prestamosUsuario;
    }

    public List<Prestamo> todos() throws Exception {
        List<Prestamo> prestamosTodos = new ArrayList<>();
        String sql = "SELECT dni, isbn, fInicio, fDevolucion FROM prestamo";

        Connection con = Conexion.establecerConexion();
        try (PreparedStatement ps = con.prepareStatement(sql);
             ResultSet filas = ps.executeQuery()) {
            while (filas.next()) {
                String dni = filas.getString("dni");
                String isbn = filas.getString("isbn");
                LocalDate fInicio = filas.getDate("fInicio").toLocalDate();
                LocalDate fDevolucion = null;
                Date date = filas.getDate("fDevolucion");
                if (date != null) fDevolucion = date.toLocalDate();

                //usamos singleton creandos objetos dummyes para luego buscarlos y que nos devuelva el libro para añadirlo.
                Usuario usuario = Usuarios.getUsuarios().buscar(new Usuario(dni, "dummy", "dummy@dummy.com", new Direccion("via", "0", "00000", "localidad")));
                Libro libro = Libros.getLibros().buscar(new Libro(isbn, "dummy", 2000, Categoria.OTROS));
                Prestamo p = new Prestamo(libro, usuario, fInicio);
                if (fDevolucion != null) p.marcarDevuelto(fDevolucion);
                prestamosTodos.add(p);
            }
        } catch (SQLException e) {
            throw new Exception("ERROR MySQL: " + e.getMessage(), e);
        }
        return prestamosTodos;
    }



}
