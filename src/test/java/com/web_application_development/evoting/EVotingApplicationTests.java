package com.web_application_development.evoting;

import io.github.bonigarcia.wdm.ChromeDriverManager;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
public class EVotingApplicationTests {

	private WebDriver driver;

	@BeforeClass
	public static void setUpClass() {
		ChromeDriverManager.getInstance().setup();
	}

	@Before
	public void setUp() {
		driver = new ChromeDriver();
		driver.get("https://evalimised.herokuapp.com");
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
		assertEquals("https://evalimised.herokuapp.com/login", driver.getCurrentUrl());

	}

	private void logIn() {
		driver.get("https://evalimised.herokuapp.com/login");
		driver.findElement(By.id("nationalIdentityNumber")).sendKeys("10101010005");
		driver.findElement(By.className("btn")).click();
		new WebDriverWait(driver, 60).until(ExpectedConditions.titleContains("Home"));
		assertEquals("https://evalimised.herokuapp.com/", driver.getCurrentUrl());
	}

}
