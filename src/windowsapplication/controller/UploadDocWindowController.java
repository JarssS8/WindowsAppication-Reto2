/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package windowsapplication.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.paint.Paint;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javax.ws.rs.core.GenericType;
import windowsapplication.beans.Category;
import windowsapplication.beans.Document;
import windowsapplication.beans.User;
import windowsapplication.service.CategoryClientREST;
import windowsapplication.service.DocumentClientREST;

/**
 *
 * @author Gaizka Andres
 */
public class UploadDocWindowController {
    @FXML
    private Label lbInfoFields;
    @FXML
    private Label lbInfoName;
    @FXML
    private ComboBox comboCategories;
    @FXML
    private Label lbInfoFile;
    @FXML
    private TextField txtNameDoc;
    @FXML
    private Button btSelectFile;
    @FXML
    private Button btUpload;
    @FXML
    private Button btBack;
   
    private Stage stage;
    
    private boolean fileSelect = false;
    
    private File selectedFile;
    
    private DocumentClientREST docREST = new DocumentClientREST();
    
    private CategoryClientREST catREST = new CategoryClientREST();
    
    private byte[] file;
    
    private User user;
    
    void setUser(User user){
        this.user=user;
    }
    
    void setStage(Stage stage) {
        this.stage = stage;
    }

    void initStage(Parent root) {
        Scene scene = new Scene(root);
        stage = new Stage();
        stage.setScene(scene);
        stage.setTitle("Upload a document");
        stage.setResizable(false);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setOnShowing(this::handleWindowShowing);
        stage.setOnCloseRequest(this::closeRequest);
        btBack.setOnAction(this::backButtonRequest);
        btUpload.setOnAction(this::uploadButtonRequest);
        btSelectFile.setOnAction(this::selectFileRequest);
        stage.show();
    }
    private void handleWindowShowing(WindowEvent event){
        comboCategories.getItems().addAll(catREST.findAllCategories(new GenericType<List<Category>>() {})); 
    }
    
    private void uploadButtonRequest(ActionEvent event){
        Boolean validation=checkValidations();
        if(validation){
            Document nDocu= new Document();
            
            nDocu.setName(txtNameDoc.getText()); 
            Category ncategory= (Category) comboCategories.getSelectionModel().getSelectedItem();
            nDocu.setCategory(catREST.findCategoryByName(Category.class,ncategory.getName()));
            nDocu.setFile(file);
            nDocu.setRatingCount(Integer.valueOf(String.valueOf(user.getId())));
            nDocu.setTotalRating(0);
            nDocu.setUploadDate(Date.valueOf(LocalDate.now()));
            docREST.newDocument(nDocu);
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("User Sent");
            alert.setHeaderText("Registration completed.");
            Button okButton = (Button) alert.getDialogPane().lookupButton(ButtonType.OK);
            okButton.setId("okbutton");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK) {
                stage.close();
            }else{
                alert.close();
            }
        }else{
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("ERROR");
            alert.setHeaderText("Uploading failed");
            alert.setContentText("All the fields are required");
            Button errorButton = (Button) alert.getDialogPane().lookupButton(ButtonType.OK);
            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.YES) {
                alert.close();
            }
        }
    }
    
    private boolean checkValidations(){
       Boolean todoOk=false, nameOk=false,catOk=false;
      

       if(txtNameDoc.getLength()>1 && txtNameDoc.getLength()<50){
           nameOk=true;
           lbInfoName.setTextFill(Paint.valueOf("BLACK"));
       }else{
           lbInfoName.setTextFill(Paint.valueOf("RED"));
       }
       if(comboCategories.getValue() != null){
           catOk=true;
       }
       
       if(nameOk && catOk && fileSelect){
           todoOk=true;
           lbInfoFields.setTextFill(Paint.valueOf("BLACK"));
       }else{
           lbInfoFields.setTextFill(Paint.valueOf("RED"));
       }
       return todoOk;
    }
    
    private void selectFileRequest(ActionEvent event){
        
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("PDF files (*.pdf)", "*.pdf");
        fileChooser.getExtensionFilters().add(extFilter);
        selectedFile = fileChooser.showOpenDialog(null);
        

        if (selectedFile != null) {
        lbInfoFile.setText("File selected: " + selectedFile.getName());
        file = readFileToByteArray(selectedFile);
        fileSelect = true;
        }else {
            lbInfoFile.setText("File selection cancelled.");
        }
    }
     private static byte[] readFileToByteArray(File file){
        FileInputStream fis = null;
        byte[] bArray = new byte[(int) file.length()];
        try{
            fis = new FileInputStream(file);
            fis.read(bArray);
            fis.close();        
        }catch(IOException ioExp){
            ioExp.printStackTrace();
        }
        return bArray;
    }
    
    
     private void closeRequest(WindowEvent event){  
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Close confirmation");
        alert.setHeaderText("You pressed the 'Close'. \n"
            + "Document upload will be cancelled.");
        alert.setContentText("Are you sure?");
        alert.getButtonTypes().setAll(ButtonType.YES, ButtonType.NO);
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.YES) {
            stage.close();
        } else {
            event.consume();
        }
    }
     
    private void backButtonRequest(ActionEvent event){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setHeaderText("Close confirmation"); 
        alert.setContentText("Are you sure that want to cancel the upload?");
        alert.initOwner(stage);
        alert.initModality(Modality.WINDOW_MODAL);
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {
            stage.close();
        }else {
            event.consume();
        }
    }
    
}
