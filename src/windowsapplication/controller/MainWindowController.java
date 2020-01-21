/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package windowsapplication.controller;

import java.io.IOException;
import java.util.List;
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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javax.ws.rs.core.GenericType;
import windowsapplication.beans.Admin;
import windowsapplication.beans.Category;
import windowsapplication.beans.Document;
import windowsapplication.beans.Free;
import windowsapplication.beans.Premium;
import windowsapplication.beans.User;
import windowsapplication.service.DocumentClientREST;
import windowsapplication.service.UserClientREST;

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
    private MenuItem miHelp;
    @FXML
    private TableView tbDocs;
    @FXML
    private TableColumn colName;
    @FXML
    private TableColumn colAuthor;
    @FXML
    private TableColumn colCategory;
    @FXML
    private TableColumn colUploadDate;
    @FXML
    private Button btExit;
    
    private UserClientREST userREST = new UserClientREST();
    
    private Stage stage;
    
    private User user;
    
    public User getUser(){
        return user;
    }
    public void setUser(User user){
        this.user = user;
    }
    
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
        miHelp.setOnAction(this::helpRequest);
        miBuscarDocs.setAccelerator(new KeyCodeCombination(KeyCode.B, KeyCombination.ALT_DOWN));
        miSubirDocs.setAccelerator(new KeyCodeCombination(KeyCode.S, KeyCombination.ALT_DOWN));
        miHelp.setAccelerator(KeyCombination.valueOf("F6"));
        colName.setCellValueFactory(new PropertyValueFactory<>("Name"));
        colCategory.setCellValueFactory(new PropertyValueFactory<>("Category"));
        colAuthor.setCellValueFactory(new PropertyValueFactory<>("Author"));
        colUploadDate.setCellValueFactory(new PropertyValueFactory<>("Upload date"));
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
        lbLastConnect.setText(user.getLastAccess().toString());
        //Insertar privilegio del usuario
        lbPrivilege.setText(user.getPrivilege().toString());
        //Insertar FullName del usuario
        lbUser.setText(user.getLogin());
        
       List<Document> userDocs = userREST.findDocumentsOfUser(new GenericType<List<Document>>() {}, user.getId());
       userDocs.stream().forEach(userwdocs-> tbDocs.getItems().add(userwdocs));
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
    
    private void helpRequest(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(
                "/windowsapplication/view/Help.fxml"));
            Parent root = (Parent) loader.load();
            HelpWindowController helpWindowController
                = ((HelpWindowController) loader.getController());
            helpWindowController.setStage(stage);
            helpWindowController.initStage(root);
        } catch (IOException ex) {
            Logger.getLogger(MainWindowController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
   
}
