/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package windowsapplication.controller;

import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseButton;
import javafx.stage.Stage;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.FixMethodOrder;
import org.junit.runners.MethodSorters;
import org.testfx.api.FxAssert;
import org.testfx.framework.junit.ApplicationTest;
import static org.testfx.matcher.base.NodeMatchers.isInvisible;
import static org.testfx.matcher.base.NodeMatchers.isVisible;
import windowsapplication.WindowsAppicationReto2;
import org.testfx.matcher.control.TableViewMatchers;

/**
 *
 * @author Adrian Corral
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
        FxAssert.verifyThat("aimar", isVisible() );
        FxAssert.verifyThat("gaizka", isVisible() );
        FxAssert.verifyThat("test", isVisible() );
        
    }
    
    @Test
    public void testB_SpecificUser() {
        clickOn("#txtName");
        write("aimar");
        clickOn("#btSearch");
        doubleClickOn("#txtName");
        write(" ");
        FxAssert.verifyThat("aimar", isVisible() );
    }
    
    @Test
    public void testC_AllDocs(){
        clickOn("#btBack");
        clickOn("#mAdmin");
        clickOn("#miAdminDocs");
        clickOn("#btSearch");
        FxAssert.verifyThat("testDoc", isVisible() );
    }
    
    @Test
    public void testD_SpecificDoc(){
        clickOn("#txtName");
        write("testDoc");
        clickOn("#btSearch");
        doubleClickOn("#txtName");
        write(" ");
        FxAssert.verifyThat("testDoc", isVisible() );
        
    }
    
    
    @Test
    public void testE_AllCategories(){
        clickOn("#btBack");
        clickOn("#mAdmin");
        clickOn("#miAdminCategorias");
        clickOn("#btSearch");
        FxAssert.verifyThat("PGR", isVisible() );
        
    }

    @Test
    public void testF_SpecificCategory(){
        clickOn("#txtName");
        write("PGR");
        clickOn("#btSearch");
        doubleClickOn("#txtName");
        write(" ");
        FxAssert.verifyThat("PGR", isVisible() );
    }
    
    @Test
    public void testG_NewCategory(){
        clickOn("#txtAuthor");
        write("nCategory");
        clickOn("#btAddCat");
        FxAssert.verifyThat("#okbutton", isVisible());
        clickOn("#okbutton");
    }
    /*
    @Test
    public void testH_NoNewCategory(){
        clickOn("#txtAuthor");
        write("parvos");
        clickOn("#btAddCat");
        FxAssert.verifyThat("#cancelbutton", isInvisible());
        clickOn("#okbutton");
    }*/
    
    @Test
    public void testI_EditCategory(){
        clickOn("nCategory", MouseButton.SECONDARY);
        clickOn("Edit");
        clickOn("#txtAuthor");
        write("EditCategory");
        clickOn("#btAddCat");
        clickOn("#okbutton");
    }
    /*
    @Test
    public void testJ_NoEditCategory(){
        clickOn("nCategory", MouseButton.SECONDARY);
        clickOn("Edit");
        clickOn("#txtAuthor");
        write("EditCategory");
        clickOn("#btAddCat");
        clickOn("#cancelbutton");
    }
    */
    
}
