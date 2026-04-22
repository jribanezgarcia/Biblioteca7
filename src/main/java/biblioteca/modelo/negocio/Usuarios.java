package biblioteca.modelo.negocio;

import biblioteca.modelo.dominio.Direccion;
import biblioteca.modelo.dominio.Usuario;
import biblioteca.modelo.negocio.mysql.Conexion;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Usuarios {
    private static Usuarios usuarios = null;

    private Usuarios() {

    }
    public static Usuarios getUsuarios() {
        //creamos un metodo static para devolver el usuario para usarlo.
        if (usuarios == null) {
            usuarios = new Usuarios();
        }
        return usuarios;
    }


    public void alta(Usuario usuario) throws Exception {
        if (usuario == null) {
            throw new Exception("El Usuario para dar de alta no puede ser nulo");
        }

        Connection conexion = Conexion.establecerConexion();
        //ponemos autocommit en false por si hay algun error al insertar en dos tablas diferentes
        try {
            conexion.setAutoCommit(false);
        } catch (SQLException e) {
            throw new Exception("ERROR al hacer AutoCommit: " + e.getMessage());
        }
        try (PreparedStatement sentenciaUsuario = conexion.prepareStatement(
                     "INSERT INTO usuario (dni, nombre, email)" +
                             " VALUES (?, ?, ?)");
             PreparedStatement sentenciaDireccion = conexion.prepareStatement("INSERT INTO direccion " +
                     "(dni, via, numero, cp, localidad)" +
                     " VALUES (?, ?, ?, ?, ?)")) {
            //insert en tabla usuario

            sentenciaUsuario.setString(1, usuario.getDni());
            sentenciaUsuario.setString(2, usuario.getNombre());
            sentenciaUsuario.setString(3, usuario.getEmail());
            int rowUsuario = sentenciaUsuario.executeUpdate();
            if (rowUsuario == 0) {
                throw new SQLException("Error al hacer el INSERT del usuario");
            }

            sentenciaDireccion.setString(1, usuario.getDni());
            sentenciaDireccion.setString(2, usuario.getDireccion().getVia());
            sentenciaDireccion.setString(3, usuario.getDireccion().getNumero());
            sentenciaDireccion.setString(4, usuario.getDireccion().getCp());
            sentenciaDireccion.setString(5, usuario.getDireccion().getLocalidad());
            int rowDireccion = sentenciaDireccion.executeUpdate();
            if (rowDireccion == 0) {
                throw new SQLException("Error al hacer el INSERT de la direccion");
            }
            //si no salta ninguna excepcion se hace el commit.
            conexion.commit();
        } catch (SQLException e) {
            //si hay alguna excepcion se deshace el commit.
            try {
                conexion.rollback();
            } catch (SQLException ex) {
                throw new Exception("ERROR al hacer rollback: " + ex.getMessage());
            }
            if (e.getErrorCode() == 1062) {
                throw new Exception("El  DNI " + usuario.getDni() + " ya existe en nuestra base de datos");
            } else {
                throw new Exception("ERROR MySQL: " + e.getMessage());
            }
        } finally {
            try {
                //volvemos a dejar como estaba AutoCommit
                conexion.setAutoCommit(true);
            } catch (SQLException ex) {
                throw new Exception("ERROR al restaurar autocommit: " + ex.getMessage());
            }
        }
        //no cerramos la conexion porque hemos implementado try with resources

    }


    public boolean baja(Usuario usuario) throws Exception {
        if (usuario == null) {
            throw new Exception("No se puede dar de baja un usuario nulo");
        }

        String consulta="DELETE FROM usuario WHERE dni = ?";
        Connection conexion = Conexion.establecerConexion();
        try (PreparedStatement sentenciaBorrarUsuario = conexion.prepareStatement(consulta)) {

            sentenciaBorrarUsuario.setString(1, usuario.getDni());
            int row = sentenciaBorrarUsuario.executeUpdate();
            if (row == 0) {
                throw new Exception("Error al dar de baja al usuario " + usuario.getNombre());

            }
            return true;

        } catch (SQLException e) {
            throw new Exception("ERROR MySQL " + e.getMessage());
        }

    }

    public Usuario buscar(Usuario usuario) throws Exception {

        if (usuario == null) {
            throw new Exception("el usuario no puede ser nulo");

        }
        Usuario usuarioBuscado = null;
        Connection conexion = Conexion.establecerConexion();
        PreparedStatement sentencia = null;
        ResultSet filasUsuario = null;
        try {
            sentencia = conexion.prepareStatement
                    ("select u.dni, u.nombre, u.email, d.via, d.numero, d.cp, d.localidad" +
                    " from usuario u" +
                    " inner join direccion d on d.dni= u.dni" +
                    " where u.dni = ?");
            sentencia.setString(1, usuario.getDni());
            filasUsuario = sentencia.executeQuery();
            if (filasUsuario.next()) {
                Direccion direccionBuscado = new Direccion
                        (filasUsuario.getString("via"),
                                filasUsuario.getString("numero"),
                                filasUsuario.getString("cp"),
                                filasUsuario.getString("localidad"));
                usuarioBuscado = new Usuario
                        (filasUsuario.getString("dni"),
                                filasUsuario.getString("nombre"),
                                filasUsuario.getString("email"),
                                direccionBuscado);
            }

        } catch (SQLException e) {
            throw new Exception("ERROR mysql: " + e.getMessage());
        } finally {
            try {
                if (filasUsuario != null) filasUsuario.close();
                if (sentencia != null) sentencia.close();
            } catch (SQLException e) {
                throw new Exception("Error al cerrar: " + e.getMessage());
            }
        }

        return usuarioBuscado;

    }


    public List<Usuario> todos() throws Exception {

        List<Usuario> listaSinNulos = new ArrayList<>();
        Connection conexion = Conexion.establecerConexion();
        Statement sentencia = null;
        ResultSet filas = null;
        String sentenciaStr = "select u.dni, u.nombre, u.email, d.via, d.numero, d.cp, d.localidad " +
                "from usuario u " +
                "inner join direccion d on d.dni = u.dni";
        sentencia = conexion.createStatement();
        filas = sentencia.executeQuery(sentenciaStr);
        try {
            while (filas.next()) {
                Direccion direccionLista = new Direccion(
                        filas.getString("via"),
                        filas.getString("numero"),
                        filas.getString("cp"),
                        filas.getString("localidad"));
                Usuario usuarioLista = new Usuario(
                        filas.getString("dni"),
                        filas.getString("nombre"),
                        filas.getString("email"),
                        direccionLista);
                listaSinNulos.add(usuarioLista);

            }
        } catch (SQLException e) {
            throw new Exception("ERROR MySQL: " + e.getMessage());
        } finally {
            try {
                filas.close();
                sentencia.close();

                Conexion.cerrarConexion();

            } catch (SQLException e) {
                throw new Exception("Error al cerrar recursos: " + e.getMessage());
            }
        }
        return listaSinNulos;
    }
}
