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
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javax.ws.rs.ForbiddenException;
import javax.ws.rs.InternalServerErrorException;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.core.GenericType;
import windowsapplication.beans.Admin;
import windowsapplication.beans.Category;
import windowsapplication.beans.Document;
import windowsapplication.beans.Free;
import windowsapplication.beans.Premium;
import windowsapplication.beans.User;
import windowsapplication.service.CategoryClientREST;
import windowsapplication.service.DocumentClientREST;
import windowsapplication.service.UserClientREST;

/**
 * Controller to the admin window
 *
 * @author Adrian Corral
 */
public class AdminWindowController {

    /**
     * Logger for class methods.
     */
    private static final Logger LOGGER = Logger.getLogger("windowsapplication.controller.AdminWindowController");
     /**
     * Button to close the window
     */
    @FXML
    private Button btBack;
    /**
     * Button to add a new category
     */
    @FXML
    private Button btAddCat;
    /**
     * Button to search data
     */
    @FXML
    private Button btSearch;
    /**
     * Label to put the info of a new category or new category name
     */
    @FXML
    private Label lbAuthor;
    /**
     * Text Field to put the new category or new category name
     */
    @FXML
    private TextField txtAuthor;
    /**
     * Text Field to put the name to search by
     */
    @FXML
    private TextField txtName;
    /**
     * Column who show the category´s id
     */
    @FXML
    private TableColumn column1;
    /**
     * Column who show the category´s name
     */
    @FXML
    private TableColumn column2;
    /**
     * Table of categories
     */
    @FXML
    private TableView tbCategories;
    /**
     * Table of users
     */
    @FXML
    private TableView tbUsers;
    /**
     * Column who show the user´s id
     */
    @FXML
    private TableColumn colUsersId;
    /**
     * Column who show the user´s name
     */
    @FXML
    private TableColumn colUsersName;
    /**
     * Column who show the user´s email
     */
    @FXML
    private TableColumn colUsersEmail;
    /**
     * Column who show the user´s full name
     */
    @FXML
    private TableColumn colUsersFullName;
    /**
     * Table of documents
     */
    @FXML
    private TableView tbDocs;
    /**
     * Column who show the document´s id
     */
    @FXML
    private TableColumn colDocsId;
    /**
     * Column who show the document´s name
     */
    @FXML
    private TableColumn colDocsName;
    /**
     * Column who show the document´s category
     */
    @FXML
    private TableColumn colDocsCategory;
    /**
     * Column who show the document´s total rating
     */
    @FXML
    private TableColumn colDocsAuthor;

    private Stage stage;

    private String call;

    private User user;

    private boolean edit;

    private String privilege;
    /**
     * Client Rest of Category
     */
    private CategoryClientREST CatREST = new CategoryClientREST();
    /**
     * Client Rest of Document
     */
    private DocumentClientREST DocREST = new DocumentClientREST();
    /**
     * Client Rest of User
     */
    private UserClientREST UserREST = new UserClientREST();

    private Premium premium;

    /**
     * Set the call to know how to load the window
     *
     * @param call From where the window call was made
     */
    void setCall(String call) {
        this.call = call;
    }

    /**
     * Sets the Stage object related to this controller.
     *
     * @param stage The Stage object to be initialized.
     */
    void setStage(Stage stage) {
        this.stage = stage;
    }

    /**
     * Set the user who is logged in the app
     *
     * @param user The user is logged
     */
    void setUser(User user) {
        this.user = user;
    }

    /**
     * Set the privilege of the user
     *
     * @param privilege Privilege of the user
     */
    void setPrivilege(String privilege) {
        this.privilege = privilege;
    }

    /**
     *
     * Put the premium if the user is premium
     *
     * @param premium
     */
    void setPremium(Premium premium) {
        this.premium = premium;
    }

