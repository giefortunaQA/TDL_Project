package com.qa.acceptance;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.remote.RemoteWebDriver;

public class WebAppTest{
	
	private static RemoteWebDriver driver;
	@BeforeAll
    public static void setup() {
        System.setProperty("webdriver.chrome.driver", "src/test/resources/drivers/chromedriver.exe");
        driver = new ChromeDriver();
    }

 
	@Test
	void testLaunch() {
		driver.get("http://127.0.0.1:5500/src/main/resources/static/index.html");
	}

    @AfterAll
    public static void cleanup() {
        driver.quit();
    }
}