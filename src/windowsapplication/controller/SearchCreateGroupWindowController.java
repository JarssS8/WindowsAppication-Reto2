/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package windowsappication.controller;

import java.util.ArrayList;
import java.util.Optional;
import java.util.Set;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import windowsappication.service.DocumentClientREST;

/**
 * This class is a controller of the "BuscarCrearGrupos" view. Contains event
 * handlers and on window showing code.
 * @author Diego Urraca
 */
public class SearchCreateGroupWindowController {
    private static final Logger LOGGER = Logger
            .getLogger("windowsclientapplication.LogOutWindowController");
    /**
     * Group name text input to search
     */
    @FXML
    private TextField txtGroupName;
    /**
     * Group name text input to create
     */
    @FXML
    private TextField txtNewGroup;
    /**
     * Password text input to create
     */
    @FXML
    private TextField txtPassword;
    /**
     * Repeat password text input to ceate
     */
    @FXML
    private TextField txtRepeatPassword;
    /**
     * Button to search for groups
     */
    @FXML
    private Button btSearch;
    /**
     * Button to go back
     */
    @FXML
    private Button btBack;
    /**
     * Button to create the group
     */
    @FXML
    private Button btCreateGroup;
    
    /**
     * The stage of this window
     */
    private Stage stage;
    /**
     * Object user to fill with the data of the user that's loging in
     */
    private User user = null;
    /**
     * Object group to fill with the data of a group
     */
    private Group group = null;
    /**
     * Collection of groups
     */
    private Set <Group> groups= null;
    
    /**
     * Method that set the stage for this window
     * @param stage from Log in window
     */
    public void setStage(Stage stage) {
        this.stage = stage;
    }
    
    
    
    
    
    /* NOTAS
    
    Si un usuario busca un grupo,debe poner su nombre exactamente.
    Si se encuentra dicho grupo, saldra un ALERT que le preguntara la clave para unirse,puede cancelarse
    si ya esta dentro del grupo, el ALERT le preguntara si quiere abandonar el grupo
    Para todo esto, necesitamos comprobar la lista de grupos del usuario, y la lista de grupos con sus nombres y sus passwords
    
    
    */
    
    
    
    
    
    
    /**
     * Method to initialize the window
     * @param root the loader for the scene
     * @param user The user wich is loged in 
     */
    public void initStage(Parent root, User user) {
        try{
            this.user = user;
            Scene scene = new Scene(root);
            stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(scene);
            stage.setTitle("Search / Create Groups");
            stage.setResizable(false);
            stage.setOnCloseRequest(this::handleCloseAction);
            btBack.setOnAction(this::handleCloseAction);
            btSearch.setOnAction(this::handleSearchGroupAction);
            btCreateGroup.setOnAction(this::handleCreateGroupAction);
            stage.show();
        } catch (Exception e) {
            LOGGER.severe("Can not initialize the group search/create window");
        }
        
    }
    
    /**
     * 
     * @param event The user clic the cross to close the window
     */
    public void handleCloseAction(WindowEvent event) {
        close();
    }
    
    public void handleSearchGroupAction(){
        this.group = null;
        String groupName = txtGroupName.getText();
        this.group = searchGroupByName(groupName);
        if(null!=this.group)
            joinGroup(this.group, this.user);
    }
    
    public void handleCreateGroupAction(){
        createGroup();
    }

    public void searchGroupByName(String groupName){
        
    }
    
    public void createGroup(){
        
    }
    
    public void joinGroup(Group group, User user){
        try{
            DocumentClientREST.joinGroup(group.groupname,group.password,user);
        } catch(Exception e){
            LOGGER.severe("Error joining the group");
        }
    }
    
    /**
     * Method that handle the close alert
     */
    public void close(){
        if(txtGroupName.getLength()!=0 || txtNewGroup.getLength()!=0 || txtPassword.getLength()!=0 || txtRepeatPassword.getLength()!=0){
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Close confirmation");
            alert.setHeaderText("You are about to close the window with some data.");
            alert.setContentText("Are you sure?");
            alert.getButtonTypes().setAll(ButtonType.YES, ButtonType.NO);
            Button buttonYes = (Button) alert.getDialogPane().lookupButton(ButtonType.YES);
            buttonYes.setId("buttonYes");
            Button buttonNo = (Button) alert.getDialogPane().lookupButton(ButtonType.NO);
            buttonNo.setId("buttonNo");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.YES) {
                stage.close();
            } else
                alert.close();
        }
        else
            stage.close();
            
    }
}
