/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package windowsapplication;

import java.util.logging.Logger;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.stage.Stage;
import windowsapplication.controller.LoginWindowController;

/**
 *
 * @author Gaizka Andres
 */
public class WindowsAppicationReto2 extends Application {

    private static final Logger LOGGER = Logger.getLogger(
            "windowsapplication.controller.WindowsAppicationReto2");
    
    

    @Override
    public void start(Stage stage) throws Exception {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().
                    getResource("/windowsapplication/view/LogIn_Window.fxml"));
            Parent root = (Parent) loader.load();
            LoginWindowController controller = loader.getController();
            controller.setStage(stage);
            controller.initStage(root);
        } catch (Exception ex) {
            LOGGER.warning("WindowsApplicationReto2: An error ocurred while "
                    + "loading the window... " + ex.getMessage());
        }

    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

}
