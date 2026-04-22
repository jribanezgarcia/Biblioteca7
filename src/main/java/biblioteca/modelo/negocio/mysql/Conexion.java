package biblioteca.modelo.negocio.mysql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.SQLOutput;

public class Conexion {

    private static final String HOST ="127.0.0.1";
    private static final String ESQUEMA="biblioteca";
    private static final String USUARIO="admin";
    private static final String CONTRASENA="biblioteca-2026";

    private static Connection conexion = null;
    //evitamos que se instancie
    private Conexion() {

    }
    public static Connection establecerConexion() throws SQLException {
        //modificamos este condicional para usar try with resources
        //como siempre se cierra, comprobamos que cuando este cerrada la vuelva a crear.
        if(conexion==null || conexion.isClosed()){
            try{
                conexion= DriverManager.getConnection("jdbc:mysql://"+HOST+"/"+ESQUEMA,USUARIO,CONTRASENA);
                System.out.println("Conexion a Base de Datos realizada correctamente");
            }catch (SQLException e){
                //creamos una Excepcion aqui por si direra error la conexion con la base de datos.
                //esta excepcion se captuara en cada metodo de Negocio.
                throw  new SQLException("Error al conectar con la base de datos "+e.getMessage());
            }

        }

        return conexion;
    }

    /*public static Connection establecerConexion(){
        if(conexion!=null){
            return conexion;
        }
        try{
            conexion= DriverManager.getConnection("jdbc:mysql://"+HOST+"/"+ESQUEMA,USUARIO,CONTRASENA);
            System.out.println("Conexion a Base de Datos realizada correctamente");
        }catch (SQLException e){
            System.out.println("ERROR MySQL:  " + e.toString());
        }
        return conexion;
    }*/
    public static void cerrarConexion(){
        try{
            if(conexion!=null){
                conexion.close();
                conexion = null;
                System.out.println("Conexion a Base de datos cerrada correctamente.");
            }
        }catch(SQLException e){
            System.out.println("ERROR MySQL: "+ e.toString());
        }
    }



}
