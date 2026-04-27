package biblioteca;

import biblioteca.vista.recursos.LocalizadorRecursos;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class AppBibliotecaFX extends Application {

    @Override
    public void start(Stage stage) {
        try {
            //añadimos el icono de la app y de la ventana.
            Image icono = new Image(LocalizadorRecursos.class.getResourceAsStream("/img/logo-ies2.png"));
            stage.getIcons().add(icono);
            FXMLLoader loader = new FXMLLoader(LocalizadorRecursos.class.getResource("Login.fxml"));
            Parent raiz = loader.load();
            Scene escena = new Scene(raiz);
            //hacemos que la ventana del login no se pueda ampliar.
            stage.setResizable(false);
            stage.setTitle("Biblioteca");
            stage.setScene(escena);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

   public static void main(String[] args) {
        launch(args);
    }
}