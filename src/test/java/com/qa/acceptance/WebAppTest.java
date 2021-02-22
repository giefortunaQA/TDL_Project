package com.qa.acceptance;

import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
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
        		"C:\\Users\\Gie\\git\\TDL_Project\\target\\reports\\ToDoListReport.html",
        		true);
    }

	@BeforeEach
	void beforeEach() {
		System.setProperty("webdriver.chrome.driver", "C:\\Users\\Gie\\git\\TDL_Project\\src\\test\\resources\\driver\\chromedriver.exe");
        driver = new ChromeDriver();
//        org.openqa.selenium.Dimension dim=new org.openqa.selenium.Dimension(1366, 768);
        driver.manage().window().maximize();;
	}
	
 
	@Test
	void testCreateList() throws Exception {
		test=report.startTest("Create a list test");
		test.log(LogStatus.INFO, "Given - we can access the To Do List webpage");
		driver.get(WebAppTestSetup.URL);
		new WebDriverWait(driver, 2).until(ExpectedConditions.attributeContains(By.id("mySidepanel"), "width", "275px"));


		WebAppTestSetup setup=new WebAppTestSetup(driver);
		test.log(LogStatus.INFO, "When we click the New List button");
		setup.getCreateFormBtn().click();
		setup.waitFor("createFormDiv");
		test.log(LogStatus.INFO, "And when I input a list name and click the tick buttton");
		setup.createList("Test List");
		setup.waitFor("displayDivRead");
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
	void testReadAllLists() throws Exception {
		test=report.startTest("Read all lists test");
		test.log(LogStatus.INFO, "Given - we can access the To Do List webpage");
		driver.get(WebAppTestSetup.URL);
		new WebDriverWait(driver, 2).until(ExpectedConditions.attributeContains(By.id("mySidepanel"), "width", "275px"));
		test.log(LogStatus.INFO, "When we click the Show All Lists button");
		WebAppTestSetup setup=new WebAppTestSetup(driver);
		setup.getReadAllBtn().click();
		setup.waitFor("displayDivRead");
		test.log(LogStatus.INFO, "Then - I should see all the lists in the database");
		String result = setup.getDisplayDivRead().getText();
		boolean contains = result.contains("Prepopulated List 1")&&result.contains("Prepopulated List 2");
		if (contains) {
			test.log(LogStatus.PASS, "Test List");
		}else {
			test.log(LogStatus.FAIL, "Found: "+ result+"instead.");
		}
		
		if (result.equals("Test List")) {
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
		test.log(LogStatus.INFO, "Given - we can access the To Do List webpage");
		driver.get(WebAppTestSetup.URL);
		new WebDriverWait(driver, 2).until(ExpectedConditions.attributeContains(By.id("mySidepanel"), "width", "275px"));
		test.log(LogStatus.INFO, "When we read the first list");
		WebAppTestSetup setup=new WebAppTestSetup(driver);
		setup.readFirstListById();
		setup.waitFor("displayDivRead");
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
		test.log(LogStatus.INFO, "Given - we can access the To Do List webpage");
		driver.get(WebAppTestSetup.URL);
		new WebDriverWait(driver,2).until(ExpectedConditions.attributeContains(By.id("mySidepanel"), "width", "275px"));
		test.log(LogStatus.INFO, "When we read the first list");
		WebAppTestSetup setup=new WebAppTestSetup(driver);
		setup.readFirstListById();
		setup.waitFor("displayDivRead");
		test.log(LogStatus.INFO, "And when we click the edit/pencil button");
		setup.getEditBtn().click();
		setup.waitFor("updateFormDiv");
		setup.update("Update");
		setup.waitFor("displayDivRead");

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

		test.log(LogStatus.INFO, "Given - we can access the To Do List webpage");
		driver.get(WebAppTestSetup.URL);
		new WebDriverWait(driver,2).until(ExpectedConditions.attributeContains(By.id("mySidepanel"), "width", "275px"));
		test.log(LogStatus.INFO, "When we read the first list");
		WebAppTestSetup setup=new WebAppTestSetup(driver);
		setup.readFirstListById();
		setup.waitFor("displayDivRead");

		test.log(LogStatus.INFO, "And when we click the delete button");
		setup.getDelBtn().click();
		setup.waitFor("displayDivDelete");
		
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
		new WebDriverWait(driver,2).until(ExpectedConditions.attributeContains(By.id("mySidepanel"), "width", "275px"));

		WebAppTestSetup setup=new WebAppTestSetup(driver);
		test.log(LogStatus.INFO, "When we create a new list called 'New List'");
		setup.getCreateFormBtn().click();
		setup.waitFor("createFormDiv");
		setup.createList("New List");
		setup.waitFor("displayDivRead");
		
		test.log(LogStatus.INFO, "When we click the New Task button");
		setup.getAddTaskBtn3().click();
		setup.waitFor("createItemSeparate");
		
		test.log(LogStatus.INFO, "And when I input values to the form");
		setup.createItemInList3("Test Item", "no");
		setup.waitFor("displayDivRead");

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
	void testUpdateItem() throws InterruptedException {
		test=report.startTest("Update Item Test");

		test.log(LogStatus.INFO, "Given - we can access the To Do List webpage");
		driver.get(WebAppTestSetup.URL);
		new WebDriverWait(driver,2).until(ExpectedConditions.attributeContains(By.id("mySidepanel"), "width", "275px"));

		WebAppTestSetup setup=new WebAppTestSetup(driver);
		test.log(LogStatus.INFO, "When we create a new task called 'New Task' that is not done");
		setup.createNewListAndTask();

		test.log(LogStatus.INFO, "And when we click the edit item button");
		moveAndClick(16,-42,0);
		setup.waitFor("updateItemFormDiv");
		
		test.log(LogStatus.INFO, "And when we input updated details");
		setup.updateItemInList3("Updated","yes");
		setup.waitFor("displayDivRead");
		
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
	
//	@Test
//	void testDeleteItem() throws InterruptedException {
//		test=report.startTest("Delete Item Test");
//
//		test.log(LogStatus.INFO, "Given - we can access the To Do List webpage");
//		driver.get(WebAppTestSetup.URL);
//		new WebDriverWait(driver,2).until(ExpectedConditions.attributeContains(By.id("mySidepanel"), "width", "275px"));
//
//		WebAppTestSetup setup=new WebAppTestSetup(driver);
//		test.log(LogStatus.INFO, "When we create a new task called 'New Task' that is not done");
//		setup.createNewListAndTask();
//		
//		test.log(LogStatus.INFO, "And when we click the delete item 3 button");
//		List<WebElement> btns=driver.findElements(By.className("btn")).stream().collect(Collectors.toList());
//		for (WebElement btn:btns) {
//			System.out.println(btn.getAttribute("id")+ " " + btn.getLocation());
//		}
//		moveAndClick(btns.get(15),42,0);
//		
//		setup.waitFor("displayDivRead");
//		System.out.println(setup.getDisplayDivRead().getText());
//		
//		test.log(LogStatus.INFO, "Then - I should see the update list without this item");
//		String result = setup.getDisplayDivRead().getText();
//		boolean deleted = result.contains("No Tasks.");
//		if (deleted) {
//			test.log(LogStatus.PASS, "Item deleted successfully.");
//		}else {
//			test.log(LogStatus.FAIL, "Found: "+ result+"instead.");
//		}
//		assertTrue(deleted);
//	}

	
	public void moveAndClick(int index,int xOff,int yOff) {
		Actions actions=new Actions(driver);
		List<WebElement> btns=driver.findElements(By.className("btn")).stream().collect(Collectors.toList());
		actions.moveToElement(btns.get(index),xOff,yOff).click().perform();
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