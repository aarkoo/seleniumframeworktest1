package seleniumcode.com.learn.seleniumframework.pageobjects;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import Abstractcomponents.Abstractcomponent;

public class CheckoutPage extends Abstractcomponent {

	WebDriver driver;

	public CheckoutPage(WebDriver driver) {
		super(driver);
		this.driver = driver;
		PageFactory.initElements(driver, this);
	}

	@FindBy(xpath = "//*[@placeholder='Select Country']")
	WebElement countryDropdown;

	@FindBy(xpath = "(//*[contains(text(),' India')])[2]")
	WebElement indiaOption;

	public void selectCountry(String country) {
		waitForSpinnerToDisappear();
		waitForToastToDisappear();
		waitforElementToBeClickable(countryDropdown);
		countryDropdown.sendKeys(country);
		clickWhenReady(indiaOption);
	}
}
