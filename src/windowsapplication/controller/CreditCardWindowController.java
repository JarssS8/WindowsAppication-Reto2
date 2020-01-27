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
import windowsapplication.beans.User;
import windowsapplication.service.UserClientREST;

/**
 * Controller UI class for payment method view in WindowsApplication-Reto2
 * application. It contains event handlers and initialization code for the view
 * defined in MetodoDePago.fxml file.
 *
 * @author Aimar Arrizabalaga
 */
public class CreditCardWindowController {

    private static final Logger LOGGER = Logger.getLogger("windowsapplication.controller.CreditCardWindowController");

    /**
     * User's new credit card number UI text field.
     */
    @FXML
    private TextField txtNumTarjeta;
    /**
     * User's credit card's expiration month UI text field.
     */
    @FXML
    private TextField txtDateMonth;
    /**
     * User's credit card's expiration year UI text field.
     */
    @FXML
    private TextField txtDateYear;
    /**
     * User's credit card's cvc UI text field.
     */
    @FXML
    private TextField txtCVC;
    /**
     * User's autorenovation option UI checkbox.
     */
    @FXML
    private CheckBox chckAutorenovation;
    /**
     * A UI label user to show the user that all fields are qeruired.
     */
    @FXML
    private Label lbInfoData;
    /**
     * Button to fire save request at the UI.
     */
    @FXML
    private Button btSave;
    /**
     * The Stage object.
     */
    private Stage stage;
    /**
     * The User object.
     */
    private User user = null;
    /**
     * The Premium object.
     */
    private Premium premium = null;
    /**
     * A string with the user's privilege.
     */
    private String privilege;
    /**
     * The UserClientREST object to send requests to the server.
     */
    private UserClientREST client = new UserClientREST();

    /**
     * Sets the user for the controller.
     *
     * @param user The User object.
     */
    public void setUser(User user) {
        this.user = user;
    }

    /**
     * Sets the premium user for the controller.
     *
     * @param user The Premium object.
     */
    public void setPremium(Premium premium) {
        this.premium = premium;
    }

    /**
     * Sets the user's privilege for the controller.
     *
     * @param privilege The string that contains the privilege of the current
     * user.
     */
    public void setPrivilege(String privilege) {
        this.privilege = privilege;
    }

    /**
     * Sets the stage for the controller.
     *
     * @param stage The Stage object.
     */
    public void setStage(Stage stage) {
        this.stage = stage;
    }

    /**
     * Initializes CreditCardWindowController stage.
     *
     * @param root The Parent object.
     */
    void initStage(Parent root) {
        Scene scene = new Scene(root);
        stage = new Stage();
        stage.setScene(scene);
        if (privilege.equals("PREMIUM")) {
            stage.setTitle(premium.getFullName() + " - Payment Data");
        } else {
            stage.setTitle(user.getFullName() + " - Payment Data");
        }
        stage.setResizable(false);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setOnShowing(this::windowShowing);
        stage.setOnCloseRequest(this::closeRequest);
        btSave.setOnAction(this::saveRequest);
        stage.show();
    }

    /**
     * Initializes the window state when it's shown.
     *
     * @param event The window event.
     */
    private void windowShowing(WindowEvent event) {
        lbInfoData.setVisible(false);
        if (privilege.equals("PREMIUM")) {
            txtNumTarjeta.setText(premium.getCardNumber().toString());
            txtDateMonth.setText(Integer.toString(premium.getExpirationMonth()));
            txtDateYear.setText(Integer.toString(premium.getExpirationYear()));
            txtCVC.setText(Integer.toString(premium.getCvc()));
            chckAutorenovation.setVisible(true);
        } else {
            txtNumTarjeta.setText("");
            txtDateMonth.setText("");
            txtDateYear.setText("");
            txtCVC.setText("");
            chckAutorenovation.setVisible(false);
        }
    }

    /**
     * Validates the data typed by the user and sends an update request to the
     * server.
     *
     * @param event The window event.
     */
    private void saveRequest(ActionEvent event) {
        try {
            if (txtNumTarjeta.getText().isEmpty()
                    || txtDateMonth.getText().isEmpty()
                    || txtDateYear.getText().isEmpty()
                    || txtCVC.getText().isEmpty()) {
                LOGGER.warning("CreditCardWindowController: All fields are required...");
                lbInfoData.setVisible(true);
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("ERROR");
                alert.setHeaderText("All the fields are required.");
                alert.setContentText("Please introduce all the fields");
                alert.showAndWait();
            } else {
                if (privilege.equals("FREE")) {
                    // Updating from FREE to PREMIUM.
                    premium = new Premium(user);
                    premium.setAutorenovation(true);
                    premium.setBeginSub(Timestamp.valueOf(LocalDateTime.now()));
                    premium.setCardNumber(Long.parseLong(txtNumTarjeta.getText()));
                    premium.setExpirationMonth(Integer.parseInt(txtDateMonth.getText()));
                    premium.setExpirationYear(Integer.parseInt(txtDateYear.getText()));
                    premium.setCvc(Integer.parseInt(txtCVC.getText()));
                    LOGGER.info("CreditCardWindowController: Updating user "
                            + "from FREE to PREMIUM...");
                    client.modifyFreeToPremium(premium);
                    // Alert to let the user see that the operation's correct.
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("OK");
                    alert.setHeaderText("Thanks for subscribing!");
                    alert.setContentText("We hope you enjoy your "
                            + "PREMIUM experience!");
                    alert.showAndWait();
                    stage.close();
                } else {
                    //Updating Premium's payment data
                    premium.setCardNumber(Long.parseLong(txtNumTarjeta.getText()));
                    premium.setCvc(Integer.parseInt(txtCVC.getText()));
                    premium.setExpirationMonth(Integer.parseInt(txtDateMonth.getText()));
                    premium.setExpirationYear(Integer.parseInt(txtDateYear.getText()));
                    client.savePaymentMethod(premium);
                    // Alert to let the user see that the operation's correct.
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("OK");
                    alert.setHeaderText("You're payment data has been "
                            + "successfully saved!");
                    alert.showAndWait();
                    stage.close();
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            LOGGER.warning("CreditCardWindowController: Error saving "
                    + "payment data..." + ex.getMessage());
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("ERROR");
            alert.setHeaderText("Sorry, an unexpected error ocurred");
            alert.setContentText("Try again later...");
            alert.showAndWait();
        }
    }

    /**
     * Closes the stage.
     *
     * @param event
     */
    private void closeRequest(WindowEvent event) {
        try {
            stage.close();
        } catch (Exception ex) {
            LOGGER.warning("CreditCardWindowController: " + ex.getMessage());
        }

    }
}
