package biblioteca;

import biblioteca.controlador.Controlador;
import biblioteca.modelo.Modelo;
import biblioteca.vista.Vista;


/** @author jribanezgarcia */

public class AppBiblioteca {
    public static void main(String[] args) {


        try {

            System.out.println("Bienvenido al programa de gestion de reservas de la Biblioteca de Alejandría");
            Modelo modelo = new Modelo();
            Vista vista = Vista.getInstancia();
            Controlador controlador = new Controlador(modelo, vista);
            controlador.comenzar();


        } catch (Exception e) {
            // Capturamos cualquier error inesperado
            // para evitar que la app se cierre bruscamente sin aviso.
            System.out.println("Error fatal: " + e.getMessage());
        }

    }
}