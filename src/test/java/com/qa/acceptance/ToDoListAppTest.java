package com.qa.acceptance;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

public class ToDoListAppTest {

	private WebDriver driver;
	
	@BeforeEach
	public void setup() {
		System.setProperty("webdriver.chrome.driver", "src/test/resources/driver/chromedriver.exe");
		driver=new ChromeDriver();
		driver.manage().window().setSize(new Dimension(1366,768));
	}
	
	@Test
	public void testCreateList() {
		driver.get("http://localhost:5500/src/main/resources/static/");
		driver.findElement(By.xpath("//*[@id=\"mySidepanel\"]/p[1]/button")).click();
//		assertEquals()
	}
}
