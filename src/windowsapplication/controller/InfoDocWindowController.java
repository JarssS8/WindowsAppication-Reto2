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
    private Label lbNewName;
    @FXML
    private TextField txtNewName;
    @FXML
    private Button btDownloadDocument;
    @FXML
    private Label lbAvgDocmuent;
    @FXML
    private Button btClose;
    @FXML
    private Button btChange;

    private Stage stage;

    private Document document;

    private User user;

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

        stage = new Stage();
        stage.setScene(scene);
        stage.setTitle("Info of the document");
        stage.setResizable(false);
        stage.setOnShowing(this::handleWindowShowing);
        stage.setOnCloseRequest(this::closeRequest);
        btRate.setOnAction(this::handleRateAction);
        btDownloadDocument.setOnAction(this::downloadDocumentRequest);
        btClose.setOnAction(this::backButtonRequest);
        btChange.setOnAction(this::changeButtonRequest);
        tbcolUser.setCellValueFactory(new PropertyValueFactory<>("ratingDate"));
        tbcolComent.setCellValueFactory(new PropertyValueFactory<>("review"));
        tbcolRating.setCellValueFactory(new PropertyValueFactory<>("rating"));
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

        final ContextMenu cm = new ContextMenu();
        MenuItem cmItem1 = new MenuItem("Go back");
        MenuItem cmItem2 = new MenuItem("Delete document");
        MenuItem cmItem3 = new MenuItem("Download PDF");
        MenuItem cmItem4 = new MenuItem("Edit");
        cmItem1.setOnAction((ActionEvent e) -> {
            stage.close();
        });
        final ContextMenu cm2 = new ContextMenu();
        MenuItem cm2Item1 = new MenuItem("Delete rating");
        cm2Item1.setOnAction((ActionEvent e) -> {
            RatingId ratingId = new RatingId();
            ratingId.setIdDocument(document.getId());
            ratingId.setIdUser(user.getId());
            ratingREST.deleteRating(ratingId);
        });
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

    private void handleWindowShowing(WindowEvent event) {
        //Insertar nombre del documento
        lbNameDocument.setText(document.getName());
        //Insertar nota media de rating
        lbAvgDocmuent.setText(String.valueOf(document.getTotalRating()));

    }

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
        ratingREST.newDocumentRating(nRating);
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Rating sent");
        alert.setHeaderText("¡Your opinion counts!");
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
        stage.close();
    }

    private void backButtonRequest(ActionEvent event) {
        stage.close();
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

    private void changeButtonRequest(ActionEvent event) {
        Document nDocument = this.document;
        nDocument.setName(txtNewName.getText());
        docREST.modifyDocument(nDocument, nDocument.getId());
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Document sent");
        alert.setHeaderText("Name changed");
        Button okButton = (Button) alert.getDialogPane().lookupButton(ButtonType.OK);
        okButton.setId("okbutton");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {
            stage.close();
        } else {
            alert.close();
        }
    }

}
