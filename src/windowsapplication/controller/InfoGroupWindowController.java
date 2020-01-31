/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package windowsapplication.controller;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.core.GenericType;
import windowsapplication.beans.Document;
import windowsapplication.beans.Group;
import windowsapplication.beans.Premium;
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
    @FXML
    private TableView tableDocGroup;
    @FXML
    private TableColumn tbColName;
    @FXML
    private TableColumn tbColLogin;
    @FXML
    private TableColumn tbColEmail;
    @FXML
    private TableColumn tbColPriv;
    
    /**
     * The stage of this window
     */
    private Stage stage;
    /**
     * Object user to fill with the data of the user that's loging in
     */
    private User user = null;
    
    private Premium premium = null;
    
    private String privilege;
    /**
     * Object group to fill with the data of a group
     */
    private Group group = null;
    /**
     * Collection of groups
     */
    private ArrayList <Group> groups= new ArrayList <Group>();
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
    
    public void setUser(User user) {
        this.user = user;
    }
    
    public void setPremium(Premium premium) {
        this.premium = premium;
    }
    
    public void setPrivilege(String privilege) {
        this.privilege = privilege;
    }
    
    /**NOTAS
     * 
     * Al iniciar,cargar una array con los grupos y sus datos del usuario, en el
     * combobox meter los nombres, y cuando de al boton de buscar, se le envia al metodo el grupo entero
     * 
     * Hacer en server metodo al que le envias un usuario y te devuelve todos los grupos junto a sus datos pero NO los documentos completos
     * cuando se quiera ver un documento, se llama a un metodo de los de documentos que abra uno con pasarle el id. 
     * (Cambiadopor descargar documento)
     * 
     * para sacar el privilegiode un user
     * findPrivilegueOfUserByLogin(login);   Return String
     * 
     */
    
    
    
    public void initStage(Parent root) {
        try{
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

            tbColLogin.setCellValueFactory(new PropertyValueFactory<>("name"));
            tbColName.setCellValueFactory(new PropertyValueFactory<>("user"));
            tbColEmail.setCellValueFactory(new PropertyValueFactory<>("category"));
            tbColPriv.setCellValueFactory(new PropertyValueFactory<>("uploadDate"));
   
            this.groups=ucr.findGroupsOfUser(new GenericType<ArrayList<Group>>() {}, this.user.getId());
            stage.show();
        }catch (NotFoundException ex) {
            LOGGER.severe("InfoGroup, groups not found: " + ex.getMessage());
        } catch (Exception ex) {
            LOGGER.severe("Can not initialize the group info window: " + ex.getMessage());
        }
        
    }
    
    public void onWindowShowing(WindowEvent event) {
        //btSearch.setDisable(true);
        for(int cont=0;cont<groups.size();cont++){
                conGroups.getItems().add( groups.get(cont).getName());
            }
             
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
    
    //Para actualizar una vez el usuario haya CREADO un grupo, llamar de nuevo a este metodo
    public void searchGroup(){
        try{
            this.group = null;
            Object obj = conGroups.getValue(); 
            for(Group g : groups){
                if(g.getName().equals(obj.toString())){
                    ObservableList<User> users = FXCollections
                            .observableArrayList(g.getUsers());
                    tableDocGroup.setItems(users);
                    break;
                }
            }

        }catch(Exception ex){
            LOGGER.severe("Error searching the group: " + ex.getMessage());
        }
    }
    
    public void tableIsEmpty(){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Table empty");
            alert.setHeaderText("There is no documents in this group.");
            alert.showAndWait();
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
