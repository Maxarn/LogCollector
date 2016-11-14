/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test_selenium;

import java.util.concurrent.TimeUnit;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.FixMethodOrder;
import org.junit.runners.MethodSorters;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

/**
 *
 * @author josanbir
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class GUIJUnitTest {
    private static WebDriver driver = null;
    
    public GUIJUnitTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
        
        System.setProperty("webdriver.gecko.driver", "src/test_selenium/geckodriver");
        driver = new FirefoxDriver();
// Wait For Page To Load

        driver.manage().timeouts().implicitlyWait(60, TimeUnit.SECONDS);
// Go to URL

        driver.get("http://localhost:8080/LogAggregatorServer/");
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(100, TimeUnit.SECONDS);
    }
    
    @After
    public void tearDown() {
        System.out.println(" Logout Successfully");

    driver.quit();
    }

    // TODO add test methods here.
    // The methods must be annotated with annotation @Test. For example:
    //
    @Test
     public void Login_test() {
         Login_page.txtbx_UserName(driver).sendKeys("admin");

     Login_page.txtbx_Password(driver).sendKeys("admin");

     Login_page.btn_LogIn(driver).click();
     
     String verifytext = driver.findElement(By.xpath("/html/body/div[1]/div/div[2]/div[2]/div/div[2]/div/div[2]/div")).getText();
     Assert.assertTrue(verifytext.toLowerCase().contains("your log"));
     System.out.println(" Login Successfully" +  verifytext);
     
     ManageCompaniesPage.Manage_Companies(driver).click();
     
     String verify_manage_comp = ManageCompaniesPage.entered_Manage_Companies(driver).getText();
     Assert.assertTrue(verify_manage_comp.toLowerCase().contains("create company"));
     System.out.println("entered manage companies page" +  verify_manage_comp);
     
     
     ManageCompaniesPage.Create_Company(driver).click();
     
     String verify_enter_cc = ManageCompaniesPage.entered_create_Company_grid(driver).getText();
     Assert.assertTrue(verify_enter_cc.toLowerCase().contains("company name"));
     System.out.println("entered create company grid" +  verify_enter_cc);
     
     ManageCompaniesPage.Enter_Company_name(driver).sendKeys("enp");
     ManageCompaniesPage.Enter_Website(driver).sendKeys("enp.se");
     ManageCompaniesPage.Enter_Details(driver).sendKeys("soft");
     ManageCompaniesPage.Click_create(driver).click();
     ManageCompaniesPage.Click_back(driver).click();
     ManageCompaniesPage.Click_back_again(driver).click();
     ManageUserPage.Logout(driver).click();
     
     }
     
     
     @Test
     public void ManageUserPage_Test(){
         
     Login_page.txtbx_UserName(driver).sendKeys("admin");

     Login_page.txtbx_Password(driver).sendKeys("admin");

     Login_page.btn_LogIn(driver).click();
     
     String verifytext = driver.findElement(By.xpath("/html/body/div[1]/div/div[2]/div[2]/div/div[2]/div/div[2]/div")).getText();
     Assert.assertTrue(verifytext.toLowerCase().contains("your log"));
     System.out.println(" Login Successfully" +  verifytext);
      
     
    ManageUserPage.Managa_User(driver).click();
     
     String verify_crp = driver.findElement(By.xpath("//span[contains(text(), 'Create user')]")).getText();
     Assert.assertTrue(verify_crp.toLowerCase().contains("create user"));
     System.out.println(" entered manage users page" +  verify_crp);
     
     ManageUserPage.Create_User(driver).click();
     
     String enter_crp = driver.findElement(By.xpath("/html/body/div[1]/div/div[2]/div[2]/div/div[1]/div/div[2]/div")).getText();
     Assert.assertTrue(enter_crp.toLowerCase().contains("create user"));
     System.out.println(" entered create users menu" +  enter_crp);
     
     
     ManageUserPage.First_name(driver).sendKeys("chu");
     ManageUserPage.last_name(driver).sendKeys("chu");
     ManageUserPage.E_mail(driver).sendKeys("chuchu@gmail");
     ManageUserPage.User_name(driver).sendKeys("chuchu");
     ManageUserPage.Pass_word(driver).sendKeys("chuchu");
     ManageUserPage.ConfirmPass_word(driver).sendKeys("chuchu");
     ManageUserPage.Select_usergroup(driver).click();
     //ManageUserPage.Administrator(driver).click();
     ManageUserPage.Regular_user(driver).click();
     ManageUserPage.just_click_anywhere(driver).click();
     ManageUserPage.Create(driver).click();
     ManageUserPage.Back(driver).click();
     ManageUserPage.Back_frm_userspage(driver).click();
     ManageUserPage.Logout(driver).click(); 
     }


     
     
}
