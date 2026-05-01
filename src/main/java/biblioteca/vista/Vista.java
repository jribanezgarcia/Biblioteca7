package biblioteca.vista;

import biblioteca.Entrada;
import biblioteca.LanzadorVentanaPrincipal;
import biblioteca.controlador.Controlador;
import biblioteca.modelo.dominio.Libro;
import biblioteca.modelo.dominio.Prestamo;
import biblioteca.modelo.dominio.Usuario;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Vista {


    //hacemos la clase Vista Singleton
    private static Vista instancia;
    private Controlador controlador;

    public Vista() {
    }
    public static Vista getInstancia(){
        if(instancia==null){
            instancia = new Vista();
        }
        return instancia;
    }

    public void setControlador(Controlador controlador) throws Exception {
        if (controlador == null) {
            throw new Exception("ERROR!: El controlador no puede ser nulo");
        }
        this.controlador = controlador;
    }

    public Controlador getControlador(){
        return controlador;
    }
    public void comenzar() {
        LanzadorVentanaPrincipal.comenzar();
        getControlador().terminar();
    }

    public void terminar() {
        System.out.println("Termina Vista"); //En el enunciado del ejercicio que solo muestre un mensaje de despedida.

    }

    private void ejecutarOpcion(Opcion opcionElegida) {
        try {
            switch (opcionElegida) {
                case Opcion.INSERTAR_USUARIO:
                    insertarUsuario();
                    break;
                case Opcion.BORRAR_USUARIO:
                    borrarUsuario();
                    break;
                case Opcion.MOSTRAR_USUARIO:
                    mostrarUsuarios();
                    break;
                case Opcion.INSERTAR_LIBRO:
                    insertarLibro();
                    break;
                case Opcion.BORRAR_LIBRO:
                    borrarLibro();
                    break;
                case Opcion.MOSTRAR_LIBROS:
                    mostrarLibros();
                    String esperar = " ";
                    while (!esperar.equals("")) {
                        System.out.println("Pulse ENTER para continuar.");
                        esperar = Entrada.cadena();
                    }
                    break;
                case Opcion.NUEVO_PRESTAMO:
                    nuevoPrestamo();
                    break;
                case Opcion.DEVOLVER_PRESTAMO:
                    devolverPrestamo();
                    break;
                case Opcion.MOSTRAR_PRESTAMOS:
                    mostrarPrestamos();
                    break;
                case Opcion.MOSTRAR_PRESTAMOS_USUARIOS:
                    mostrarPrestamosUsuario();
                    break;
                case Opcion.SALIR:
                    controlador.terminar(); //aquí llamamos al metodo terminar para cumplir con la secuencia de llamadas dada en el ejercicio.
                    System.out.println("Gracias por visitar la aplicación de la Biblioteca de Alejandría");
                    break;
            }
        } catch (Exception e) {
            System.out.println("Error inesperado, dato introducido INCORRECTO");
            System.out.println(e.getMessage());
            System.out.println("El programa se reiniciara");
            //opcionElegida = null;
        }

    }

    private void insertarUsuario() throws Exception {
        System.out.println("||||||  AÑADIR NUEVO USUARIO  |||||||");
        try {
            Usuario usuarioPrueba = Consola.nuevoUsuario(false);
            controlador.alta(usuarioPrueba);
            System.out.println("El usuario " + usuarioPrueba.getNombre() + " ha sido añadido correctamente");
        } catch (Exception e) {
            System.out.println("Error al añadir el usuario, " + e.getMessage());
        }
    }

    private void borrarUsuario() throws Exception {
        System.out.println("\n|||||| BORRAR USUARIO |||||");
        try {
            System.out.println("Introduzca el DNI del usuario para darlo de baja");
            Usuario usuarioBorrar = Consola.nuevoUsuario(true);
            controlador.baja(usuarioBorrar);
            System.out.println("Usuario dado de baja correctamente");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private void mostrarUsuarios() throws Exception {
        System.out.println("\n|||||  LISTAR USUARIOS  ||||");
        List<Usuario> listadoUsuario = controlador.listadoUsuario();
        Collections.sort(listadoUsuario); //Usamos ahora Collections.sort para usar el CompareTo de la clase Usuario implementado por la interfaz
        int contador = 0;
        if(listadoUsuario.size()>0){
            for(Usuario usuario :listadoUsuario){
                System.out.println("\n" + "Usuario Nº= " + (contador + 1) + " " + usuario.getNombre());
                contador++;
            }
        }else{
            System.out.println("La lista de usuarios esta vacía");
        }

    }

    private void insertarLibro() {
        System.out.println("\n|||||  ALTA NUEVO LIBRO  |||||");
        try {
            Libro libroPrueba = Consola.nuevoLibro(false);
            controlador.alta(libroPrueba);
            System.out.println("El libro " + libroPrueba + "\nHa sido añadido a la biblioteca correctamente");
        } catch (Exception e) {
            System.out.println("Error al añadir el libro, " + e.getMessage());
        }
    }

    private void borrarLibro() {
        try {
            System.out.println("Introduzca el ISBN del libro para darlo de baja");
            Libro libroBorrar = Consola.nuevoLibro(true);
            controlador.baja(libroBorrar);
            System.out.println("El libro ha sido dado de baja");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private void mostrarLibros() throws Exception {
        System.out.println("\n|||||  LISTADO DE LIBROS DE LA BIBLIOTECA  ||||");
        List<Libro> mostrarLibros = controlador.listadoLibros();
        if(mostrarLibros==null){
            throw new Exception("El listado de libros no puede ser null");
        }
        //ahora hacemos Collections sort usando el CompareTo
        Collections.sort(mostrarLibros);
        int contador = 0;
        if(mostrarLibros.size()>0){

        for (Libro l:mostrarLibros) {
            if (l != null) {
                System.out.println("\n" + "Libro Nº=" + (contador + 1) + l);
                contador++;
            }
        }
        }else{
            System.out.println("No hay libros para listar");
        }
    }

    private void nuevoPrestamo() throws Exception {
        System.out.println("\n|||||REALIZAR NUEVO PRÉSTAMO|||||");
        System.out.println("Introduzca el ISBN del libro a Prestar");
        //La Vista ya no busca: pasa el "dummie" directamente al controlador.
        Libro libroDummie = Consola.nuevoLibro(true);
        System.out.println("Introduzca el dni del Usuario para hacer el préstamo");
        Usuario usuarioDummie = Consola.nuevoUsuario(true);
        LocalDate fechaPrestamo = Consola.leerFecha();
        try {
            controlador.prestar(libroDummie, usuarioDummie, fechaPrestamo);
            System.out.println("\nSe ha realizado el nuevo prestamo del libro con ISBN " + libroDummie.getIsbn());
            DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            System.out.println("La fecha de devolución del prestamo es " +
                    fechaPrestamo.plus(Period.ofDays(15)).format(formato));
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private void devolverPrestamo() throws Exception {
        System.out.println("\n|||||| DEVOLVER PRÉSTAMO ||||||");
        System.out.println("Introduzca el ISBN del libro a devolver");
        //La Vista ya no busca: pasa el "dummie" directamente al controlador.
        Libro libroDummie = Consola.nuevoLibro(true);
        System.out.println("Introduzca el dni del Usuario que va a devolver el libro.");
        Usuario usuarioDummie = Consola.nuevoUsuario(true);
        LocalDate fechaPrestamo = Consola.leerFecha();
        try {
            controlador.devolver(libroDummie, usuarioDummie, fechaPrestamo);
            System.out.println("\nSe ha realizado la devolución del libro");
            System.out.println("El libro se ha devuelto correctamente");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private void mostrarPrestamos() throws Exception {
        System.out.println("\n|||||  HISTORIAL DE PRESTAMOS ||||");
        List<Prestamo> historialPrestamos = controlador.listadoPrestamos();
        if (historialPrestamos == null) {
            throw new Exception("El historial de prestamos no puede ser nulo");
        }
        Comparator<Prestamo> comparadorFecha = Comparator.comparing(Prestamo::getfInicio).reversed(); //usamos reversed para que la ordenacion sea al reves
        Comparator<Prestamo> comparadorNombreUsuario=Comparator.comparing(Prestamo::getUsuario,Comparator.comparing(Usuario::getNombre));
        Comparator<Prestamo> comparadorFechaAndUsuario=comparadorFecha.thenComparing(comparadorNombreUsuario);
        historialPrestamos.sort(comparadorFechaAndUsuario);

        if(historialPrestamos.size()==0){
            System.out.println("No hay ningún préstamo realizado hasta el momento");
        }else{
            int i=0;
            for(Prestamo  prestamo : historialPrestamos){
                System.out.println("\nLibro prestado nº" + (i + 1) + ".\n" + prestamo);
                i++;
        }

        }

    }

    private void mostrarPrestamosUsuario() throws Exception {
        System.out.println("\n|||||  MOSTRAR PRESTAMOS DE USUARIO  ||||");
        try{
            System.out.println("Introduce el dni del usuario para ver sus prestamos");
            //La Vista ya no busca: pasa el "dummie" directamente al controlador.
            Usuario usuarioDummie = Consola.nuevoUsuario(true);
            List<Prestamo> historialPrestamosUsuario = controlador.listadoPrestamos(usuarioDummie);
            if (historialPrestamosUsuario == null) {
                throw new Exception("El historial de prestamos del usuario no puede ser nulo");
            }
            Comparator<Prestamo> comparadorFecha = Comparator.comparing(Prestamo::getfInicio).reversed(); //usamos reversed para que la ordenacion sea al reves
            Comparator<Prestamo> comparadorNombreUsuario=Comparator.comparing(Prestamo::getUsuario,Comparator.comparing(Usuario::getNombre));
            Comparator<Prestamo> comparadorFechaAndUsuario=comparadorFecha.thenComparing(comparadorNombreUsuario);
            historialPrestamosUsuario.sort(comparadorFechaAndUsuario);

            //Si el metodo historial() nos devuelve un Array con longitud 0, es que no se han producido préstamos aún.
            if (historialPrestamosUsuario.size() == 0) {
                throw new Exception("No hay libros prestados para este usuario.");
            }else{
                //si no salta ninguna excepcion imprimimos la lista.
                int i = 0;
                for (Prestamo  prestamo : historialPrestamosUsuario) {
                    System.out.println("\nLibro prestado nº" + (i + 1) + ".\n" + prestamo);
                    i++;
            }

            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }
}

