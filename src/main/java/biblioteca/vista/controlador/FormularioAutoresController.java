package biblioteca.vista.controlador;

import biblioteca.modelo.dominio.Autor;
import biblioteca.utilidades.Dialogos;
import biblioteca.vista.Vista;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import javax.swing.*;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class FormularioAutoresController implements Initializable {

    @FXML
    private Button botonAceptarAutor;

    @FXML
    private Button botonAddAutor;

    @FXML
    private Button botonCancelarAutor;

    @FXML
    private Button botonDelAutor;

    @FXML
    private TextField txtApellidosAutor;

    @FXML
    private TextField txtNacionalidadAutor;

    @FXML
    private TextField txtNombreAutor;
    @FXML
    private ListView<Autor> listViewAutores;

    private ObservableList<Autor> listaAutorObservable;

    private List<Autor> autores= new ArrayList<>();

    private Autor registro;


    public List<Autor> getAutores() {
        return autores;
    }

    //cargamos los autores previos al abrir el formulario, eso ser hara en FormularioLibroController
    public void setAutores(List<Autor> autores) {
        this.autores = autores;
        this.listaAutorObservable.addAll(autores);
    }


    //metodo para seleccionar desde la ListView
    @FXML
    void SeleccionarAutor(MouseEvent event){
        this.registro = this.listViewAutores.getSelectionModel().getSelectedItem();
    }
    @FXML
    void BorrarAutor(ActionEvent event) throws Exception {

        if(this.registro==null){
            Dialogos.mostrarDialogoInformacion("Error","No has seleccionado ningún Autor");
        }else{
            try{
                autores.remove(this.registro);
                listaAutorObservable.remove(this.registro);
                Dialogos.mostrarDialogoInformacion("Borrar Autor","Autor borrado correctamente");
            }catch(Exception e){
                Dialogos.mostrarDialogoAdvertencia("ERROR",e.getMessage());
            }
        }
    }
    @FXML
    void onCancelar(ActionEvent event) throws IOException {
        //borramos autores al cancelar.
        autores.clear();
        Stage escenarioActual = (Stage) ((Node) event.getSource()).getScene().getWindow();
        escenarioActual.close();
    }
    @FXML
    void onAceptar(ActionEvent event){
        Stage escenario = (Stage) ((Node) event.getSource()).getScene().getWindow();
        escenario.close();
    }

    @FXML
    void onAddAutor(ActionEvent event){
        //Leemos los campos, creamos el autor y lo añadimos a la lista.
        try{
            String nombre=txtNombreAutor.getText();
            String apellidos=txtApellidosAutor.getText();
            String nacionalidad=txtNacionalidadAutor.getText();
            Autor autor = new Autor(nombre,apellidos,nacionalidad);
            //añadimos el autor creado a las dos listas
            autores.add(autor);
            listaAutorObservable.add(autor);
            //limpiamos los campos
            txtNombreAutor.clear();
            txtApellidosAutor.clear();
            txtNacionalidadAutor.clear();

        } catch (Exception e) {
            Dialogos.mostrarDialogoError("ERROR añadir autor",e.getMessage());
        }

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //enlazamos la ObservableList con la listViewAutores
        listaAutorObservable = FXCollections.observableArrayList();
        listViewAutores.setItems(listaAutorObservable);

    }
}

