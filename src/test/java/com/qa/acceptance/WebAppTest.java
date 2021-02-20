package com.qa.acceptance;

import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.test.context.ActiveProfiles;
import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;


@ActiveProfiles("test")
public class WebAppTest {
	
	private static WebDriver driver;
	private static ExtentReports report;
	private static ExtentTest test;
	@BeforeAll
	public static void beforeAll() {
		report = new ExtentReports(
				"C:\\Users\\Gie\\Downloads\\STS\\Workspace\\Selenium\\target\\reports\\extentreports\\ToDoListReport",
				true);
	}
	@BeforeEach
	public void beforeEach() {
		System.setProperty("webdriver.chrome.driver", "/src/test/resources/driver/chromedriver.exe");
		driver = new ChromeDriver();
		driver.manage().window().setSize(new Dimension(1366, 768));
	}
	
	@Test
	public void testCreateList() throws Exception{
		test=report.startTest("Create a list test");
		test.log(LogStatus.INFO, "Given - we can access the To Do List webpage");
		driver.get(WebAppTestSetup.URL);
//		 WHEN
		test.log(LogStatus.INFO, "When we click the New List button");
		WebAppTestSetup setup=new WebAppTestSetup(driver);
		setup.getCreateFormBtn().click();
		test.log(LogStatus.INFO, "And when I input a list name and click the tick buttton");
		setup.getListName().sendKeys("Test List");
		setup.getCreateListBtn().click();
		// THEN
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
	
	@Test
	public void testReadAllLists() throws Exception {
		test=report.startTest("Read all lists test");
//		 GIVEN
		test.log(LogStatus.INFO, "Given - we can access the To Do List webpage");
		driver.get(WebAppTestSetup.URL);
//		 WHEN
		test.log(LogStatus.INFO, "When we click the Show All Lists button");
		WebAppTestSetup setup=new WebAppTestSetup(driver);
		setup.getReadAllBtn().click();

		// THEN
		test.log(LogStatus.INFO, "Then - I should see all the lists in the database");
		String result = setup.getDisplayDivRead().getText();
		boolean contains = result.contains("No Lists.");
		if (contains) {
			test.log(LogStatus.PASS, "No lists yet since test");
		}else {
			test.log(LogStatus.FAIL, "Found: "+ result+"instead.");
		}
		
		if (result.equals("No Lists.")) {
			test.log(LogStatus.FAIL, "Element not prepopulated");
		}else {
			test.log(LogStatus.PASS, "Details loaded");
		}
		assertNotEquals("No Lists.", result);
		assertTrue(contains);
	}
	@Test
	public void testReadListById() throws Exception{
		test=report.startTest("Read list by Id Test");
//		 GIVEN
		test.log(LogStatus.INFO, "Given - we can access the To Do List webpage");
		driver.get(WebAppTestSetup.URL);
//		 WHEN
		test.log(LogStatus.INFO, "When we click the refresh button");
		WebAppTestSetup setup=new WebAppTestSetup(driver);
		setup.getReadAllBtn().click();

		// THEN
		test.log(LogStatus.INFO, "Then - I should see all the lists in the database");
		String result = setup.getDisplayDivRead().getText();
		boolean contains = result.contains("Prepopulated List 1")&& result.contains("Prepopulated List 2");
		if (contains) {
			test.log(LogStatus.PASS, "All lists are displayed");
		}else {
			test.log(LogStatus.FAIL, "Found: "+ result+"instead.");
		}
		
		if (result.equals("No Lists.")) {
			test.log(LogStatus.FAIL, "Element not prepopulated");
		}else {
			test.log(LogStatus.PASS, "Details loaded");
		}
		assertNotEquals("No Lists.", result);
		assertTrue(contains);
	}
//	@Test
//	public void testUpdateList() throws Exception{
//		
//	}
	
	
	@AfterEach
	public void afterEach() {
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
