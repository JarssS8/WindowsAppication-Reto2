/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package windowsapplication.controller;

import javafx.stage.Stage;
import org.junit.After;
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
public class UploadDocWindowControllerIT extends ApplicationTest {

    @Override
    public void start(Stage stage) throws Exception {
        new WindowsAppicationReto2().start(stage);
    }
    
    @After
    public void cleanText() {
        
        doubleClickOn("#txtNameDoc");
        eraseText(1);
    }
    
    @Test
    public void testA_NoName() {
        clickOn("#txtLogin");
        write("aimar");
        clickOn("#txtPass");
        write("12345678A");
        clickOn("#btLogin");
        clickOn("#mDocumentos");
        clickOn("#miSubirDocs");
        clickOn("#comboCategories");
        //clickOn("#btSelectFile");
        clickOn("#btUpload");
        clickOn("#errorbutton");
    }

    @Test
    public void testB_NoCategory() {;
        clickOn("#txtNameDoc");
        write("nDoc");
        //clickOn("#btSelectFile");
        clickOn("#btUpload");
        clickOn("#errorbutton");
    }

    @Test
    public void testC_NoFile() {
        clickOn("#txtNameDoc");
        write("nDoc");
        clickOn("#comboCategories");
        clickOn("#btUpload");
        clickOn("#errorbutton");
    }

    @Test
    public void testD_AllOk() {
        clickOn("#txtNameDoc");
        write("nDoc");
        clickOn("#comboCategories");
        //clickOn("#btSelectFile");
        clickOn("#btUpload");
        clickOn("#errorbutton");
    }
}
