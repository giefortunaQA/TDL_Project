package com.qa.acceptance;

import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.remote.RemoteWebDriver;
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
public class WebAppTest {
	
	private static RemoteWebDriver driver;
	private static ExtentReports report;
	private static ExtentTest test;

	@BeforeAll
	public static void beforeAll() {
		report = new ExtentReports(
				"C:\\Users\\Gie\\git\\TDL_Project\\target\\reports\\WebAppTestReport.html",
				true);
		
	}
	@BeforeEach
	public void beforeEach() {
		System.setProperty(
				"webdriver.chrome.driver",
				"C:\\Users\\Gie\\git\\TDL_Project\\src\\test\\resources\\driver\\chromedriver.exe");
		driver=new ChromeDriver();
		driver.manage().window().setSize(new Dimension(1366, 768));
	}

	@Test
	public void testCreateList() throws Exception {
		test = report.startTest("Create a list test");
		test.log(LogStatus.INFO, "Given - we can access the To Do List webpage");
		driver.get(WebAppTestSetup.URL);
		WebAppTestSetup setup = new WebAppTestSetup(driver);
//		new WebDriverWait(driver, 2).until(ExpectedConditions.attributeContains(By.id("mySidepanel"), "width", "275px"));
//		 WHEN
		test.log(LogStatus.INFO, "When we click the New List button");
		setup.getCreateFormBtn().click();

//		new WebDriverWait(driver, 2).until(ExpectedConditions.attributeContains(By.id("createFormDiv"), "display", "block"));
		test.log(LogStatus.INFO, "And when I input a list name and click the tick buttton");
		setup.getListName().sendKeys("Test List");
		setup.getCreateListBtn().click();
//		new WebDriverWait(driver, 2).until(ExpectedConditions.attributeContains(By.id("displayDivRead"),"display","block"));
		// THEN
		test.log(LogStatus.INFO, "Then - I should see the list record");
		String result = setup.getDisplayDivRead().getText();
		boolean contains = result.contains("Test List");
		if (contains) {
			test.log(LogStatus.PASS, "List created");
		} else {
			test.log(LogStatus.FAIL, "Found: " + result + "instead.");
		}
		assertTrue(contains);
	}

	@Test
	public void testReadAllLists() throws Exception {
		test = report.startTest("Read all lists test");
//		 GIVEN
		test.log(LogStatus.INFO, "Given - we can access the To Do List webpage");
		driver.get(WebAppTestSetup.URL);
//		new WebDriverWait(driver, 2).until(ExpectedConditions.attributeContains(By.id("mySidepanel"), "width", "275px"));

//		 WHEN
		test.log(LogStatus.INFO, "When we click the Show All Lists button");
		WebAppTestSetup setup = new WebAppTestSetup(driver);
		setup.getReadAllBtn().click();

		// THEN
		test.log(LogStatus.INFO, "Then - I should see all the lists in the database");
		String result = setup.getDisplayDivRead().getText();
		boolean contains = result.contains("Prepopulated List 1") && result.contains("Prepopulated List 2");
		if (contains) {
			test.log(LogStatus.PASS, "Prepopulated lists read successfully");
		} else {
			test.log(LogStatus.FAIL, "Found: " + result + "instead.");
		}

		if (result.equals("Test List")) {
			test.log(LogStatus.FAIL, "Element not prepopulated");
		} else {
			test.log(LogStatus.PASS, "Details loaded");
		}
		assertNotEquals("No Lists.", result);
		assertTrue(contains);
	}

	@Test
	void testReadListById() throws Exception {
		test = report.startTest("Read list by Id Test");

		test.log(LogStatus.INFO, "Given - we can access the To Do List webpage");
		driver.get(WebAppTestSetup.URL);
//		new WebDriverWait(driver, 2).until(ExpectedConditions.attributeContains(By.id("mySidepanel"), "width", "275px"));


		test.log(LogStatus.INFO, "When we read the first list");
		WebAppTestSetup setup = new WebAppTestSetup(driver);
		setup.readFirstListById();
//		new WebDriverWait(driver, 2).until(ExpectedConditions.attributeContains(setup.getDisplayDivRead(), "display", "block"));


		test.log(LogStatus.INFO, "Then - I should see this list and the items in it");
		String result = setup.getDisplayDivRead().getText();
		boolean contains = result.contains("Prepopulated List 1");
		if (contains) {
			test.log(LogStatus.PASS, "List displayed successfully.");
		} else {
			test.log(LogStatus.FAIL, "Found: " + result + " instead.");
		}
		assertTrue(contains);
	}

