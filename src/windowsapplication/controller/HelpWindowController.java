/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package windowsapplication.controller;

import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

/**
 * Controller of the Help window
 * @author Gaizka Andres
 */
public class HelpWindowController {
    @FXML
    private WebView webView;
    
    private Stage stage;
    
    void setStage(Stage stage) {
        this.stage = stage;
    }
    void initStage(Parent root) {
        Scene scene = new Scene(root);
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setScene(scene);
        stage.setTitle("Help Page");
        stage.setResizable(false);
        stage.setMinWidth(800);
        stage.setMinHeight(600);
        stage.setOnShowing(this::handleWindowShowing);
        stage.show();
    }
    /**
     * Charge a html with the help text
     * @param event 
     */
     private void handleWindowShowing(WindowEvent event){
        WebEngine webEngine = webView.getEngine();
        webEngine.load(getClass()
                .getResource("/windowsapplication/view/Help.html").toExternalForm());
    }

    
    
    
}
