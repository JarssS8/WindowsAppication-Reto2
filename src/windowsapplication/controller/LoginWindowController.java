/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package windowsapplication.controller;

import java.io.IOException;
import java.util.Optional;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.Mnemonic;
import javafx.scene.paint.Paint;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javax.ws.rs.ClientErrorException;
import javax.ws.rs.InternalServerErrorException;
import javax.ws.rs.NotAuthorizedException;
import javax.ws.rs.NotFoundException;
import windowsapplication.beans.Admin;
import windowsapplication.beans.Free;
import windowsapplication.beans.Premium;
import windowsapplication.beans.User;
import windowsapplication.service.UserClientREST;

/**
 * This class is a controller UI class for LogIn_Window view. Contains event
 * handlers and on window showing code.
 *
 * @author Adrian Corral
 */
public class LoginWindowController {

    /**
     * The stage is used for instance an stage attribute and can store the stage
     * from other class or send the stage to other class.
     */
    private Stage stage;

    /**
     * This is the login button on the view.
     */
    @FXML
    private Button btLogin;

    /**
     * This is the text input by the user that define the username.
     */
    @FXML
    private TextField txtLogin;

    /**
     * This is the text input by the user that define the password.
     */
    @FXML
    private PasswordField txtPass;

    /**
     * This is the link to navigate to the sign up window.
     */
    @FXML
    private Hyperlink linkClickHere;

    @FXML
    private Label lbLogin;

    @FXML
    private Label lbPass;

    private UserClientREST client = new UserClientREST();

    private static final Logger LOGGER = Logger.getLogger(
            "windowsapplication.controller.LoginWindowController");

    /**
     * @return Return the stage of this class
     */
    public Stage getStage() {
        return stage;
    }

    /**
     * @param stage Sets the stage for this class
     */
    public void setStage(Stage stage) {
        this.stage = stage;
    }

    /**
     * This method initialize the window and everything thats the stage needs.
     * This calls other method when shows the window to set attributes of the
     * window
     *
     * @param root The parent object
     */
    public void initStage(Parent root) {
        try {
            Scene scene = new Scene(root);
            //Stage Properties
            stage.setScene(scene);
            stage.setTitle("LogIn");
            stage.setResizable(false);
            stage.setOnShowing(this::handleWindowShowing);
            stage.setOnCloseRequest(this::onCloseRequest);
            stage.getScene().addEventFilter(KeyEvent.KEY_PRESSED, this::keyEventController);
            //Listeners
            txtLogin.textProperty().addListener(this::textChange);
            txtPass.textProperty().addListener(this::textChange);
            //Stage show
            stage.show();
        } catch (Exception ex) {
            LOGGER.warning("LoginWindowController: An error ocurred while "
                    + "loading the window... " + ex.getMessage());
        }
    }

    /**
     * This is the method to control the components of this window when we shows
     * the window.
     *
     * @param event The event is the window that is being showed.
     */
    private void handleWindowShowing(WindowEvent event) {
        btLogin.setDisable(true);
        btLogin.setMnemonicParsing(true);
        btLogin.setText("_Login");
        btLogin.setTooltip(new Tooltip("Click to complete the login"));
        lbLogin.setTooltip(new Tooltip("Username to login"));
        lbPass.setTooltip(new Tooltip("Password to login"));
        txtLogin.setPromptText("Insert the username here");
        txtLogin.setTooltip(new Tooltip("The username should have between 4 and 10 characters"));
        txtPass.setPromptText("Insert the password here");
        txtPass.setTooltip(new Tooltip("The username should have between 8 and 14 characters, including at least one number and one uppercase"));
        linkClickHere.setTooltip(new Tooltip("Click here to sign up"));
        linkClickHere.setMnemonicParsing(true);
        KeyCombination keyCode = new KeyCodeCombination(KeyCode.S, KeyCombination.ALT_DOWN);
        Mnemonic mnemonicCode = new Mnemonic(linkClickHere, keyCode);
        getStage().getScene().addMnemonic(mnemonicCode);

    }

    /**
     * This method is used if the user try to close the application clicking in
     * the red cross(right-top in the stage) and control if the user is sure to
     * close the application.
     *
     * @param event The event is the user trying to close the application with
     * the cross of the stage.
     */
    public void onCloseRequest(WindowEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setHeaderText("Close confirmation");
        alert.setTitle("Exit Window");
        alert.setContentText("Are you sure that want close the application?");
        alert.initOwner(stage);
        alert.initModality(Modality.WINDOW_MODAL);
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {
            stage.close();
            Platform.exit();

        } else {
            event.consume();
        }
    }

