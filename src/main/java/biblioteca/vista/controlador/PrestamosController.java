package biblioteca.vista.controlador;

import biblioteca.modelo.dominio.Prestamo;
import biblioteca.vista.Vista;
import biblioteca.vista.recursos.LocalizadorRecursos;
import biblioteca.vista.utilidades.Dialogos;
import javafx.beans.property.SimpleStringProperty;
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
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.awt.event.KeyEvent;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.ResourceBundle;

public class PrestamosController implements Initializable {

    DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    @FXML
    private Button botonDevolver;

    @FXML
    private Button botonPrestar;

    @FXML
    private TableColumn<Prestamo, String> colEstado;

    @FXML
    private TableColumn<Prestamo, String> colFechaDevolucion;

    @FXML
    private TableColumn<Prestamo, String> colFechaInicio;

    @FXML
    private TableColumn<Prestamo, String> colILibro;

    @FXML
    private TableColumn<Prestamo, String> colUsuario;
    @FXML
    private TableView<Prestamo> tablePrestamos;
    @FXML
    private TextField txtBuscarPrestamo;
    List<Prestamo> listaPrestamos;
    private ObservableList<Prestamo> listaPrestamosVisible;
    private Prestamo registro;
    private String filtro;



    @FXML
    void cargarFormularioPrestamoController(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader=new FXMLLoader(LocalizadorRecursos.class.getResource("FormularioPrestamo.fxml"));
        Parent raiz = fxmlLoader.load();
        Scene escenaAMUsuaios = new Scene(raiz);
        Stage escenarioAMUsuarios= new Stage();
        escenarioAMUsuarios.initModality(Modality.APPLICATION_MODAL);
        escenarioAMUsuarios.setTitle("Realizar Prestamo");
        escenarioAMUsuarios.setScene(escenaAMUsuaios);
        escenarioAMUsuarios.setResizable(false);
        escenarioAMUsuarios.showAndWait();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.colUsuario.setCellValueFactory(fila->new SimpleStringProperty(fila.getValue().getUsuario().getNombre()));
        this.colILibro.setCellValueFactory(fila->new SimpleStringProperty(fila.getValue().getLibro().getTitulo()));
        //Usamos expresiones lambda para sacar la fecha y cambiamos el formato a formato español.
        this.colFechaInicio.setCellValueFactory(
                fila->new SimpleStringProperty(fila.getValue().getfInicio().format(formato).toString()));
        //hacemos otra lambda con un condional para que si no es null transforme al formato español y sino que ponga vacio.
        this.colFechaDevolucion.setCellValueFactory(fila -> {
            LocalDate fd = fila.getValue().getfDevolucion();
            if(fd!=null){
                return new SimpleStringProperty(fd.format(formato).toString());
            }else{
                return new SimpleStringProperty("");
            }
        });
        this.colEstado.setCellValueFactory(fila-> {
            if(fila.getValue().isDevuelto()){
                return new SimpleStringProperty("Devuelto");
            }else{
                return new SimpleStringProperty("No Devuelto");
            }
                });
        this.filtro="";
        listaPrestamosVisible= FXCollections.observableArrayList();
        this.tablePrestamos.setItems(listaPrestamosVisible);
        try{
            this.listaPrestamos= Vista.getInstancia().getControlador().listadoPrestamos();
        } catch (Exception e) {
            Dialogos.mostrarDialogoError("Error al inizializar Prestamos",e.getMessage());
        }
        try {
            this.refrescarTabla();
        } catch (Exception e) {
            biblioteca.utilidades.Dialogos.mostrarDialogoError("ERROR al inicializar",e.getMessage());
        }

    }
    @FXML
    void BuscarPrestamo(KeyEvent event) throws Exception {
        this.filtro= this.txtBuscarPrestamo.getText();
        this.refrescarTabla();

    }

    private void refrescarTabla() throws Exception {
        this.registro = null;
        this.tablePrestamos.getSelectionModel().clearSelection();

        this.listaPrestamos = Vista.getInstancia().getControlador().listadoPrestamos();
        this.listaPrestamosVisible.clear();//borramos lo que se ve

        //revisar esta parte, puedo poner gettitulo?
        for(Prestamo p : this.listaPrestamos) {
            if(this.filtro.isEmpty() || p.getLibro().getTitulo().toLowerCase().contains(this.filtro.toLowerCase())) {
                this.listaPrestamosVisible.add(p);
            }
        }
    }
}
