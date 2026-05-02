package biblioteca.vista.controlador;

import biblioteca.vista.recursos.LocalizadorRecursos;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class LibrosController {

    @FXML
    private Button botonBajaLibro;

    @FXML
    private Button botonModificarLibro;

    @FXML
    private TextField campoBuscarLibro;

    @FXML
    private TableColumn<?, ?> colAnio;

    @FXML
    private TableColumn<?, ?> colAutores;

    @FXML
    private TableColumn<?, ?> colCategoria;

    @FXML
    private TableColumn<?, ?> colISBN;

    @FXML
    private TableColumn<?, ?> colTitulo;
    @FXML
    void cargarFormularioLibroController(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader=new FXMLLoader(LocalizadorRecursos.class.getResource("FormularioLibro.fxml"));
        Parent raiz = fxmlLoader.load();
        Scene escena = new Scene(raiz);
        Stage escenarioDMLibro= new Stage();
        escenarioDMLibro.initModality(Modality.APPLICATION_MODAL);
        escenarioDMLibro.setTitle("Alta Libro");
        escenarioDMLibro.setScene(escena);
        escenarioDMLibro.setResizable(false);
        escenarioDMLibro.showAndWait();

    }

}