/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package windowsapplication.controller;

import java.io.IOException;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import static windowsapplication.beans.Privilege.FREE;
import windowsapplication.beans.User;

/**
 *
 * @author Aimar Arrizabalaga
 */
public class ProfileWindowController {

    @FXML
    private TextField txtNewUsername;
    @FXML
    private TextField txtNewEmail;
    @FXML
    private TextField txtNewFullname;
    @FXML
    private TextField txtNewPassword;
    @FXML
    private TextField txtNewPasswordRepeat;
    @FXML
    private TextField txtLastPassword;
    @FXML
    private Label lbUsername;
    @FXML
    private Label lbEmail;
    @FXML
    private Label lbFullName;
    @FXML
    private Label lbPrivilege;
    @FXML
    private Button btPremium;
    @FXML
    private Button btEdit;
    @FXML
    private Button btBack;

    private Stage stage;

    private User user;

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void setUser(User user) {
        this.user = user;
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
        btBack.setOnAction(this::backButtonRequest);
        btEdit.setOnAction(this::editRequest);
        btPremium.setOnAction(this::premiumRequest);
        stage.show();
    }

    private void windowShowing(WindowEvent event) {

        txtNewUsername.setVisible(false);
        txtNewEmail.setVisible(false);
        txtNewFullname.setVisible(false);
        txtNewPassword.setDisable(true);
        txtNewPasswordRepeat.setDisable(true);
        txtLastPassword.setDisable(true);
        /*if(user.getPrivilege().equals(FREE)){
            btPremium.setVisible(true);
        }else{
            btPremium.setVisible(false);
        }
        lbUsername.setText(user.getLogin());
        lbFullName.setText(user.getFullName());
        lbEmail.setText(user.getEmail());
        lbPrivilege.setText(user.getPrivilege().toString());*/
    }

    private void editRequest(ActionEvent event) {
        txtNewUsername.setVisible(true);
        txtNewEmail.setVisible(true);
        txtNewFullname.setVisible(true);
        txtNewPassword.setDisable(false);
        txtNewPasswordRepeat.setDisable(false);
        txtLastPassword.setDisable(false);
    }

    private void premiumRequest(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().
                getResource("/windowsapplication/view/MetodoDePago.fxml"));
            Parent root = (Parent) loader.load();
            CreditCardWindowController creditCardWindowController = loader.getController();
            creditCardWindowController.setStage(stage);
            creditCardWindowController.initStage(root);
        } catch (IOException ex) {

        }
    }

    private void closeRequest(WindowEvent event) {
        stage.close();

    }

    private void backButtonRequest(ActionEvent event) {
        stage.close();
    }
}
