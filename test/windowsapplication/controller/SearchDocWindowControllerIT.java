/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package windowsapplication.controller;

import javafx.scene.control.ComboBox;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import org.junit.After;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.FixMethodOrder;
import org.junit.runners.MethodSorters;
import org.testfx.api.FxAssert;
import org.testfx.framework.junit.ApplicationTest;
import static org.testfx.matcher.base.NodeMatchers.isVisible;
import windowsapplication.WindowsAppicationReto2;

/**
 *
 * @author Gaizka Andrews
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class SearchDocWindowControllerIT extends ApplicationTest {
    private ComboBox comboCategories;
    
    @Override
    public void start(Stage stage) throws Exception {
        new WindowsAppicationReto2().start(stage);
    }
    
    @Test
    public void testA_allDocs() {
        clickOn("#txtLogin");
        write("aimar");
        clickOn("#txtPass");
        write("12345678A");
        clickOn("#btLogin");
        clickOn("#mDocumentos");
        clickOn("#miBuscarDocs");
        clickOn("#btSearch");
    }

    @Test
    public void testC_NameError() {
        clickOn("#txtLogin");
        write("aimar");
        clickOn("#txtPass");
        write("12345678A");
        clickOn("#btLogin");
        clickOn("#mDocumentos");
        clickOn("#miBuscarDocs");
        clickOn("#txtNameDoc");
        write(" ");
        comboCategories = lookup("#comboCategories").queryComboBox();
        clickOn("#comboCategories");
        push(KeyCode.SPACE);
        push(KeyCode.DOWN);
        clickOn("#datePickerDoc");
        write("8/06/2020");
        clickOn("#btSearch");
        FxAssert.verifyThat("#errorbutton", isVisible());
        clickOn("#errorbutton");
        
    }

    @Test
    public void testD_CategoryError() {
        clickOn("#txtLogin");
        write("aimar");
        clickOn("#txtPass");
        write("12345678A");
        clickOn("#btLogin");
        clickOn("#mDocumentos");
        clickOn("#miBuscarDocs");
        clickOn("#txtNameDoc");
        write("testDoc");
        clickOn("#datePickerDoc");
        write("8/06/2020");
        clickOn("#btSearch");
        FxAssert.verifyThat("#errorbutton", isVisible());
        clickOn("#errorbutton");
        
    }

    @Test
    public void testB_DateError() {
        clickOn("#txtLogin");
        write("aimar");
        clickOn("#txtPass");
        write("12345678A");
        clickOn("#btLogin");
        clickOn("#mDocumentos");
        clickOn("#miBuscarDocs");
        clickOn("#txtNameDoc");
        write("testDoc");
        clickOn("#comboCategories");
        comboCategories = lookup("#comboCategories").queryComboBox();
        clickOn("#comboCategories");
        push(KeyCode.SPACE);
        push(KeyCode.DOWN);
        clickOn("#btSearch");
        FxAssert.verifyThat("#errorbutton", isVisible());
        clickOn("#errorbutton");
        
    }
    @Test
    public void testE_SpecificDoc() {
        clickOn("#txtLogin");
        write("aimar");
        clickOn("#txtPass");
        write("12345678A");
        clickOn("#btLogin");
        clickOn("#mDocumentos");
        clickOn("#miBuscarDocs");
        clickOn("#txtNameDoc");
        write("testDoc");
        clickOn("#comboCategories");
        comboCategories = lookup("#comboCategories").queryComboBox();
        clickOn("#comboCategories");
        push(KeyCode.SPACE);
        push(KeyCode.DOWN);
         clickOn("#datePickerDoc");
        write("8/06/2020");
        clickOn("#btSearch");
        FxAssert.verifyThat("testDoc", isVisible());
    }
}
