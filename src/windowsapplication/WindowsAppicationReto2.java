  
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package windowsapplication;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.stage.Stage;
import windowsapplication.controller.MainWindowController;

/**
 *
 * @author Gaizka Andres
 */
public class WindowsAppicationReto2 extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().
            getResource("/windowsapplication/view/Main.fxml"));
        Parent root = (Parent) loader.load();
        MainWindowController mainWindowController = loader.getController();
        mainWindowController.setStage(stage);
        mainWindowController.initStage(root);
        
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}