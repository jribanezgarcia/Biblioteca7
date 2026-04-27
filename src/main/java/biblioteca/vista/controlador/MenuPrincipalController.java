package biblioteca.vista.controlador;

import biblioteca.vista.recursos.LocalizadorRecursos;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MenuPrincipalController  {

    @FXML
    private Button botonUsuarios;

    @FXML
    private AnchorPane panelContenido;

    @FXML
    void cargarUsuarios(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader=new FXMLLoader(LocalizadorRecursos.class.getResource("Usuarios.fxml"));
        Parent raiz = fxmlLoader.load();
        Scene escena3 = new Scene(raiz);
        Stage escenarioUsuarios= new Stage();
        escenarioUsuarios.initModality(Modality.APPLICATION_MODAL);
        escenarioUsuarios.setTitle("Menu Usuarios");
        escenarioUsuarios.setScene(escena3);
        escenarioUsuarios.showAndWait();



    }


}
