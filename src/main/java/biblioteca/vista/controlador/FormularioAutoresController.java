package biblioteca.vista.controlador;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import java.io.IOException;

public class FormularioAutoresController {

    @FXML
    private Button botonCancelarAutores;

    @FXML
    void onCancelar(ActionEvent event) throws IOException {
        Stage escenarioActual = (Stage) ((Node) event.getSource()).getScene().getWindow();
        escenarioActual.close();
    }

}

