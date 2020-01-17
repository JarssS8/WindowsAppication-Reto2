/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package windowsapplication.controller;

import java.io.IOException;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import windowsapplication.beans.Admin;
import windowsapplication.beans.Free;
import windowsapplication.beans.Premium;
import windowsapplication.beans.User;

/**
 *
 * @author Adrián Corral
 */
public class MainWindowController {
    @FXML
    private Label lbUser;
    @FXML
    private Label lbLastConnect;
    @FXML
    private Label lbPrivilege;
    @FXML
    private Label lbIndex;
    @FXML
    private Menu mGroups;
    @FXML
    private Menu mAdmin;
    @FXML
    private MenuItem miBuscarDocs;
    
    @FXML
    private MenuItem miSubirDocs;
    @FXML
    private MenuItem miAdminGrupos;
    @FXML
    private MenuItem miAdminCategorias;
    @FXML
    private MenuItem miAdminDocs;
    
   
    @FXML
    private Button btExit;
    
    private Stage stage;
    
    public Stage getStage(){
        return stage;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void initStage(Parent root) {
        Scene scene = new Scene(root);
        
        stage.setScene(scene);
        stage.setTitle("ToLearn()");
        stage.setResizable(false);
        stage.setOnShowing(this::handleWindowShowing);
        stage.setOnCloseRequest(this::closeRequest);
        btExit.setOnAction(this::exitButtonRequest);
        miSubirDocs.setOnAction(this::uploadDocRequest);
        miBuscarDocs.setOnAction(this::searchDocRequest);
        miAdminGrupos.setOnAction(this::adminrequest);
        miAdminCategorias.setOnAction(this::adminrequest);
        miAdminDocs.setOnAction(this::adminrequest);
        miBuscarDocs.setAccelerator(new KeyCodeCombination(KeyCode.B, KeyCombination.ALT_DOWN));
        miSubirDocs.setAccelerator(new KeyCodeCombination(KeyCode.S, KeyCombination.ALT_DOWN));
        
        
        stage.show();
    }
    
    private void handleWindowShowing(WindowEvent event){
        
       //Usuario que recibe del login
        User user = new User();
        if(user instanceof Free){
            mGroups.setVisible(false);
            mAdmin.setVisible(false);
        }
        if(user instanceof Premium){
            mGroups.setVisible(true);
            mAdmin.setVisible(false);
        }
        if(user instanceof Admin){
            mGroups.setVisible(true);
            mAdmin.setVisible(true);
        }
        
        //Insertar numero del indice de la lista
        lbIndex.setText(" ");
        //Insertar ultima conex del usuario
        lbLastConnect.setText(" ");
        //Insertar privilegio del usuario
        lbPrivilege.setText(" ");
        //Insertar FullName del usuario
        lbUser.setText(" ");
        
        /*
        Cargar los documentos del usuario en la tabla
        */
        
    }
  
    private void closeRequest(WindowEvent event){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setHeaderText("Close confirmation");
        alert.setTitle("Exit Window");
        alert.setContentText("Are you sure that want close the application?");
        alert.initOwner(stage);
        alert.initModality(Modality.WINDOW_MODAL);
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {
            stage.close();
            Platform.exit();

        } else {
            event.consume();
        }
    }
    
    private void exitButtonRequest(ActionEvent event){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setHeaderText("Close confirmation"); 
        alert.setContentText("Are you sure that want close the application?");
        alert.initOwner(stage);
        alert.initModality(Modality.WINDOW_MODAL);
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {
             try {
                 FXMLLoader loader = new FXMLLoader(getClass().getResource(
                     "/windowsapplication/view/LogIn_Window.fxml"));
                 Parent root = (Parent) loader.load();
                 LoginWindowController logInController
                     = ((LoginWindowController) loader.getController());
                 logInController.setStage(stage);
                 logInController.initStage(root);
             } catch (IOException ex) {
                 
             }
        }
    }
    
    private void searchDocRequest(ActionEvent event){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(
                "/windowsapplication/view/BuscarDocumento.fxml"));
            Parent root = (Parent) loader.load();
            SearchDocWindowController SearchDocController
                = ((SearchDocWindowController) loader.getController());
            SearchDocController.setStage(stage);
            SearchDocController.initStage(root);
        } catch (IOException ex) {
            
        }
    }
    
    private void uploadDocRequest(ActionEvent event){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(
                "/windowsapplication/view/SubirDocumento.fxml"));
            Parent root = (Parent) loader.load();
            UploadDocWindowController UploadDocController
                = ((UploadDocWindowController) loader.getController());
            UploadDocController.setStage(stage);
            UploadDocController.initStage(root);
        } catch (IOException ex) {
            
        }
    }
    
    private void adminrequest(ActionEvent event){
        String call = "nothing";
        try {
            if(event.getSource().equals(miAdminGrupos)){
                call="users";
            }if(event.getSource().equals(miAdminCategorias)){
                call="categories";
            }if(event.getSource().equals(miAdminDocs)){
                call="documents";
            }
            FXMLLoader loader = new FXMLLoader(getClass().getResource(
                "/windowsapplication/view/VerUsersCategoriasDocs.fxml"));
            Parent root = (Parent) loader.load();
            AdminWindowController adminWindowController
                = ((AdminWindowController) loader.getController());
            adminWindowController.setCall(call);
            adminWindowController.setStage(stage);
            adminWindowController.initStage(root);
        } catch (IOException ex) {
            Logger.getLogger(MainWindowController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    
   
}
