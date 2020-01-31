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
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javax.ws.rs.core.GenericType;
import windowsapplication.beans.Admin;
import windowsapplication.beans.Category;
import windowsapplication.beans.Document;
import windowsapplication.beans.Premium;
import windowsapplication.beans.User;
import windowsapplication.service.DocumentClientREST;
import windowsapplication.service.UserClientREST;

/**
 *
 * @author Aimar Arrizabalaga
 */
public class MainWindowController {

    private static final Logger LOGGER = Logger.getLogger(
        "windowsapplication.controller.MainWindowController");

    @FXML
    private Label lbUser;
    @FXML
    private Label lbLastConnect;
    @FXML
    private Label lbPrivilege;
    @FXML
    private Label lbIndex;
    @FXML
    private MenuBar menuBar;
    @FXML
    private Menu mProfile;
    @FXML
    private Menu mDocuments;
    @FXML
    private Menu mHelp;
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
    private MenuItem miAdminUsuarios;
    @FXML
    private MenuItem miAdminCategorias;
    @FXML
    private MenuItem miAdminDocs;
    @FXML
    private MenuItem miAyuda;
    @FXML
    private TableView tbDocs;
    @FXML
    private TableColumn colName;
    @FXML
    private TableColumn colCategory;
    @FXML
    private TableColumn colUploadDate;
    @FXML
    private Button btLogOut;

    private Stage stage;

    private User user;

    private Premium premium = null;

    private String privilege;

    UserClientREST client = new UserClientREST();

