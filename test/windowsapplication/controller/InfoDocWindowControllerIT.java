/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package windowsapplication.controller;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.util.ResourceBundle;
import javafx.scene.control.ComboBox;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.stage.Stage;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.testfx.api.FxAssert;
import org.testfx.framework.junit.ApplicationTest;
import static org.testfx.matcher.base.NodeMatchers.isVisible;
import static org.testfx.matcher.control.LabeledMatchers.hasText;
import windowsapplication.WindowsAppicationReto2;

/**
 *
 * @author Gaizka Andres
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class InfoDocWindowControllerIT extends ApplicationTest {

    private static final String urlFichero
        = ResourceBundle.getBundle("windowsapplication.controller.testConf").getString("DOWNLOAD");
    private ComboBox comboRating;

    @Override
    public void start(Stage stage) throws Exception {
        new WindowsAppicationReto2().start(stage);
    }

    /**
     * Permite poner la ruta de un fichero en el fileChooser.
     *
     * @param filePath El path del fichero a leer.
     */
    private void applyPath(String filePath) {
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        StringSelection stringSelection = new StringSelection(filePath);
        clipboard.setContents(stringSelection, stringSelection);
        press(KeyCode.CONTROL).press(KeyCode.V).release(KeyCode.V).release(KeyCode.CONTROL);
        push(KeyCode.ENTER);
    }

    @Test
    public void TestA_DataCharge() {
        clickOn("#txtLogin");
        write("aimar");
        clickOn("#txtPass");
        write("12345678A");
        clickOn("#btLogin");
        clickOn("#mAdmin");
        clickOn("#miAdminDocs");
        clickOn("#btSearch");
        clickOn("testDoc");
        FxAssert.verifyThat("#lbNameDocument", hasText("testDoc"));
    }

    @Test
    public void TestB_NewRate() {
        clickOn("#txtComent");
        write("Muy malo");
        comboRating = lookup("#comboRating").queryComboBox();
        clickOn("#comboRating");
        push(KeyCode.SPACE);
        push(KeyCode.DOWN);
        clickOn("#btRate");
        FxAssert.verifyThat("#okbutton", isVisible());
        clickOn("#okbutton");
    }

    @Test
    public void TestC_Edit() {

        //clickOn(  , MouseButton.SECONDARY);
        clickOn(1054.0, 287.0, MouseButton.SECONDARY);
        clickOn("Edit");
        FxAssert.verifyThat("#txtNewName", isVisible());
    }

    @Test
    public void TestD_NewName() {
        clickOn("#txtNewName");
        write("NewName");
        clickOn("#btChange");
        FxAssert.verifyThat("#okbutton", isVisible());
        clickOn("#okbutton");
    }

    @Test
    public void TestE_Download() {
        clickOn(1054.0, 287.0, MouseButton.SECONDARY);
        clickOn("Download PDF");
        applyPath(urlFichero);
        
    }

    @Test
    public void TestF_Delete() {
        clickOn(1054.0, 287.0, MouseButton.SECONDARY);
        clickOn("Delete document");
        FxAssert.verifyThat("#okbutton", isVisible());
        clickOn("#okbutton");
    }
}
