package biblioteca.vista.controlador;

import biblioteca.modelo.dominio.Direccion;
import biblioteca.modelo.dominio.Usuario;
import biblioteca.utilidades.Dialogos;
import biblioteca.vista.Vista;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import java.util.List;
import javafx.event.ActionEvent;

public class FormularioUsuarioController {

    @FXML
    private Button botonGuardarUsuario;

    @FXML
    private Label labelUsuario;

    @FXML
    private TextField txtCodigoPostal;

    @FXML
    private TextField txtDni;

    @FXML
    private TextField txtEmail;

    @FXML
    private TextField txtLocalidad;

    @FXML
    private TextField txtNombre;

    @FXML
    private TextField txtNumero;

    @FXML
    private TextField txtVia;

    private List<Usuario> listaUsuarios;

    private Usuario registro;

    public void setListaUsuarios(List<Usuario> listaUsuarios){
        this.listaUsuarios = listaUsuarios;
    }
    public void setRegistro(Usuario u){
        this.registro = u;
        if(this.registro!=null){
            labelUsuario.setText("Editar Usuario");
            this.txtDni.setText(this.registro.getDni());
            this.txtEmail.setText(this.registro.getEmail());
            this.txtLocalidad.setText(this.registro.getLocalidad());
            this.txtNombre.setText(this.registro.getNombre());
            this.txtNumero.setText(this.registro.getNumero());
            this.txtVia.setText(this.registro.getVia());
            this.txtCodigoPostal.setText(this.registro.getCp());
        }else{
            labelUsuario.setText("Añadir Usuario");
        }
    }
    public Usuario getRegistro(){
        return this.registro;
    }
    @FXML
    void GuardarUsuario(ActionEvent event){
        try{
            String dni= this.txtDni.getText();
            String nombre=this.txtNombre.getText();
            String email=this.txtEmail.getText();
            String via=this.txtVia.getText();
            String numero=this.txtNumero.getText();
            String cp=this.txtCodigoPostal.getText();
            String localidad=this.txtLocalidad.getText();

            if(this.registro==null){
                Direccion direccion = new Direccion(via,numero,cp,localidad);
                Usuario u= new Usuario(dni,nombre,email,direccion);
                if(this.listaUsuarios.contains(u)==false){
                    //Añadimos el usuario a la base de datos.
                    Vista.getInstancia().getControlador().alta(u);
                    Dialogos.mostrarDialogoAdvertencia("Mensaje Usuario","Usuario añadido correctamente");
                    this.registro= u;
                    Stage escenarioActual = (Stage) ((Node) event.getSource()).getScene().getWindow();
                    escenarioActual.close();
                }else{
                    Dialogos.mostrarDialogoAdvertencia("Mensaje Usuario","El Usuario ya existe");
                }
            //modificamos un usuario existente
            }else{
                /*this.registro.setDni(dni);
                this.registro.setNombre(nombre);
                this.registro.setEmail(email);
                this.registro.setDireccion(new Direccion(via,numero,cp,localidad));*/
                //prueba "modificar" base de datos , en realdiad es baja y alta no update
                Direccion direccion = new Direccion(via, numero, cp, localidad);
                Usuario uNuevo = new Usuario(dni, nombre, email, direccion);
                // this.registro sigue siendo el usuario ORIGINAL (con el DNI antiguo)
                Vista.getInstancia().getControlador().baja(this.registro);
                Vista.getInstancia().getControlador().alta(uNuevo);

                Dialogos.mostrarDialogoAdvertencia("Mensaje Usuario","Usuario MODIFICADO correctamente");
                Stage escenarioActual = (Stage) ((Node) event.getSource()).getScene().getWindow();
                escenarioActual.close();
            }


        }catch (NumberFormatException e){
            Dialogos.mostrarDialogoError("Error Guardar Usuario", "Tiene que ser un numero ese campo");
        } catch (Exception e) {
            Dialogos.mostrarDialogoError("Error Guardar Usuario", e.getMessage());
        }

    }




}
