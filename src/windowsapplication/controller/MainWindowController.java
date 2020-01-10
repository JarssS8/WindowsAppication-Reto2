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
import javafx.scene.input.KeyEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import windowsapplication.beans.Admin;
import windowsapplication.beans.Free;
import windowsapplication.beans.Premium;
import windowsapplication.beans.User;

/**
 *
 * @author Gaizka Andres
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
    private MenuItem miDatos;
    @FXML
    private MenuItem miBuscarDocs;
    @FXML
    private MenuItem miSubirDocs;
    @FXML
    private MenuItem miVerGrupos;
    @FXML
    private MenuItem miBuscarGrupos;
    @FXML
    private MenuItem miAdminGrupos;
    @FXML
    private MenuItem miAdminCategorias;
    @FXML
    private MenuItem miAdminDocs;
    @FXML
    private MenuItem miAyuda;
    @FXML
    private Button btExit;
        private Stage stage;

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
        miDatos.setOnAction(this::profileRequest);
        miBuscarDocs.setAccelerator(new KeyCodeCombination(KeyCode.B, KeyCombination.ALT_DOWN));
        miSubirDocs.setAccelerator(new KeyCodeCombination(KeyCode.S, KeyCombination.ALT_DOWN));
        miDatos.setAccelerator(new KeyCodeCombination(KeyCode.D, KeyCombination.ALT_DOWN));
        miVerGrupos.setAccelerator(new KeyCodeCombination(KeyCode.G, KeyCombination.ALT_DOWN));
        miBuscarGrupos.setAccelerator(new KeyCodeCombination(KeyCode.H, KeyCombination.ALT_DOWN));
        
        stage.getScene().addEventFilter(KeyEvent.KEY_PRESSED, this::variousShortcut);
        
        
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
    
    public void variousShortcut(KeyEvent ke) {
        KeyCode pressButton = ke.getCode();
        if (pressButton.equals(KeyCode.F1)) {
            
        }
    }
    private void profileRequest(ActionEvent event){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(
                "/windowsapplication/view/Perfil.fxml"));
            Parent root = (Parent) loader.load();
            ProfileWindowController profileWindowController
                = ((ProfileWindowController) loader.getController());
            profileWindowController.setStage(stage);
            profileWindowController.initStage(root);
        } catch (IOException ex) {
            
        }
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
}