    public User getUser() {
        return user;
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

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public Stage getStage() {
        return this.stage;
    }

    public void initStage(Parent root) {
        try {
            Scene scene = new Scene(root);
            stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(scene);
            stage.setTitle("ToLearn()");
            stage.setResizable(false);
            stage.setOnShowing(this::handleWindowShowing);
            stage.setOnCloseRequest(this::closeRequest);
            btLogOut.setOnAction(this::exitButtonRequest);
            miSubirDocs.setOnAction(this::uploadDocRequest);
            miBuscarDocs.setOnAction(this::searchDocRequest);
            miDatos.setOnAction(this::profileRequest);
            miBuscarDocs.setAccelerator(new KeyCodeCombination(KeyCode.B, KeyCombination.ALT_DOWN));
            miSubirDocs.setAccelerator(new KeyCodeCombination(KeyCode.S, KeyCombination.ALT_DOWN));
            miDatos.setAccelerator(new KeyCodeCombination(KeyCode.D, KeyCombination.ALT_DOWN));
            miAyuda.setAccelerator(new KeyCodeCombination(KeyCode.H, KeyCombination.ALT_DOWN));
            miAdminUsuarios.setOnAction(this::adminrequest);
            miAdminCategorias.setOnAction(this::adminrequest);
            miAdminDocs.setOnAction(this::adminrequest);
            mHelp.setOnAction(this::helpRequest);
            tbDocs.getSelectionModel().selectedItemProperty().addListener(this::handleUsersTableSelection);
            miVerGrupos.setAccelerator(new KeyCodeCombination(KeyCode.G, KeyCombination.ALT_DOWN));
            colName.setCellValueFactory(new PropertyValueFactory<>("name"));
            colCategory.setCellValueFactory(new PropertyValueFactory<>("category"));
            colUploadDate.setCellValueFactory(new PropertyValueFactory<>("uploadDate"));
            
            stage.show();

        } catch (Exception ex) {
            LOGGER.warning("MainWindowController: Sorry, an error occurred "
                + "while loading the main window..." + ex.getMessage());
        }
    }

    private void handleWindowShowing(WindowEvent event) {
        try {
            //Usuario que recibe del login
            if (privilege.equals("FREE")) {
                // mGroups.setVisible(false);
                //mAdmin.setVisible(false);
                lbLastConnect.setText(user.getLastAccess().toString());
                lbUser.setText(user.getFullName());
            }
            if (privilege.equals("PREMIUM")) {
                // mGroups.setVisible(true);
                //mAdmin.setVisible(false);
                lbLastConnect.setText(premium.getLastAccess().toString());
                lbUser.setText(premium.getFullName());
            }
            if (privilege.equals("ADMIN")) {
                // mGroups.setVisible(true);
                // mAdmin.setVisible(true);
                lbLastConnect.setText(user.getLastAccess().toString());
                lbUser.setText(user.getFullName());
            }
            //Insertar numero del indice de la lista
            lbIndex.setText(" ");
            //Insertar ultima conex del usuario
            lbPrivilege.setText(privilege);

            Long id = null;
            if (privilege.equals("PREMIUM")) {
                id = premium.getId();
            } else {
                id = user.getId();
            }
            ObservableList<Document> userDocs = null;
            userDocs = FXCollections.observableArrayList(client.findDocumentsOfUser(new GenericType<List<Document>>() {
            }, id));
            tbDocs.setItems(userDocs);
        } catch (Exception ex) {
            LOGGER.warning("MainWindowController: Sorry, an error occurred "
                + "while loading the main window..." + ex.getMessage());
        }

    }

 

    private void profileRequest(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(
                "/windowsapplication/view/Perfil.fxml"));
            Parent root = (Parent) loader.load();
            ProfileWindowController profileWindowController
                = ((ProfileWindowController) loader.getController());
            profileWindowController.setStage(stage);
            profileWindowController.setPrivilege(privilege);
            if (privilege.equals("PREMIUM")) {
                profileWindowController.setPremium(premium);
            } else {
                profileWindowController.setUser(user);
            }
            profileWindowController.initStage(root);
            stage.close();
        } catch (Exception ex) {
            LOGGER.warning("MainWindowController: Sorry, an error occurred "
                + "while loading the window..." + ex.getMessage());
        }
    }

    private void closeRequest(WindowEvent event) {
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

    public void exitButtonRequest(ActionEvent event) {
        try {
            stage.close();
            FXMLLoader loader = new FXMLLoader(getClass().
                getResource("/windowsapplication/view/LogIn_Window.fxml"));
            Parent root = (Parent) loader.load();
            LoginWindowController controller = loader.getController();
            controller.setStage(stage);
            controller.initStage(root);
        } catch (Exception ex) {
            LOGGER.warning("MainWindowController: An error ocurred while "
                + "loading the window... " + ex.getMessage());
        }
    }

    private void searchDocRequest(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(
                "/windowsapplication/view/BuscarDocumento.fxml"));
            Parent root = (Parent) loader.load();
            SearchDocWindowController SearchDocController
                = ((SearchDocWindowController) loader.getController());
            SearchDocController.setStage(stage);
            SearchDocController.initStage(root);
        } catch (Exception ex) {
            LOGGER.warning("MainWindowController: " + ex.getMessage());
        }
    }

    private void uploadDocRequest(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(
                "/windowsapplication/view/SubirDocumento.fxml"));
            Parent root = (Parent) loader.load();
            UploadDocWindowController UploadDocController
                = ((UploadDocWindowController) loader.getController());
            UploadDocController.setUser(user);
            UploadDocController.setPremium(premium);
            UploadDocController.setPrivilege(privilege);
            UploadDocController.setStage(stage);
            UploadDocController.initStage(root);
        } catch (Exception ex) {
            LOGGER.warning("MainWindowController: " + ex.getMessage());
        }
    }

    private void adminrequest(ActionEvent event) {
        String call = "nothing";
        try {
            if (event.getSource().equals(miAdminUsuarios)) {
                call = "users";
            }
            if (event.getSource().equals(miAdminCategorias)) {
                call = "categories";
            }
            if (event.getSource().equals(miAdminDocs)) {
                call = "documents";
            }
            FXMLLoader loader = new FXMLLoader(getClass().getResource(
                "/windowsapplication/view/VerUsersCategoriasDocs.fxml"));
            Parent root = (Parent) loader.load();
            AdminWindowController adminWindowController
                = ((AdminWindowController) loader.getController());
            adminWindowController.setPrivilege(privilege);
            adminWindowController.setPremium(premium);
            adminWindowController.setCall(call);
            adminWindowController.setStage(stage);
            adminWindowController.setUser(user);
            adminWindowController.initStage(root);
        } catch (Exception ex) {
            LOGGER.warning("MainWindowController: " + ex.getMessage());
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
        } catch (Exception ex) {
            LOGGER.warning("MainWindowController: " + ex.getMessage());
        }
    }

    private void handleUsersTableSelection(ObservableValue observable,
        Object oldValue, Object newValue) {
        if (newValue != null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource(
                    "/windowsapplication/view/VerDocumento.fxml"));
                Parent root = (Parent) loader.load();
                InfoDocWindowController infoDocWindowController
                    = ((InfoDocWindowController) loader.getController());
                infoDocWindowController.setStage(stage);
                infoDocWindowController.setUser(user);
                infoDocWindowController.setDocument((Document) tbDocs.getSelectionModel().getSelectedItem());
                infoDocWindowController.initStage(root);
            } catch (Exception ex) {
            LOGGER.warning("MainWindowController: " + ex.getMessage());
        }
        }
    }

}
