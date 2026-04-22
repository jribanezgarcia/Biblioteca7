package biblioteca.modelo.dominio;

import java.time.Duration;

public class Audiolibro extends Libro{
    private Duration duracion;
    private String formato;
    public Audiolibro(String isbn,String titulo, Integer anio,Categoria categoria,Duration duracion, String formato) throws Exception {
        super(isbn,titulo,anio,categoria);
        this.setDuracion(duracion);
        this.setFormato(formato);
    }
    public Audiolibro(Audiolibro audiolibro) throws Exception {
        super(audiolibro);//valida el null del constructor de libro
        this.setDuracion(audiolibro.getDuracion());
        this.setFormato(audiolibro.getFormato());

    }

    public Duration getDuracion() {
        return duracion;
    }

    public void setDuracion(Duration duracion) throws Exception {
        if(duracion==null){
            throw new Exception("La duracion no puede ser nula");
        }
        this.duracion = duracion;
    }

    public String getFormato() {
        return formato;
    }

    public void setFormato(String formato) throws Exception {
        if(formato == null || formato.isBlank()){
            throw new Exception("el formato no puede ser nulo");
        }
        this.formato = formato;
    }


    @Override

    public String toString() {
        long horas = duracion.toHours();
        int minutos = duracion.toMinutesPart();
        int segundos = duracion.toSecondsPart();
        return "\n- Audiolibro -"
                +super.toString()+ //usamos String.format para que sea mas legible con "%02d para cumplir con hh:mm:ss que pide el ejercicio"
                String.format("Duración= %02d:%02d:%02d", horas, minutos, segundos)+
                //"Duración= " +horas+": "+minutos+": "+segundos +
                ", formato='" + formato + '\'';
    }
}
