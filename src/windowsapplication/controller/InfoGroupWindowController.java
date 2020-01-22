/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package windowsapplication.controller;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Observable;
import java.util.Optional;
import java.util.Set;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
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
import javax.ws.rs.core.GenericType;
import windowsapplication.beans.Group;
import windowsapplication.beans.User;
import windowsapplication.service.GroupClientREST;
import windowsapplication.service.UserClientREST;

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
     * Client REST for Groups
     */
    private GroupClientREST gcr;
    /**
     * Client REST for Users
     */
    private UserClientREST ucr;
    
    public void setStage(Stage stage) {
        this.stage = stage;
    }
    
    
    
    /**NOTAS
     * 
     * Al iniciar,cargar una array con los grupos y sus datos del usuario, en el
     * combobox meter los nombres, y cuando de al boton de buscar, se le envia al metodo el grupo entero
     * 
     * Hacer en server metodo al que le envias un usuario y te devuelve todos los gruypos junto a sus datos pero NO los documentos completos
     * cuando se quiera ver un documento, se llama a un metodo de los de documentos que abra uno con pasarle el id.
     * 
     */
    
    
    
    public void initStage(Parent root, User user) {
        try{
            this.user = user;
            Scene scene = new Scene(root);
            stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(scene);
            stage.setTitle("Info of Groups");
            stage.setResizable(false);
            stage.setOnShowing(this::onWindowShowing);
            stage.setOnCloseRequest(this::handleCloseAction);
            btBack.setOnAction(this::handleButtonAction);
            btSearch.setOnAction(this::handleButtonAction);
            
            //groups=ucr.findGroupsOfUser(new GenericType<Set<Group>>() {}, user.getId().toString());
            //^^^^^^^^^^^^ Recogemos la lista de grupos
            //TODO, esto coge un array de grupos, lo pasa a un aray de nombres y lo carga en el combobox, tiene que hacerse 
            //con la lista de grupos que llega del servidor
            ArrayList <Group> grups = new ArrayList <Group>();
            Group g = new Group();
            g.setName("Grupo uno");
            g.setPassword("Pass");
            grups.add(g);
            g = new Group();
            g.setName("Grupo dos");
            g.setPassword("Pass");
            grups.add(g);
            
            ArrayList <String> names = new ArrayList <String>();
            for(Group gro : grups){
                String aux = gro.getName();
                names.add(aux);
            }
            LOGGER.info("Lista: " + grups.get(0).getName());
            
            conGroups.getItems().setAll(names);
            
            stage.show();
        } catch (Exception ex) {
            LOGGER.severe("Can not initialize the group info window: " + ex.getMessage());
        }
        
    }
    
    public void onWindowShowing(WindowEvent event) {
         btSearch.setDisable(true);
    }
    
    public void handleButtonAction(ActionEvent event){
        if(event.getSource().equals(btBack)){
            closeBack();
        }
        if(event.getSource().equals(btSearch)){
            searchGroup();
        }
        
    }
    public void handleCloseAction(WindowEvent event) {
        closeCross(event);
    }
    
    
    
    
    
    
    
    
    public void searchGroup(){
        try{
            this.group = null;
            //this.group = gcr.findGroupByName(new GenericType<Group>() {}, groupName);
            if(null!=this.group){
                
            }
        }catch(Exception ex){
            LOGGER.severe("Checkgroup error: " + ex.getMessage());
        }
    }
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
   
    /**
     * Close the window with the red cross button of the window
     * @param event
     */
    public void closeCross(WindowEvent event){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Close confirmation");
        alert.setHeaderText("You are about to close this window.");
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
            event.consume();
    }
    
    /**
     * Close the window with 'Back' button
     */
    public void closeBack(){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Close confirmation");
        alert.setHeaderText("You are about to close this window.");
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
}