	@Test
	void testUpdateList() throws Exception {
		test = report.startTest("Update list by Id Test");

		test.log(LogStatus.INFO, "Given - we can access the To Do List webpage");
		driver.get(WebAppTestSetup.URL);
//		new WebDriverWait(driver, 2).until(ExpectedConditions.attributeContains(By.id("mySidepanel"), "width", "275px"));

		test.log(LogStatus.INFO, "When we read the first list");
		WebAppTestSetup setup = new WebAppTestSetup(driver);
		setup.getRefreshBtn().click();
//		new WebDriverWait(driver, 2).until(ExpectedConditions.attributeContains(setup.getYourList1Btn(), "display", "block"));
		setup.readFirstListById();
//		new WebDriverWait(driver, 2).until(ExpectedConditions.attributeContains(setup.getDisplayDivRead(), "display", "block"));

		test.log(LogStatus.INFO, "And when we click the edit/pencil button");
		setup.update("Update");
//		new WebDriverWait(driver, 2).until(ExpectedConditions.attributeContains(setup.getDisplayDivRead(), "display", "block"));

		test.log(LogStatus.INFO, "Then - I should see this list with new name");
		String result = setup.getDisplayDivRead().getText();
		boolean contains = result.contains("Update");
		if (contains) {
			test.log(LogStatus.PASS, "List updated successfully.");
		} else {
			test.log(LogStatus.FAIL, "Found: " + result + " instead.");
		}
		assertTrue(contains);
	}


	@Test
	void testCreateItem() {
		test = report.startTest("Create a item test");
		test.log(LogStatus.INFO, "Given - we can access the To Do List webpage");
		driver.get(WebAppTestSetup.URL);
//		 WHEN
		test.log(LogStatus.INFO, "When we read the first list");
		WebAppTestSetup setup = new WebAppTestSetup(driver);
		setup.readFirstListById();
//		wait.until(ExpectedConditions.attributeContains(setup.getDisplayDivRead(), "display", "block"));

		test.log(LogStatus.INFO, "And when I click the add task button");
		setup.getAddTaskBtn1().click();
//		wait.until(ExpectedConditions.attributeContains(By.id("createItemSeparate"), "display", "block"));

		test.log(LogStatus.INFO, "And when I input values to the form");
		setup.createItemInList1("Test Item", "no");
		// THEN
		test.log(LogStatus.INFO, "Then - I should see the item inside its parent list record");
		String result = setup.getDisplayDivRead().getText();
		boolean contains = result.contains("Test Item");
		if (contains) {
			test.log(LogStatus.PASS, "List created");
		} else {
			test.log(LogStatus.FAIL, "Found: " + result + " instead.");
		}
		assertTrue(contains);
	}

	@Test
	void testUpdateItem() {
		test = report.startTest("Update Item Test");

		test.log(LogStatus.INFO, "Given - we can access the To Do List webpage");
		driver.get(WebAppTestSetup.URL);
//		 WHEN
		test.log(LogStatus.INFO, "When we read the first list");
		WebAppTestSetup setup = new WebAppTestSetup(driver);
		setup.readFirstListById();

		test.log(LogStatus.INFO, "And when we click the edit item button");
		setup.getEditItem1Btn().click();
//		wait.until(ExpectedConditions.attributeContains(By.id("updateItemFormDiv"), "display", "block"));

		test.log(LogStatus.INFO, "And when we input updated details");
		setup.updateItemInList1("Updated", "yes");
//		wait.until(ExpectedConditions.attributeContains(setup.getDisplayDivRead(), "display", "block"));	

		test.log(LogStatus.INFO, "Then - I should see this task with new name and marked done");
		String resultBox = setup.getDisplayDivRead().getText();
		WebElement resultItem = driver.findElement(By.tagName("strike"));
		boolean checkUpdatedName = resultItem.getText().equals("Updated");
		boolean checkUpdatedDone = resultItem.isDisplayed();
		if (checkUpdatedDone) {
			test.log(LogStatus.PASS, "Item marked done successfully.");
		} else {
			test.log(LogStatus.FAIL, "Item not striked.");
		}

		if (checkUpdatedName) {
			test.log(LogStatus.PASS, "Item name updated successfully");
		} else {
			test.log(LogStatus.FAIL, "Found: " + resultBox + " instead");
		}
		assertTrue(checkUpdatedDone);
		assertTrue(checkUpdatedName);
	}

	@Test
	void testDeleteItem() {
		test = report.startTest("Delete Item Test");

		test.log(LogStatus.INFO, "Given - we can access the To Do List webpage");
		driver.get(WebAppTestSetup.URL);
//		 WHEN
		test.log(LogStatus.INFO, "When we read the first list");
		WebAppTestSetup setup = new WebAppTestSetup(driver);
		setup.readFirstListById();

		test.log(LogStatus.INFO, "And when we click the delete item 1 button");
		setup.getDeleteItem1Btn().click();
//		wait.until(ExpectedConditions.attributeContains(setup.getDisplayDivRead(), "display", "block"));

		test.log(LogStatus.INFO, "Then - I should see the update list without this item");
		String result = setup.getDisplayDivRead().getText();
		boolean success = !result.contains("Prepopulated Item 1");
		if (success) {
			test.log(LogStatus.PASS, "Item deleted successfully.");
		} else {
			test.log(LogStatus.FAIL, "Found: " + result + "instead.");
		}
		assertTrue(success);
	}

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
