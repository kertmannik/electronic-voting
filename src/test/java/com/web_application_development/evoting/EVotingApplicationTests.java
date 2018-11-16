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

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class EVotingApplicationTests {

    final String url = "https://evalimised.herokuapp.com/";
    private WebDriver driver;

    @BeforeClass
    public static void setUpClass() {
        ChromeDriverManager.getInstance().setup();
    }

    @Before
    public void setUp() {
        driver = new ChromeDriver();
        driver.get(url);
        logIn();
    }

    @After
    public void tearDown() {
        logOut();
        driver.close();
    }

    @Test
    public void contextLoads() {
    }

    private void logOut() {
        driver.findElement(By.className("dropdown-toggle")).click();
        driver.findElement(By.className("linkButton")).click();
        assertEquals(url + "login", driver.getCurrentUrl());

    }

    private void logIn() {
        logIn(driver);
        assertEquals(url, driver.getCurrentUrl());
    }

    private void logIn(WebDriver driver) {
        driver.get(url + "login");
        driver.findElement(By.id("nationalIdentityNumber")).sendKeys("10101010005");
        driver.findElement(By.className("btn")).click();
        new WebDriverWait(driver, 1000).until(ExpectedConditions.titleContains("Home"));
    }

    private void failLogInWith(String identityCode, String errorMessage) {
        logOut();
        driver.get(url + "login");
        driver.findElement(By.id("nationalIdentityNumber")).sendKeys(identityCode);
        driver.findElement(By.className("btn")).click();
        new WebDriverWait(driver, 1000).until(ExpectedConditions.visibilityOf(driver.findElement(By.id("error-text"))));
        assertEquals(errorMessage, driver.findElement(By.id("error-text")).getText());
        logIn();
    }

    @Test
    public void failLogInWithAccountNotFound() {
        failLogInWith("12345", "Account not found!");
    }

    @Test
    public void failLogInWithUserRefused() {
        failLogInWith("10101010016", "User cancelled Smart-ID request!");
    }

    private void vote(WebDriver driver) {
        try {
            driver.findElement(By.id("take-back-vote-button")).click();
        } catch (NoSuchElementException e) {
            System.out.println("No active vote");
        }
        assertEquals(0, driver.findElements(By.id("take-back-vote-button")).size());
        driver.findElement(By.id("vote-button")).click();
    }

    @Test
    public void realTimeVotes() {
        String parentWindow = driver.getWindowHandle();
        ChromeDriver secondDriver = new ChromeDriver();
        logIn(secondDriver);
        vote(secondDriver);
        secondDriver.close();
        driver.switchTo().window(parentWindow);
        assertTrue(driver.findElement(By.id("vote-notifications-container")).getText().contains("+1"));
    }

    @Test
    public void vote() {
        vote(driver);
        driver.navigate().refresh();
        new WebDriverWait(driver, 1000).until(ExpectedConditions.presenceOfElementLocated(By.id("take-back-vote-button")));
        assertNotEquals(0, driver.findElements(By.id("take-back-vote-button")).size());
    }

    @Test
    public void candidacy() {
        driver.get(url + "candidacy");
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

    private void confirmHomePageSearchResultsWithAssertFalse(String keysToSend, String message, String condition) {
        driver.findElement(By.id("candidateTable_filter")).findElement(By.className("form-control")).sendKeys(keysToSend);
        assertFalse(message, driver.findElement(By.id("candidateTable_wrapper")).getText().contains(condition));
    }

    @Test
    public void searchForParty() {
        confirmHomePageSearchResultsWithAssertFalse("Erakond Eesti Normaalsed", "Found a party that shouldn't be displayed", "Ükskõiksuserakond");
    }

    @Test
    public void searchForRegion() {
        confirmHomePageSearchResultsWithAssertFalse("Põhja-Eesti", "Found a region that shouldn't be displayed", "Lääne-Eesti");
    }

    private void confirmStatisticsSearchResultsWithAssertFalse(String keysToSend, String message, String condition) {
        driver.get(url + "statistics");
        driver.findElement(By.id("votesTable_filter")).findElement(By.className("form-control")).sendKeys(keysToSend);
        assertFalse(message, driver.findElement(By.id("votesTable_wrapper")).getText().contains(condition));
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
        driver.get(url + "statistics");
        assertTrue(driver.findElement(By.id("piechart")).getText().contains("Ükskõiksuserakond"));
    }
}
