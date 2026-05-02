package biblioteca.vista.controlador;

import biblioteca.vista.recursos.LocalizadorRecursos;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class FormularioLibroController {

    @FXML
    private Button addAutor;
    @FXML
    void cargarFormularioAutoresController(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader=new FXMLLoader(LocalizadorRecursos.class.getResource("FormularioAutores.fxml"));
        Parent raiz = fxmlLoader.load();
        Scene escena = new Scene(raiz);
        Stage escenarioDMLibro= new Stage();
        escenarioDMLibro.initModality(Modality.APPLICATION_MODAL);
        escenarioDMLibro.setTitle("Añadir Autores");
        escenarioDMLibro.setScene(escena);
        escenarioDMLibro.setResizable(false);
        escenarioDMLibro.showAndWait();

    }

}
