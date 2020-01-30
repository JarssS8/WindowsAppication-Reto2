/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package windowsapplication.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Date;
import java.time.LocalDate;
import java.util.Base64;
import java.util.List;
import java.util.Optional;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javax.ws.rs.core.GenericType;
import windowsapplication.beans.Category;
import windowsapplication.beans.Document;
import windowsapplication.beans.Rating;
import windowsapplication.beans.RatingId;
import windowsapplication.beans.User;
import windowsapplication.service.DocumentClientREST;
import windowsapplication.service.RatingClientREST;

/**
 * Controller of the Info Doc window
 *
 * @author Gaizka Andres
 */
public class InfoDocWindowController {

    /**
     * Button to add a new rating
     */
    @FXML
    private Button btRate;
    /**
     * Combobox to charge the score
     */
    @FXML
    private ComboBox comboRating;
    /**
     * Text Field to put the review of the document
     */
    @FXML
    private TextField txtComent;
    /**
     * Label to put the document´s name
     */
    @FXML
    private Label lbNameDocument;
    /**
     * Table of document´s ratings
     */
    @FXML
    private TableView tbComentsRatings;
    /**
     * Column of the date the rating has done
     */
    @FXML
    private TableColumn tbcolUser;
    /**
     * Column of the rating´s review
     */
    @FXML
    private TableColumn tbcolComent;
    /**
     * Column of the rating´s score
     */
    @FXML
    private TableColumn tbcolRating;
    /**
     * Info of the new document´s name
     */
    @FXML
    private Label lbNewName;
    /**
     * Text Field of the document´s new name
     */
    @FXML
    private TextField txtNewName;
    @FXML
    private Button btDownloadDocument;
    /**
     * Label to put the total score of the document
     */
    @FXML
    private Label lbAvgDocmuent;
    /**
     * Button to close the window
     */
    @FXML
    private Button btClose;
    /**
     * Button to change the document´s name
     */
    @FXML
    private Button btChange;

    private Stage stage;

    private Document document;

    private User user;
    /**
     * Client Rest of Document
     */
    private DocumentClientREST docREST = new DocumentClientREST();
    /**
     * Client Rest of Rating
     */
    private RatingClientREST ratingREST = new RatingClientREST();

    /**
     * Sets the Stage object related to this controller.
     *
     * @param stage The Stage object to be initialized.
     */
    void setStage(Stage stage) {
        this.stage = stage;
    }

    /**
     * Set the user who is logged in the app
     *
     * @param user The user is logged
     */
    void setUser(User user) {
        this.user = user;
    }

    /**
     * Set the document of which we will see the data
     * @param document document which we will see the data
     */
    void setDocument(Document document) {
        this.document = document;
    }

