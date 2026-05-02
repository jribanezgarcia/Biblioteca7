package biblioteca.vista.controlador;

import biblioteca.modelo.dominio.Usuario;
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
    private ObservableList<Usuario> ListaUsuariosVisible;
    private Usuario registro;
    private String filtro;

    @FXML
    void cargarFormularioUsuarioController(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader=new FXMLLoader(LocalizadorRecursos.class.getResource("FormularioUsuario.fxml"));
        Parent raiz = fxmlLoader.load();
        Scene escenaAMUsuaios = new Scene(raiz);
        Stage escenarioAMUsuarios= new Stage();
        escenarioAMUsuarios.initModality(Modality.APPLICATION_MODAL);
        escenarioAMUsuarios.setTitle("Formulario Usuario");
        escenarioAMUsuarios.setScene(escenaAMUsuaios);
        escenarioAMUsuarios.setResizable(false);
        escenarioAMUsuarios.showAndWait();

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
        ListaUsuariosVisible= FXCollections.observableArrayList();
        this.tablaUsuarios.setItems(ListaUsuariosVisible);
        //tengo que hacer un try catch para manejar la excepcion puesto que inizialice no deja hacer throws
        try {
            this.listaUsuarios= Vista.getInstancia().getControlador().listadoUsuario();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        //refrescamos la tabla para cargarla al inicio.
        try {
            this.refrescarTabla();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }


    }
    @FXML
    void Buscar(KeyEvent event) throws Exception {
        this.filtro = this.campoBuscarUsuario.getText();
        this.refrescarTabla();
    }

    private void refrescarTabla() throws Exception {
        this.registro = null;
        this.tablaUsuarios.getSelectionModel().clearSelection();

        this.listaUsuarios = Vista.getInstancia().getControlador().listadoUsuario();
        this.ListaUsuariosVisible.clear();

        for(Usuario u : this.listaUsuarios) {
            if(this.filtro.isEmpty() || u.getNombre().toLowerCase().contains(this.filtro.toLowerCase())) {
                this.ListaUsuariosVisible.add(u);
            }
        }
    }
}
