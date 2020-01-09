/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package windowsappication.controller;

import java.util.Optional;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

/**
 * This class is a controller of the "VerGrupos" view. Contains event
 * handlers and on window showing code.
 * @author Diego Urraca
 */
public class InfoGroupWindowController {
    private static final Logger LOGGER = Logger
            .getLogger("windowsclientapplication.LogOutWindowController");
    /**
     * Combo box with the names of the groups
     */
    @FXML
    private ComboBox conGroups;
     /**
     * Label for the name of the admin of the group
     */
    @FXML
    private Label lbGroup;
    /**
     * Button to search for a group
     */
    @FXML
    private Button btSearch;
    /**
     * Button to go back
     */
    @FXML
    private Button btBack;
    
    /**
     * The stage of this window
     */
    private Stage stage;
    /**
     * Object user to fillwith the data of the user that's loging in
     */
    private User user = null;
    
    
    public void setStage(Stage stage) {
        this.stage = stage;
    }
    
    
    
    /**NOTAS
     * 
     * Al iniciar,cargar una array con los grupos y su datos del usuario, en el
     * combobox meter los nombres, y cuandode al boton de buscar, se le envia al metodo el grupos y su datos
     * 
     */
    
    
    
    public void initStage(Parent root, User user) {
        try{
            Scene scene = new Scene(root);
            stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(scene);
            stage.setTitle("Search / Create Groups");
            stage.setResizable(false);
            stage.setOnShowing(this::onWindowShowing);
            stage.setOnCloseRequest(this::handleCrossCloseAction);
            btBack.setOnAction(this::handleBackAction);
            btSearch.setOnAction(this::handleSearchGroupAction);
            btCreateGroup.setOnAction(this::handleCreateGroupAction);
            stage.show();
        } catch (Exception e) {
            LOGGER.severe("Can not initialize the group search/create window");
        }
        
    }
    public void handleCrossCloseAction(WindowEvent event) {
        close();
    }
    
    public void handleBackAction(){
        stage.close();
    }
    
    public void handleSearchGroupAction(){
        
    }
    
    public void handleCreateGroupAction(){
        
    }
    /**
     * Method that handle the close alert
     */
    public void close(){ //TODO adaptar a la nueva app
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Close confirmation");
        alert.setHeaderText("You are about to close the application.");
        alert.setContentText("Are you sure?");
        alert.getButtonTypes().setAll(ButtonType.YES, ButtonType.NO);
        Button buttonYes = (Button) alert.getDialogPane().lookupButton(ButtonType.YES);
        buttonYes.setId("buttonYes");
        Button buttonNo = (Button) alert.getDialogPane().lookupButton(ButtonType.NO);
        buttonNo.setId("buttonNo");
        Optional<ButtonType> result = alert.showAndWait();
        
        //TODO actualizar fecha de conexion y cerrar programa
        
        if (result.get() == ButtonType.YES) {
            try {
                //client.logOut(user);
                LOGGER.info("User loggin date updated sucesfully");
            } /*catch (ServerConnectionErrorException e) {
                LOGGER.warning("Couldn't connect to server. LogOut date not updated.");
            }*/finally{
                Platform.exit();
            }
        } else {
            alert.close();
        }
        
        
    }
    
    /**
     * Method that loads the texts and prepare the objects of the window
     *
     * @param event The event is the window that is being showed.
     */
    public void onWindowShowing(WindowEvent event) {//TODO adaptar a la nueva app
        LOGGER.info("Starting loading the window");
        
    }
}
