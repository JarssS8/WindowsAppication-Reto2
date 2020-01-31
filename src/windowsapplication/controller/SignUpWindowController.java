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
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Paint;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javax.ws.rs.ForbiddenException;
import windowsapplication.beans.Free;
import windowsapplication.beans.Privilege;
import windowsapplication.beans.Status;
import windowsapplication.beans.User;
import windowsapplication.service.UserClientREST;
import windowsapplication.utilities.Encryptation;
import windowsapplication.utilities.Util;

/**
 * This class is a controller UI class for SignUp_Window view. Contains event
 * handlers and on window showing code.
 *
 * @author Adrián Corral
 */
public class SignUpWindowController {

    private static final Logger LOGGER = Logger
        .getLogger("Windowsapplication.controller.SignUpWindowController");

    @FXML
    private Button btBack;
    @FXML
    private Button btSignUp;
    @FXML
    private Button btHelpMe;
    @FXML
    private TextField txtUsername;
    @FXML
    private TextField txtEmail;
    @FXML
    private TextField txtPassword;
    @FXML
    private TextField txtRepeatPassword;
    @FXML
    private TextField txtFullName;
    @FXML
    private Label lbUsernameCaution;
    @FXML
    private Label lbEmailCaution;
    @FXML
    private Label lbPasswordCaution1;
    @FXML
    private Label lbPasswordCaution2A;
    @FXML
    private Label lbPasswordCaution3;
    @FXML
    private Label lbFullNameCaution;
    @FXML
    private Label lbUsername;
    @FXML
    private Label lbPassword;
    @FXML
    private Label lbEmail;
    @FXML
    private Label lbRepeatPassword;
    @FXML
    private Label lbFullName;

    private Stage stage;

    private UserClientREST client = new UserClientREST();

    /**
     * This method receives a Stage object.
     *
     * @param stage A Stage object.
     */
    public void setStage(Stage stage) {
        this.stage = stage;
    }

    /**
     * This method initializes the window and everything the stage needs. Calls
     * other method when showing the window to set its attributes.
     *
     * @param root The parent object
     */
    public void initStage(Parent root) {
        try {
            Scene scene = new Scene(root);
            stage = new Stage();
            stage.setScene(scene);
            stage.setTitle("Registration");
            stage.setResizable(false);
            stage.setOnShowing(this::handleWindowShowing);
            stage.initModality(Modality.APPLICATION_MODAL);
            btBack.setOnAction(this::handleButtonAction);
            btSignUp.setOnAction(this::handleButtonAction);
            btHelpMe.setOnAction(this::handleHelpMeButtonAction);
            stage.setOnCloseRequest(this::handleCloseAction);
            btSignUp.setDisable(false);
            stage.getScene().addEventFilter(KeyEvent.KEY_PRESSED, this::handleKeyEventAction);
            stage.show();
        } catch (Exception ex) {
            LOGGER.warning("SignUpWindowController: Error while loading the window..." + ex.getMessage());

        }

    }

    /**
     * This is the method to control the components of this window when we show
     * the window.
     *
     * @param event The event is the window that is being showed.
     */
    private void handleWindowShowing(WindowEvent event) {
        try {
            LOGGER.info("Setting the window...");

            //Prompt Text
            txtUsername.setPromptText("Introduce login");
            txtEmail.setPromptText("Introduce email");
            txtPassword.setPromptText("Introduce password");
            txtRepeatPassword.setPromptText("Repeat password");
            txtFullName.setPromptText("Introduce full name");

            //Tooltips
            btSignUp.setTooltip(new Tooltip("Click to complete the registration"));
            btBack.setTooltip(new Tooltip("Return to LogIn"));
            btHelpMe.setTooltip(new Tooltip("Open help window"));
            lbUsername.setTooltip(new Tooltip("Username to login"));
            lbPassword.setTooltip(new Tooltip("Password to login"));
            lbRepeatPassword.setTooltip(new Tooltip("Repeat the password"));
            lbEmail.setTooltip(new Tooltip("Email to send information"));
            lbFullName.setTooltip(new Tooltip("Your Full Name"));

            //Mnemonic
            btSignUp.setMnemonicParsing(true);
            btBack.setMnemonicParsing(true);
            btHelpMe.setMnemonicParsing(true);
            btSignUp.setText("_Sign Up");
            btBack.setText("_Back");
            btHelpMe.setText("_HelpMe");
        } catch (Exception ex) {
            LOGGER.warning("SignUpWindowController: Error while loading the window..." + ex.getMessage());
        }

    }

