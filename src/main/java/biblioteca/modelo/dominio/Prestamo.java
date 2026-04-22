package biblioteca.modelo.dominio;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;

import static java.time.temporal.ChronoUnit.DAYS;

public class Prestamo {
    private LocalDate fInicio;
    private LocalDate fLimite;
    private boolean devuelto;
    private LocalDate fDevolucion;
    private Libro libro;
    private Usuario usuario;


    public Prestamo(Libro libro, Usuario usuario, LocalDate fInicio) {
        this.libro = libro;
        this.usuario = usuario;
        this.fInicio = fInicio;
        this.fLimite = fInicio.plus(Period.ofDays(15)); //sumamos 15 días a la fecha de inicio que se ponga del préstamo
        this.devuelto = false;//se inicia a false para poder consultar si esta activo o no
        this.fDevolucion = null;//Se inicializa a null como pone el ejercicio.
    }


    public Libro getLibro() {
        return libro;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public LocalDate getfInicio() {
        return fInicio;
    }

    public LocalDate getfLimite() {
        return fLimite;
    }

    public LocalDate getfDevolucion() {
        return fDevolucion;
    }

    public boolean isDevuelto() {
        return devuelto;
    }

    //Metodo para calcular los días de retraso usando DAYS.between, devolviendo un int.
    public int diasRetraso() {
        LocalDate fechaParaComparar;
        if (getfDevolucion() == null) {
            fechaParaComparar = LocalDate.now();
        } else {
            fechaParaComparar = getfDevolucion();
        }
        int calculoDiasRetraso = (int) DAYS.between(getfLimite(), fechaParaComparar);
        int diasDeRetraso;
        if (calculoDiasRetraso <= 0) {
            return diasDeRetraso = 0;
        } else {
            return diasDeRetraso = calculoDiasRetraso;
        }
    }
//Metodo para comprobar que un préstamo si ha vencido.
    public boolean estaVencido() {
        boolean estaVencido = false;
        if (LocalDate.now().isAfter(getfLimite()) && getfDevolucion() == null) {
            estaVencido = true;
        }
        return estaVencido;
    }
    //Metodo para cambiar el estado de un prestamo cuando es devuelvo.
    public void marcarDevuelto(LocalDate fecha) throws Exception {
        this.devuelto = true;
        this.fDevolucion = fecha;
    }
    //Modificamos el metodo toString para que sea mas entendible cuando se consulte un prestamo.
    //Añadimos con DateTimeFormatter el formato de dias de España.
    //Añadimos un condicional para que si devuelto es true, muestre la informacion de cuando se devolvió.
    @Override
    public String toString() {
        DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String mensaje = this.libro +
                "\nUsuario: "+this.usuario.getNombre() +
                "\nFecha inicio préstamo=" + this.fInicio.format(formato) +
                "| Fecha límite préstamo=" + this.fLimite.format(formato);
        if (this.devuelto == true) {
            mensaje += "\nEstado = Devuelto el dia " + this.fDevolucion.format(formato) + ".";
        } else {
            mensaje += "\nEstado = No devuelto.";
        }
        return mensaje;
    }
}
