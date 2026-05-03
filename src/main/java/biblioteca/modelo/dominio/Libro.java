package biblioteca.modelo.dominio;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Libro implements Comparable<Libro>{
    //realizo el cambio del ISBN_PATTER a 13 digitos que empiecen por 978 o 979 para adaptarlo a la base de datos
    public static final String ISBN_PATTERN = "^(978|979)[0-9]{10}$";
    private Categoria categoria;
    private String isbn;
    private String titulo;
    private int anio;
    private List<Autor> autores;
    //Constructor de Libro en el que inicializamos un ArrayList de Autor.
    public Libro(String isbn, String titulo, int anio, Categoria categoria) throws Exception {

        this.setIsbn(isbn);
        this.setTitulo(titulo);
        this.setAnio(anio);
        this.setCategoria(categoria);
        this.autores = new ArrayList<>();
    }
    //Constructor copia de Libro, incluyendo una copia profunda del ArrayList de Autores.
    public Libro(Libro libro) throws Exception {
        if (libro == null) {
            throw new Exception("No se puede copiar un libro nulo");
        }
        setIsbn(libro.getIsbn());
        setTitulo(libro.getTitulo());
        setAnio(libro.getAnio());
        setCategoria(libro.getCategoria());
        this.autores = new ArrayList<>();
        for(Autor autor : libro.autores){
            if(autor != null){
                this.autores.add(new Autor(autor));
            }
        }


    }

    public List<Autor> getAutores() {
        return autores;
    }

    public void setAutores(List<Autor> autores) {
        this.autores = autores;
    }

    //cambiamos a public este metodo para usarlo en la tabla
    public String getAutoresComoCadena() {
        String cadenaDeAutores = "";
        int numerar = 1;
        for (Autor autor: autores) {
            if (autor != null) {
                cadenaDeAutores += numerar + "." + autor.toString() + "\n";
                numerar++;
            }
        }
        return cadenaDeAutores;
    }

    public Categoria getCategoria() {
        return categoria;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }

    public String getIsbn() {
        return isbn;
    }

    //metodo para validar el ISBN, hacemos un trim para evitar errores si se pusiera un espacio al principio o final
    public void setIsbn(String isbn) throws Exception {

        if (isbn == null || isbn.isBlank()) {
            throw new Exception("El ISBN no puede estar vacío");
        }
        if (!isbn.trim().matches(ISBN_PATTERN)) {
            throw new Exception("El formato del ISBN no es válido. " +
                    "\nDebe ser un número de 13 dígitos que empiece por 978 o 979." +
                    "\nEjemplo: 9788437604947");
        }
        this.isbn = isbn.trim();
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) throws Exception {
        if (titulo == null || titulo.isBlank()) {
            throw new Exception("el titulo no puede estar vacío");
        }
        this.titulo = titulo;
    }

    public int getAnio() {
        return anio;
    }

    public void setAnio(int anio) throws Exception {
        if (anio < 0) {
            throw new Exception("ERROR,el año del libro no puede ser menor que 0");
        }
        this.anio = anio;
    }


    //modificamos el metodo para añadir un autores al ArrayList
    public void addAutor(Autor autor) throws Exception {
        if (autor == null) {
            throw new Exception("El autor no puede ser nulo, debe estar inicializado");
        }
        if(autores.contains(autor)) {
            throw new Exception("ERROR. El autor ya está añadido");
        }
        //sino salta ninguna Excepcion se añade el autor.
        autores.add(autor);

        }

//Cambiamos getClass por instanceof para que al buscar por isbn
//libro y audiolibro sean considerados iguales
@Override
    public boolean equals(Object o) {
        if (!(o instanceof Libro)) return false;

        Libro libro = (Libro)o; //casteamos Object o para que sea libro.

        return Objects.equals(getIsbn(), libro.getIsbn());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getIsbn());
    }

    @Override
    public String toString() {
        return "\nTitulo='" + this.titulo + '\'' +
                "\nCategoria=" + this.categoria +
                "\nISBN='" + this.isbn + '\'' +
                "\nAño=" + this.anio +
                "\n" + getAutoresComoCadena();
    }
    //metodo ordena primero alfabéticamente sin tener en cuenta mayusculas o minúsculas.
    public int compareTo(Libro o) {
        int resultadoComparacion = this.titulo.compareToIgnoreCase(o.titulo);
        return resultadoComparacion;
    }
}
