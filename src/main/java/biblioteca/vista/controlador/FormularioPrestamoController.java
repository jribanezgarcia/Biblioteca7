package biblioteca.vista.controlador;

import biblioteca.modelo.dominio.Libro;
import biblioteca.modelo.dominio.Usuario;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;

public class FormularioPrestamoController implements Initializable {
    @FXML
    private Button botonAceptarPrestamo;

    @FXML
    private Button botonCancelarPrestamo;

    @FXML
    private DatePicker datePickerPrestamo;

    @FXML
    private ComboBox<Libro> desplegableLibro;

    @FXML
    private ComboBox<Usuario> desplegableUsuario;

    @FXML
    private Label labelFechaPrestamo;

    @FXML
    private Label labelPrincipalPrestamo;

    private List<Libro> listaLibros;
    private List<Usuario> listaUsuarios;
    private Libro registroLibro;
    private Usuario registroUsuario;
    private ObservableList<Usuario> listaUsuariosObservable;
    private ObservableList<Libro> listaLibroObservable;

    public List<Libro> getListaLibros() {
        return listaLibros;
    }

    public void setListaLibros(List<Libro> listaLibros) {
        this.listaLibros = listaLibros;
        //llenamos la lista observable
        this.listaLibroObservable.addAll(listaLibros);
    }

    public List<Usuario> getListaUsuarios() {
        return listaUsuarios;
    }

    public void setListaUsuarios(List<Usuario> listaUsuarios) {
        this.listaUsuarios = listaUsuarios;
        //llenamos la lista observable
        this.listaUsuariosObservable.addAll(listaUsuarios);
    }

    public Libro getRegistroLibro() {
        return registroLibro;
    }

    public void setRegistroLibro(Libro registroLibro) {
        this.registroLibro = registroLibro;
    }

    public Usuario getRegistroUsuario() {
        return registroUsuario;
    }

    public void setRegistroUsuario(Usuario registroUsuario) {
        this.registroUsuario = registroUsuario;
    }

    @FXML
    void onCancelar(ActionEvent event) throws IOException {
        Stage escenarioActual = (Stage) ((Node) event.getSource()).getScene().getWindow();
        escenarioActual.close();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //ponemos el valor por defecto de hoy.
        datePickerPrestamo.setValue(LocalDate.now());
        //inicializamos las listas observables y se las pasamos al combobox
        listaLibroObservable= FXCollections.observableArrayList();
        desplegableLibro.setItems(listaLibroObservable);
        listaUsuariosObservable= FXCollections.observableArrayList();
        desplegableUsuario.setItems(listaUsuariosObservable);

    }
}
