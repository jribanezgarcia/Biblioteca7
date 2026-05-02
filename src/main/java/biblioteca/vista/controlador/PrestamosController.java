package biblioteca.vista.controlador;

import biblioteca.vista.recursos.LocalizadorRecursos;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class PrestamosController {

    @FXML
    private Button botonDevolver;

    @FXML
    private Button botonPrestar;

    @FXML
    private TableColumn<?, ?> colEstado;

    @FXML
    private TableColumn<?, ?> colFechaDevolucion;

    @FXML
    private TableColumn<?, ?> colFechaInicio;

    @FXML
    private TableColumn<?, ?> colILibro;

    @FXML
    private TableColumn<?, ?> colUsuario;
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

}
