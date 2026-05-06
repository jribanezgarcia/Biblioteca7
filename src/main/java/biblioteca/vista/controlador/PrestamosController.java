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
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.ResourceBundle;
import javafx.scene.input.KeyEvent;

public class PrestamosController implements Initializable {

    private static final DateTimeFormatter FORMATO_FECHA = DateTimeFormatter.ofPattern("dd/MM/yyyy");
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
    void DevolverLibro(ActionEvent event) throws Exception {
        if(this.registro==null){
           Dialogos.mostrarDialogoInformacion("Error al devolver libro","No has seleccionado ningún Préstamo");
        }else{
            try{
                FXMLLoader fxmlLoader= new FXMLLoader(LocalizadorRecursos.class.getResource("FormularioDevolucion.fxml"));
                Parent raiz= fxmlLoader.load();
                Scene escena= new Scene(raiz);
                FormularioDevolucionController cF=fxmlLoader.getController();
                Stage escenario = new Stage();
                escenario.initModality(Modality.APPLICATION_MODAL);
                escenario.setTitle("Devolver Libro");
                escenario.setScene(escena);
                escenario.setResizable(false);
                escenario.showAndWait();
                LocalDate fDevolucion= cF.getFechaDevolucion();
                //si la fecha es distinta a null es que se puede prestar el libro
                if (fDevolucion!=null){
                    if(fDevolucion.isBefore(this.registro.getfInicio())){
                        Dialogos.mostrarDialogoAdvertencia("Error de fecha",
                                "La fecha de devolución no puede ser anterior a la fecha del préstamo");
                    }else{
                        try{
                            Vista.getInstancia().getControlador().devolver(this.registro.getLibro(),this.registro.getUsuario(),fDevolucion);
                            Dialogos.mostrarDialogoInformacion("Devolver Libro","Libro devuelto correctamente");
                        } catch (Exception e) {
                            Dialogos.mostrarDialogoError("ERROR al devolver el préstamo ",e.getMessage());
                        }

                    }

                }

            } catch (Exception e) {
                Dialogos.mostrarDialogoAdvertencia("ERROR",e.getMessage());
            }
            this.refrescarTabla();
        }
    }
    @FXML
    void PrestarLibro(ActionEvent event) {
        try {
            FXMLLoader fxmlLoader=new FXMLLoader(LocalizadorRecursos.class.getResource("FormularioPrestamo.fxml"));
            Parent raiz = fxmlLoader.load();
            Scene escena = new Scene(raiz);
            FormularioPrestamoController cF= fxmlLoader.getController();
            cF.setListaLibros(Vista.getInstancia().getControlador().listadoLibros());
            cF.setListaUsuarios(Vista.getInstancia().getControlador().listadoUsuario());
            Stage escenario= new Stage();
            escenario.initModality(Modality.APPLICATION_MODAL);
            escenario.setTitle("Realizar Prestamo");
            escenario.setScene(escena);
            escenario.setResizable(false);
            escenario.showAndWait();
            Prestamo p = cF.getRegistro();
            if(p != null){
                Vista.getInstancia().getControlador().prestar(p.getLibro(), p.getUsuario(), p.getfInicio());
            }
            this.refrescarTabla();
        } catch (Exception e) {
            Dialogos.mostrarDialogoError("ERROR Prestar Libro", e.getMessage());
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.colUsuario.setCellValueFactory(fila->new SimpleStringProperty(fila.getValue().getUsuario().getNombre()));
        this.colILibro.setCellValueFactory(fila->new SimpleStringProperty(fila.getValue().getLibro().getTitulo()));
        //Usamos expresiones lambda para sacar la fecha y cambiamos el formato a formato español.
        this.colFechaInicio.setCellValueFactory(
                fila->new SimpleStringProperty(fila.getValue().getfInicio().format(FORMATO_FECHA).toString()));
        //hacemos otra lambda con un condicional para que si no es null transforme al formato español y sino que ponga vacio.
        this.colFechaDevolucion.setCellValueFactory(fila -> {
            LocalDate fd = fila.getValue().getfDevolucion();
            if(fd!=null){
                return new SimpleStringProperty(fd.format(FORMATO_FECHA).toString());
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
            Dialogos.mostrarDialogoError("ERROR al inicializar",e.getMessage());
        }

    }
    @FXML
    void BuscarPrestamo(KeyEvent event) throws Exception {
        this.filtro= this.txtBuscarPrestamo.getText();
        this.refrescarTabla();
    }
    @FXML
    void SeleccionarPrestamo(MouseEvent event){
        this.registro=this.tablePrestamos.getSelectionModel().getSelectedItem();
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
