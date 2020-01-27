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
import java.util.logging.Level;
import java.util.logging.Logger;
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
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
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
 *
 * @author Gaizka Andres
 */
public class InfoDocWindowController {

    @FXML
    private Button btRate;
    @FXML
    private ComboBox comboRating;
    @FXML
    private TextField txtComent;
    @FXML
    private Label lbNameDocument;
    @FXML
    private TableView tbComentsRatings;
    @FXML
    private TableColumn tbcolUser;
    @FXML
    private TableColumn tbcolComent;
    @FXML
    private TableColumn tbcolRating;
    @FXML
    private Label lbAuthorDocument;
    @FXML
    private Button btDownloadDocument;
    @FXML
    private Label lbAvgDocmuent;
    @FXML
    private Button btClose;

    private Stage stage;

    private User user;

    private Document document;

    private DocumentClientREST docREST = new DocumentClientREST();
    private RatingClientREST ratingREST = new RatingClientREST();

    void setStage(Stage stage) {
        this.stage = stage;
    }

    void setUser(User user) {
        this.user = user;
    }

    void setDocument(Document document) {
        this.document = document;
    }

    void initStage(Parent root) {
        Scene scene = new Scene(root);

        stage.setScene(scene);
        stage.setTitle("Info of the document");
        stage.setResizable(false);
        stage.setOnShowing(this::handleWindowShowing);
        stage.setOnCloseRequest(this::closeRequest);
        btRate.setOnAction(this::handleRateAction);
        btDownloadDocument.setOnAction(this::downloadDocumentRequest);
        btClose.setOnAction(this::backButtonRequest);

        final ContextMenu cm = new ContextMenu();
        MenuItem cmItem1 = new MenuItem("Go back");
        MenuItem cmItem2 = new MenuItem("Delete document");
        MenuItem cmItem3 = new MenuItem("Download PDF");
        cmItem1.setOnAction((ActionEvent e) -> {
            stage.close();
        });
        final ContextMenu cm2 = new ContextMenu();
        MenuItem cm2Item1 = new MenuItem("Delete rating");
        cm2Item1.setOnAction((ActionEvent e) -> {
            RatingId ratingid = new RatingId();
            Rating rating = (Rating) tbComentsRatings.getSelectionModel().getSelectedItem();
            ratingid.setIdDocument(document.getId());
            ratingid.setIdUser(user.getId());
            rating.setId(ratingid);
            ratingREST.deleteRating(rating.getId());
        });
        cm2.getItems().addAll(cm2Item1);
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
        cm.getItems().addAll(cmItem1, cmItem2, cmItem3);
        stage.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent e) -> {
            if (e.getButton() == MouseButton.SECONDARY) {
                cm.show(stage, e.getScreenX(), e.getScreenY());
            }
        });
        stage.show();

    }

    private void handleWindowShowing(WindowEvent event) {
        //Insertar nombre del documento
        lbNameDocument.setText(document.getName());
        //Insertar nombre del autor
        lbAuthorDocument.setText(" ");
        //Insertar nota media de rating
        lbAvgDocmuent.setText(String.valueOf(document.getTotalRating()));

        tbcolUser.setCellValueFactory(new PropertyValueFactory<>("user"));
        tbcolComent.setCellValueFactory(new PropertyValueFactory<>("review"));
        tbcolRating.setCellValueFactory(new PropertyValueFactory<>("rating"));
        ObservableList<Rating> ratings;
        ratings = FXCollections.observableArrayList(ratingREST.DocumentsRating(new GenericType<List<Rating>>() {
        }, document.getId()));
        tbComentsRatings.setItems(ratings);
        comboRating.getItems().addAll("0", "1", "2", "3", "4", "5");
    }

    private void handleRateAction(ActionEvent event) {
        Rating nRating = new Rating();
        nRating.setDocument(document);
        nRating.setRating(Integer.parseInt(comboRating.getValue().toString()));
        nRating.setReview(txtComent.getText());
        nRating.setRatingDate(Date.valueOf(LocalDate.now()));
        document.setRatingCount(document.getRatingCount() + 1);
        int nTotalrating = document.getTotalRating() + Integer.parseInt(comboRating.getValue().toString());
        document.setTotalRating(nTotalrating / document.getRatingCount());
        docREST.modifyDocument(document, document.getId());
        ratingREST.newDocumentRating(nRating);
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Rating sent");
        alert.setHeaderText("¡Your opinion counts!.");
        Button okButton = (Button) alert.getDialogPane().lookupButton(ButtonType.OK);
        okButton.setId("okbutton");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {
            stage.close();
        } else {
            alert.close();
        }
    }

    private void downloadDocumentRequest(ActionEvent event) {

        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("PDF files (*.pdf)", "*.pdf");
        fileChooser.getExtensionFilters().add(extFilter);
        File fileC = fileChooser.showSaveDialog(stage);

        writeBytesToFile(document.getFile(), fileC);

    }

    private void closeRequest(WindowEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(
                    "/windowsapplication/view/VerUsersCategoriasDocs.fxml"));
            Parent root = (Parent) loader.load();
            AdminWindowController adminWindowController
                    = ((AdminWindowController) loader.getController());
            
            adminWindowController.setStage(stage);
            adminWindowController.setUser(user);
            adminWindowController.initStage(root);
        } catch (IOException ex) {
            Logger.getLogger(InfoDocWindowController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void backButtonRequest(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(
                    "/windowsapplication/view/VerUsersCategoriasDocs.fxml"));
            Parent root = (Parent) loader.load();
            AdminWindowController adminWindowController
                    = ((AdminWindowController) loader.getController());
            
            adminWindowController.setStage(stage);
            adminWindowController.setUser(user);
            adminWindowController.initStage(root);
        } catch (IOException ex) {
            Logger.getLogger(InfoDocWindowController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

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

}
