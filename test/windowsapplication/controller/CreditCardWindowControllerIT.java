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
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Ignore;
import org.junit.runners.MethodSorters;
import org.testfx.api.FxAssert;
import org.testfx.framework.junit.ApplicationTest;
import static org.testfx.matcher.base.NodeMatchers.isEnabled;
import static org.testfx.matcher.base.NodeMatchers.isVisible;
import windowsapplication.WindowsAppicationReto2;

/**
 * Testing class for payment method view and controller.
 *
 * @author Aimar Arrizabalaga
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class CreditCardWindowControllerIT extends ApplicationTest {

    @Override
    public void start(Stage stage) throws Exception {
        new WindowsAppicationReto2().start(stage);
    }

    /**
     * Cleans all the text fields before every test.
     */
    @After
    public void cleanText() {
        doubleClickOn("#txtCardNumber");
        eraseText(1);
        doubleClickOn("#txtDateMonth");
        eraseText(1);
        doubleClickOn("#txtDateYear");
        eraseText(1);
        doubleClickOn("#txtCVC");
        eraseText(1);
    }

    /**
     * Checks all the text fields are informed.
     */
    @Test
    public void testA_CardNumberIsInformed() {
        // Navigating through the windows.
        clickOn("#txtLogin");
        write("gaizka");
        clickOn("#txtPass");
        write("12345678A");
        clickOn("#btLogin");
        clickOn("#mProfile");
        clickOn("#miDatos");
        clickOn("#btPremium");
        // Cleaning the text fields.
        doubleClickOn("#txtCardNumber");
        eraseText(1);
        doubleClickOn("#txtDateMonth");
        eraseText(1);
        doubleClickOn("#txtDateYear");
        eraseText(1);
        doubleClickOn("#txtCVC");
        eraseText(1);
        // Inform the text fields with test data.
        clickOn("#txtCardNumber");
        write("");
        clickOn("#txtDateMonth");
        write("10");
        clickOn("#txtDateYear");
        write("2021");
        clickOn("#txtCVC");
        write("100");
        clickOn("#btSave");
        // Error alert shows.
        FxAssert.verifyThat("All the fields are required.", isVisible());
        // Close alert.
        clickOn("#button");
    }

    /**
     * Checks all the text fields are informed.
     */
    @Test
    public void testB_ExpireMonthIsInformed() {
        clickOn("#txtCardNumber");
        write("1111111111111111");
        clickOn("#txtDateMonth");
        write("");
        clickOn("#txtDateYear");
        write("2021");
        clickOn("#txtCVC");
        write("100");
        clickOn("#btSave");
        FxAssert.verifyThat("All the fields are required.", isVisible());
        clickOn("#button");
    }

    /**
     * Checks all the text fields are informed.
     */
    @Test
    public void testC_ExpireYearIsInformed() {
        clickOn("#txtCardNumber");
        write("1111111111111111");
        clickOn("#txtDateMonth");
        write("10");
        clickOn("#txtDateYear");
        write("");
        clickOn("#txtCVC");
        write("100");
        clickOn("#btSave");
        FxAssert.verifyThat("All the fields are required.", isVisible());
        clickOn("#button");
    }

    /**
     * Checks all the text fields are informed.
     */
    @Test
    public void testD_CvcIsInformed() {
        clickOn("#txtCardNumber");
        write("1111111111111111");
        clickOn("#txtDateMonth");
        write("10");
        clickOn("#txtDateYear");
        write("2021");
        clickOn("#txtCVC");
        write("");
        clickOn("#btSave");
        FxAssert.verifyThat("All the fields are required.", isVisible());
        clickOn("#button");
    }
}
