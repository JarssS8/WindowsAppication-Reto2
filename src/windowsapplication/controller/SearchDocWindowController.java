/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package windowsapplication.controller;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
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
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.paint.Paint;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import windowsapplication.beans.Category;
import windowsapplication.beans.Document;
import windowsapplication.service.CategoryClientREST;
import windowsapplication.service.DocumentClientREST;

public class SearchDocWindowController {
    @FXML
    private Label lbParameter;
    @FXML
    private Button btSearch;  
    @FXML
    private TextField txtName;
    @FXML
    private DatePicker datePickerDoc;
    @FXML
    private ComboBox comboCategories;
    @FXML
    private Button btBack;
    
    private Stage stage;
    
    private DocumentClientREST DocREST;
    
    private CategoryClientREST CatREST;
    
    void setStage(Stage stage) {
        this.stage = stage;
    }

    void initStage(Parent root) {
       
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("Search a document");
        stage.setResizable(false);
        stage.setOnCloseRequest(this::closeRequest);
        btSearch.setOnAction(this::searchButtonRequest);
        btBack.setOnAction(this::backButtonRequest);
        stage.setOnShowing(this::handleWindowShowing);
        ObservableList<Category> cats = FXCollections.observableArrayList(CatREST.findAllCategories(Category.class));
        comboCategories.setItems(cats);
        stage.show();
        
    }
    private void handleWindowShowing(WindowEvent event){
     
       
    }
   
    
    private void searchButtonRequest(ActionEvent event){
        if(searchValidations()){
            DocREST.findDocumentNameByParameters(Document.class, txtName.getText(), comboCategories.getValue().toString(), datePickerDoc.getValue().toString());
            lbParameter.setTextFill(Paint.valueOf("BLACK"));
        }else{
            lbParameter.setTextFill(Paint.valueOf("RED"));
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("ERROR");
            alert.setHeaderText("Uploading failed");
            alert.setContentText("Check the validation tips");
            Button errorButton = (Button) alert.getDialogPane().lookupButton(ButtonType.OK);
            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.YES) {
                alert.close();
            }
        }
    }
    private boolean searchValidations(){
        Boolean todoOk = false,nameOk = false,catOk = false,dateOk = false;
        if(txtName.getLength()>1 && txtName.getLength()<50){
           nameOk=true;
        }
        if(comboCategories.getValue() != null){
           catOk=true;
        }
        if(datePickerDoc.getValue() != null){
            dateOk=true;
        }
        if(nameOk || catOk || dateOk){
           todoOk=true;
        }
        return todoOk;
    }
 
    private void closeRequest(WindowEvent event){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Close confirmation");
        alert.setHeaderText("You pressed the 'Close'. \n"
            + "Document search will be cancelled.");
        alert.setContentText("Are you sure?");
        alert.getButtonTypes().setAll(ButtonType.YES, ButtonType.NO);
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.YES) {
            try {
                stage = new Stage();
                FXMLLoader loader = new FXMLLoader(getClass().
                    getResource("/windowsapplication/view/Main.fxml"));
                Parent root = (Parent) loader.load();
                MainWindowController mainWindowController = loader.getController();
                mainWindowController.setStage(stage);
                mainWindowController.initStage(root);
            } catch (IOException ex) {
                
            }
        } else {
            event.consume();
        }
    }
    
    private void backButtonRequest(ActionEvent event){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setHeaderText("Close confirmation"); 
        alert.setContentText("Are you sure that want to stop searching?");
        alert.initOwner(stage);
        alert.initModality(Modality.WINDOW_MODAL);
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().
                    getResource("/windowsapplication/view/Main.fxml"));
                Parent root = (Parent) loader.load();
                MainWindowController mainWindowController = loader.getController();
                mainWindowController.setStage(stage);
                mainWindowController.initStage(root);
            } catch (IOException ex) {
              
            }
        }else {
            event.consume();
        }
    }
}
