package biblioteca.vista.controlador;

import biblioteca.modelo.dominio.Usuario;
import biblioteca.utilidades.Dialogos;
import biblioteca.vista.Vista;
import biblioteca.vista.recursos.LocalizadorRecursos;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class UsuariosController implements Initializable {

    @FXML
    private Button botonAltaUsuario;

    @FXML
    private Button botonBajaUsuario;

    @FXML
    private Button botonModificarUsuario;

    @FXML
    private TextField campoBuscarUsuario;

    @FXML
    private TableColumn<Usuario, String> colDni;

    @FXML
    private TableColumn<Usuario, String> colCp;

    @FXML
    private TableColumn<Usuario, String> colEmail;

    @FXML
    private TableColumn<Usuario, String> colLocalidad;

    @FXML
    private TableColumn<Usuario, String> colNombre;

    @FXML
    private TableColumn<Usuario, String> colNumero;

    @FXML
    private TableColumn<Usuario, String> colVia;

    @FXML
    private TableView<Usuario> tablaUsuarios;

    List<Usuario> listaUsuarios;
    private ObservableList<Usuario> listaUsuariosVisible;
    private Usuario registro;
    private String filtro;


    @FXML
    void addUsuario(ActionEvent event) throws Exception {
        FXMLLoader fxmlLoader=new FXMLLoader(LocalizadorRecursos.class.getResource("FormularioUsuario.fxml"));
        Parent raiz = fxmlLoader.load();
        Scene escenaAMUsuaios = new Scene(raiz);
        //pasamos el controlador a la nueva ventana
        FormularioUsuarioController cF= fxmlLoader.getController();
        cF.setListaUsuarios(this.listaUsuarios);
        cF.setRegistro(null);
        Stage escenarioAMUsuarios= new Stage();
        escenarioAMUsuarios.initModality(Modality.APPLICATION_MODAL);
        escenarioAMUsuarios.setTitle("Formulario Usuario");
        escenarioAMUsuarios.setScene(escenaAMUsuaios);
        escenarioAMUsuarios.setResizable(false);
        escenarioAMUsuarios.showAndWait();
        //refrescamos la tabla despues de añadir un usuario
        //Añadimos el usuario a la base de datos.
        Usuario u = cF.getRegistro();
        if(u!=null){
            try{
                Vista.getInstancia().getControlador().alta(u);
            } catch (Exception e) {
                Dialogos.mostrarDialogoAdvertencia("ERROR", e.getMessage());
            }
        }

        this.refrescarTabla();

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        this.colNombre.setCellValueFactory(new PropertyValueFactory<Usuario,String>("nombre"));
        this.colDni.setCellValueFactory(new PropertyValueFactory<Usuario,String>("dni"));
        this.colEmail.setCellValueFactory(new PropertyValueFactory<Usuario,String>("email"));
        this.colVia.setCellValueFactory(new PropertyValueFactory<Usuario,String>("via"));
        this.colNumero.setCellValueFactory(new PropertyValueFactory<Usuario,String>("numero"));
        this.colCp.setCellValueFactory(new PropertyValueFactory<Usuario,String>("cp"));
        this.colLocalidad.setCellValueFactory(new PropertyValueFactory<Usuario,String>("localidad"));
        this.filtro ="";
        //creamos la tabla visible
        listaUsuariosVisible= FXCollections.observableArrayList();
        //se la pasamos a la tabla
        this.tablaUsuarios.setItems(listaUsuariosVisible);
        //tengo que hacer un try catch para manejar la excepcion puesto que inizialice no deja hacer throws
        //cargamos la lista de la BD
        try {
            this.listaUsuarios= Vista.getInstancia().getControlador().listadoUsuario();
        } catch (Exception e) {
            Dialogos.mostrarDialogoError("ERROR al inicializar",e.getMessage());
        }
        //refrescamos la tabla para cargarla pasando los datos de listaUsuarios a ListaUsuariosVisible
        try {
            this.refrescarTabla();
        } catch (Exception e) {
            Dialogos.mostrarDialogoError("ERROR al inicializar",e.getMessage());
        }


    }
    @FXML
    void BuscarUsuario(KeyEvent event) throws Exception {
        this.filtro = this.campoBuscarUsuario.getText();
        this.refrescarTabla();
    }
    @FXML
    void BorrarUsuario(ActionEvent event) throws Exception {

        if(this.registro==null){
            Dialogos.mostrarDialogoInformacion("Error","No has seleccionado ningun Usuario");
        }else{
            try{
                Vista.getInstancia().getControlador().baja(this.registro);
                Dialogos.mostrarDialogoInformacion("Borrar Usuario","Usuario borrado correctamente");
            }catch(Exception e){
                Dialogos.mostrarDialogoAdvertencia("ERROR",e.getMessage());
            }
            this.refrescarTabla();
        }
    }
    @FXML
    void EditarUsuario(ActionEvent event){

        if(this.registro==null){
            Dialogos.mostrarDialogoAdvertencia("ERROR Editar Usuario","No has seleccionado ningun Usuario");
        }else{
            try{
                //guardamos el usuario viejo antes de modificar el registro.
                Usuario usuarioViejo= this.registro;
                FXMLLoader fxmlLoader = new FXMLLoader(LocalizadorRecursos.class.getResource("FormularioUsuario.fxml"));
                Parent raiz = fxmlLoader.load();
                Scene escena = new Scene(raiz);

                FormularioUsuarioController cF= fxmlLoader.getController();
                //pasamos la list de usuarios al formulario
                cF.setListaUsuarios(this.listaUsuarios);
                //pasamos el usuario seleccionado al formulario
                cF.setRegistro(this.registro);

                Stage nuevoEscenario=new Stage();
                nuevoEscenario.initModality(Modality.APPLICATION_MODAL);
                nuevoEscenario.setTitle("Editar persona...");
                nuevoEscenario.setScene(escena);
                nuevoEscenario.showAndWait();

                //Guardamos el usuario modificado.
                Usuario usuarioModificado = cF.getRegistro();
                //try catch con las llamadas a la base de datos.
                if(usuarioModificado!=null && !usuarioModificado.equals(usuarioViejo)){
                    try{
                        Vista.getInstancia().getControlador().baja(usuarioViejo);
                        Vista.getInstancia().getControlador().alta(usuarioModificado);
                    } catch (Exception e) {
                        Dialogos.mostrarDialogoAdvertencia("ERROR",e.getMessage());
                    }
                }

                this.refrescarTabla();

            } catch (Exception e) {
                Dialogos.mostrarDialogoError("ERROR",e.getMessage());
            }
        }
    }
    //metodo para seleccionar desde la tabla al pinchar
    @FXML
    void SeleccionarUsuario(MouseEvent event){
        this.registro = this.tablaUsuarios.getSelectionModel().getSelectedItem();
    }

    //metodo para pasar el array que viene de la BD a la ObservableTable
    private void refrescarTabla() throws Exception {
        this.registro = null;
        this.tablaUsuarios.getSelectionModel().clearSelection();

        this.listaUsuarios = Vista.getInstancia().getControlador().listadoUsuario();
        this.listaUsuariosVisible.clear();//borramos lo que se ve

        for(Usuario u : this.listaUsuarios) {
            if(this.filtro.isEmpty() || u.getNombre().toLowerCase().contains(this.filtro.toLowerCase())) {
                this.listaUsuariosVisible.add(u);
            }
        }
    }
}
