/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package windowsapplication.controller;

import java.util.ArrayList;
import java.util.Optional;
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
    /**
     * Table of users
     */
    @FXML
    private TableView tableDocGroup;
    /**
     * Column, name of user
     */
    @FXML
    private TableColumn tbColName;
    /**
     * Column, login of user
     */
    @FXML
    private TableColumn tbColLogin;
    /**
     * Column, email of user
     */
    @FXML
    private TableColumn tbColEmail;
    /**
     * Column, privileges of user
     */
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
    /**
     * Method to sets the stage
     * @param stage 
     */
    public void setStage(Stage stage) {
        this.stage = stage;
    }
    /**
     * 
     * @param user 
     */
    public void setUser(User user) {
        this.user = user;
    }
    /**
     * Sets in case of user is premium
     * @param premium 
     */
    public void setPremium(Premium premium) {
        this.premium = premium;
    }
    /**
     * Sets the privilege of the user
     * @param privilege 
     */
    public void setPrivilege(String privilege) {
        this.privilege = privilege;
    }
     /**
      * Init method that loads the objects of the view
      * @param root 
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
    /**
     * Method to load names of groups into combobox
     * @param event 
     */
    public void onWindowShowing(WindowEvent event) {
        //btSearch.setDisable(true);
        for(int cont=0;cont<groups.size();cont++){
                conGroups.getItems().add( groups.get(cont).getName());
            }
             
    }
    
    /**
     * Method that handle the action of buttons
     * @param event 
     */
    public void handleButtonAction(ActionEvent event){
        if(event.getSource().equals(btBack)){
            closeBack();
        }
        if(event.getSource().equals(btSearch)){
            searchGroup();
        }
        
    }
    
    /**
     * Method that handle the action of close the windows by red cross of window
     * @param event 
     */
    public void handleCloseAction(WindowEvent event) {
        closeCross(event);
    }
    
    /**
     * Method to search the data of a group and load the table
     */
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
