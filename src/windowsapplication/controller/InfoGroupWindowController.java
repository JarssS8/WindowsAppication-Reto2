/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package windowsapplication.controller;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.function.ToLongFunction;
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
import windowsapplication.beans.Category;
import windowsapplication.beans.Document;
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
            

            tbColLogin.setCellValueFactory(new PropertyValueFactory<>("name"));
            tbColName.setCellValueFactory(new PropertyValueFactory<>("user"));
            tbColEmail.setCellValueFactory(new PropertyValueFactory<>("category"));
            tbColPriv.setCellValueFactory(new PropertyValueFactory<>("uploadDate"));
            
            
            //*this.groups=ucr.findGroupsOfUser(new GenericType<ArrayList<Group>>() {}, user.getId().toString());
            //^^^^^^^^^^^^ Recogemos la lista de grupos
            //TODO, esto coge un array de grupos, lo pasa a un aray de nombres y lo carga en el combobox, tiene que hacerse 
            //con la lista de grupos que llega del servidor
            
            //////Code for test, erase later
            User u = new User();
            Group g = new Group();
            Document d = null;
            Set <Document> docs = new HashSet<Document>();
            Set <Document> auxDocs = new HashSet<Document>();
            
            u.setFullName("A Dummy User");
            g.setName("Grupo uno");
            g.setPassword("Pass");
            g.setGroupAdmin(u);
            auxDocs.add(d);
            g.setDocuments(auxDocs);
            
            this.groups.add(g);
            
            /*Category c = new Category();
            DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            Date dat = new Date();
            dateFormat.format(dat);
            c.setName("Cat1");
            g = new Group();
            u = new User();
            d = new Document();
            d.setName("Doc1");
            d.setCategory(c);
            d.setUploadDate(dat);
            u.setFullName("Another Dummy User");
            d.setUser(u);
            g.setName("Grupo dos");
            g.setPassword("Pass");
            g.setGroupAdmin(u);
            docs.add(d);
            d = new Document();
            d.setName("Doc2");
            docs.add(d);
            g.setDocuments(docs);
            this.groups.add(g);
            //////Code for test,erase later
            
            //Borrar if OK
            ArrayList <String> names = new ArrayList <String>();
            for(Group gro : grups){
                String aux = gro.getName();
                names.add(aux);
            }*/
            
            
            for(int cont=0;cont<groups.size();cont++){
                conGroups.getItems().add( groups.get(cont).getName());
            }
            
            stage.show();
        } catch (Exception ex) {
            LOGGER.severe("Can not initialize the group info window: " + ex.getMessage());
        }
        
    }
    
    public void onWindowShowing(WindowEvent event) {
        //btSearch.setDisable(true);
        //Si no es admin del grupo, no puede subir archivos
             
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
                    this.group = g;
                    
                    if(!g.getDocuments().isEmpty()){
                        LOGGER.info("ENTRA EN EL IF");
                        /*ObservableList<User> users = FXCollections.observableArrayList()
                                
                                (ObservableList<User>) g.getUsers();
                        tableDocGroup.setItems(users);*/
                    }
                    else{
                        tableIsEmpty();
                    }
                    //TODO el array aun estando null da error o sale que tiene un dato ??¿?¿?
                        
                        
                }
                break;
            }
            
            

            //this.group = conGroups.
            LOGGER.info("SALE DEL IF");
            
            
            
           /* this.group = null;
            //this.group = gcr.findGroupByName(new GenericType<Group>() {}, groupName);
            if(null!=this.group){
                
            }*/
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
