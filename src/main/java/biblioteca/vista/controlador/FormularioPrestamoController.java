package biblioteca.vista.controlador;

import biblioteca.modelo.dominio.Libro;
import biblioteca.modelo.dominio.Prestamo;
import biblioteca.modelo.dominio.Usuario;
import biblioteca.utilidades.Dialogos;
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

import javax.swing.*;
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
    private ObservableList<Usuario> listaUsuariosObservable;
    private ObservableList<Libro> listaLibroObservable;
    private Prestamo registro;

    public Prestamo getRegistro() {
        return registro;
    }

    public void setRegistro(Prestamo registro) {
        this.registro = registro;
    }

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

    @FXML
    void onGuardar(ActionEvent event){

        try{
            if(desplegableUsuario.getValue()==null){
                Dialogos.mostrarDialogoAdvertencia("ERRROR!","El Usuario no puede estar vacio");
                return;
            }
            if(desplegableLibro.getValue()==null){
                Dialogos.mostrarDialogoAdvertencia("ERRROR!","El Libro no puede estar vacio");
                return;
            }
            if(datePickerPrestamo.getValue()==null){
                Dialogos.mostrarDialogoAdvertencia("ERRROR!","La fecha no puede estar vacia");
                return;
            }
            Usuario u= desplegableUsuario.getValue();
            Libro l=desplegableLibro.getValue();
            LocalDate d=datePickerPrestamo.getValue();
            this.registro=new Prestamo(l,u,d);
            Stage escenarioActual = (Stage) ((Node) event.getSource()).getScene().getWindow();
            escenarioActual.close();



        } catch (Exception e) {
            Dialogos.mostrarDialogoError("ERROR al Prestar",e.getMessage());
        }

    }
}