/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package windowsapplication.controller;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.Set;
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
import javax.ws.rs.core.GenericType;
import windowsapplication.beans.Category;
import windowsapplication.beans.Document;
import windowsapplication.beans.Premium;
import windowsapplication.beans.User;
import windowsapplication.service.CategoryClientREST;
import windowsapplication.service.DocumentClientREST;
import windowsapplication.service.UserClientREST;

/**
 *
 * @author Adrian Corral
 */
public class AdminWindowController {

    @FXML
    private Button btBack;
    @FXML
    private Button btAddCat;
    @FXML
    private Button btSearch;
    @FXML
    private Label lbAuthor;
    @FXML
    private TextField txtAuthor;
    @FXML
    private TextField txtName;
    @FXML
    private TableColumn column1;
    @FXML
    private TableColumn column2;
    @FXML
    private TableView tbCategories;
    @FXML
    private TableView tbUsers;
    @FXML
    private TableColumn colUsersId;
    @FXML
    private TableColumn colUsersName;
    @FXML
    private TableColumn colUsersEmail;
    @FXML
    private TableColumn colUsersFullName;
    @FXML
    private TableView tbDocs;
    @FXML
    private TableColumn colDocsId;
    @FXML
    private TableColumn colDocsName;
    @FXML
    private TableColumn colDocsCategory;
    @FXML
    private TableColumn colDocsAuthor;

    private Stage stage;

    private String call;

    private User user;

    private boolean edit;

    private CategoryClientREST CatREST = new CategoryClientREST();

    private DocumentClientREST DocREST = new DocumentClientREST();

    private UserClientREST UserREST = new UserClientREST();

    void setCall(String call) {
        this.call = call;
    }

    void setStage(Stage stage) {
        this.stage = stage;
    }

    void setUser(User user) {
        this.user = user;
    }

    void initStage(Parent root) {
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
        tbDocs.getSelectionModel().selectedItemProperty().addListener(this::handleUsersTableSelection);
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
                UserREST.deleteUser(user.getId());
            }
            if (call.equalsIgnoreCase("categories")) {
                Category category = (Category) tbCategories.getSelectionModel().getSelectedItem();
                CatREST.deleteCategory(category.getId());
            }
        });
        cmItem3.setOnAction((ActionEvent e) -> {
            if (call.equalsIgnoreCase("categories")) {
                lbAuthor.setText("New name: ");
                txtAuthor.setPromptText("New name");
                btAddCat.setText("Change");
                edit = true;
            }
        });
        cm.getItems().addAll(cmItem1, cmItem3, cmItem2);
        tbCategories.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent e) -> {
            if (e.getButton() == MouseButton.SECONDARY) {
                cm.show(stage, e.getScreenX(), e.getScreenY());
            }
        });
        tbUsers.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent e) -> {
            if (e.getButton() == MouseButton.SECONDARY) {
                cm.show(stage, e.getScreenX(), e.getScreenY());
            }
        });
        stage.show();
    }

    private void handleWindowShowing(WindowEvent event) {

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
    }

    private void newCategoryRequest(ActionEvent event) {
        if (edit) {
            Category ncategory = (Category) tbCategories.getSelectionModel().getSelectedItem();
            ncategory.setName(txtAuthor.getText());
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
            nCategory.setName(txtAuthor.getText());
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
            } else {
                alert.close();
            }
        }

    }

    private void searchRequest(ActionEvent event) {
        if (call.equalsIgnoreCase("users")) {
            ObservableList<User> users;
            if (txtName.getText().trim().isEmpty()) {
                users = FXCollections.observableArrayList(UserREST.findAllUsers(new GenericType<List<User>>() {
                }));
            } else {
                users = FXCollections.observableArrayList(UserREST.findUserByLogin(Premium.class, txtName.getText()));
            }

            tbUsers.setItems(users);

        }
        if (call.equalsIgnoreCase("categories")) {
            ObservableList<Category> categories;
            if (txtName.getText().trim().isEmpty()) {
                categories = FXCollections.observableArrayList(CatREST.findAllCategories(new GenericType<List<Category>>() {
                }));

            } else {
                categories = FXCollections.observableArrayList(CatREST.findCategoryByName(Category.class, txtName.getText()));
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
    }

    private void closeRequest(WindowEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(
                    "/windowsapplication/view/Main.fxml"));
            Parent root = (Parent) loader.load();
            MainWindowController controller = loader.getController();
            controller.setStage(stage);
            controller.initStage(root);
        } catch (IOException ex) {
            Logger.getLogger(AdminWindowController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    private void exitButtonRequest(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(
                    "/windowsapplication/view/Main.fxml"));
            Parent root = (Parent) loader.load();
            MainWindowController controller = loader.getController();
            controller.setStage(stage);
            controller.initStage(root);
        } catch (IOException ex) {
            Logger.getLogger(AdminWindowController.class.getName()).log(Level.SEVERE, null, ex);
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
            } catch (IOException ex) {
                Logger.getLogger(AdminWindowController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

}
