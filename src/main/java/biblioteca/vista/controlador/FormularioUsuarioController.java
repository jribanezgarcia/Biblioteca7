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

import java.io.IOException;
import java.util.List;
import javafx.event.ActionEvent;

public class FormularioUsuarioController {

    @FXML
    private Button botonCancelar;
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
                if(!this.listaUsuarios.contains(u)){
                    Dialogos.mostrarDialogoAdvertencia("Mensaje Usuario","Usuario añadido correctamente");
                    this.registro=u;
                    Stage escenarioActual = (Stage) ((Node) event.getSource()).getScene().getWindow();
                    escenarioActual.close();
                }else{
                    Dialogos.mostrarDialogoAdvertencia("Mensaje Usuario","El Usuario ya existe");
                }
            }else{
                //creamos un nuevo objeto para pasarselo al controlador
                Direccion direccion= new Direccion(via,numero,cp,localidad);
                this.registro = new Usuario(dni,nombre,email,direccion);
                Dialogos.mostrarDialogoInformacion("Mensaje Usuario","Usuario modificado correctamente");
                Stage escenarioActual = (Stage) ((Node) event.getSource()).getScene().getWindow();
                escenarioActual.close();

            }

        }catch (NumberFormatException e){
            Dialogos.mostrarDialogoError("Error Guardar Usuario", "Tiene que ser un numero ese campo");
        } catch (Exception e) {
            Dialogos.mostrarDialogoError("Error Guardar Usuario", e.getMessage());
        }

    }
    @FXML
    void onCancelar(ActionEvent event) throws IOException {
        this.registro=null;
        Stage escenarioActual = (Stage) ((Node) event.getSource()).getScene().getWindow();
        escenarioActual.close();
    }




}
