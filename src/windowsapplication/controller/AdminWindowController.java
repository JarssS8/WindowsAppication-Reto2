/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package windowsapplication.controller;

import java.util.List;
import java.util.Set;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
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
    private TableColumn column3;
    @FXML
    private TableColumn column4;
    @FXML
    private TableView tbData;

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
        column1.setCellValueFactory(new PropertyValueFactory<>("ID"));
        column2.setCellValueFactory(new PropertyValueFactory<>("Name"));
        column3.setCellValueFactory(new PropertyValueFactory<>("Author"));
        column4.setCellValueFactory(new PropertyValueFactory<>("Upload Date"));
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
            column1.setText("ID");
            column2.setText("Login");
            column3.setText("Full Name");
            column4.setText("Email");
          
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
            column1.setText("ID");
            column1.setPrefWidth(290);
            column2.setText("Name");
            column2.setPrefWidth(290);
            column3.setVisible(false);
            column4.setVisible(false);
            
        }
        if (call.equalsIgnoreCase("documents")) {

            lbAuthor.setVisible(true);
            lbAuthor.setDisable(false);
            txtAuthor.setVisible(true);
            txtAuthor.setDisable(false);
            btAddCat.setVisible(false);
            btAddCat.setDisable(true);
            txtName.setPromptText("Name of the document");
            column1.setText("ID");
            column2.setText("Name");
            column3.setText("Author");
            column4.setText("Upload Date");
            

        }
    }
    
    private void newCategoryRequest(ActionEvent event){
        Category nCategory = new Category();
        nCategory.setName(txtAuthor.getText());
        CatREST.newCategory(nCategory);
    }
    private void searchRequest(ActionEvent event) {
        if (call.equalsIgnoreCase("users")) {
            
            tbData.getItems().add(UserREST.findUserByLogin(User.class, txtName.getText()));
            
        }if (call.equalsIgnoreCase("categories")) {
            List<Category> categories = CatREST.findCategoryByName(new GenericType<List<Category>>() {}, txtName.getText()); 
            categories.stream().forEach(category-> tbData.getItems().add(category));
            
        }if (call.equalsIgnoreCase("documents")) {
            DocREST.findDocumentNameByName(new GenericType<List<Document>>() {}, txtName.getText());
            
        }
    }

    private void closeRequest(WindowEvent event) {
        stage.close();
        
    }

    private void exitButtonRequest(ActionEvent event) {
        stage.close();
        
    }

}
