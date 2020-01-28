/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package windowsapplication.controller;

import javafx.scene.input.MouseButton;
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
public class AdminWindowControllerIT extends ApplicationTest {
    
    @Override
    public void start(Stage stage) throws Exception {
        new WindowsAppicationReto2().start(stage);
    }

    @Test
    public void testA_AllUsers() {
        clickOn("#txtLogin");
        write("aimar");
        clickOn("#txtPass");
        write("12345678A");
        clickOn("#btLogin");
        clickOn("#mAdmin");
        clickOn("#miAdminUsuarios");
        clickOn("#btSearch");
    }
    
    @Test
    public void testB_SpecificUser() {
        clickOn("#txtName");
        write("aimar");
        clickOn("#btSearch");
    }
    
    @Test
    public void testC_AllDocs(){
        clickOn("#btBack");
        clickOn("#mAdmin");
        clickOn("#miAdminDocs");
        clickOn("#btSearch");
    }
    
    @Test
    public void testD_SpecificDoc(){
        clickOn("#txtName");
        write("Orni");
        clickOn("#btSearch");
    }
    
    
    @Test
    public void testE_AllCategories(){
        clickOn("#btBack");
        clickOn("#mAdmin");
        clickOn("#miAdminCategorias");
        clickOn("#btSearch");
    }

    @Test
    public void testF_SpecificCategory(){
        clickOn("#txtName");
        write("Ornitorrinco");
        clickOn("#btSearch");
    }
    
    @Test
    public void testG_NewCategory(){
        clickOn("#txtAuthor");
        write("nCategory");
        clickOn("#btAddCat");
        clickOn("#okbutton");
    }
    
    @Test
    public void testH_EditCategory(){
        //Preguntar por click derecho
    }
    
}
