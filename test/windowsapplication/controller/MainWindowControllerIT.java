/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package windowsapplication.controller;

import javafx.stage.Stage;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;
import org.testfx.api.FxAssert;
import org.testfx.framework.junit.ApplicationTest;
import static org.testfx.matcher.base.NodeMatchers.isVisible;
import static org.testfx.matcher.control.LabeledMatchers.hasText;
import windowsapplication.WindowsAppicationReto2;

/**
 *
 * @author aimar
 */
public class MainWindowControllerIT extends ApplicationTest {

    @Override
    public void start(Stage stage) throws Exception {
        new WindowsAppicationReto2().start(stage);
    }

    @Before
    public void setUpWindow() {
        clickOn("#txtLogin");
        write("aimar");
        clickOn("#txtPass");
        write("12345678A");
        clickOn("#btLogin");
        FxAssert.verifyThat("#lbUser", hasText("Aimar Arrizabalaga"));
    }
    
    @Test
    public void checkWindowShowsDataFromUser() {
        FxAssert.verifyThat("#lbUser", hasText("Aimar Arrizabalaga"));
        FxAssert.verifyThat("#lbPrivilege", hasText("PREMIUM"));
    }
    
    @Test
    public void checkLogOutButton() {
        clickOn("#btLogOut");
        FxAssert.verifyThat("#btLogin", isVisible());
    }
    
    @Test
    public void profileMenuNavigationWorks() {
        clickOn("#mProfile");
        clickOn("#miDatos");
    }
    
    @Test
    public void documentMenuNavigationWorks() {
        clickOn("#mDocuments");
        clickOn("#miBuscarDocs");
        FxAssert.verifyThat("#btSearch", isVisible());
        clickOn("#btBack");
        clickOn("#okButton");
        clickOn("#mDocuments");
        clickOn("#miSubirDocs");
        FxAssert.verifyThat("#btUpload", isVisible());
    }

}
