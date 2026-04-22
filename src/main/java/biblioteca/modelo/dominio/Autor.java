package biblioteca.modelo.dominio;

import java.util.Objects;
import java.util.StringTokenizer;

public class Autor {
    private String nombre;
    private String apellidos;
    private String nacionalidad;

    public Autor(String nombre, String apellidos, String nacionalidad) throws Exception {
        this.setNombre(nombre);
        this.setApellidos(apellidos);
        this.setNacionalidad(nacionalidad);
    }

    public Autor(Autor autor) throws Exception { //Constructor copia de Autor
        if (autor == null) {
            throw new Exception("No se puede copiar un autor nulo");
        }
        this.setNombre(autor.getNombre());
        this.setApellidos(autor.getApellidos());
        this.setNacionalidad(autor.getNacionalidad());
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) throws Exception {
        if (nombre == null || nombre.isBlank()) {
            throw new Exception("El nombre del autor no puede estar vacío");
        }
        this.nombre = nombre;
    }


    public String getApellidos() {

        return apellidos;
    }

    public void setApellidos(String apellidos) throws Exception {
        if (apellidos == null || apellidos.isBlank()) {
            throw new Exception("Los apellidos del autor no pueden estar vacío");
        }
        this.apellidos = apellidos;
    }

    public String getNacionalidad() {
        return nacionalidad;
    }

    public void setNacionalidad(String nacionalidad) throws Exception {
        if (nacionalidad == null || nacionalidad.isBlank()) {
            throw new Exception("la nacionalidad del autor no puede estar vacía");
        }
        this.nacionalidad = nacionalidad;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Autor autor = (Autor) o;
        return Objects.equals(getNombre(), autor.getNombre()) && Objects.equals(getApellidos(),
                autor.getApellidos()) && Objects.equals(getNacionalidad(), autor.getNacionalidad());
    }

    @Override
    public String toString() {
        return "Autor->" +
                this.nombre + " " +
                this.apellidos + " " +
                " con nacionalidad " + this.nacionalidad + "."
                ;
    }

    @Override
    public int hashCode() {
        return Objects.hash(getNombre(), getApellidos(), getNacionalidad());
    }

    public String getNombreCompleto() {
        return "el nombre completo del Autor es: " + this.nombre + " " + this.apellidos;
    }

    public String iniciales() { //Usamos ahora StrinTokenizer para quitar el array normal
        String inicialesNombre = "";
        String inicialesApellido = "";

        StringTokenizer tokenNombre = new StringTokenizer(getNombre().toUpperCase());//pasamos a mayúsculas
        while (tokenNombre.hasMoreTokens()) {
            inicialesNombre = inicialesNombre + tokenNombre.nextToken().charAt(0) + ".";//sacamos la inicial
        }

        StringTokenizer tokenApellido = new StringTokenizer(getApellidos().toUpperCase());
        while (tokenApellido.hasMoreTokens()) {
            inicialesApellido = inicialesApellido + tokenApellido.nextToken().charAt(0) + ".";
        }

        return inicialesNombre + " " + inicialesApellido;
    }

}
