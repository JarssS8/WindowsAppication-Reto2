/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package windowsapplication.controller;

import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import utilities.util.Util;
import windowsapplication.beans.Category;
import windowsapplication.beans.Premium;
import windowsapplication.beans.User;
import windowsapplication.service.UserClientREST;
import windowsapplication.utilities.Encryptation;

/**
 * Controller UI class for Profile view in WindowsApplication-Reto2 application.
 * It contains event handlers and initialization code for the view defined in
 * Perfil.fxml file.
 *
 * @author Aimar Arrizabalaga
 */
public class ProfileWindowController {

    private static final Logger LOGGER = Logger.getLogger("windowsapplication.controller.ProfileWindowController");
    /**
     * User's new mail UI text field.
     */
    @FXML
    private TextField txtNewEmail;
    /**
     * User's new full name UI text field.
     */
    @FXML
    private TextField txtNewFullname;
    /**
     * User's new password UI text field.
     */
    @FXML
    private TextField txtNewPassword;
    /**
     * User's new password repeat UI text field.
     */
    @FXML
    private TextField txtNewPasswordRepeat;
    /**
     * User's current password UI text field.
     */
    @FXML
    private TextField txtCurrentPassword;
    /**
     * User's user login name UI label.
     */
    @FXML
    private Label lbUsername;
    /**
     * User's email UI label.
     */
    @FXML
    private Label lbEmail;
    /**
     * User's full name UI label.
     */
    @FXML
    private Label lbFullName;
    /**
     * User's privilege UI label.
     */
    @FXML
    private Label lbPrivilege;
    /**
     * Button to fire premium request at the UI.
     */
    @FXML
    private Button btPremium;
    /**
     * Button to fire edit request at the UI.
     */
    @FXML
    private Button btEdit;
    /**
     * Button to fire back button request at the UI.
     */
    @FXML
    private Button btBack;
    /**
     * Button to fire save request at the UI.
     */
    @FXML
    private Button btSave;
    /**
     * The stage object.
     */
    private Stage stage;
    /**
     * The user object.
     */
    private User user;
    /**
     * The premium object.
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
     * Sets the stage for the controller.
     *
     * @param stage The Stage object.
     */
    public void setStage(Stage stage) {
        this.stage = stage;
    }

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
     * @param premium The premium object.
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
     * Initializes ProfileWindowController stage.
     *
     * @param root The Parent object.
     */
    public void initStage(Parent root) {
        try {
            Scene scene = new Scene(root);
            stage = new Stage();
            stage.setScene(scene);
            // Checking the privilege of the user.
            if (privilege.equals("PREMIUM")) {
                stage.setTitle(premium.getFullName() + " - Profile");
            } else {
                stage.setTitle(user.getFullName() + " - Profile");
            }
            stage.setResizable(false);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setOnShowing(this::windowShowing);
            stage.setOnCloseRequest(this::closeRequest);
            btBack.setOnAction(this::backButtonRequest);
            btEdit.setOnAction(this::editRequest);
            btSave.setOnAction(this::saveNewDataRequest);
            btPremium.setOnAction(this::premiumRequest);
            stage.show();

            final ContextMenu cm = new ContextMenu();
            MenuItem cmItem1 = new MenuItem("Go back");
            MenuItem cmItem2 = new MenuItem("Edit");
            MenuItem cmItem3 = new MenuItem("Delete");
            cmItem1.setOnAction((ActionEvent e) -> {
                stage.close();
            });

            cmItem2.setOnAction((ActionEvent e) -> {
                editRequest(e);
            });
            cmItem3.setOnAction((ActionEvent e) -> {
                if (privilege.equals("PREMIUM")) {
                    client.deleteUser(premium.getId());
                } else {
                    client.deleteUser(user.getId());
                }
            });

            cm.getItems().addAll(cmItem1, cmItem2, cmItem3);
            stage.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent e) -> {
                if (e.getButton() == MouseButton.SECONDARY) {
                    cm.show(stage, e.getScreenX(), e.getScreenY());
                }
            });
            
        } catch (Exception ex) {
            LOGGER.warning("ProfileWindowController: An error occurred while "
                    + "initializing the window... " + ex.getMessage());
        }
    }

    /**
     * Initializes the window state when it's shown.
     *
     * @param event The window event.
     */
    private void windowShowing(WindowEvent event) {
        try {
            txtNewEmail.setVisible(false);
            txtNewEmail.setPromptText("example@extension.cope");
            txtNewFullname.setVisible(false);
            txtNewFullname.setPromptText("Full name");
            txtNewPassword.setDisable(true);
            txtNewPasswordRepeat.setDisable(true);
            txtCurrentPassword.setDisable(true);
            btSave.setVisible(false);
            btPremium.setVisible(true);

            if (privilege.equals("PREMIUM")) {
                btPremium.setText("Edit payment data");
                lbUsername.setText(premium.getLogin());
                lbFullName.setText(premium.getFullName());
                lbEmail.setText(premium.getEmail());
                lbPrivilege.setText(premium.getPrivilege().toString());
            } else {
                btPremium.setText("Go premium now!");
                lbUsername.setText(user.getLogin());
                lbFullName.setText(user.getFullName());
                lbEmail.setText(user.getEmail());
                lbPrivilege.setText(user.getPrivilege().toString());
                if (privilege.equals("ADMIN")) {
                    btPremium.setVisible(false);
                }
            }
        } catch (Exception ex) {
            LOGGER.warning("ProfileWindowController: An error occurred while "
                    + "showing the window... " + ex.getMessage());
        }

    }

    /**
     * Shows text fields to edit user's data.
     *
     * @param event The window event.
     */
    private void editRequest(ActionEvent event) {
        try {
            txtNewEmail.setVisible(true);
            txtNewEmail.requestFocus();
            txtNewFullname.setVisible(true);
            txtNewPassword.setDisable(false);
            txtNewPasswordRepeat.setDisable(false);
            txtCurrentPassword.setDisable(false);
            btSave.setVisible(true);
        } catch (Exception ex) {
            LOGGER.warning("ProfileWindowController: An error occurred while "
                    + "editing fields... " + ex.getMessage());
        }

    }

    /**
     * Validates the data typed by the user and sends an update request to the
     * server.
     *
     * @param event The window event.
     */
    private void saveNewDataRequest(ActionEvent event) {
        try {
            Boolean error = false;
            User auxUser = new User();
            String email = null;
            String password = null;

            if (privilege.equals("PREMIUM")) {
                email = premium.getEmail();
                password = premium.getPassword();
                auxUser.setId(premium.getId());
            } else {
                email = user.getEmail();
                password = user.getPassword();
                auxUser.setId(user.getId());
            }

            // CHECKING THE NEW EMAIL
            if (!txtNewEmail.getText().isEmpty()) { // If email field is not empty
                if (checkEmail(txtNewEmail.getText())) { // if email format is correct
                    if (!txtNewEmail.getText().equals(email)) { // If new email is different from the current one
                        auxUser.setEmail(txtNewEmail.getText());
                    } else {
                        txtNewEmail.requestFocus();
                        error = true;
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Email Error");
                        alert.setHeaderText("The new email must be different from the current one!");
                        alert.showAndWait();
                    }
                } else {
                    txtNewEmail.requestFocus();
                    error = true;
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Email Error");
                    alert.setHeaderText("The email format is not allowed!");
                    alert.setContentText("example@extension.cope");
                    alert.showAndWait();
                }
            } else {
                if (privilege.equals("PREMIUM")) {
                    auxUser.setEmail(premium.getEmail());
                } else {
                    auxUser.setEmail(user.getEmail());
                }
            }

            // CHECKING THE NEW FULL NAME
            if (!txtNewFullname.getText().isEmpty() && !error) { //If fullName field is not empty
                if (txtNewFullname.getText().trim().length() < 44) { //if fullName is too long
                    auxUser.setFullName(txtNewFullname.getText());
                } else { // Full name too long
                    txtNewFullname.requestFocus();
                    error = true;
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Full name Error");
                    alert.setHeaderText("Full name if too long!");
                    alert.setContentText("Max 44 characters long");
                    alert.showAndWait();
                }
            } else {
                if (privilege.equals("PREMIUM")) {
                    auxUser.setFullName(premium.getFullName());
                } else {
                    auxUser.setFullName(user.getFullName());
                }
            }

            // CHECKING THE NEW PASSWORD
            if (!txtNewPassword.getText().isEmpty() && !error) {
                if (checkPassword(txtNewPassword.getText())) {
                    if (txtCurrentPassword.getText().equals(password)) {
                        if (checkPassRepeat(txtNewPassword.getText(), txtNewPasswordRepeat.getText())) {
                            // New password is correct
                            String encryptedKey = txtNewPassword.getText().trim();
                            encryptedKey = Encryptation.encrypt(encryptedKey);
                            auxUser.setPassword(encryptedKey);
                        } else { // New password and repeat new password don't match
                            txtNewPassword.requestFocus();
                            error = true;
                            Alert alert = new Alert(Alert.AlertType.ERROR);
                            alert.setTitle("New Password Error");
                            alert.setHeaderText("New passwords don't match!");
                            alert.showAndWait();
                        }
                    } else { // Current password not correct
                        txtCurrentPassword.requestFocus();
                        error = true;
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Password Error");
                        alert.setHeaderText("Last password not correct!");
                        alert.setContentText("Introduce the last password");
                        alert.showAndWait();
                    }
                } else { // Password not correct
                    txtNewPassword.requestFocus();
                    error = true;
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Password Error");
                    alert.setHeaderText("Invalid password format!");
                    alert.setContentText("8 - 14 characters, including a lower case!");
                    alert.showAndWait();
                }
            }

            if (!error) {
                // SENDING NEW DATA TO THE SERVER
                client.modifyUserData(auxUser);

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("OK");
                alert.setHeaderText("Data updated successfully!");
                alert.setContentText("Returning to the main menu...");
                alert.showAndWait();

                // RESETING THE EDIT DATA TEXT FIELDS.
                /*txtNewEmail.setText("");
                txtNewFullname.setText("");
                txtNewPassword.setText("");
                txtNewPasswordRepeat.setText("");
                txtCurrentPassword.setText("");
                txtNewEmail.setVisible(false);
                txtNewFullname.setVisible(false);
                txtNewPassword.setDisable(true);
                txtNewPasswordRepeat.setDisable(true);
                txtCurrentPassword.setDisable(true);
                btSave.setVisible(false);*/
 /*try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource(
                            "/windowsapplication/view/Perfil.fxml"));
                    Parent root = (Parent) loader.load();
                    ProfileWindowController profileWindowController
                            = ((ProfileWindowController) loader.getController());
                    profileWindowController.setStage(stage);
                    profileWindowController.setPrivilege(privilege);
                    if (privilege.equals("PREMIUM")) {
                        profileWindowController.setPremium(premium);
                    } else {
                        profileWindowController.setUser(user);
                    }
                    profileWindowController.initStage(root);
                    stage.close();
                } catch (Exception ex) {

                }*/
                stage.close();

            }
        } catch (Exception ex) {
            LOGGER.warning("ProfileWindowController: " + ex.getMessage());
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Sorry, an error occurred");
            alert.setContentText("Try again later...");
            alert.showAndWait();
        }

    }

    /**
     * Loads the payment method window controller.
     *
     * @param event The window event.
     */
    private void premiumRequest(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().
                    getResource("/windowsapplication/view/MetodoDePago.fxml"));
            Parent root = (Parent) loader.load();
            CreditCardWindowController creditCardWindowController = loader.getController();
            creditCardWindowController.setStage(stage);
            creditCardWindowController.setPrivilege(privilege);
            if (privilege.equals("PREMIUM")) {
                creditCardWindowController.setPremium(premium);
            } else {
                creditCardWindowController.setUser(user);
            }
            creditCardWindowController.initStage(root);
        } catch (Exception ex) {
            LOGGER.warning(ex.getMessage());
        }
    }

    /**
     * Closes the stage.
     *
     * @param event The window event.
     */
    private void closeRequest(WindowEvent event) {
        try {
            stage.close();
        } catch (Exception ex) {
            LOGGER.warning("ProfileWindowController: " + ex.getMessage());
        }

    }

    /**
     * Closes the stage.
     *
     * @param event The window event.
     */
    private void backButtonRequest(ActionEvent event) {
        try {
            stage.close();
        } catch (Exception ex) {
            LOGGER.warning("ProfileWindowController: " + ex.getMessage());
        }
    }

    /**
     * A method that validates the pattern of the email.
     *
     * @param email A string with the email.
     * @return check A boolean that return the checking of the email.
     */
    private boolean checkEmail(String email) {
        boolean check = false;
        check = Util.validarEmail(email);
        return check;
    }

    /**
     * A method that validates if both password fields are the same.
     *
     * @param password the password the client set.
     * @param passwordRepeat the repetition of the password.
     * @return checkRepeat A boolean that return the check of the passwords is
     * ok.
     */
    private boolean checkPassRepeat(String password, String passwordRepeat) {
        boolean checkRepeat = false;

        if (password.equalsIgnoreCase(passwordRepeat)) {
            checkRepeat = true;
        }

        return checkRepeat;
    }

    /**
     * A method that checks if the password has an uppercase and a number.
     *
     * @param password the password the client set.
     * @return check A boolean that return if the validation is ok.
     */
    private boolean checkPassword(String password) {

        boolean capital = false;
        boolean number = false;
        boolean check = false;

        for (int i = 0; i < password.length(); i++) {
            char ch = password.charAt(i);
            if (Character.isDigit(ch)) {
                number = true;
            }
            if (Character.isUpperCase(ch)) {
                capital = true;
            }
        }

        if (capital && number) {
            check = true;
        }
        return check;
    }

}
