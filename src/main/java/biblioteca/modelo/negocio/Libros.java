package biblioteca.modelo.negocio;

import biblioteca.modelo.dominio.Audiolibro;
import biblioteca.modelo.dominio.Autor;
import biblioteca.modelo.dominio.Categoria;
import biblioteca.modelo.dominio.Libro;
import biblioteca.modelo.negocio.mysql.Conexion;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class Libros {

    private static Libros libros = null;

    private Libros() {

    }
    public static Libros getLibros() {
        //creamos un metodo static para devolver el libro para usarlo.
        if (libros == null) {
            libros = new Libros();
        }
        return libros;
    }


    public void alta(Libro libro) throws Exception {
        //hay que controlar que el libro no este duplicado.

        if (libro == null) {
            throw new Exception("el libro no puede ser nulo para darlo de alta");
        }
        if (buscar(libro) != null) {
            throw new Exception("!!ERROR!!! El libro ya estaba dado de alta");
        }
        List<Autor> autoresLibro = libro.getAutores();
        boolean esAudiolibro=false;

        if (libro instanceof Audiolibro) {
            esAudiolibro=true;
        } else {
            esAudiolibro=false;
        }
        String insertAutor=
                "INSERT INTO autor (nombre, apellidos, nacionalidad) values (?,?,?)";
        String insertLibro=
                "insert into libro (isbn, titulo, anio, categoria)"+
                " values (?, ?, ?, ?)";
        String insertAudioLibro=
                "insert into audiolibro(isbn, duracion_segundos, formato)"+
                " values (?, ?, ?)";
        //añade isbn y idAutor para ello buscamos el idAutor donde coincidan los tres parametros
        //puesto que es unique key nombre, apellidos, nacionalidad
        String insertLibroAutor=
                "INSERT INTO libro_autor (isbn, idAutor)" +
                " SELECT ?, idAutor  FROM autor " +
                " WHERE nombre = ? AND apellidos = ? AND nacionalidad = ?";


        //añadimos primero libro y audiolibro por las restricciones de las tablas para que luego se referencien
        Connection conexion = Conexion.establecerConexion();
        try {
            conexion.setAutoCommit(false); //hacemos el autocommit para asegurarnos insertar en las dos tablas
        } catch (SQLException e) {
            throw new Exception("ERROR al configurar la transacción: " + e.getMessage());
        }
        try(PreparedStatement pstLibro=conexion.prepareStatement(insertLibro);
            PreparedStatement pstAudioLibro = conexion.prepareStatement(insertAudioLibro);
            PreparedStatement pstAutor = conexion.prepareStatement(insertAutor);
            PreparedStatement pstLibroAutor = conexion.prepareStatement(insertLibroAutor)) {
            pstLibro.setString(1,libro.getIsbn());
            pstLibro.setString(2,libro.getTitulo());
            pstLibro.setInt(3,libro.getAnio());
            pstLibro.setString(4,libro.getCategoria().name()); //tb podemos usar .toString()
            int filasLibro= pstLibro.executeUpdate();
            if(filasLibro!=1){
                throw new SQLException("Error al insertar filas libro");
            }
            if(esAudiolibro){
                pstAudioLibro.setString(1,libro.getIsbn());
                pstAudioLibro.setInt(2, (int) ((Audiolibro) libro).getDuracion().toSeconds());//parseamos la entrada
                pstAudioLibro.setString(3,((Audiolibro) libro).getFormato());
                int filasAudioLibro= pstAudioLibro.executeUpdate();
                if(filasAudioLibro!=1){
                    throw new SQLException("Error añadir filas audiolibro");
                }
            }
            //añadimos el autor y libro_autor en la misma vuelta cuando ya estan referenciados.
            for (Autor autor : autoresLibro) {
                pstAutor.setString(1, autor.getNombre());
                pstAutor.setString(2, autor.getApellidos());
                pstAutor.setString(3, autor.getNacionalidad());
                pstAutor.executeUpdate();
                pstLibroAutor.setString(1, libro.getIsbn());
                pstLibroAutor.setString(2, autor.getNombre());
                pstLibroAutor.setString(3, autor.getApellidos());
                pstLibroAutor.setString(4, autor.getNacionalidad());
                pstLibroAutor.executeUpdate();
            }
            //hacemos el commit si no ha saltado ninguna excepcion.
            conexion.commit();
        } catch (SQLException e) {
            try {
                conexion.rollback(); //hacemos rollback si ha habido algun error
            } catch (SQLException ex) {
                throw new Exception("ERROR al hacer rollback: " + ex.getMessage());
            }
            throw new Exception("Error MySQL: " + e.getMessage());
        } finally {
            try {
                conexion.setAutoCommit(true);
            } catch (SQLException ex) {
                throw new Exception("ERROR al restaurar autocommit: " + ex.getMessage());
            }
        }

    }

    public boolean bajaLibro(Libro libro) throws Exception {
        if (libro == null) {
            throw new Exception("el libro no puede ser nulo para darlo de baja");
        }
        /*Primero borramos en la tabla libro por isbn que hará que se borre en audiolibro y libro_autor
        después borramos por autores que no esten en libro_autor, que seran los que estaban relacionados
        con el libro que se acaba de borrar
        */
        //quitamos buscar libro dos veces, lo controlaremos con el executeupodate
        String pstDeleteLibro="delete from libro where isbn = ?";
        String pstDeleteAutores="delete from autor where idAutor not in(select idAutor from libro_autor)";

        Connection conexion = Conexion.establecerConexion();
        try(PreparedStatement sentenciaBorrarLibro= conexion.prepareStatement(pstDeleteLibro);
        PreparedStatement sentenciaBorrarAutores= conexion.prepareStatement(pstDeleteAutores)){
            sentenciaBorrarLibro.setString(1,libro.getIsbn());
            int filasBL= sentenciaBorrarLibro.executeUpdate();
            if (filasBL ==0){
                throw new Exception("Libro no encontrado para dar de baja");
            }
            //si no se lanza la excepcion borramos los autores que no tienen isbn asociado.
            sentenciaBorrarAutores.executeUpdate();

        }catch (SQLException e){
            if(e.getErrorCode()==1451){
                throw new Exception("No se puede borrar el libro con ISBN "+libro.getIsbn()+
                        " porque tiene préstamos activos");
            }
            throw new Exception("ERROR MySQL "+e.getMessage());
        }

        return true;
    }
    public Libro buscar(Libro libro) throws Exception {
        if (libro == null) {
            throw new Exception("el libro no puede ser nulo");
        }

        Libro libroBuscado = null;
        //Modificamos y hacemos ahora una sola consulta para traer libros y autores que controlaremos mas adelante.
        String buscarLibro =
                "SELECT l.isbn, l.titulo, l.anio, l.categoria, " +
                        "       al.duracion_segundos, al.formato, " +
                        "       a.nombre, a.apellidos, a.nacionalidad " +
                        "FROM libro l " +
                        "LEFT JOIN audiolibro al ON al.isbn = l.isbn " +
                        "LEFT JOIN libro_autor la ON la.isbn = l.isbn " +
                        "LEFT JOIN autor a ON a.idAutor = la.idAutor " +
                        "WHERE l.isbn = ?";

        Connection conexion = Conexion.establecerConexion();
        try (PreparedStatement pstLibro = conexion.prepareStatement(buscarLibro);) {

            pstLibro.setString(1, libro.getIsbn());

            try (ResultSet rsLibro = pstLibro.executeQuery()) {
                //Controlamos con el null que si libro ya se añadio, no lo vuelva añadir pero si el autor.
                while (rsLibro.next()) {
                    if(libroBuscado==null){
                        String isbn = rsLibro.getString("isbn");
                        String titulo = rsLibro.getString("titulo");
                        int anio = rsLibro.getInt("anio");
                        Categoria categoria = Categoria.valueOf(rsLibro.getString("categoria"));
                        int duracion = rsLibro.getInt("duracion_segundos");
                        String formato = rsLibro.getString("formato");

                        if (formato != null) { // validamos si formato no es nulo, entonces es audiolibro
                            libroBuscado = new Audiolibro(isbn, titulo, anio, categoria,
                                    Duration.ofSeconds(duracion), formato);
                        } else { // es libro normal
                            libroBuscado = new Libro(isbn, titulo, anio, categoria);
                        }

                    }
                    //En cada vuelta ahora añadimos el autor que venga, habra varias filas, una por cada autor pero el mismo libro.
                    String nombreAutor= rsLibro.getString("nombre");
                    if(nombreAutor!=null){
                        libroBuscado.addAutor(new Autor(nombreAutor, rsLibro.getString("apellidos")
                                , rsLibro.getString("nacionalidad")));
                    }
                }
            }

        } catch (SQLException e) {
            throw new Exception("ERROR MySQL: " + e.getMessage());
        }

        return libroBuscado;
    }

    public List<Libro> todos() throws Exception {
        List<Libro> librosBD = new ArrayList<>();
        String leerLibros = """
          SELECT l.isbn, l.titulo, l.anio, l.categoria,
                 al.duracion_segundos, al.formato,
                 a.nombre, a.apellidos, a.nacionalidad
          FROM libro l
          LEFT JOIN audiolibro al ON l.isbn = al.isbn
          LEFT JOIN libro_autor la ON l.isbn = la.isbn
          LEFT JOIN autor a ON la.idAutor = a.idAutor
          ORDER BY l.titulo
          """;
        Connection con = Conexion.establecerConexion();
        try (PreparedStatement ptLibros = con.prepareStatement(leerLibros);
             ResultSet filasLibros = ptLibros.executeQuery()) {

            String isbnActual = null;
            Libro libroActual = null;

            while (filasLibros.next()) {
                String isbn = filasLibros.getString("isbn");

                if (!isbn.equals(isbnActual)) {
                    String titulo = filasLibros.getString("titulo");
                    int anio = filasLibros.getInt("anio");
                    Categoria categoria = Categoria.valueOf(filasLibros.getString("categoria"));
                    String formato = filasLibros.getString("formato");

                    if (formato != null) {
                        Duration duracion = Duration.ofSeconds(filasLibros.getInt("duracion_segundos"));
                        libroActual = new Audiolibro(isbn, titulo, anio, categoria, duracion, formato);
                    } else {
                        libroActual = new Libro(isbn, titulo, anio, categoria);
                    }

                    librosBD.add(libroActual);
                    isbnActual = isbn;
                }

                String nombreAutor = filasLibros.getString("nombre");
                if (nombreAutor != null) {
                    libroActual.addAutor(new Autor(
                        nombreAutor,
                        filasLibros.getString("apellidos"),
                        filasLibros.getString("nacionalidad")
                    ));
                }
            }

        } catch (SQLException e) {
            throw new Exception("ERROR MySQL: " + e.getMessage());
        }
        return librosBD;
    }
}