    void initStage(Parent root) {
        Scene scene = new Scene(root);

        stage = new Stage();
        stage.setScene(scene);
        stage.setTitle("Info of the document");
        stage.setResizable(false);
        stage.setOnShowing(this::handleWindowShowing);
        /**
         * Actions of the buttons
         */
        stage.setOnCloseRequest(this::closeRequest);
        btRate.setOnAction(this::handleRateAction);
        btDownloadDocument.setOnAction(this::downloadDocumentRequest);
        btClose.setOnAction(this::backButtonRequest);
        btChange.setOnAction(this::changeButtonRequest);
        /**
         * Factories of the ratings table
         */
        tbcolUser.setCellValueFactory(new PropertyValueFactory<>("ratingDate"));
        tbcolComent.setCellValueFactory(new PropertyValueFactory<>("review"));
        tbcolRating.setCellValueFactory(new PropertyValueFactory<>("rating"));
        /**
         * Observable List with the ratings
         */
        ObservableList<Rating> ratings;
        ratings = FXCollections.observableArrayList(ratingREST.DocumentsRating(new GenericType<List<Rating>>() {
        }, document.getId()));
        tbComentsRatings.setItems(ratings);
        comboRating.getItems().addAll("0", "1", "2", "3", "4", "5");
        lbNewName.setVisible(false);
        txtNewName.setVisible(false);
        txtNewName.setDisable(false);
        btChange.setVisible(false);
        btChange.setDisable(true);
        /**
         * Context Meny of the window
         */
        final ContextMenu cm = new ContextMenu();
        MenuItem cmItem1 = new MenuItem("Go back");
        MenuItem cmItem2 = new MenuItem("Delete document");
        MenuItem cmItem3 = new MenuItem("Download PDF");
        MenuItem cmItem4 = new MenuItem("Edit");
        cmItem1.setOnAction((ActionEvent e) -> {
            stage.close();
        });
        /**
         * Context menu of the table
         */
        final ContextMenu cm2 = new ContextMenu();
        MenuItem cm2Item1 = new MenuItem("Delete rating");
        cm2Item1.setOnAction((ActionEvent e) -> {
            RatingId ratingId = new RatingId();
            ratingId.setIdDocument(document.getId());
            ratingId.setIdUser(user.getId());
            ratingREST.deleteRating(ratingId);
        });
        /**
         * Actions of the window context menu
         */
        cm2.getItems().addAll(cm2Item1);
        cmItem1.setOnAction((ActionEvent e) -> {
            stage.close();
        });
        tbComentsRatings.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent e) -> {
            if (e.getButton() == MouseButton.SECONDARY) {
                cm2.show(stage, e.getScreenX(), e.getScreenY());
            }
        });

        Long dId = document.getId();
        cmItem2.setOnAction((ActionEvent e) -> {

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Delete document");
            alert.setHeaderText("Are you sure you want to delete this document?");
            alert.setContentText("If you press yes button, is no going back");
            Button okButton = (Button) alert.getDialogPane().lookupButton(ButtonType.OK);
            okButton.setId("okbutton");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK) {
                docREST.deleteDocument(dId);
                stage.close();
            } else {
                alert.close();
            }
        });
        cmItem3.setOnAction((ActionEvent e) -> {
            FileChooser fileChooser = new FileChooser();
            FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("PDF files (*.pdf)", "*.pdf");
            fileChooser.getExtensionFilters().add(extFilter);
            File fileC = fileChooser.showSaveDialog(stage);

            writeBytesToFile(document.getFile(), fileC);
        });
        cmItem4.setOnAction((ActionEvent e) -> {
            lbNewName.setVisible(true);
            txtNewName.setVisible(true);
            txtNewName.setDisable(false);
            btChange.setVisible(true);
            btChange.setDisable(false);
            lbNewName.setText("New name:");

        });
        cm.getItems().addAll(cmItem1, cmItem2, cmItem3, cmItem4);
        stage.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent e) -> {
            if (e.getButton() == MouseButton.SECONDARY) {
                cm.show(stage, e.getScreenX(), e.getScreenY());
            }
        });
        stage.show();

    }

    /**
     * Initializes window state. Insert the data of the document
     *
     * @param event The window event
     */
    private void handleWindowShowing(WindowEvent event) {
        //Insertar nombre del documento
        lbNameDocument.setText(document.getName());
        //Insertar nota media de rating
        lbAvgDocmuent.setText(String.valueOf(document.getTotalRating()));

    }

    /**
     * Action when the add new rate button is pressed
     *
     * @param event
     */
    private void handleRateAction(ActionEvent event) {
        Rating nRating = new Rating();
        Long idD = document.getId();
        Long idU = user.getId();
        RatingId nId = new RatingId();
        nId.RatingId(idD, idU);
        nRating.setId(nId);
        nRating.setDocument(document);
        nRating.setRating(Integer.parseInt(comboRating.getValue().toString()));
        nRating.setReview(txtComent.getText());
        nRating.setRatingDate(Date.valueOf(LocalDate.now()));
        document.setRatingCount(document.getRatingCount() + 1);
        int nTotalrating = document.getTotalRating() + Integer.parseInt(comboRating.getValue().toString());
        document.setTotalRating(nTotalrating / document.getRatingCount());
        docREST.modifyDocument(document, document.getId());
        //ratingREST.newDocumentRating(nRating);
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Rating sent");
        alert.setHeaderText("¡Your opinion counts!");
        Button okButton = (Button) alert.getDialogPane().lookupButton(ButtonType.OK);
        okButton.setId("okbutton");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {
            alert.close();
        } else {
            alert.close();
        }
    }

    /**
     * Action when the download file option is pressed
     *
     * @param event
     */
    private void downloadDocumentRequest(ActionEvent event) {

        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("PDF files (*.pdf)", "*.pdf");
        fileChooser.getExtensionFilters().add(extFilter);
        File fileC = fileChooser.showSaveDialog(stage);

        writeBytesToFile(document.getFile(), fileC);

    }

    /**
     * Action when the X button is pressed
     *
     * @param event
     */
    private void closeRequest(WindowEvent event) {
        stage.close();
    }

    /**
     * Action when exit button is pressed
     *
     * @param event
     */
    private void backButtonRequest(ActionEvent event) {
        stage.close();
    }

    /**
     * Method who turn a byte array to a file
     *
     * @param file a array of bytes
     * @param fileC the File
     */
    private void writeBytesToFile(byte[] file, File fileC) {
        FileOutputStream fileOuputStream = null;

        try {
            fileOuputStream = new FileOutputStream(fileC.getPath());
            fileOuputStream.write(file);

        } catch (IOException e) {

        } finally {
            if (fileOuputStream != null) {
                try {
                    fileOuputStream.close();
                } catch (IOException e) {

                }
            }
        }
    }

    /**
     * Action when the edit document button is pressed
     *
     * @param event
     */
    private void changeButtonRequest(ActionEvent event) {
        Document nDocument = this.document;
        nDocument.setName(txtNewName.getText());
        docREST.modifyDocument(nDocument, nDocument.getId());
        lbNameDocument.setText(txtNewName.getText());
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Document sent");
        alert.setHeaderText("Name changed");
        Button okButton = (Button) alert.getDialogPane().lookupButton(ButtonType.OK);
        okButton.setId("okbutton");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {
            alert.close();
        } else {
            alert.close();
        }
    }

}
