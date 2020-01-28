/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package windowsapplication.controller;

import javafx.stage.Stage;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.FixMethodOrder;
import org.junit.runners.MethodSorters;
import org.testfx.framework.junit.ApplicationTest;
import windowsapplication.WindowsAppicationReto2;

/**
 *
 * @author Gaizka Andrews
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class SearchDocWindowControllerIT extends ApplicationTest {

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
    public void testB_NameError() {
        clickOn("#comboCategories");
        clickOn("#datePickerDoc");
        clickOn("#btSearch");
    }

    @Test
    public void testC_CategoryError() {
        clickOn("#txtNameDoc");
        write("testDoc");
        clickOn("#datePickerDoc");
        clickOn("#btSearch");
    }

    @Test
    public void testD_DateError() {
        clickOn("#txtNameDoc");
        write("testDoc");
        clickOn("#comboCategories");
        clickOn("#btSearch");
    }
}
