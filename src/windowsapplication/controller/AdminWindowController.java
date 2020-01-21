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
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
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
 * @author Gaizka Andres
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

    private CategoryClientREST CatREST = new CategoryClientREST();

    private DocumentClientREST DocREST = new DocumentClientREST();

    private UserClientREST UserREST = new UserClientREST();

    void setCall(String call) {
        this.call = call;
    }

    void setStage(Stage stage) {
        this.stage = stage;
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
        colDocsAuthor.setCellValueFactory(new PropertyValueFactory<>("user"));
        colDocsCategory.setCellValueFactory(new PropertyValueFactory<>("category"));
        colDocsName.setCellValueFactory(new PropertyValueFactory<>("login"));
        colDocsId.setCellValueFactory(new PropertyValueFactory<>("id"));
        column1.setCellValueFactory(new PropertyValueFactory<>("id"));
        column2.setCellValueFactory(new PropertyValueFactory<>("name"));
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

            lbAuthor.setVisible(true);
            lbAuthor.setDisable(false);
            txtAuthor.setVisible(true);
            txtAuthor.setDisable(false);
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
        Category nCategory = new Category();
        nCategory.setName(txtAuthor.getText());
        CatREST.newCategory(nCategory);
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Category Sent");
        alert.setHeaderText("Registration completed.");
        Button okButton = (Button) alert.getDialogPane().lookupButton(ButtonType.OK);
        okButton.setId("okbutton");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {
            stage.close();
        } else {
            alert.close();
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
                categories = FXCollections.observableArrayList(CatREST.findCategoryByName(new GenericType<List<Category>>() {
                }, txtName.getText()));
            }
            tbCategories.setItems(categories);

        }
        if (call.equalsIgnoreCase("documents")) {
            DocREST.findDocumentNameByName(new GenericType<List<Document>>() {
            }, txtName.getText());

        }
    }

    private void closeRequest(WindowEvent event) {
        stage.close();

    }

    private void exitButtonRequest(ActionEvent event) {
        stage.close();

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
                infoDocWindowController.initStage(root);
            } catch (IOException ex) {
                Logger.getLogger(AdminWindowController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

}
