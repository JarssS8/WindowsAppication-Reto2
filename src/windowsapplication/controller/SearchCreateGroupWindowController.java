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
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.Tooltip;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javax.ws.rs.NotAuthorizedException;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.core.GenericType;
import windowsapplication.beans.Admin;
import windowsapplication.beans.Free;
import windowsapplication.beans.Group;
import windowsapplication.beans.Premium;
import windowsapplication.beans.User;
import windowsapplication.service.GroupClientREST;
import windowsapplication.service.UserClientREST;
import windowsapplication.service.UserClientRESTold;

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
    private TextField txtNewGroupName;
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
     * Help label for tooltip
     */
    @FXML
    private Label lblHelp;
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
     * Client REST for groups
     */
    private GroupClientREST cr;
    
    private UserClientREST ucr;
    /**
     * Method that set the stage for this window
     * @param stage from Log in window
     */
    public void setStage(Stage stage) {
        this.stage = stage;
    }
    /**
     * Method to initialize the window
     * @param root the loader for the scene
     * @param user The user wich is loged in 
     */
    public void initStage(Parent root, User user) {
        
        //Borrar despies,codigo de prueba simulacion de user loggeado
        String login = "user1";
        try{
            String priv = ucr.findPrivilegeOfUserByLogin(login);
            switch(priv){
                case "FREE":
                    this.user = new Free();
                    this.user = ucr.findUserByLogin(Free.class, login);
                    break;
                case "PREMIUM":
                    this.user = new Premium();
                    this.user = ucr.findUserByLogin(Premium.class, login);
                    break;
                case "ADMIN":
                    this.user = new Admin();
                    this.user = ucr.findUserByLogin(Admin.class, login);
                    break;
            }
            //Codigo de prueba
            
            this.user = user;
            Scene scene = new Scene(root);
            stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(scene);
            stage.setTitle("Search / Create Groups");
            stage.setResizable(false);
            stage.setOnCloseRequest(this::handleCloseAction);
            btBack.setOnAction(this::handleButtonAction);
            btSearch.setOnAction(this::handleButtonAction);
            btCreateGroup.setOnAction(this::handleButtonAction);
            stage.setOnShowing(this::onWindowShowing);
            txtGroupName.textProperty().addListener(this::textChange);
            txtNewGroupName.textProperty().addListener(this::textChange);
            txtPassword.textProperty().addListener(this::textChange);
            txtRepeatPassword.textProperty().addListener(this::textChange);
            stage.show();
        } catch (Exception ex) {
            ex.printStackTrace();
            LOGGER.severe("Can not initialize the group search/create window: " + ex.getMessage());
        }
        
    }
    
    /**
     * Method that handle On window showing
     * @param event 
     */
    public void onWindowShowing(WindowEvent event){
        btSearch.setDisable(true);
        btCreateGroup.setDisable(true);
        lblHelp.setTooltip(new Tooltip("-Group name must have 4-15 characters\n-Password must have 5-15 characters\nBoth passwords must be equals"));
    }
    
    /**
     * Method that checks if the texts of the windows changes and are okay 
     * @param observable
     * @param oldValue
     * @param newValue 
     */
    private void textChange(ObservableValue observable, String oldValue, String newValue) {//borrar estos parametros si no se usan
        if(txtGroupName.getText().trim().length()>=3 &&
                txtGroupName.getText().trim().length()<=16)
            btSearch.setDisable(false);
        else
            btSearch.setDisable(true);
        if(txtNewGroupName.getText().trim().length()>=3 && 
                txtNewGroupName.getText().trim().length()<16 &&
                txtPassword.getText().trim().length()>=4 && 
                txtPassword.getText().trim().length()<16 &&
                txtPassword.getText().equals(txtRepeatPassword.getText())){
            btCreateGroup.setDisable(false);
        }
        else
            btCreateGroup.setDisable(true);
    }

    /**
     * Method that handle the actions of the buttons
     * @param event 
     */
    public void handleButtonAction(ActionEvent event){
        if(event.getSource().equals(btBack)){
            closeBack();
        }
        if(event.getSource().equals(btSearch)){
            searchGroup();
        }
        if(event.getSource().equals(btCreateGroup)){
            createGroup();
        }
    }
    public void handleCloseAction(WindowEvent event) {
        closeCross(event);
    }

    /**
     * Method to search a group
     */
    public void searchGroup(){
        try{
            this.group = null;
            String groupName = txtGroupName.getText();
            this.group = cr.findGroupByName(new GenericType<Group>() {}, groupName);
            if(null!=this.group){
                Group auxGroup = this.group;
                User auxUser= this.user;
                if(userInGroup(auxGroup, auxUser)){//If the user is already in the group, we ask if it wants to leave
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setTitle("Leave confirmation");
                    alert.setHeaderText("You are already in this group.");
                    alert.setContentText("Do you want to leave?");
                    alert.getButtonTypes().setAll(ButtonType.YES, ButtonType.NO);
                    Button buttonYes = (Button) alert.getDialogPane().lookupButton(ButtonType.YES);
                    buttonYes.setId("buttonYes");
                    Button buttonNo = (Button) alert.getDialogPane().lookupButton(ButtonType.NO);
                    buttonNo.setId("buttonNo");
                    Optional<ButtonType> result = alert.showAndWait();
                    if (result.get() == ButtonType.YES) {
                        cr.leaveGroup(new GenericType<Group>() {},auxGroup.getId().toString(),auxUser.getId().toString());
                    } else
                        alert.close();
                }
                else{//If not, we ask the user the pass to join the group
                    if(passOk(auxGroup)){//Correct pass, join to group
                        joinGroup(auxGroup, auxUser);
                    }
                    else{//Wrong pass? kick in the ass!
                        Alert error = new Alert(Alert.AlertType.ERROR);
                        error.setTitle("Error");
                        error.setHeaderText("Wrong password");
                    }
                }
            }
        }catch(NotFoundException ex){
            LOGGER.severe("Checkgroup error: " + ex.getMessage());
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("ERROR");
            alert.setHeaderText("ERROR");
            alert.setContentText("Group doesn't exist");
        }catch(Exception ex){
            LOGGER.severe("Checkgroup error: " + ex.getMessage());
        }
    }
    
    /**
     * Method thats show an input dialog to ask the magic word of the group to the user
     * @param group
     * @return 
     */
    public boolean passOk (Group group){
        boolean ok=false;
        String dialogText = null;
        try{
            TextInputDialog dialog = new TextInputDialog();
            dialog.setTitle("Join group");
            //WARNING if empty throws an error
            dialog.setHeaderText("You are about to join "+ group.getName() +" group");
            dialog.setContentText("Password:");
            Optional<String> result = dialog.showAndWait();
            if (result.isPresent()) {
                dialogText=result.get();
                if(dialogText.equals(group.getPassword())){//Check of the magic word
                    ok=true;
                }
            }
        }catch(Exception ex){
            LOGGER.severe("Error creating the dialog for the pass: " +  ex.getMessage());
        }finally{
            return ok;  
        }
    }
    

    
    /**
     * Method to verify if a user is in a group
     * @param group
     * @param user 
     * @return  the boolean that says if the user is in the group
     */
    public boolean userInGroup(Group group, User user){
        boolean isIn = false;   
        try{
            ArrayList <User> userList = cr.findUsersOfGroup(new GenericType<ArrayList<User>>() {}, group.getId().toString());
            for(User auxUser : userList){
                if(user.getId().equals(auxUser.getId())){
                    isIn=true;
                    break;
                }
            }
        }catch(NotFoundException ex){
            LOGGER.severe("Error creating the dialog for the pass: " +  ex.getMessage());
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("ERROR");
            alert.setHeaderText("ERROR");
            alert.setContentText("Group doesn't exist");
        }catch(Exception ex){
            LOGGER.severe("Error checking the user in the group: " + ex.getMessage());
        }finally{
            return isIn;
        }
    }
    
    /**
     * Method to create a new group
     */
    public void createGroup(){//TODO create group
        try{
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("New group confirmation");
            alert.setHeaderText("You are about to create '" + txtNewGroupName.getText() + "'");
            alert.setContentText("Are you sure?");
            alert.getButtonTypes().setAll(ButtonType.YES, ButtonType.NO);
            Button buttonYes = (Button) alert.getDialogPane().lookupButton(ButtonType.YES);
            buttonYes.setId("buttonYes");
            Button buttonNo = (Button) alert.getDialogPane().lookupButton(ButtonType.NO);
            buttonNo.setId("buttonNo");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.YES) {
                Group auxGroup = new Group();
                Set <User> userList = new HashSet <User>();
                auxGroup.setId(1L);
                auxGroup.setName(txtNewGroupName.getText());
                auxGroup.setPassword(txtPassword.getText());
                auxGroup.setGroupAdmin(this.user);
                userList.add(this.user);
                auxGroup.setUsers(userList);
                cr.createGroup(auxGroup);
                Alert alert2 = new Alert(Alert.AlertType.INFORMATION);
                alert2.setTitle("Create Group");
                alert2.setHeaderText("Group created successfuly");
            } else
                alert.close();
        }catch(NotAuthorizedException ex){
            LOGGER.severe("Error Creating the group: " + ex.getMessage());
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("ERROR");
            alert.setHeaderText("ERROR");
            alert.setContentText("Group already exist");
            
        }catch(Exception ex){
            ex.printStackTrace();
            LOGGER.severe("Error Creating the group: " + ex.getMessage());
        }
    }
    
    /**
     * Method that adds a user to a group
     * @param group
     * @param user 
     */
    public void joinGroup(Group group, User user){
        try{
            cr.joinGroup(user, group.getName(), group.getPassword(), user.getId().toString());
        }catch(NotFoundException ex){
            LOGGER.severe("Error joining the group: " + ex.getMessage());
            LOGGER.severe("Error creating the dialog for the pass: " +  ex.getMessage());
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("ERROR");
            alert.setHeaderText("ERROR");
            alert.setContentText("Group doesn't exist");
        }catch(Exception ex){
            LOGGER.severe("Error joining the group: " + ex.getMessage());
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
