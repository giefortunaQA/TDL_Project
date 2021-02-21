package com.qa.acceptance;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;

import com.qa.main.TdlApplication;
import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT, classes = TdlApplication.class)
@Sql(scripts = { "classpath:schema.sql", "classpath:data.sql" }, executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
@ActiveProfiles("test")
public class WebAppTest{
	
	private static RemoteWebDriver driver;
	private static ExtentReports report;
	private static ExtentTest test;
	
	@BeforeAll
    public static void beforeAll() {
        report=new ExtentReports(
        		"C:\\Users\\Gie\\Downloads\\STS\\Workspace\\Selenium\\target\\reports\\ToDoListReport.html",
        		true);
    }

	@BeforeEach
	void beforeEach() {
		System.setProperty("webdriver.chrome.driver", "C:\\Users\\Gie\\git\\TDL_Project\\src\\test\\resources\\driver\\chromedriver.exe");
        driver = new ChromeDriver();
        
	}
 
	@Test
	void testCreateList() throws Exception {
		test=report.startTest("Create a list test");
		test.log(LogStatus.INFO, "Given - we can access the To Do List webpage");
		driver.get(WebAppTestSetup.URL);
		WebAppTestSetup setup=new WebAppTestSetup(driver);
		new WebDriverWait(driver, 2).until(ExpectedConditions.attributeContains(By.id("mySidepanel"), "width", "275px"));
		test.log(LogStatus.INFO, "When we click the New List button");
		setup.getCreateFormBtn().click();
		test.log(LogStatus.INFO, "And when I input a list name and click the tick buttton");
		setup.getListName().sendKeys("Test List");
		setup.getCreateListBtn().click();
		test.log(LogStatus.INFO, "Then - I should see the list record");
		String result = setup.getDisplayDivRead().getText();
		boolean contains = result.contains("Test List");
		if (contains) {
			test.log(LogStatus.PASS, "List created");
		}else {
			test.log(LogStatus.FAIL, "Found: "+ result+"instead.");
		}
		assertTrue(contains);
	}

	@AfterEach
	void afterEach() {
		driver.close();
		report.endTest(test);
	}
	
    @AfterAll
    public static void afterAll() {
    	driver.quit();
		report.flush();
		report.close();
    }
}