    /**
     * This method is used if the user tries to close the application clicking
     * in the red cross(right-top in the stage) and asks the user for
     * confirmation to close.
     *
     * @param event The event is the user trying to close the application with
     * the cross of the stage.
     *
     */
    private void handleCloseAction(WindowEvent event) {
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Close confirmation");
        alert.setHeaderText("Registration will be cancelled");
        alert.setContentText("Are you sure you want to exit?");
        alert.getButtonTypes().setAll(ButtonType.YES, ButtonType.NO);
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.YES) {
            stage.close();
        } else {
            event.consume();
        }
    }

    /**
     * This method receives and checks a Key Event and shows the help window if
     * the event is a F1 key pressing.
     *
     * @param event A KeyEvent event.
     */
    public void handleKeyEventAction(KeyEvent event) {
        try {
            //Check if the Key pressed is F1.
            if (event.getCode() == KeyCode.F1) {
                //Load Help window.
                LOGGER.info("Loading SignUpHelp window...");
                FXMLLoader loader
                    = new FXMLLoader(getClass().getResource(
                        "/windowsclientapplication/view/Help.fxml"));
                Parent root = (Parent) loader.load();
                HelpWindowController helpController
                    = ((HelpWindowController) loader.getController());
                helpController.initStage(root);
            }
        } catch (Exception ex) {
            LOGGER.severe("Error showing the help window");
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("ERROR");
            alert.setHeaderText("There was an error loading the help window");
            alert.showAndWait();
        }
    }

    /**
     * This method handles the HelpMe button. Loads, initializes and shows the
     * SignUpHelp window.
     *
     * @param event An ActionEvent event.
     */
    public void handleHelpMeButtonAction(ActionEvent event) {
        try {
            FXMLLoader loader
                = new FXMLLoader(getClass().getResource(
                    "/windowsclientapplication/view/Help.fxml"));
            Parent root = (Parent) loader.load();
            HelpWindowController helpController
                = ((HelpWindowController) loader.getController());
            helpController.initStage(root);
        } catch (Exception ex) {
            LOGGER.severe("Error showing the help window");
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("ERROR");
            alert.setHeaderText("There was an error loading the help window");
            alert.showAndWait();
        }
    }

    /**
     * A method that registers the button actions
     *
     * @param event An ActionEvent object.
     */
    public void handleButtonAction(ActionEvent event) {
        try {
            if (event.getSource().equals(btBack)) {
                LOGGER.info("Closing the window");
                Alert alert = new Alert(AlertType.CONFIRMATION);
                alert.setTitle("Close confirmation");
                alert.setHeaderText("Registration will be cancelled");
                alert.setContentText("Are you sure you want to exit?");
                alert.getButtonTypes().setAll(ButtonType.YES, ButtonType.NO);
                Optional<ButtonType> result = alert.showAndWait();
                if (result.get() == ButtonType.YES) {
                    try {
                        FXMLLoader loader = new FXMLLoader(getClass().
                            getResource("/windowsapplication/view/LogIn_Window.fxml"));
                        Parent root = (Parent) loader.load();
                        LoginWindowController controller = loader.getController();
                        controller.setStage(stage);
                        controller.initStage(root);
                        stage.close();
                    } catch (Exception ex) {
                        LOGGER.warning("WindowsApplicationReto2: An error ocurred while "
                            + "loading the window... " + ex.getMessage());
                    }
                } else {
                    alert.close();
                }
            }
            if (event.getSource().equals(btSignUp)) {
                if (checkValidation()) {
                    LOGGER.info("Creating new user...");
                    User user = new User();
                    user.setLogin(txtUsername.getText().trim());
                    String encryptedKey = txtPassword.getText().trim();
                    encryptedKey = Encryptation.encrypt(encryptedKey);
                    user.setPassword(encryptedKey);
                    user.setEmail(txtEmail.getText().trim());
                    user.setFullName(txtFullName.getText().trim());
                    user.setLastAccess(Timestamp.valueOf(LocalDateTime.now()));
                    user.setLastPasswordChange(Timestamp.valueOf(LocalDateTime.now()));
                    user.setPrivilege(Privilege.FREE);
                    user.setStatus(Status.ENABLED);

                    LOGGER.info("Sending the user...");

                    Free free = client.createUser(user, Free.class);

                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("OK");
                    alert.setHeaderText("Signing up successful!");
                    alert.setContentText("Redirecting to the log in window...");
                    alert.showAndWait();
                    try {
                        FXMLLoader loader = new FXMLLoader(getClass().
                            getResource("/windowsapplication/view/LogIn_Window.fxml"));
                        Parent root = (Parent) loader.load();
                        LoginWindowController controller = loader.getController();
                        controller.setStage(stage);
                        controller.initStage(root);
                        stage.close();
                    } catch (Exception ex) {
                        LOGGER.warning("WindowsApplicationReto2: An error ocurred while "
                            + "loading the window... " + ex.getMessage());
                    }
                }
            }
        } catch (ForbiddenException ex) {
            LOGGER.warning("SignUpWindowController: " + ex.getMessage());
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("ERROR");
            alert.setHeaderText("Username is not available");
            alert.setContentText("Try again with a different one...");
            alert.showAndWait();
        } catch (Exception ex) {
            LOGGER.warning("SignUpWindowController: " + ex.getMessage());
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Sorry, an error has ocurred");
            alert.setContentText("Try again later...");
            alert.showAndWait();
        }
    }

    /**
     * A method that validates the pattern of the email
     *
     * @param email A string with the email
     * @return check A boolean that return the checking of the email
     */
    private boolean checkEmail(String email) {
        boolean check = false;
        check = Util.validarEmail(email);
        return check;
    }

    /**
     * A method that validates if both password fields are the same
     *
     * @param password the password the client set
     * @param passwordRepeat the repetition of the password
     * @return checkRepeat A boolean that return the check of the passwords is
     * ok
     */
    private boolean checkPassRepeat(String password, String passwordRepeat) {
        boolean checkRepeat = false;

        if (password.equalsIgnoreCase(passwordRepeat)) {
            checkRepeat = true;
        }

        return checkRepeat;
    }

    /**
     * A method that checks if the password has an uppercase and a number
     *
     * @param password the password the client set
     * @return check A boolean that return if the validation is ok
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

    /**
     * A method that validate the text change of the fields
     *
     */
    private boolean checkValidation() {
        boolean passCheck = checkPassword(txtPassword.getText().trim());
        boolean passCheckRepeat = checkPassRepeat(txtPassword.getText()
            .trim(), txtRepeatPassword.getText().trim());
        boolean emailCheck = checkEmail(txtEmail.getText().trim());

        boolean username = false;
        boolean passwordlength = false;
        boolean passwordCheck = false;
        boolean passwordRepeat = false;
        boolean email = false;
        boolean fullname = false;
        boolean ok = false;
        boolean focused = false;

        //Check username
        if (txtUsername.getText().trim().length() > 3
            && txtUsername.getText().trim().length() < 11) {

            username = true;
            lbUsernameCaution.setTextFill(Paint.valueOf("BLACK"));

        } else {
            if (!focused) {
                txtUsername.requestFocus();
                focused = true;
            }
            lbUsernameCaution.setTextFill(Paint.valueOf("RED"));

        }//End check username

        //EmailCheck
        if (emailCheck) {

            email = true;
            lbEmailCaution.setTextFill(Paint.valueOf("BLACK"));

        } else {
            if (!focused) {
                txtEmail.requestFocus();
                focused = true;
            }
            lbEmailCaution.setTextFill(Paint.valueOf("RED"));

        }//End EmailCheck 

        //Check FullName
        if (!txtFullName.getText().trim().isEmpty() && txtFullName.getText().trim().length() < 44) {

            fullname = true;
            lbFullNameCaution.setTextFill(Paint.valueOf("BLACK"));

        } else {
            if (!focused) {
                txtFullName.requestFocus();
                focused = true;
            }
            lbFullNameCaution.setTextFill(Paint.valueOf("RED"));

        }//End Check FullName

        //Check password
        if (txtPassword.getText().trim().length() > 7
            && txtPassword.getText().trim().length() < 15) {

            passwordlength = true;
            lbPasswordCaution1.setTextFill(Paint.valueOf("BLACK"));

        } else {
            if (!focused) {
                txtPassword.requestFocus();
                focused = true;
            }
            lbPasswordCaution1.setTextFill(Paint.valueOf("RED"));

        }// End check password

        //PassCheck
        if (passCheck) {

            passwordCheck = true;
            lbPasswordCaution2A.setTextFill(Paint.valueOf("BLACK"));

        } else {
            if (!focused) {
                txtPassword.requestFocus();
                focused = true;
            }
            lbPasswordCaution2A.setTextFill(Paint.valueOf("RED"));

        }//End PassCheck

        //PassCheckRepeat
        if (passCheckRepeat) {

            passwordRepeat = true;
            lbPasswordCaution3.setTextFill(Paint.valueOf("BLACK"));
        } else {
            if (!focused) {
                txtRepeatPassword.requestFocus();
                focused = true;
            }
            lbPasswordCaution3.setTextFill(Paint.valueOf("RED"));

        }//End PassCheckRepeat       

        //Check all
        if (username && passwordlength && passwordRepeat && passwordCheck
            && email && fullname) {

            ok = true;

        }//End chack all

        return ok;
    }

}
