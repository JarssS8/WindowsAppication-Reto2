/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package windowsapplication.controller;

import javafx.scene.input.MouseButton;
import javafx.stage.Stage;
import org.junit.Before;
import org.junit.Test;
import org.junit.FixMethodOrder;
import org.junit.Ignore;
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
    
    @Before
    public void setUp() {
        clickOn("#txtLogin");
        write("mikel");
        clickOn("#txtPass");
        write("12345678A");
        clickOn("#btLogin");
        clickOn("#mProfile");
        clickOn("#miDatos");
    }
    
    @Ignore
    public void testA_checkTextFieldsAreNotVisible() {
        FxAssert.verifyThat("#txtNewEmail", isInvisible());
        FxAssert.verifyThat("#txtNewFullname", isInvisible());
        FxAssert.verifyThat("#txtCurrentPassword", isInvisible());
        FxAssert.verifyThat("#txtNewPassword", isInvisible());
        FxAssert.verifyThat("#txtNewPasswordRepeat", isInvisible());
        FxAssert.verifyThat("#btSave", isInvisible());
        FxAssert.verifyThat("#btPremium", isVisible());
        FxAssert.verifyThat("#btEdit", isVisible());
        FxAssert.verifyThat("#btBack", isVisible());
    }
    
    @Ignore
    public void testB_checkEmailFormatInvalid() {
        clickOn("#btEdit");
        clickOn("#txtNewEmail");
        write("test@gmail");
        clickOn("#btSave");
        FxAssert.verifyThat("The email format is not allowed!", isVisible());
        clickOn("#okButton");
    }
    
    @Ignore
    public void testC_checkFullnameFormatInvalid() {
        clickOn("#btEdit");
        clickOn("#txtNewFullname");
        write("XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX");
        clickOn("#btSave");
        FxAssert.verifyThat("Full name if too long!", isVisible());
        clickOn("#okButton");
    }

    @Test
    public void testD_check_btDelete_on_context_menu() {
        clickOn(1054.0, 287.0, MouseButton.SECONDARY);
        clickOn("Delete");
        FxAssert.verifyThat("#okButton", isVisible());
        clickOn("#okButton");
        FxAssert.verifyThat("#okButton", isVisible());
        clickOn("#okButton");
        FxAssert.verifyThat("#btLogOut", isVisible());
        clickOn("#btLogOut");
        clickOn("#txtLogin");
        write("mikel");
        clickOn("#txtPass");
        write("12345678A");
        clickOn("#btLogin");
        FxAssert.verifyThat("#okButton", isVisible());
        
    }
    
    @Ignore
    public void testE_check_btBack_on_context_menu() {
        clickOn(1054.0, 287.0, MouseButton.SECONDARY);
        clickOn("Go back");
        FxAssert.verifyThat("#btLogOut", isVisible());
    }
    
    @Ignore
    public void testF_check_btEdit_on_context_menu() {
        clickOn(1054.0, 287.0, MouseButton.SECONDARY);
        clickOn("Edit");
        FxAssert.verifyThat("#btSave", isVisible());
    }
}
