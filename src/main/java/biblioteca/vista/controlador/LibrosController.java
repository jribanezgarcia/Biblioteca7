package biblioteca.vista.controlador;

import biblioteca.modelo.dominio.Audiolibro;
import biblioteca.modelo.dominio.Libro;
import biblioteca.utilidades.Dialogos;
import biblioteca.vista.Vista;
import biblioteca.vista.recursos.LocalizadorRecursos;
import javafx.beans.property.SimpleListProperty;
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
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import java.net.URL;
import java.time.Duration;
import java.util.List;
import java.util.ResourceBundle;

public class LibrosController implements Initializable {

    List<Libro> listaLibros;

    @FXML
    private Button botonBajaLibro;

    @FXML
    private Button botonModificarLibro;

    @FXML
    private TextField campoBuscarLibro;
    @FXML
    private Button botonAltaLibro;
    @FXML
    private TableColumn<Libro, String> colAnio;
    @FXML
    private TableColumn<Libro, String> colAutores;
    @FXML
    private TableColumn<Libro, String> colCategoria;
    @FXML
    private TableColumn<Libro, String> colDuracion;
    @FXML
    private TableColumn<Libro, String> colFormato;
    @FXML
    private TableColumn<Libro, String> colISBN;
    @FXML
    private TableColumn<Libro, String> colTitulo;
    @FXML
    private TableView<Libro> tablaLibros;
    private ObservableList<Libro> listaLibrosVisible;

    private Libro registro;

    private String filtro;


    @FXML
    void addLibro(ActionEvent event) throws Exception {
            FXMLLoader fxmlLoader=new FXMLLoader(LocalizadorRecursos.class.getResource("FormularioLibro.fxml"));
            Parent raiz = fxmlLoader.load();
            Scene escena = new Scene(raiz);
            //pasamos este controler a la nueva clase.
            FormularioLibroController cF= fxmlLoader.getController();
            cF.setListaLibros(this.listaLibros);
            cF.setRegistro(null);

            Stage escenarioDMLibro= new Stage();
            escenarioDMLibro.initModality(Modality.APPLICATION_MODAL);
            escenarioDMLibro.setTitle("Formulario Libro");
            escenarioDMLibro.setScene(escena);
            escenarioDMLibro.setResizable(false);
            escenarioDMLibro.showAndWait();
            Libro l = cF.getRegistro();
            if (l!=null){
                try{
                    Vista.getInstancia().getControlador().alta(l);
                }catch (Exception e){
                    Dialogos.mostrarDialogoAdvertencia("ERROR Añadir Libro", e.getMessage());
                }

            }
            this.refrescarTabla();
    }

    @FXML
    void BorrarLibro(ActionEvent event) throws Exception {
        if(this.registro==null){
            Dialogos.mostrarDialogoInformacion("Error al borrar libro","No has seleccionado ningun Libro");
        }else{
            try{
                Vista.getInstancia().getControlador().baja(this.registro);
                Dialogos.mostrarDialogoInformacion("Borrar Libro","Libro borrado correctamente");
            }catch (Exception e){
                Dialogos.mostrarDialogoAdvertencia("ERROR",e.getMessage());
            }
            this.refrescarTabla();

        }
    }
    @FXML
    void EditarLibro(ActionEvent event){
        if(this.registro==null){
            Dialogos.mostrarDialogoAdvertencia("ERROR Editar Libro","No has seleccionado ningún Libro");
        }else{
            try{
                Libro libroViejo=this.registro;
                FXMLLoader fxmlLoader = new FXMLLoader(LocalizadorRecursos.class.getResource("FormularioLibro.fxml"));
                Parent raiz = fxmlLoader.load();
                Scene escena = new Scene(raiz);
                FormularioLibroController cF= fxmlLoader.getController();
                cF.setListaLibros(this.listaLibros);
                cF.setRegistro(this.registro);
                Stage nuevoEscenario=new Stage();
                nuevoEscenario.initModality(Modality.APPLICATION_MODAL);
                nuevoEscenario.setTitle("Editar libro...");
                nuevoEscenario.setScene(escena);
                nuevoEscenario.setMaximized(false);
                nuevoEscenario.showAndWait();
                Libro libroModificado = cF.getRegistro();
                if(libroModificado!=null && !libroModificado.equals(libroViejo)){
                    try{
                        Vista.getInstancia().getControlador().baja(libroViejo);
                        Vista.getInstancia().getControlador().alta(libroModificado);
                    }catch (Exception e){
                        Dialogos.mostrarDialogoAdvertencia("ERROR Editar Libro",e.getMessage());
                    }
                }
                this.refrescarTabla();
            } catch (Exception e) {
                Dialogos.mostrarDialogoError("ERROR",e.getMessage());
            }
        }
    }
    @FXML
    void SeleccionarLibro(MouseEvent event){
        this.registro = this.tablaLibros.getSelectionModel().getSelectedItem();
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.colAnio.setCellValueFactory(new PropertyValueFactory<Libro,String>("anio"));
        //ponemos autores como cadena para que use el metodo que estaba inerte de Libros que ahora se llama getAutoresComoCadena
        this.colAutores.setCellValueFactory(new PropertyValueFactory<Libro,String>("autoresComoCadena"));
        //hacemos dos lambdas comparar si viene null ese campo y o mostrar su contenido o poner "-".
        this.colDuracion.setCellValueFactory(fila ->
                new SimpleStringProperty(fila.getValue() instanceof Audiolibro
                        ? ((Audiolibro) fila.getValue()).getDuracionComoCadena()
                        : "-")
        );
        this.colFormato.setCellValueFactory(fila -> {
            Libro l = fila.getValue();
            if (l instanceof Audiolibro) {
                return new SimpleStringProperty(((Audiolibro) l).getFormato());
            }
            return new SimpleStringProperty("-");
        });
        this.colISBN.setCellValueFactory(new PropertyValueFactory<Libro,String>("isbn"));
        this.colCategoria.setCellValueFactory(new PropertyValueFactory<Libro,String>("categoria"));
        this.colTitulo.setCellValueFactory(new PropertyValueFactory<Libro,String>("titulo"));
        this.filtro="";
        listaLibrosVisible= FXCollections.observableArrayList();
        this.tablaLibros.setItems(listaLibrosVisible);
        //envolvemos dentro de un try catch porque initialize no deja hacer throws
        try {
            this.listaLibros = Vista.getInstancia().getControlador().listadoLibros();
        } catch (Exception e) {
            Dialogos.mostrarDialogoError("ERROR al inicializar",e.getMessage());
        }
        //capturamos aqui tb la excepcion.
        try {
            this.refrescarTabla();
        } catch (Exception e) {
            Dialogos.mostrarDialogoError("ERROR al inicializar",e.getMessage());
        }
    }
    @FXML
    void BuscarLibro(KeyEvent event) throws Exception {
        this.filtro = this.campoBuscarLibro.getText();
        this.refrescarTabla();
    }
    private void refrescarTabla() throws Exception {
        this.registro = null;
        this.tablaLibros.getSelectionModel().select(null);

        this.listaLibrosVisible.clear();
        this.listaLibros = Vista.getInstancia().getControlador().listadoLibros();

        for(Libro l:this.listaLibros)
        {
            if(this.filtro.isEmpty() || l.getTitulo().toLowerCase().contains(filtro.toLowerCase()))
            {
                this.listaLibrosVisible.add(l);
            }
        }

        this.tablaLibros.refresh();
    }
}