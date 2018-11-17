package com.web_application_development.evoting;

import io.github.bonigarcia.wdm.ChromeDriverManager;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.concurrent.TimeUnit;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class EVotingApplicationTests {

    private final String URL = "https://evalimised.herokuapp.com/";
    private WebDriver driver;

    @BeforeClass
    public static void setUpClass() {
        ChromeDriverManager.getInstance().setup();
    }

    @Before
    public void setUp() {
        driver = new ChromeDriver();
        driver.get(URL);
        logIn(driver);
    }

    @After
    public void tearDown() {
        logOut();
        driver.close();
    }

    @Test
    public void contextLoads() {
    }

    @Test
    public void failLogInWithAccountNotFound() {
        failLogInWith("12345", "Account not found!");
    }

    @Test
    public void failLogInWithUserRefused() {
        failLogInWith("10101010016", "User cancelled Smart-ID request!");
    }

    @Test
    public void realTimeVotes() {
        String parentWindow = driver.getWindowHandle();
        ChromeDriver secondDriver = new ChromeDriver();
        logIn(secondDriver);
        vote(secondDriver);
        secondDriver.close();
        driver.switchTo().window(parentWindow);
        new WebDriverWait(driver, 1000).until(ExpectedConditions.presenceOfElementLocated(By.id("vote-notifications-container")));
        assertTrue(driver.findElement(By.id("vote-notifications-container")).getText().contains("+1"));
    }

    @Test
    public void vote() {
        vote(driver);
        driver.manage().timeouts().implicitlyWait(1, TimeUnit.SECONDS);
        driver.navigate().refresh();
        new WebDriverWait(driver, 1000).until(ExpectedConditions.presenceOfElementLocated(By.id("take-back-vote-button")));
        assertNotEquals(0, driver.findElements(By.id("take-back-vote-button")).size());
    }

    @Test
    public void candidacy() {
        driver.get(URL + "candidacy");
        try {
            driver.findElement(By.id("take-back-candidacy-button")).click();
        } catch (NoSuchElementException e) {
            System.out.println("No active candidacy");
        }
        assertEquals(0, driver.findElements(By.id("take-back-candidacy-button")).size());
        driver.findElement(By.id("submit-candidacy-button")).click();
        assertNotEquals(0, driver.findElements(By.id("take-back-candidacy-button")).size());
    }

    @Test
    public void searchForCandidate() {
        driver.findElement(By.id("candidateTable_filter")).findElement(By.className("form-control")).sendKeys("Demo");
        assertTrue("Couldn't find candidate", driver.findElement(By.id("candidateTable_wrapper")).getText().contains("Demo"));
    }

    @Test
    public void searchForParty() {
        confirmHomePageSearchResultsWithAssertFalse("Erakond Eesti Normaalsed", "Found a party that shouldn't be displayed", "Ükskõiksuserakond");
    }

    @Test
    public void searchForRegion() {
        confirmHomePageSearchResultsWithAssertFalse("Põhja-Eesti", "Found a region that shouldn't be displayed", "Lääne-Eesti");
    }

    @Test
    public void filterVotingResultsByRegion() {
        confirmStatisticsSearchResultsWithAssertFalse("Lääne-Eesti", "Found a region that shouldn't be displayed", "Põhja-Eesti");
    }

    @Test
    public void filterVotingResultsByParty() {
        confirmStatisticsSearchResultsWithAssertFalse("Ükskõiksuserakond", "Found a party that shouldn't be displayed", "Fašistlik erakond");
    }

    @Test
    public void filterVotingResultsThroughoutTheCountry() {
        driver.get(URL + "statistics");
        assertTrue(driver.findElement(By.id("piechart")).getText().contains("Ükskõiksuserakond"));
    }

    private void logIn(WebDriver driver) {
        driver.get(URL + "login");
        driver.findElement(By.id("nationalIdentityNumber")).sendKeys("10101010005");
        driver.findElement(By.className("btn")).click();
        new WebDriverWait(driver, 1000).until(ExpectedConditions.titleContains("Home"));
        assertEquals(URL, driver.getCurrentUrl());
    }

    private void logOut() {
        driver.findElement(By.className("dropdown-toggle")).click();
        driver.findElement(By.className("linkButton")).click();
        assertEquals(URL + "login", driver.getCurrentUrl());
    }

    private void confirmStatisticsSearchResultsWithAssertFalse(String keysToSend, String message, String condition) {
        driver.get(URL + "statistics");
        driver.findElement(By.id("votesTable_filter")).findElement(By.className("form-control")).sendKeys(keysToSend);
        assertFalse(message, driver.findElement(By.id("votesTable_wrapper")).getText().contains(condition));
    }

    private void confirmHomePageSearchResultsWithAssertFalse(String keysToSend, String message, String condition) {
        driver.findElement(By.id("candidateTable_filter")).findElement(By.className("form-control")).sendKeys(keysToSend);
        assertFalse(message, driver.findElement(By.id("candidateTable_wrapper")).getText().contains(condition));
    }

    private void failLogInWith(String identityCode, String errorMessage) {
        logOut();
        driver.get(URL + "login");
        driver.findElement(By.id("nationalIdentityNumber")).sendKeys(identityCode);
        driver.findElement(By.className("btn")).click();
        new WebDriverWait(driver, 1000).until(ExpectedConditions.visibilityOf(driver.findElement(By.id("error-text"))));
        assertEquals(errorMessage, driver.findElement(By.id("error-text")).getText());
        logIn(driver);
    }


    private void vote(WebDriver driver) {
        try {
            driver.findElement(By.id("take-back-vote-button")).click();
        } finally {
            assertEquals(0, driver.findElements(By.id("take-back-vote-button")).size());
            driver.findElement(By.id("vote-button")).click();
        }
    }

}
