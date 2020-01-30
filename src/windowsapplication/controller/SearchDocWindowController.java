/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package windowsapplication.controller;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
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
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.paint.Paint;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javax.ws.rs.core.GenericType;
import windowsapplication.beans.Category;
import windowsapplication.beans.Document;
import windowsapplication.service.CategoryClientREST;
import windowsapplication.service.DocumentClientREST;

public class SearchDocWindowController {

    /**
     * Table of Documents
     */
    @FXML
    private TableView tableDocs;
    /**
     * Column of the document´s name
     */
    @FXML
    private TableColumn tbcolName;
    /**
     * Columns of the document´s category
     */
    @FXML
    private TableColumn tbcolCategory;
    /**
     * Column of the document´s total rating
     */
    @FXML
    private TableColumn tbcolAuthor;
    /**
     * Column of the document´s upload date
     */
    @FXML
    private TableColumn tbcolDate;
    /**
     * Label to put the search tips
     */
    @FXML
    private Label lbParameter;
    /**
     * Button to search data
     */
    @FXML
    private Button btSearch;
    /**
     * Text Field to put the document´s name
     */
    @FXML
    private TextField txtNameDoc;
    /**
     * Date picker to select a upload date
     */
    @FXML
    private DatePicker datePickerDoc;
    /**
     * Combo Box to charge all the categories
     */
    @FXML
    private ComboBox comboCategories;
    /**
     * Button to close the window
     */
    @FXML
    private Button btBack;

    private Stage stage;
    /**
     * Client Rest of Document
     */
    private DocumentClientREST docREST = new DocumentClientREST();
    /**
     * Client Rest of Category
     */
    private CategoryClientREST catREST = new CategoryClientREST();

    /**
     * Sets the Stage object related to this controller.
     *
     * @param stage The Stage object to be initialized.
     */
    void setStage(Stage stage) {
        this.stage = stage;
    }

    void initStage(Parent root) {
        Scene scene = new Scene(root);
        stage = new Stage();
        stage.setScene(scene);
        stage.setTitle("Search a document");
        stage.setResizable(false);
        stage.setOnCloseRequest(this::closeRequest);
        btSearch.setOnAction(this::searchButtonRequest);
        btBack.setOnAction(this::backButtonRequest);
        stage.setOnShowing(this::handleWindowShowing);

        stage.show();

    }

    /**
     * Initializes window state. Create the factories of the document´s table
     *
     * @param event The window event
     */
    private void handleWindowShowing(WindowEvent event) {
        tbcolName.setCellValueFactory(new PropertyValueFactory<>("name"));
        tbcolCategory.setCellValueFactory(new PropertyValueFactory<>("category"));
        tbcolAuthor.setCellValueFactory(new PropertyValueFactory<>("totalRating"));
        tbcolDate.setCellValueFactory(new PropertyValueFactory<>("uploadDate"));
        comboCategories.getItems().addAll(catREST.findAllCategories(new GenericType<List<Category>>() {
        }));

    }

    /**
     * Action when the search button is pressed
     *
     * @param event
     */
    private void searchButtonRequest(ActionEvent event) {
        /**
         * Check if the parameters are empty
         */
        if (txtNameDoc.getText().trim().isEmpty() && comboCategories.getValue() == null && datePickerDoc.getValue() == null) {
            ObservableList<Document> documents;
            documents = FXCollections.observableArrayList(docREST.findAllDocuments(new GenericType<List<Document>>() {
            }));
            tableDocs.setItems(documents);

        } else {
            /**
             * Check search validations
             */
            if (searchValidations()) {

                LocalDate pick = datePickerDoc.getValue();
                Instant instant = Instant.from(pick.atStartOfDay(ZoneId.of("GMT")));
                Date pickerdate = Date.from(instant);
                SimpleDateFormat formatter = new SimpleDateFormat("");
                formatter.format(pickerdate);
                ObservableList<Document> documentsTF
                        = FXCollections.observableArrayList(docREST.findDocumentNameByParameters(new GenericType<List<Document>>() {
                        },
                                txtNameDoc.getText(),
                                comboCategories.getValue().toString()));

                tableDocs.getItems().addAll(documentsTF);

            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("ERROR");
                alert.setHeaderText("Searching failed");
                alert.setContentText("All the fields are required");
                Button errorButton = (Button) alert.getDialogPane().lookupButton(ButtonType.OK);
                errorButton.setId("errorbutton");
                Optional<ButtonType> result = alert.showAndWait();
                if (result.get() == ButtonType.YES) {
                    alert.close();
                }
            }
        }

    }

    /**
     * Check validations search
     *
     * @return boolean saying if you have passed the validations
     */
    private boolean searchValidations() {
        Boolean todoOk = false, nameOk = false, catOk = false, dateOk = false;
        if (txtNameDoc.getLength() > 1 && txtNameDoc.getLength() < 50) {
            nameOk = true;
        }
        if (comboCategories.getValue() != null) {
            catOk = true;
        }
        if (datePickerDoc.getValue() == null) {
            dateOk = false;
        }
        if (nameOk && catOk && dateOk) {
            todoOk = true;
        }
        return todoOk;
    }

    /**
     * Action when the button is pressed
     *
     * @param event
     */
    private void closeRequest(WindowEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Close confirmation");
        alert.setHeaderText("You pressed the 'Close'. \n"
                + "Document search will be cancelled.");
        alert.setContentText("Are you sure?");
        alert.getButtonTypes().setAll(ButtonType.YES, ButtonType.NO);
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.YES) {
            stage.close();
        } else {
            event.consume();
        }
    }

    /**
     * Action when the close button is pressed
     *
     * @param event
     */
    private void backButtonRequest(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setHeaderText("Close confirmation");
        alert.setContentText("Are you sure that want to stop searching?");
        alert.initOwner(stage);
        alert.initModality(Modality.WINDOW_MODAL);
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {
            stage.close();
        } else {
            event.consume();
        }
    }
}