    void initStage(Parent root) {
        try {
            Scene scene = new Scene(root);
            stage = new Stage();
            stage.setScene(scene);
            stage.setTitle("Administration");
            stage.setResizable(false);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setOnShowing(this::handleWindowShowing);
            stage.setOnCloseRequest(this::closeRequest);
            btBack.setOnAction(this::exitButtonRequest);
            btAddCat.setOnAction(this::newCategoryRequest);
            btSearch.setOnAction(this::searchRequest);
            tbDocs.getSelectionModel().selectedItemProperty().addListener(this::handleDocsTableSelection);
            colUsersEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
            colUsersFullName.setCellValueFactory(new PropertyValueFactory<>("fullName"));
            colUsersName.setCellValueFactory(new PropertyValueFactory<>("login"));
            colUsersId.setCellValueFactory(new PropertyValueFactory<>("id"));
            colDocsAuthor.setCellValueFactory(new PropertyValueFactory<>("totalRating"));
            colDocsCategory.setCellValueFactory(new PropertyValueFactory<>("category"));
            colDocsName.setCellValueFactory(new PropertyValueFactory<>("name"));
            colDocsId.setCellValueFactory(new PropertyValueFactory<>("id"));
            column1.setCellValueFactory(new PropertyValueFactory<>("id"));
            column2.setCellValueFactory(new PropertyValueFactory<>("name"));
            final ContextMenu cm = new ContextMenu();
            MenuItem cmItem1 = new MenuItem("Go back");
            MenuItem cmItem2 = new MenuItem("Delete");
            MenuItem cmItem3 = new MenuItem("Edit");
            cmItem1.setOnAction((ActionEvent e) -> {
                stage.close();
            });
            cmItem2.setOnAction((ActionEvent e) -> {

                if (call.equalsIgnoreCase("users")) {
                    User user = (User) tbUsers.getSelectionModel().getSelectedItem();
                    try {
                        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                        alert.setTitle("Confirmation");
                        alert.setHeaderText("Are you sure you want to delete the user?");
                        alert.setContentText("All the data will be erased...");
                        Button okbutton = (Button) alert.getDialogPane().lookupButton(ButtonType.OK);
                        okbutton.setId("okButton");
                        Optional<ButtonType> result = alert.showAndWait();
                        if (result.get() == ButtonType.OK) {
                            UserREST.deleteUser(user.getId());
                        } else {
                            e.consume();
                        }
                    } catch (InternalServerErrorException ex) {
                        LOGGER.warning("AdminWindowController: Error deleting user..." + ex.getMessage());
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("ERROR");
                        alert.setHeaderText("Sorry, an error occurred");
                        alert.setContentText("Try again later...");
                        Button errorButton = (Button) alert.getDialogPane().lookupButton(ButtonType.OK);
                        errorButton.setId("okButton");
                        alert.showAndWait();
                    } catch (Exception ex) {
                        LOGGER.warning("AdminWindowController: Error deleting user..." + ex.getMessage());
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("ERROR");
                        alert.setHeaderText("Sorry, an error occurred");
                        alert.setContentText("Try again later...");
                        Button errorButton = (Button) alert.getDialogPane().lookupButton(ButtonType.OK);
                        errorButton.setId("okButton");
                        alert.showAndWait();
                    }
                }

                if (call.equalsIgnoreCase("categories")) {
                    Category category = (Category) tbCategories.getSelectionModel().getSelectedItem();
                    try {
                        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                        alert.setTitle("Confirmation");
                        alert.setHeaderText("Are you sure you want to delete the category?");
                        alert.setContentText("All the data will be erased...");
                        Button okbutton = (Button) alert.getDialogPane().lookupButton(ButtonType.OK);
                        okbutton.setId("okButton");
                        Optional<ButtonType> result = alert.showAndWait();
                        if (result.get() == ButtonType.OK) {
                            CatREST.deleteCategory(category.getId());
                        } else {
                            e.consume();
                        }
                    } catch (InternalServerErrorException ex) {
                        LOGGER.warning("AdminWindowController: Error deleting category..." + ex.getMessage());
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("ERROR");
                        alert.setHeaderText("Sorry, an error occurred");
                        alert.setContentText("Try again later...");
                        Button errorButton = (Button) alert.getDialogPane().lookupButton(ButtonType.OK);
                        errorButton.setId("okButton");
                        alert.showAndWait();
                    } catch (Exception ex) {
                        LOGGER.warning("AdminWindowController: Error deleting category..." + ex.getMessage());
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("ERROR");
                        alert.setHeaderText("Sorry, an error occurred");
                        alert.setContentText("Try again later...");
                        Button errorButton = (Button) alert.getDialogPane().lookupButton(ButtonType.OK);
                        errorButton.setId("okButton");
                        alert.showAndWait();
                    }
                }
            });
            cmItem3.setOnAction((ActionEvent e) -> {
                if (call.equalsIgnoreCase("categories")) {
                    lbAuthor.setText("New name: ");
                    txtAuthor.setPromptText("New name");
                    btAddCat.setText("Change");
                    edit = true;
                }
            }
            );
            cm.getItems().addAll(cmItem1, cmItem3, cmItem2);
            tbCategories.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent e) -> {
                if (e.getButton() == MouseButton.SECONDARY) {
                    cm.show(stage, e.getScreenX(), e.getScreenY());
                }
            }
            );
            tbUsers.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent e) -> {
                if (e.getButton() == MouseButton.SECONDARY) {
                    cm.show(stage, e.getScreenX(), e.getScreenY());
                }
            }
            );
            stage.show();
        } catch (Exception ex) {
            LOGGER.warning("AdminWindowCOntroller: " + ex.getMessage());
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("ERROR");
            alert.setHeaderText("Sorry, an error occurred while initializing the window.");
            alert.setContentText("Try again later...");
            Button errorButton = (Button) alert.getDialogPane().lookupButton(ButtonType.OK);
            errorButton.setId("okButton");
            alert.showAndWait();
        }

        }
    

    /**
     * Initializes window state. Check the call and load the window depending on
     * the answer
     *
     * @param event The window event
     */
    private void handleWindowShowing(WindowEvent event) {
        try {
            if (call.equalsIgnoreCase("users")) {

                lbAuthor.setVisible(false);
                lbAuthor.setDisable(true);
                txtAuthor.setVisible(false);
                txtAuthor.setDisable(true);
                btAddCat.setVisible(false);
                btAddCat.setDisable(true);
                txtName.setPromptText("Name of the user");
                tbCategories.setVisible(false);
                tbCategories.setDisable(true);
                tbDocs.setVisible(false);
                tbDocs.setDisable(true);

            }
            if (call.equalsIgnoreCase("categories")) {

                lbAuthor.setVisible(true);
                lbAuthor.setDisable(false);
                txtAuthor.setVisible(true);
                txtAuthor.setDisable(false);
                btAddCat.setVisible(true);
                btAddCat.setDisable(false);
                lbAuthor.setText("New category: ");
                txtAuthor.setPromptText("New category name");
                txtName.setPromptText("Name of the category");
                tbUsers.setVisible(false);
                tbUsers.setDisable(true);
                tbDocs.setVisible(false);
                tbDocs.setDisable(true);

            }
            if (call.equalsIgnoreCase("documents")) {

                txtAuthor.setVisible(false);
                txtAuthor.setDisable(true);
                lbAuthor.setVisible(false);
                lbAuthor.setDisable(true);
                btAddCat.setVisible(false);
                btAddCat.setDisable(true);
                txtName.setPromptText("Name of the document");
                tbCategories.setVisible(false);
                tbCategories.setDisable(true);
                tbUsers.setVisible(false);
                tbUsers.setDisable(true);
            }
        } catch (Exception ex) {
            LOGGER.warning("AdminWindowCOntroller: " + ex.getMessage());
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("ERROR");
            alert.setHeaderText("Sorry, an error occurred while initializing the window.");
            alert.setContentText("Try again later...");
            Button errorButton = (Button) alert.getDialogPane().lookupButton(ButtonType.OK);
            errorButton.setId("okButton");
            alert.showAndWait();
        }

    }

    /**
     * Action of the add Category button
     *
     * @param event
     */
    private void newCategoryRequest(ActionEvent event) {
        try {
            if (edit) {
                Category ncategory = (Category) tbCategories.getSelectionModel().getSelectedItem();
                ncategory.setName(txtAuthor.getText());
                LOGGER.info("Searching category...");
                CatREST.modifyCategory(ncategory);

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Category Sent");
                alert.setHeaderText("Name changed.");
                Button okButton = (Button) alert.getDialogPane().lookupButton(ButtonType.OK);
                okButton.setId("okbutton");
                Optional<ButtonType> result = alert.showAndWait();
                if (result.get() == ButtonType.OK) {
                    alert.close();
                    ObservableList<Category> categories;
                    categories = FXCollections.observableArrayList(CatREST.findAllCategories(new GenericType<List<Category>>() {
                    }));
                    categories.stream().forEach(category -> category.getName());
                    tbCategories.setItems(categories);
                } else {
                    alert.close();
                }
            } else {
                ObservableList<Category> categories;
                categories = FXCollections.observableArrayList(CatREST.findAllCategories(new GenericType<List<Category>>() {
                }));
                categories.stream().forEach(category -> category.getName());
                Category nCategory = new Category();
                nCategory.setName(txtAuthor.getText().trim());
                CatREST.newCategory(nCategory);

                tbCategories.setItems(categories);
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Category Sent");
                alert.setHeaderText("Registration completed.");
                Button okButton = (Button) alert.getDialogPane().lookupButton(ButtonType.OK);
                okButton.setId("okbutton");
                Optional<ButtonType> result = alert.showAndWait();
                if (result.get() == ButtonType.OK) {
                    alert.close();
                    categories = FXCollections.observableArrayList(CatREST.findAllCategories(new GenericType<List<Category>>() {
                    }));
                    categories.stream().forEach(category -> category.getName());
                    tbCategories.setItems(categories);
                    txtAuthor.setText(" ");
                } else {
                    alert.close();
                }
            }
        } catch (ForbiddenException ex) {
            LOGGER.warning("AdminWindowCOntroller: " + ex.getMessage());
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("ERROR");
            alert.setHeaderText("The name category already exists!");
            alert.setContentText("Try again with a different one...");
            Button errorButton = (Button) alert.getDialogPane().lookupButton(ButtonType.OK);
            errorButton.setId("okButton");
            alert.showAndWait();
        } catch (InternalServerErrorException ex) {
            LOGGER.warning("AdminWindowCOntroller: " + ex.getMessage());
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("ERROR");
            alert.setHeaderText("Sorry, an error occurred.");
            alert.setContentText("Try again later...");
            Button errorButton = (Button) alert.getDialogPane().lookupButton(ButtonType.OK);
            errorButton.setId("okButton");
            alert.showAndWait();
        } catch (Exception ex) {
            LOGGER.warning("AdminWindowCOntroller: " + ex.getMessage());
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("ERROR");
            alert.setHeaderText("Sorry, an error occurred.");
            alert.setContentText("Try again later...");
            Button errorButton = (Button) alert.getDialogPane().lookupButton(ButtonType.OK);
            errorButton.setId("okButton");
            alert.showAndWait();
        }

    }

    /**
     * Action when the Search button is pressed
     *
     * @param event
     */
    private void searchRequest(ActionEvent event) {
        try {
            if (call.equalsIgnoreCase("users")) {
                ObservableList<User> users;
                ObservableList<Free> frees;
                ObservableList<Admin> admins;
                ObservableList<Premium> premiums;
                if (txtName.getText().trim().isEmpty()) {
                    users = FXCollections.observableArrayList(UserREST.findAllUsers(new GenericType<List<User>>() {
                    }));
                    tbUsers.setItems(users);
                } else {
                    switch (privilege) {
                        case ("ADMIN"): {
                            admins = FXCollections.observableArrayList(UserREST.findUserByLogin(Admin.class,
                                    txtName.getText()));
                            tbUsers.setItems(admins);
                            break;

                        }
                        case ("PREMIUM"): {
                            premiums = FXCollections.observableArrayList(UserREST.findUserByLogin(Premium.class,
                                    txtName.getText()));
                            tbUsers.setItems(premiums);
                            break;

                        }
                        default: {
                            frees = FXCollections.observableArrayList(UserREST.findUserByLogin(Free.class,
                                    txtName.getText()));
                            tbUsers.setItems(frees);
                        }
                    }
                }
            }
            if (call.equalsIgnoreCase("categories")) {
                ObservableList<Category> categories;
                if (txtName.getText().trim().isEmpty()) {
                    categories = FXCollections.observableArrayList(CatREST.findAllCategories(new GenericType<List<Category>>() {
                    }));

                } else {
                    categories = FXCollections.observableArrayList(CatREST.findCategoryByName(Category.class,
                            txtName.getText()));
                }
                tbCategories.setItems(categories);

            }
            if (call.equalsIgnoreCase("documents")) {
                ObservableList<Document> documents;
                if (txtName.getText().trim().isEmpty()) {
                    documents = FXCollections.observableArrayList(DocREST.findAllDocuments(new GenericType<List<Document>>() {
                    }));
                } else {
                    documents = FXCollections.observableArrayList(DocREST.findDocumentNamebyName(new GenericType<List<Document>>() {
                    }, txtName.getText()));
                }
                tbDocs.setItems(documents);

            }
        } catch (NotFoundException ex) {
            LOGGER.warning("AdminWindowCOntroller: " + ex.getMessage());
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("ERROR");
            alert.setHeaderText("Your search produced no result");
            alert.setContentText("Try again with a different value...");
            Button errorButton = (Button) alert.getDialogPane().lookupButton(ButtonType.OK);
            errorButton.setId("okButton");
            alert.showAndWait();
        } catch (InternalServerErrorException ex) {
            LOGGER.warning("AdminWindowCOntroller: " + ex.getMessage());
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("ERROR");
            alert.setHeaderText("Sorry, an error occurred.");
            alert.setContentText("Try again later...");
            Button errorButton = (Button) alert.getDialogPane().lookupButton(ButtonType.OK);
            errorButton.setId("okButton");
            alert.showAndWait();
        } catch (Exception ex) {
            LOGGER.warning("AdminWindowCOntroller: " + ex.getMessage());
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("ERROR");
            alert.setHeaderText("Sorry, an error occurred.");
            alert.setContentText("Try again later...");
            Button errorButton = (Button) alert.getDialogPane().lookupButton(ButtonType.OK);
            errorButton.setId("okButton");
            alert.showAndWait();
        }

    }

    /**
     * Action when the X button is pressed
     *
     * @param event
     */
    private void closeRequest(WindowEvent event) {
        stage.close();
    }

    /**
     * Action when exit button is pressed
     *
     * @param event
     */
    private void exitButtonRequest(ActionEvent event) {
        stage.close();
    }

    /**
     * Action when a cell of the Document table is pressed
     *
     * @param observable
     * @param oldValue
     * @param newValue
     */
    private void handleDocsTableSelection(ObservableValue observable,
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
                LOGGER.warning("AdminWindowCOntroller: " + ex.getMessage());
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("ERROR");
                alert.setHeaderText("Sorry, an error occurred.");
                alert.setContentText("Try again later...");
                Button errorButton = (Button) alert.getDialogPane().lookupButton(ButtonType.OK);
                errorButton.setId("okButton");
                alert.showAndWait();
            }
        }
    }

}
