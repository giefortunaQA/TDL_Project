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

	public void createList(String name) {
		createFormBtn.click();
		listName.sendKeys(name);
		createListBtn.click();
	}
}
