package biblioteca.vista.controlador;

import biblioteca.vista.recursos.LocalizadorRecursos;
import biblioteca.vista.utilidades.Dialogos;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class LoginController {

    @FXML
    private Label txtMensajeLogin;

    @FXML
    private PasswordField txtPassword;

    @FXML
    private TextField txtUsuario;

    @FXML
    void entrarLogin(ActionEvent event) {

        try{
            String password= "1234";
            String user= "admin";
            String usuarioTxt = txtUsuario.getText();
            String passwordTxt = txtPassword.getText();
            if(user.equals(usuarioTxt)&&password.equals(passwordTxt)){
                txtMensajeLogin.setText("Usuario Logueado correctamente");
                Image icono = new Image(LocalizadorRecursos.class.getResourceAsStream("/img/logo-ies2.png"));
                //creamos el enlace para pasar a la ventana del Menu
                FXMLLoader fxmlLoader=new FXMLLoader(LocalizadorRecursos.class.getResource("MenuPrincipal.fxml"));
                Parent raiz = fxmlLoader.load();
                Scene escena2 = new Scene(raiz);
                //no se usa porque no se le pasa nada a la siguiente pantalla.
                //MenuPrincipalController cF = fxmlLoader.getController();
                Stage nuevoEscenario= new Stage();
                nuevoEscenario.getIcons().add(icono);
                nuevoEscenario.initModality(Modality.APPLICATION_MODAL);
                nuevoEscenario.setTitle("Menu Principal");
                nuevoEscenario.setScene(escena2);
                //ponemos la pantalla principal maximizada
                nuevoEscenario.setMaximized(true);
                //ponemos un mensaje de confirmacion si se cierra la ventana principal.
                nuevoEscenario.setOnCloseRequest(e->confirmarSalida(nuevoEscenario, e));
                nuevoEscenario.show();
                //cerramos la ventana actual despues de hacer login correctamente.
                Stage escenarioActual = (Stage) ((Node) event.getSource()).getScene().getWindow();
                escenarioActual.close();

            }else if (usuarioTxt.isBlank()){
                txtMensajeLogin.setText("Usuario no puede estar en blanco");
            }else if (passwordTxt.isBlank()){
                txtMensajeLogin.setText("Password no puede estar en blanco");
            }else{
                txtMensajeLogin.setText("ERROR al hacer login");
            }
        } catch (Exception e) {
            throw new RuntimeException("ERROR entrarLogin "+e);
        }

    }
    //metodo que lanza una ventana para confirmar la salida
    private void confirmarSalida(Stage escenario, WindowEvent e) {
        if (Dialogos.mostrarDialogoConfirmacion("Salir", "¿Estás seguro de que quieres salir de la aplicación?", escenario)) {
            escenario.close();
        }
        else {
            e.consume();
        }
    }

}
