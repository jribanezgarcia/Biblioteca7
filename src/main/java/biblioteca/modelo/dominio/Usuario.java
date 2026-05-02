package biblioteca.modelo.dominio;

import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Usuario implements Comparable<Usuario>{
    public static final String DNI_PATTERN = "([0-9]{8}|[XYZxyz][0-9]{7})[A-HJ-NP-TV-Za-hj-np-tv-z]"; //Constante para la validación de un NIE o DNI
    public static final String EMAIL_BASIC="^[A-Za-z][\\w.-]{2,14}@[A-Za-z](\\-?[A-Za-z0-9]+)*\\.[A-Za-z]{2,6}$"; //Constante para la validacion de un email
    private String dni;
    private String nombre;
    private String email;
    private Direccion direccion;

    public Usuario(String dni, String nombre, String email, Direccion direccion) throws Exception {
        this.setDni(dni);
        this.setNombre(nombre);
        this.setEmail(email);
        this.setDireccion(direccion);

    }
    //Constructor copia de Usuario con copia profunda de la dirección.
    public Usuario(Usuario usuario) throws Exception {
        if(usuario==null){
            throw new Exception("El usuario es nulo, no puede hacerse copia");
        }
        this.setDni(usuario.getDni());
        this.setNombre(usuario.getNombre());
        this.setEmail(usuario.getEmail());
        // Crear una NUEVA dirección para no duplicarla, lo hacemos asi porque la clase no tiene constructor copia
        this.direccion = new Direccion(
                usuario.getDireccion().getVia(),
                usuario.getDireccion().getNumero(),
                usuario.getDireccion().getCp(),
                usuario.getDireccion().getLocalidad());
    }

    public String getDni() {
        return dni;
    }

    public Direccion getDireccion() {
        return direccion;
    }

    public void setDireccion(Direccion direccion) throws Exception {
        if (direccion == null )
        {
            throw new Exception("la dirección no puede ser nula");
        }
        this.direccion = direccion;
    }

   //añadimos estos getters para que me lean las tablas en la clase UsuarioController
    //todas las columnas deben llevar sus getters de clase asociados.
    public String getVia() {
        return direccion.getVia();
    }

    public String getNumero() {
        return direccion.getNumero();
    }

    public String getCp() {
        return direccion.getCp();
    }

    public String getLocalidad() {
        return direccion.getLocalidad();
    }

    public void setDni(String dni) throws Exception {
        if (dni == null || dni.isBlank()) {
            throw new Exception("El ID no puede ser nulo o vacío");
        }
        if(!dni.matches(DNI_PATTERN))
        {
            throw new Exception("El ID no es correcto");
        }

        this.dni = dni.toUpperCase();
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) throws Exception {
        if (nombre == null || nombre.isBlank())
        {
            throw new Exception("el nombre no puede estar vacío");
        }
        this.nombre = nombre;
    }
    public void setEmail(String email) throws Exception {
        if (email == null || email.isBlank()) {
            throw new Exception("El email no puede ser nulo o vacío");
        }
        Pattern patron=Pattern.compile(EMAIL_BASIC);
        Matcher texto=patron.matcher(email);
        if(!texto.matches())
        {
            throw new Exception("el formato del email no es correcto");
        }
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Usuario usuario = (Usuario) o;
        return Objects.equals(getDni(), usuario.getDni());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getDni());
    }

    @Override
    public String toString() {
        return "Usuario"+
                "\nnombre='" + this.nombre + '\'' +
                "\ndni='" + this.dni + '\'' +
                "\nemail='" + this.email + '\'' +
                "\n"+ this.direccion;
    }
    //comparamos por nombre ignorando mayúsculas y minúsculas.
    @Override
    public int compareTo(Usuario o) {
        return this.nombre.compareToIgnoreCase(o.nombre);
    }
}