    /**
     * Checks every time that the user change the TextField and if both formats
     * are correct enables the login button.
     *
     * @param event The event when the text is changing.
     */
    private void textChange(ObservableValue observable, String oldValue, String newValue) {
        boolean passok = checkPassword(txtPass.getText().trim());
        boolean usernameOk = false;
        boolean passwordCheck = false;
        //Check if user got the correct format
        usernameOk = txtLogin.getText().trim().length() >= 4 && txtLogin.getText().trim().length() <= 10;
        //Check password got corect format
        passwordCheck = txtPass.getText().trim().length() >= 8
                && txtPass.getText().trim().length() <= 14 && passok;
        //Enable Login Button
        btLogin.setDisable(!usernameOk || !passwordCheck);
    }

    /**
     * This method checks if the password has the correct format.
     *
     * @param password a String that contains the passwors written by the user.
     * @return check A boolean.
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
     * This method sends the txtLogin and the txtPass to the factory and waits
     * if the user exists on dataBase and the password is correct to go to the
     * logout window.
     *
     * @param event The event is the user clicking on the login button.
     * @throws LoginNotFoundException If login does not exist in the database.
     * @throws WrongPasswordException If password does not match with the user.
     */
    public void loginClick(ActionEvent event) throws ClientErrorException {
        try {
            String login = txtLogin.getText().trim().toString();
            String pass = txtPass.getText().trim().toString();
            String privilege = client.findPrivilegeOfUserByLogin(login);
            User user = null;
            switch (privilege) {
                case ("ADMIN"): {
                    user = client.logIn(Admin.class, login, pass);
                    break;
                }
                case ("PREMIUM"): {
                    user = client.logIn(Premium.class, login, pass);
                    break;
                }
                default: {
                    user = client.logIn(Free.class, login, pass);
                }
            }
            if (user != null) {
                lbLogin.setTextFill(Paint.valueOf("BLACK"));
                lbPass.setTextFill(Paint.valueOf("BLACK"));
                FXMLLoader loader = new FXMLLoader(getClass().getResource(
                        "/windowsapplication/view/Main.fxml"));
                Parent root = (Parent) loader.load();
                MainWindowController controller = loader.getController();
                controller.setStage(stage);
                controller.setUser(user);
                controller.initStage(root);
            }
        } catch (NotFoundException ex) {
            LOGGER.warning("LoginWindowController: Login not found" + ex.getMessage());
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("LogIn Error");
            alert.setContentText("User does not exist");
            alert.showAndWait();
        } catch (NotAuthorizedException ex) {
            LOGGER.warning("LoginWindowController: Wrong password" + ex.getMessage());
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Password Error");
            alert.setContentText("Password does not exist");
            alert.showAndWait();
        } catch (InternalServerErrorException ex) {
            LOGGER.warning("LoginWindowController: Server connection error" + ex.getMessage());
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Server Error");
            alert.setContentText("Unable to connect with server");
            alert.showAndWait();
        } catch (ClientErrorException ex) {
            ex.printStackTrace();
            LOGGER.warning("LoginWindowController: Exception on LoginWindowController" + ex.getMessage());
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText("ClientErrorException");
            alert.showAndWait();
        } catch (Exception ex) {
            LOGGER.warning("LoginWindowController: Exception on LoginWindowController " + ex.getMessage());
            ex.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText("Sorry, an error has ocurred");
            alert.showAndWait();
        }
    }

    /**
     * This method opens the sign up window when the user clicks on the
     * hyperlink click here
     *
     * @param event The event
     * @throws IOException Error when can't access to the fxml view
     */
    public void signUpClick(ActionEvent event) throws IOException {
        //Clean Login windows Login and Password fields before loading the Sign 
        //up window.
        txtLogin.setText("");
        txtPass.setText("");
        FXMLLoader loader = new FXMLLoader(getClass().getResource(
                "/windowsapplication/view/SignUp_Window.fxml"));
        Parent root = (Parent) loader.load();
        SignUpWindowController controller
                = ((SignUpWindowController) loader.getController());
        controller.setStage(stage);
        controller.initStage(root);
        
    }

    /**
     * This method controls when we press one key and if is equals to someone
     * previously defined do something
     *
     * @param keyEvent is the event for the key that we press
     */
    public void keyEventController(KeyEvent keyEvent) {
        /*
        try {
            if (keyEvent.getCode() == KeyCode.F1) {//If user press F1 key
                FXMLLoader loader
                        = new FXMLLoader(getClass().getResource(
                                "/windowsclientapplication/view/Help.fxml"));
                Parent root = (Parent) loader.load();
                HelpController helpController
                        = ((HelpController) loader.getController());
                helpController.initAndShowStage(root, LOGIN);
            }
            
        } catch (IOException ex) {
            LOGGER.severe("Error showing the help page");
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("ERROR ON HELP WINDOW");
            alert.setContentText("There was an error loading the help window");
            alert.showAndWait();
        }
         */
    }

}
