/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package windowsapplication.controller;

import javafx.stage.Stage;
import org.junit.Test;
import org.junit.FixMethodOrder;
import org.junit.runners.MethodSorters;
import org.testfx.api.FxAssert;
import org.testfx.framework.junit.ApplicationTest;
import static org.testfx.matcher.base.NodeMatchers.isInvisible;
import static org.testfx.matcher.base.NodeMatchers.isVisible;
import windowsapplication.WindowsAppicationReto2;

/**
 *
 * @author aimar
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ProfileWindowControllerIT extends ApplicationTest {

    @Override
    public void start(Stage stage) throws Exception {
        new WindowsAppicationReto2().start(stage);
    }
    
    @Test
    public void testA_checkTextFieldsAreNotVisible() {
        clickOn("#txtLogin");
        write("gaizka");
        clickOn("#txtPass");
        write("12345678A");
        clickOn("#btLogin");
        clickOn("#mProfile");
        clickOn("#miDatos");
        FxAssert.verifyThat("#txtNewEmail", isInvisible());
        FxAssert.verifyThat("#txtNewFullname", isInvisible());
        FxAssert.verifyThat("#btSave", isInvisible());
    }
    
    @Test
    public void testB_checkEmailFormatInvalid() {
        clickOn("#btEdit");
        clickOn("#txtNewEmail");
        write("test@gmail");
        clickOn("#btSave");
        FxAssert.verifyThat("The email format is not allowed!", isVisible());
        clickOn("#okButton");
    }
    
    @Test
    public void testC_checkFullnameFormatInvalid() {
        clickOn("#btEdit");
        clickOn("#txtNewFullname");
        write("qqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqq");
        clickOn("#btSave");
        FxAssert.verifyThat("Full name if too long!", isVisible());
        clickOn("#okButton");
    }

}
