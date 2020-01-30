/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package windowsapplication.controller;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.io.File;
import java.util.ResourceBundle;
import javafx.scene.control.ComboBox;
import javafx.scene.input.KeyCode;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.junit.After;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.FixMethodOrder;
import org.junit.runners.MethodSorters;
import org.testfx.api.FxAssert;
import org.testfx.framework.junit.ApplicationTest;
import static org.testfx.matcher.base.NodeMatchers.isInvisible;
import static org.testfx.matcher.base.NodeMatchers.isVisible;
import windowsapplication.WindowsAppicationReto2;

/**
 *
 * @author Gaizka Andrews
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class UploadDocWindowControllerIT extends ApplicationTest {

    private static final String urlFichero
        = ResourceBundle.getBundle("windowsapplication.controller.testConf").getString("URL");
    private ComboBox ComboCategories;
    private FileChooser fileChooser;

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

    @After
    public void cleanText() {

        doubleClickOn("#txtNameDoc");
        eraseText(1);
    }

    @Test
    public void testC_NoName() {
        
        ComboCategories = lookup("#comboCategories").queryComboBox();
        clickOn("#comboCategories");
        push(KeyCode.SPACE);
        push(KeyCode.DOWN);

        clickOn("#btSelectFile");
        applyPath(urlFichero);
        clickOn("#btUpload");
        FxAssert.verifyThat("#errorbutton", isVisible());
        clickOn("#errorbutton");
    }

    @Test
    public void testA_NoCategory() {
        clickOn("#txtLogin");
        write("aimar");
        clickOn("#txtPass");
        write("12345678A");
        clickOn("#btLogin");
        clickOn("#mDocumentos");
        clickOn("#miSubirDocs");
        clickOn("#txtNameDoc");
        write("nDoc");
        clickOn("#btSelectFile");
        applyPath(urlFichero);
        clickOn("#btUpload");
        FxAssert.verifyThat("#errorbutton", isVisible());
        clickOn("#errorbutton");
    }

    @Test
    public void testB_NoFile() {
        fileChooser.setInitialDirectory(new File(""));
        clickOn("#txtNameDoc");
        write("nDoc");
        ComboCategories = lookup("#comboCategories").queryComboBox();
        clickOn("#comboCategories");
        push(KeyCode.SPACE);
        push(KeyCode.DOWN);
        clickOn("#btUpload");
        FxAssert.verifyThat("#errorbutton", isVisible());
        clickOn("#errorbutton");
    }

    @Test
    public void testD_AllOk() {
        clickOn("#txtNameDoc");
        write("nDoc");

        clickOn("#btSelectFile");
        applyPath(urlFichero);
        clickOn("#btUpload");
        FxAssert.verifyThat("#okbutton", isInvisible());
        clickOn("#okbutton");
    }

}
