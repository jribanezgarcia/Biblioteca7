package biblioteca.vista.controlador;

import biblioteca.vista.utilidades.Dialogos;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class FormularioDevolucionController implements Initializable {
    @FXML
    private Button botonAceptarDevolucion;

    @FXML
    private Button botonCancelarDevolucion;

    @FXML
    private DatePicker datePickerDevolucion;

    @FXML
    private Label labelFechaPrestamo;

    private LocalDate fechaDevolucion;

    public LocalDate getFechaDevolucion(){
        return fechaDevolucion;
    }
    @FXML
    void oNGuardar(ActionEvent event){
        if(datePickerDevolucion.getValue()==null){
            Dialogos.mostrarDialogoAdvertencia("ERROR", "Debes seleccionar una fecha de devolución");
            return;
        }
        this.fechaDevolucion= datePickerDevolucion.getValue();
        Stage escenarioActual = (Stage) ((Node) event.getSource()).getScene().getWindow();
        escenarioActual.close();

    }
    @FXML
    void onCancelar(ActionEvent event){

        this.fechaDevolucion = null;
        Stage escenarioActual = (Stage) ((Node) event.getSource()).getScene().getWindow();
        escenarioActual.close();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        datePickerDevolucion.setValue(LocalDate.now());
    }
}
