package seleniumcode.com.learn.seleniumframework.pageobjects;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import Abstractcomponents.Abstractcomponent;
import java.time.Duration;

public class Landingpage extends Abstractcomponent {

	WebDriver driver;

	public Landingpage(WebDriver driver) {
		super(driver);
		this.driver = driver;
		PageFactory.initElements(driver, this);
	}

	// page factory POM
	@FindBy(id = "userEmail")
	WebElement email;

	@FindBy(id = "userPassword")
	WebElement password;

	@FindBy(id = "login")
	WebElement login;

	public void loginpage(String emailid, String pass) {
		email.sendKeys(emailid);
		password.sendKeys(pass);
		clickWhenReady(login);
	}

	public void goTo(String applicationUrl) {
		driver.get(applicationUrl);
	}
	
	public boolean verifyLoginSuccess() {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
		wait.until(driver1 ->
			driver1.getCurrentUrl().contains("dashboard") ||
			driver1.findElements(By.cssSelector("[class*='toast-error']")).size() > 0
		);
		return driver.getCurrentUrl().contains("dashboard");
	}
	
	public String getLoginError() {
		return driver.findElement(By.cssSelector("[class*='toast-error']")).getText();
	}
}
