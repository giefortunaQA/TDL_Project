package com.qa.acceptance;

import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;

import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;

@Disabled("Diabled because void driver bug not fixed")
@ActiveProfiles("test")
@Sql (scripts = { "classpath:schema.sql", "classpath:data.sql" }, executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
public class WebAppTest {
	
	private static WebDriver driver;
	private static ExtentReports report;
	private static ExtentTest test;
	@BeforeAll
	static void beforeAll() {
		report = new ExtentReports(
				"C:\\Users\\Gie\\Downloads\\STS\\Workspace\\Selenium\\target\\reports\\extentreports\\ToDoListReport",
				true);
	}
	@BeforeEach
	void beforeEach() {
		System.setProperty("webdriver.chrome.driver", "/src/test/resources/driver/chromedriver.exe");
		driver = new ChromeDriver();
		driver.manage().window().setSize(new Dimension(1366, 768));
	}
	
	@Test
	void testCreateList() throws Exception{
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
			test.log(LogStatus.FAIL, "Found: "+ result+" instead.");
		}
		assertTrue(contains);
	}
	
	@Test
	void testReadAllLists() throws Exception {
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
			test.log(LogStatus.FAIL, "Found: "+ result+" instead.");
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
	void testReadListById() throws Exception{
		test=report.startTest("Read list by Id Test");
//		 GIVEN
		test.log(LogStatus.INFO, "Given - we can access the To Do List webpage");
		driver.get(WebAppTestSetup.URL);
//		 WHEN
		test.log(LogStatus.INFO, "When we read the first list");
		WebAppTestSetup setup=new WebAppTestSetup(driver);
		setup.readFirstListById();

		// THEN
		test.log(LogStatus.INFO, "Then - I should see this list and the items in it");
		String result = setup.getDisplayDivRead().getText();
		boolean contains = result.contains("Prepopulated List 1");
		if (contains) {
			test.log(LogStatus.PASS, "List displayed successfully.");
		}else {
			test.log(LogStatus.FAIL, "Found: "+ result+" instead.");
		}
		assertTrue(contains);
	}
	
	@Test
	void testUpdateList() throws Exception{
		test=report.startTest("Update list by Id Test");
//		 GIVEN
		test.log(LogStatus.INFO, "Given - we can access the To Do List webpage");
		driver.get(WebAppTestSetup.URL);
//		 WHEN
		test.log(LogStatus.INFO, "When we read the first list");
		WebAppTestSetup setup=new WebAppTestSetup(driver);
		setup.readFirstListById();

		test.log(LogStatus.INFO, "And when we click the edit/pencil button");
		setup.update("Update");
		// THEN
		test.log(LogStatus.INFO, "Then - I should see this list with new name");
		String result = setup.getDisplayDivRead().getText();
		boolean contains = result.contains("Update");
		if (contains) {
			test.log(LogStatus.PASS, "List updated successfully.");
		}else {
			test.log(LogStatus.FAIL, "Found: "+ result+" instead.");
		}
		assertTrue(contains);
	}
	
	@Test
	void testDeleteList() throws Exception{
		test=report.startTest("Delete list Test");
//		 GIVEN
		test.log(LogStatus.INFO, "Given - we can access the To Do List webpage");
		driver.get(WebAppTestSetup.URL);
//		 WHEN
		test.log(LogStatus.INFO, "When we read the first list");
		WebAppTestSetup setup=new WebAppTestSetup(driver);
		setup.readFirstListById();

		test.log(LogStatus.INFO, "And when we click the delete button");
		setup.getDelBtn().click();
		// THEN
		test.log(LogStatus.INFO, "Then - I should see a confirmation of deletion");
		String result = setup.getDisplayDivDelete().getText();
		boolean contains = result.contains("To Do List deleted permanently.");
		if (contains) {
			test.log(LogStatus.PASS, "List delete successfully.");
		}else {
			test.log(LogStatus.FAIL, "Found: "+ result+"  instead.");
		}
		assertTrue(contains);
	}
	
	@Test
	void testCreateItem() {
		test=report.startTest("Create a item test");
		test.log(LogStatus.INFO, "Given - we can access the To Do List webpage");
		driver.get(WebAppTestSetup.URL);
//		 WHEN
		test.log(LogStatus.INFO, "When we read the first list");
		WebAppTestSetup setup=new WebAppTestSetup(driver);
		setup.readFirstListById();
		test.log(LogStatus.INFO, "And when I click the add task button");
		setup.getAddTaskBtn1().click();
		test.log(LogStatus.INFO, "And when I input values to the form");
		setup.createItemInList1("Test Item", "no");
		// THEN
		test.log(LogStatus.INFO, "Then - I should see the item inside its parent list record");
		String result = setup.getDisplayDivRead().getText();
		boolean contains = result.contains("Test Item");
		if (contains) {
			test.log(LogStatus.PASS, "List created");
		}else {
			test.log(LogStatus.FAIL, "Found: "+ result+" instead.");
		}
		assertTrue(contains);
	}
	
	@Test
	void testUpdateItem() {
		test=report.startTest("Update Item Test");
//		 GIVEN
		test.log(LogStatus.INFO, "Given - we can access the To Do List webpage");
		driver.get(WebAppTestSetup.URL);
//		 WHEN
		test.log(LogStatus.INFO, "When we read the first list");
		WebAppTestSetup setup=new WebAppTestSetup(driver);
		setup.readFirstListById();

		test.log(LogStatus.INFO, "And when we click the edit item button");
		setup.getEditItem1Btn().click();
		test.log(LogStatus.INFO, "And when we input updated details");
		setup.updateItemInList1("Updated","yes");
		// THEN
		test.log(LogStatus.INFO, "Then - I should see this task with new name and marked done");
		String resultBox = setup.getDisplayDivRead().getText();
		WebElement resultItem=driver.findElement(By.tagName("strike"));
		boolean checkUpdatedName=resultItem.getText().equals("Updated");
		boolean checkUpdatedDone = resultItem.isDisplayed();
		if (checkUpdatedDone) {
			test.log(LogStatus.PASS, "Item marked done successfully.");
		}else {
			test.log(LogStatus.FAIL, "Item not striked.");
		}
		
		if(checkUpdatedName) {
			test.log(LogStatus.PASS, "Item name updated successfully");
		}else {
			test.log(LogStatus.FAIL, "Found: "+resultBox+" instead");
		}
		assertTrue(checkUpdatedDone);
		assertTrue(checkUpdatedName);
	}
	
	@Test
	void testDeleteItem() {
		test=report.startTest("Delete Item Test");
//		 GIVEN
		test.log(LogStatus.INFO, "Given - we can access the To Do List webpage");
		driver.get(WebAppTestSetup.URL);
//		 WHEN
		test.log(LogStatus.INFO, "When we read the first list");
		WebAppTestSetup setup=new WebAppTestSetup(driver);
		setup.readFirstListById();

		test.log(LogStatus.INFO, "And when we click the delete item 1 button");
		setup.getDeleteItem1Btn().click();
		// THEN
		test.log(LogStatus.INFO, "Then - I should see the update list without this item");
		String result = setup.getDisplayDivRead().getText();
		boolean success = !result.contains("Prepopulated Item 1");
		if (success) {
			test.log(LogStatus.PASS, "Item deleted successfully.");
		}else {
			test.log(LogStatus.FAIL, "Found: "+ result+"instead.");
		}
		assertTrue(success);
	}
	
	@AfterEach
	void afterEach() {
		driver.close();
		report.endTest(test);
	}
	
	@AfterAll
	static void afterAll() {
		driver.quit();
		report.flush();
		report.close();
	}

}
