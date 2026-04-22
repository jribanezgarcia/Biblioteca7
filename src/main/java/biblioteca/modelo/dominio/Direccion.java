package biblioteca.modelo.dominio;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Direccion {
    public static final String  CP_PATTERN ="[\\d]{5}";
    private String via;
    private String numero;
    private String cp;
    private String localidad;
    public Direccion (String via, String numero, String cp, String localidad) throws Exception {
        setVia(via);
        setNumero(numero);
        setCp(cp);
        setLocalidad(localidad);
    }

    public String getLocalidad() {
        return localidad;
    }

    public void setLocalidad(String localidad) throws Exception {
        if (localidad == null || localidad.isBlank())
        {
            throw new Exception("la localidad no puede estar vacía");
        }
        this.localidad = localidad;
    }

    public String getVia() {
        return via;
    }

    public void setVia(String via) throws Exception {
        if (via == null || via.isBlank())
        {
            throw new Exception("la via no puede estar vacia");
        }

        this.via = via;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) throws Exception {
        if (numero == null || numero.isBlank())
        {
            throw new Exception("el numero de la dirección no puede estar vacío");
        }
        this.numero = numero;
    }

    public String getCp() {
        return cp;
    }

    //Validación del código postal
    public void setCp(String cp) throws Exception {
        Pattern patron=Pattern.compile(CP_PATTERN);
        Matcher texto=patron.matcher(cp);
        if(!texto.matches())
        {
            throw new Exception("El código postal no es correcto");
        }
        this.cp = cp;
    }

    @Override
    public String toString() {
        return "La Direccion es :" +
                "via='" + this.via + '\'' +
                ", numero='" + this.numero + '\'' +
                ", cp='" + this.cp + '\'' +
                ", localidad='" + this.localidad + '\'';
    }
}
