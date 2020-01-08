/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package windowsapplication.controller;

import java.io.File;
import java.io.IOException;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

/**
 *
 * @author Gaizka Andres
 */
public class UploadDocWindowController {
    @FXML
    private ComboBox comboCategories;
    @FXML
    private Label lbFile;
    @FXML
    private TextField txtNameDoc;
    @FXML
    private Button btSelectFile;
    @FXML
    private Button btUpload;
    @FXML
    private Button btBack;
    
    
    
    
    private Stage stage;
    
    void setStage(Stage stage) {
        this.stage = stage;
    }

    void initStage(Parent root) {
        Scene scene = new Scene(root);
        stage = new Stage();
        stage.setScene(scene);
        stage.setTitle("Upload a document");
        stage.setResizable(false);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setOnShowing(this::handleWindowShowing);
        stage.setOnCloseRequest(this::closeRequest);
        btBack.setOnAction(this::backButtonRequest);
        btUpload.setOnAction(this::uploadButtonRequest);
        btSelectFile.setOnAction(this::selectFileRequest);
        stage.show();
    }
    private void handleWindowShowing(WindowEvent event){
       //Cargar combobox con las categorias
        comboCategories.getItems().addAll("Hola","Holitas");
        
    }
    
    private void closeRequest(WindowEvent event){  
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Close confirmation");
        alert.setHeaderText("You pressed the 'Close'. \n"
            + "Document upload will be cancelled.");
        alert.setContentText("Are you sure?");
        alert.getButtonTypes().setAll(ButtonType.YES, ButtonType.NO);
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.YES) {
            stage.close();
        } else {
            event.consume();
        }
    }
    private void backButtonRequest(ActionEvent event){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setHeaderText("Close confirmation"); 
        alert.setContentText("Are you sure that want to cancel the upload?");
        alert.initOwner(stage);
        alert.initModality(Modality.WINDOW_MODAL);
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {
            stage.close();
        }else {
            event.consume();
        }
    }
    
    private void uploadButtonRequest(ActionEvent event){
        Boolean validation=checkValidations();
        if(validation){
            //Enviar datos de documento y documento a la base de datos
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("FUNCIONA");
            alert.setHeaderText("VALIDACIONESOK");
            alert.setContentText("Check the validation tips");
            Button errorButton = (Button) alert.getDialogPane().lookupButton(ButtonType.OK);
            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.YES) {
                alert.close();
            }
        }else{
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("ERROR");
            alert.setHeaderText("Uploading failed");
            alert.setContentText("Check the validation tips");
            Button errorButton = (Button) alert.getDialogPane().lookupButton(ButtonType.OK);
            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.YES) {
                alert.close();
            }
        }
    }
    
    private boolean checkValidations(){
       Boolean todoOk=false, nameOk=false,catOk=false,fileOk=false;
      

       if(txtNameDoc.getLength()>1 && txtNameDoc.getLength()<50){
           nameOk=true;
       }
       if(comboCategories.getValue() != null){
           catOk=true;
       }
       if(nameOk && catOk){
           todoOk=true;
       }
       return todoOk;
    }
    
    private void selectFileRequest(ActionEvent event){
        
        FileChooser fileChooser = new FileChooser();
        File selectedFile = fileChooser.showOpenDialog(null);
        fileChooser.getExtensionFilters().
            addAll(
                new FileChooser.ExtensionFilter("PDF Files", "*.pdf"));
        
        if (selectedFile != null) {
        lbFile.setText("File selected: " + selectedFile.getName());
        }else {
            lbFile.setText("File selection cancelled.");
        }
    }
    
}
