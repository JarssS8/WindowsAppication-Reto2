/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package windowsapplication.controller;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;
import javafx.application.Platform;
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
import windowsapplication.beans.Document;
import windowsapplication.beans.Premium;
import windowsapplication.beans.User;
import windowsapplication.service.UserClientREST;

/**
 *
 * @author Aimar Arrizabalaga
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
    private MenuBar menuBar;
    @FXML
    private Menu mProfile;
    @FXML
    private Menu mDocuments;
    @FXML
    private Menu mGroups;
    @FXML
    private Menu mAdmin;
    @FXML
    private Menu mHelp;
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
    private MenuItem miAdminUsuarios;
    @FXML
    private MenuItem miAdminGrupos;
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

    private static final Logger LOGGER = Logger.getLogger(
            "windowsapplication.controller.MainWindowController");

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
            miVerGrupos.setAccelerator(new KeyCodeCombination(KeyCode.G, KeyCombination.ALT_DOWN));
            miBuscarGrupos.setAccelerator(new KeyCodeCombination(KeyCode.H, KeyCombination.ALT_DOWN));
            stage.getScene().addEventFilter(KeyEvent.KEY_PRESSED, this::variousShortcut);
            colName.setCellValueFactory(new PropertyValueFactory<>("name"));
            colCategory.setCellValueFactory(new PropertyValueFactory<>("category"));
            colUploadDate.setCellValueFactory(new PropertyValueFactory<>("uploadDate"));
            // Creating Menus
            menuBar = new MenuBar();
            mProfile = new Menu("Profile");
            miDatos = new MenuItem("Your data");
            mDocuments = new Menu("Documents");
            miBuscarDocs = new MenuItem("Search document");
            miSubirDocs = new MenuItem("Upload document");
            mGroups = new Menu("Groups");
            miVerGrupos = new MenuItem("View groups");
            miBuscarGrupos = new MenuItem("Search groups");
            mAdmin = new Menu("Administration");
            miAdminUsuarios = new Menu("Administrate users");
            miAdminDocs = new Menu("Administrate documents");
            miAdminGrupos = new Menu("Administrate groups");
            miAdminCategorias = new Menu("Administrate categories");
            mHelp = new Menu("Help");
            miAyuda = new MenuItem("Help");
            mProfile.getItems().add(miDatos);
            mDocuments.getItems().addAll(miBuscarDocs, miSubirDocs);
            mGroups.getItems().addAll(miVerGrupos, miBuscarGrupos);
            mAdmin.getItems().addAll(miAdminUsuarios, miAdminDocs, miAdminGrupos, miAdminCategorias);
            mHelp.getItems().add(miAyuda);
            menuBar.getMenus().addAll(mProfile, mDocuments, mGroups, mAdmin, mHelp);
            
            /*
            final ContextMenu cm = new ContextMenu();
            MenuItem cmItem1 = new MenuItem("Edit"); 
            MenuItem cmItem2 = new MenuItem("Go back");
            MenuItem cmItem3 = new MenuItem("Delete");
            
            cmItem1.setOnAction((ActionEvent e) -> {
                    lbAuthor.setText("New name: ");
                    txtAuthor.setPromptText("New name");
                    btAddCat.setText("Change");
                    edit = true;
            });

            cmItem2.setOnAction((ActionEvent e) -> {
                stage.close();
            });
            
            cmItem3.setOnAction((ActionEvent e) -> {
                    Document document = (Document) tbDocs.getSelectionModel().getSelectedItem();
                    UserREST.deleteUser(user.getId());
            });
            
            cm.getItems().addAll(cmItem1, cmItem3, cmItem2);
            tbCategories.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent e) -> {
                if (e.getButton() == MouseButton.SECONDARY) {
                    cm.show(stage, e.getScreenX(), e.getScreenY());
                }
            });
            tbDocs.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent e) -> {
                if (e.getButton() == MouseButton.SECONDARY) {
                    cm.show(stage, e.getScreenX(), e.getScreenY());
                }
            });
            
            // End Menus

            */
            stage.show();
            
        } catch (Exception ex) {
            ex.printStackTrace();
            LOGGER.warning("MainWindowController: Sorry, an error occurred "
                    + "while loading the main window..." + ex.getMessage());
        }
}


    private void handleWindowShowing(WindowEvent event) {

        //Usuario que recibe del login
        if (privilege.equals("FREE")) {
            mGroups.setVisible(false);
            mAdmin.setVisible(false);
            lbLastConnect.setText(user.getLastAccess().toString());
            lbUser.setText(user.getFullName());
        }
        if (privilege.equals("PREMIUM")) {
            mGroups.setVisible(true);
            mAdmin.setVisible(false);
            lbLastConnect.setText(premium.getLastAccess().toString());
            lbUser.setText(premium.getFullName());
        }
        if (privilege.equals("ADMIN")) {
            mGroups.setVisible(true);
            mAdmin.setVisible(true);
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
    }

    public void variousShortcut(KeyEvent ke) {
        KeyCode pressButton = ke.getCode();
        if (pressButton.equals(KeyCode.F1)) {

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
        } catch (IOException ex) {

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

    private void exitButtonRequest(ActionEvent event) {
        try {
            stage.close();
            FXMLLoader loader = new FXMLLoader(getClass().
                    getResource("/windowsapplication/view/LogIn_Window.fxml"));
            Parent root = (Parent) loader.load();
            LoginWindowController controller = loader.getController();
            controller.setStage(stage);
            controller.initStage(root);
        } catch (Exception ex) {
            LOGGER.warning("WindowsApplicationReto2: An error ocurred while "
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
        } catch (IOException ex) {

        }
    }

    private void uploadDocRequest(ActionEvent event) {
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
