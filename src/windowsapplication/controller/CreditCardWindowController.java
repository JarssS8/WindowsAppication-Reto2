/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package windowsapplication.controller;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import windowsapplication.beans.Premium;
import windowsapplication.beans.Privilege;
import windowsapplication.beans.User;
import windowsapplication.service.UserClientREST;

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
    private CheckBox chckAutorenovation;
    @FXML
    private Label lbInfoData;
    @FXML
    private Button btSave;

    private Stage stage;

    private User user = null;

    private Premium premium = null;

    private UserClientREST client = new UserClientREST();

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
        stage.setTitle(user.getFullName() + "- Payment data");
        stage.setResizable(false);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setOnShowing(this::windowShowing);
        stage.setOnCloseRequest(this::closeRequest);
        btSave.setOnAction(this::saveRequest);
        stage.show();
    }

    private void windowShowing(WindowEvent event) {
        lbInfoData.setVisible(false);
        premium = new Premium(user);
        txtNumTarjeta.setText(premium.getCardNumber().toString());
        txtDateMonth.setText(Integer.toString(premium.getExpirationMonth()));
        txtDateYear.setText(Integer.toString(premium.getExpirationMonth()));
        txtCVC.setText(Integer.toString(premium.getCvc()));
        if (user.getPrivilege().equals(Privilege.PREMIUM)) {
            chckAutorenovation.setVisible(true);
        } else {
            chckAutorenovation.setVisible(false);
        }
    }

    private void saveRequest(ActionEvent event) {
        try {
            if (txtNumTarjeta.getText().isEmpty()
                    || txtDateMonth.getText().isEmpty()
                    || txtDateYear.getText().isEmpty()
                    || txtCVC.getText().isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("ERROR");
                alert.setHeaderText("All the fields are required.");
                alert.setContentText("Please introduce all the fields");
                alert.showAndWait();
            } else {
                if (user.getPrivilege().equals(Privilege.FREE)) {
                    premium.setAutorenovation(true);
                    premium.setBeginSub(Timestamp.valueOf(LocalDateTime.now()));
                    premium.setCardNumber(Long.getLong(txtNumTarjeta.getText()));
                    premium.setExpirationMonth(Integer.parseInt(txtDateMonth.getText()));
                    premium.setExpirationYear(Integer.parseInt(txtDateYear.getText()));
                    premium.setCvc(Integer.parseInt(txtCVC.getText()));
                    client.modifyFreeToPremium(premium);

                } else {
                    premium.setCardNumber(Long.parseLong(txtNumTarjeta.getText()));
                    premium.setCvc(Integer.parseInt(txtCVC.getText()));
                    premium.setExpirationMonth(Integer.parseInt(txtDateMonth.getText()));
                    premium.setExpirationYear(Integer.parseInt(txtDateYear.getText()));
                    client.savePaymentMethod(premium);
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("OK");
                    alert.setHeaderText("You're payment data has been successfully saved!");
                    alert.showAndWait();
                }

            }
        } catch (Exception ex) {
            LOGGER.warning("CreditCardWindowController: Error saving payment data..." + ex.getMessage());
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
