/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package windowsapplication.controller;

import java.util.Optional;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import windowsapplication.beans.Premium;
import windowsapplication.beans.User;

/**
 *
 * @author Aimar Arrizabalaga
 */
public class CreditCardWindowController {
    
    private static final Logger LOGGER = Logger.getLogger("windowsapplication.controller.CreditCardWindowController");

    @FXML
    private TextField txtNumTarjeta;
    @FXML
    private TextField txtDateMonth;
    @FXML
    private TextField txtDateYear;
    @FXML
    private TextField txtCVC;
    @FXML
    private Label lbInfoData;
    @FXML
    private Button btSave;
    
    private Stage stage;
    
    private User user;
    
    public void setUser(User user) {
        this.user = user;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }
    
    void initStage(Parent root) {
        Scene scene = new Scene(root);
        stage = new Stage();
        stage.setScene(scene);
        stage.setTitle(/*user.getFullName() +*/" Profile");
        stage.setResizable(false);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setOnShowing(this::windowShowing);
        stage.setOnCloseRequest(this::closeRequest);
        btSave.setOnAction(this::saveRequest);
        stage.show();
    }

    private void windowShowing(WindowEvent event) {
        lbInfoData.setVisible(false);
    }
    
    private void saveRequest(ActionEvent event){
        if(txtNumTarjeta.getText().isEmpty() || txtDateMonth.getText().isEmpty() || txtDateYear.getText().isEmpty() || txtCVC.getText().isEmpty()){
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("ERROR");
            alert.setHeaderText("All the fields are required.");
            alert.setContentText("Please introduce all the fields");
            alert.getButtonTypes().setAll(ButtonType.YES, ButtonType.NO);
            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.YES) {
                lbInfoData.setVisible(true);
                event.consume();

            } else {
                event.consume();
            }
        }else{
            //Premium nPremium = new Premium(user.getId(),user.getEmail(),user.get);
            //nPremium.setCardNumber(new Long(txtNumTarjeta.getText().trim()));
            //nPremium.setExpirationMonth(Integer.parseInt(txtDateMonth.getText().trim()));
            //nPremium.setExpirationYear(Integer.parseInt(txtDateYear.getText().trim()));
            //nPremium.setCvc(Integer.parseInt(txtCVC.getText().trim()));
            //Llamar Metodo ModifyFreeToPremium
            
        }
    }
    private void closeRequest(WindowEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Close confirmation");
        alert.setHeaderText("You pressed the 'Close'. \n"
            + "The account update will be cancelled.");
        alert.setContentText("Are you sure?");
        alert.getButtonTypes().setAll(ButtonType.YES, ButtonType.NO);
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.YES) {
            stage.close();
            
        } else {
            event.consume();
        }
    }
}
