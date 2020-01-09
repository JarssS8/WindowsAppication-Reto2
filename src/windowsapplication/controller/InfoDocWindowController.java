/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package windowsapplication.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import windowsapplication.beans.Rating;
import windowsapplication.service.DocumentClientREST;
import windowsapplication.service.RatingClientREST;

/**
 *
 * @author Gaizka Andres
 */
public class InfoDocWindowController {
    @FXML
    private TextField txtRating;
    @FXML
    private TextField txtComent;
    @FXML
    private Label lbNameDocument;
        
    @FXML
    private Label lbAuthorDocument;
        
    @FXML
    private Label lbAvgDocmuent;
        
    @FXML
    private Button btClose;
    
    private Stage stage;
    
    private DocumentClientREST docREST;
   
    void initStage(Parent root) {
        Scene scene = new Scene(root);
        
        stage = new Stage();
        stage.setScene(scene);
        stage.setTitle("Info of the document");
        stage.setResizable(false);
        stage.setOnShowing(this::handleWindowShowing);
        stage.setOnCloseRequest(this::closeRequest);
        btClose.setOnAction(this::backButtonRequest);
        
        stage.show();
        
    }
    private void handleWindowShowing(WindowEvent event){
        //Insertar nombre del documento
        lbNameDocument.setText(" ");
        //Insertar nombre del autor
        lbAuthorDocument.setText(" ");
        //Insertar nota media de rating
        lbAvgDocmuent.setText(" ");
        /*
        Cargar ratings sobre el documento en la tabla
        */
        docREST.findRatingsOfDocument(Rating.class, "1");
   }
    private void closeRequest(WindowEvent event){  
        stage.close();
    }
    private void backButtonRequest(ActionEvent event){
        stage.close();
    }
     
}
