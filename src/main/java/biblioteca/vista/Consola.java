package biblioteca.vista;

import biblioteca.Entrada;
import biblioteca.modelo.dominio.*;

import java.time.Duration;
import java.time.LocalDate;

public class Consola {
    private Consola() { //para evitar que se pueda instanciar la clase
    }

    public static void mostrarMenu() {
        System.out.println("***************************************************************************************");
        System.out.println("                MENú PRINCIPAL DE LA BIBLIOTECA DE ALEJANDRIA");
        System.out.println("***************************************************************************************");
        System.out.println();
        System.out.println("1.- Alta usuario");
        System.out.println("2.- Borrar usuario");
        System.out.println("3.- Listar usuarios");
        System.out.println("4.- Alta libro");
        System.out.println("5.- Baja libro");
        System.out.println("6.- Listar libros");
        System.out.println("7.- Nuevo préstamo");
        System.out.println("8.- Devolver préstamo");
        System.out.println("9.- Mostrar todos los préstamos");
        System.out.println("10.-Mostrar préstamos de un usuario");
        System.out.println("0.- Salir");
    }

    public static Opcion ElegirOpcion() {
        Opcion opcionElegida = null;
        int ordinalOpcion;
        do {
            System.out.println("\nElige una opción (0-10): ");
            ordinalOpcion = Entrada.entero();
        } while (ordinalOpcion < 0 || ordinalOpcion > 10);

        switch (ordinalOpcion) {
            case 1:
                opcionElegida = Opcion.INSERTAR_USUARIO;
                break;
            case 2:
                opcionElegida = Opcion.BORRAR_USUARIO;
                break;
            case 3:
                opcionElegida = Opcion.MOSTRAR_USUARIO;
                break;
            case 4:
                opcionElegida = Opcion.INSERTAR_LIBRO;
                break;
            case 5:
                opcionElegida = Opcion.BORRAR_LIBRO;
                break;
            case 6:
                opcionElegida = Opcion.MOSTRAR_LIBROS;
                break;
            case 7:
                opcionElegida = Opcion.NUEVO_PRESTAMO;
                break;
            case 8:
                opcionElegida = Opcion.DEVOLVER_PRESTAMO;
                break;
            case 9:
                opcionElegida = Opcion.MOSTRAR_PRESTAMOS;
                break;
            case 10:
                opcionElegida = Opcion.MOSTRAR_PRESTAMOS_USUARIOS;
                break;
            case 0:
                opcionElegida = Opcion.SALIR;
                break;
        }
        return opcionElegida;
    }

    public static Usuario nuevoUsuario(boolean paraBuscar) throws Exception {

        if (paraBuscar) {
            String idBusqueda = Entrada.cadena();
            Usuario usuarioVirtual = new Usuario(idBusqueda, "vacio",
                    "vacio@vario.com", new Direccion("via", "numero", "04001", "Almeria"));
            return usuarioVirtual;
        } else {
            System.out.println("Introduce los datos del usuario para darlo de alta");
            System.out.println("Introduce DNI o NIE del nuevo usuario");
            String dni = Entrada.cadena();
            System.out.println("Introduce el nombre del Usuario");
            String nombre = Entrada.cadena();
            System.out.println("Introduce una dirección email válida, por ej. ->> nombre@dominio.com ");
            String email = Entrada.cadena();
            System.out.println("Introduce la dirección completa del usuario: Calle, Número, Código Postal y Localidad");
            System.out.println("Introduzca la calle");
            String via = Entrada.cadena();
            System.out.println("Introduzca el numero de la calle");
            String numero = Entrada.cadena();
            System.out.println("Introduzca el código postal");
            String cp = Entrada.cadena();
            System.out.println("Introduzca la localidad");
            String localidad = Entrada.cadena();
            Direccion direccion = new Direccion(via, numero, cp, localidad);
            Usuario usuarioNuevo = new Usuario(dni, nombre, email, direccion);
            return usuarioNuevo;
        }

    }

    public static Libro nuevoLibro(boolean paraBuscar) throws Exception {
        if (paraBuscar) {
            String isbBuscar = Entrada.cadena();
            Libro libroVirtual = new Libro(isbBuscar, "titulo", 2000, Categoria.OTROS);
            return libroVirtual;
        } else {
            int tipoLibro=0;
            do{
                System.out.println("Pulsa 1 para Libro"+"\nPulsa 2 para Audiolibro");
                tipoLibro = Entrada.entero();
                if(tipoLibro!=1&&tipoLibro!=2){
                    System.out.println("Opción Incorrecta");
                }
            }while(tipoLibro!=1 && tipoLibro!=2);
            // Datos comunes a Libro y Audiolibro
            System.out.println("Introduce el número ISBN (13 dígitos).");
            System.out.println("Debe comenzar con 978 o 979.");
            System.out.println("Ejemplo válido: 9783161484100");
            String isbn = Entrada.cadena();
            System.out.println("Inserta titulo del libro");
            String titulo = Entrada.cadena();
            System.out.println("Introduce el año del libro que debe ser un número positivo");
            int anio = Entrada.entero();
            for (int i = 0; i < Categoria.values().length; i++) {
                System.out.printf("%d.- %s%n", Categoria.values()[i].ordinal() + 1, Categoria.values()[i]);
            }
            int numCategoria;
            do {
                System.out.println("Introduzca la Categoria del Libro 1-7");
                numCategoria = Entrada.entero();
            } while (numCategoria < 1 || numCategoria >= Categoria.values().length + 1);
            Categoria categoria = Categoria.values()[numCategoria - 1];

            // Crear objeto según el tipo elegido
            Libro libroNuevo;
            if (tipoLibro == 2) {
                System.out.println("Introduce los minutos Audiolibro");
                Duration duracion = Duration.ofMinutes(Entrada.entero());
                System.out.println("Introduce el Formato del Audiolibro\nmp3, mp4B, AA/AAX u otro");
                String formato = Entrada.cadena();
                libroNuevo = new Audiolibro(isbn, titulo, anio, categoria, duracion, formato);
            } else {
                libroNuevo = new Libro(isbn, titulo, anio, categoria);
            }

            // Autores (común a ambos tipos)
            int numeroAutores = -1;
            do {
                System.out.println("¿Cuántos Autores tiene el libro?");
                numeroAutores = Entrada.entero();
                if (numeroAutores < 1) {
                    System.out.println("Error al introducir el número de autores");
                }
            } while (numeroAutores < 1);

            for (int i = 0; i < numeroAutores; i++) {
                libroNuevo.addAutor(nuevoAutor());
            }

            return libroNuevo;
        }

    }

    private static Autor nuevoAutor() throws Exception {
        System.out.println("Introduce el nombre del Autor");
        String nombreAutor = Entrada.cadena();
        System.out.println("Introduce los Apellidos del Autor");
        String apellidosAutor = Entrada.cadena();
        System.out.println("Introduce la nacionalidad del Autor");
        String nacionalidadAutor = Entrada.cadena();
        Autor nuevoAutor = new Autor(nombreAutor, apellidosAutor, nacionalidadAutor);
        return nuevoAutor;
    }

    public static LocalDate leerFecha() {
        LocalDate fechaHoy = LocalDate.now();
        return fechaHoy;
    }
    //añadimos este metodo para lanzar el mensaje de Termina Consola aunque no sale en el diagrama si en la descripción de la tarea.
    public static void terminar() {
        System.out.println("Termina Consola");
    }
}
