package com.qa.acceptance;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import lombok.Data;

@Data
public class WebAppTestSetup {

	public final static String URL="http://127.0.0.1:5500/src/main/resources/static/";
	private WebDriver driver;
	
	
	public WebAppTestSetup(WebDriver driver) {
		super();
		this.driver=driver;
		PageFactory.initElements(driver,this);
	}

	@FindBy(id="readAllBtn")
	private WebElement readAllBtn;
	
	@FindBy(id="displayDivRead")
	private WebElement displayDivRead;
	
	@FindBy(id="createFormBtn")
	private WebElement createFormBtn;
	
	@FindBy(id="name")
	private WebElement listName;
	
	@FindBy(id="createListBtn")
	private WebElement createListBtn;

	
	@FindBy(id="refreshBtn")
	private WebElement refreshBtn;
	
	@FindBy(id="YourList1")
	private WebElement yourList1Btn;
	
	@FindBy(id="Edit1")
	private WebElement editBtn;
	
	@FindBy(id="nameUpdate")
	private WebElement listNameUpdate;
	
	@FindBy(id="updateBtn")
	private WebElement updateBtn;
	
	@FindBy(id="Delete1")
	private WebElement delBtn;
	
	@FindBy(id="displayDivDelete")
	private WebElement displayDivDelete;
	@FindBy(id="createItemBtn")
	private WebElement createItemBtn;
	@FindBy(id="addTaskBtn1")
	private WebElement addTaskBtn1;
	@FindBy(id="itemName")
	private WebElement itemName;
	@FindBy(id="itemDone")
	private WebElement itemDone;
	@FindBy(id="editItem1")
	private WebElement editItem1Btn;
	@FindBy(id="itemNameUpdate")
	private WebElement itemNameUpdate;
	@FindBy(id="itemDoneUpdate")
	private WebElement itemDoneUpdate;
	@FindBy(id="updateItemBtn")
	private WebElement updateItemBtn;
	@FindBy(id="DeleteItem1")
	private WebElement deleteItem1Btn;
	
	public void createList(String name) {
		createFormBtn.click();
		listName.sendKeys(name);
		createListBtn.click();
	}
	
	public void readFirstListById(){
		refreshBtn.click();
		yourList1Btn.click();
	}
	
	public void update(String name) {
		editBtn.click();
		listNameUpdate.sendKeys(name);
		updateBtn.click();
	}
	
	public void createItemInList1(String name, String done) {
		itemName.sendKeys(name);
		itemDone.sendKeys(done);
		createItemBtn.click();
	}
	public void updateItemInList1(String name,String done) {
		itemNameUpdate.sendKeys(name);
		itemDoneUpdate.sendKeys(done);
		updateItemBtn.click();
	}
	

}
