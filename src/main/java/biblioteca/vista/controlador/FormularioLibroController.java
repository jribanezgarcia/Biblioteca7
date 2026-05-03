package biblioteca.vista.controlador;

import biblioteca.modelo.dominio.Audiolibro;
import biblioteca.modelo.dominio.Autor;
import biblioteca.modelo.dominio.Categoria;
import biblioteca.modelo.dominio.Libro;
import biblioteca.vista.recursos.LocalizadorRecursos;
import biblioteca.vista.utilidades.Dialogos;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class FormularioLibroController implements Initializable {

    @FXML
    private Button addAutor;

    @FXML
    private Button botonAceptarLibro;

    @FXML
    private CheckBox checkBoxAudioLibro;

    @FXML
    private ComboBox<Categoria> comboBoxCategoria;

    @FXML
    private Label labelLibro;

    @FXML
    private TextField txtAnio;

    @FXML
    private TextField txtDuracion;

    @FXML
    private TextField txtFormato;

    @FXML
    private TextField txtIsbn;

    @FXML
    private TextField txtTitulo;
    @FXML
    private ListView<Autor> listAutoresAniadidos;
    //probamos si asi no hae nullpointerexception
    private List<Libro> listaLibros =new ArrayList<>();
    private List<Autor> listaAutores = new ArrayList<>();
    private Libro registro;
    private ObservableList<Autor> listaAutoresObservable;
    //revisar nombre
    public void setListaLibros(List<Libro> listaLibros){
        this.listaLibros= listaLibros;
    }
    public void setRegistro(Libro l){
        this.registro = l;
        if (this.registro!=null){
            labelLibro.setText("Modificar Libro");
            this.txtIsbn.setText(this.registro.getIsbn());
            this.txtTitulo.setText(this.registro.getTitulo());
            //parseamos a int
            this.txtAnio.setText(Integer.toString(this.registro.getAnio()));
            this.comboBoxCategoria.getSelectionModel().select(this.registro.getCategoria());
            //cargamos los autores del libro en la lista observable para mostrarlos en el ListView
            this.listaAutores=  this.registro.getAutores();
            this.listaAutoresObservable.setAll(this.listaAutores);
            //comprobamos que es AudioLibro
            if(this.registro instanceof Audiolibro){
                //si es audiolibro el checkbox tiene que estar marcado
                this.checkBoxAudioLibro.setSelected(true);
                this.txtDuracion.setDisable(false);   // <-- Necesario
                this.txtFormato.setDisable(false);
                this.txtDuracion.setText(String.valueOf(((Audiolibro) this.registro).getDuracion().toSeconds()));
                this.txtFormato.setText(((Audiolibro) this.registro).getFormato());
            }else{
                //desmarcamos el checkbox y ocultamos los campos
                this.checkBoxAudioLibro.setSelected(false);
                this.txtDuracion.setDisable(true);
                this.txtFormato.setDisable(true);
            }
        }else{
            labelLibro.setText("Añadir Libro");
        }
    }
    public Libro getRegistro(){
        return this.registro;
    }


    @FXML
    void cargarFormularioAutoresController(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader=new FXMLLoader(LocalizadorRecursos.class.getResource("FormularioAutores.fxml"));
        Parent raiz = fxmlLoader.load();
        Scene escena = new Scene(raiz);
        //añadimos la referencia del controller de autores
        FormularioAutoresController cF= fxmlLoader.getController();
        cF.setAutores(this.listaAutores);
        Stage escenarioDMLibro= new Stage();
        escenarioDMLibro.initModality(Modality.APPLICATION_MODAL);
        escenarioDMLibro.setTitle("Añadir Autores");
        escenarioDMLibro.setScene(escena);
        escenarioDMLibro.setResizable(false);
        escenarioDMLibro.showAndWait();
        //cogemos la lista creada pasandola por el controlador de autores
        this.listaAutores = cF.getAutores();
        //añadimos la lista nueva a la observable list
        listaAutoresObservable.setAll(this.listaAutores);

    }

    //mostramos los campos duracion y formato si se marca el checkbox.
    @FXML
    void esAudioLibro(ActionEvent event){
        boolean marcado= this.checkBoxAudioLibro.isSelected();
        //siempre que no este marcado que no se muestren los campos
        this.txtDuracion.setDisable(!marcado);
        this.txtFormato.setDisable(!marcado);

    }
    @FXML
    void GuardarLibro(ActionEvent event){

        try {
            //ponemos esta validacion para que no nos deje añadir un libro sin autores.
            if(listaAutores.isEmpty()){
                Dialogos.mostrarDialogoAdvertencia("Error Autores","Debes añadir por lo menos un autor");
                return;
            }
            Categoria categoria = this.comboBoxCategoria.getValue();
            String isbn = txtIsbn.getText();
            String titulo = txtTitulo.getText();
            int anio = Integer.parseInt(txtAnio.getText());
            Libro l = null;
           if(this.registro==null){
               //si el registro es null hacemos el alta
               if(checkBoxAudioLibro.isSelected()){
                    Duration duracion = Duration.ofSeconds(Long.parseLong(txtDuracion.getText()));
                    String formato = txtFormato.getText();
                    l = new Audiolibro(isbn, titulo, anio, categoria, duracion, formato);
                    for(Autor a : listaAutores){
                        l.addAutor(a);
                    }
                }else{
                   l= new Libro(isbn,titulo,anio,categoria);
                   for(Autor a : listaAutores){
                       l.addAutor(a);
                   }

                }
               //comprobamos que el libro no este ya en la lista, si es asi lo ponemos a null.
               if(!this.listaLibros.contains(l)){
                   biblioteca.utilidades.Dialogos.mostrarDialogoAdvertencia("Mensaje Libros","Libro añadido correctamente");
                   this.registro=l;
                   Stage escenario = (Stage) ((Node) event.getSource()).getScene().getWindow();
                   escenario.close();
               }else{
                   Dialogos.mostrarDialogoAdvertencia("Añadir Libro","El libro ya existe");
               }
               //si el registro no es null, modificamos.
            }else{
               if(checkBoxAudioLibro.isSelected()){
                   Duration duracion = Duration.ofSeconds(Long.parseLong(txtDuracion.getText()));
                   String formato = txtFormato.getText();
                   this.registro = new Audiolibro(isbn, titulo, anio, categoria, duracion, formato);
               }else{
                   this.registro = new Libro(isbn, titulo, anio, categoria);
               }
               for(Autor autor : listaAutores) this.registro.addAutor(autor);
               Dialogos.mostrarDialogoInformacion("Modificar Libro","Libro modificado correctamente");
               Stage escenario = (Stage) ((Node) event.getSource()).getScene().getWindow();
               escenario.close();

           }

        } catch (NumberFormatException e) {
            Dialogos.mostrarDialogoError("Error al Guardar Libro","Año y Duracion tienen que ser numeros");

        } catch (Exception e) {
            Dialogos.mostrarDialogoError("Error Guardar Libro", e.getMessage());
        }

    }
    @FXML
    void onCancelar(ActionEvent event) throws IOException {
       this.registro =null;
        Stage escenarioActual = (Stage) ((Node) event.getSource()).getScene().getWindow();
        escenarioActual.close();
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        comboBoxCategoria.setItems(FXCollections.observableArrayList(Categoria.values()));
        this.txtDuracion.setDisable(true);
        this.txtFormato.setDisable(true);
        //creamos una lista vacia
        listaAutoresObservable = FXCollections.observableArrayList();
        //se la añadimos al listview que de momento mostrara una lista vacia.
        listAutoresAniadidos.setItems(listaAutoresObservable);


    }


}
