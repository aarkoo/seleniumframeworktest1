package Abstractcomponents;

import java.time.Duration;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.ElementClickInterceptedException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class Abstractcomponent {

	protected WebDriver driver;
	
	public Abstractcomponent(WebDriver driver) {
		this.driver = driver;
	}

	public void waitforelementtoappear(By findby) {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
		wait.until(ExpectedConditions.visibilityOfElementLocated(findby));
	}
	
	public void waitforElementToDisappear(By findby) {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
		wait.until(ExpectedConditions.invisibilityOfElementLocated(findby));
	}
	
	public void waitforElementToBeClickable(WebElement element) {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
		wait.until(ExpectedConditions.elementToBeClickable(element));
	}

	public void clickWhenReady(WebElement element) {
		int attempts = 0;
		int maxAttempts = 5;
		while (attempts < maxAttempts) {
			try {
				waitForSpinnerToDisappear();
				WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
				wait.until(ExpectedConditions.elementToBeClickable(element));
				element.click();
				return;
			} catch (ElementClickInterceptedException | StaleElementReferenceException e) {
				attempts++;
				if (attempts >= maxAttempts) {
					// Last resort: try JavaScript click to bypass overlays
					try {
						((JavascriptExecutor) driver).executeScript("arguments[0].click();", element);
						return;
					} catch (Exception jsEx) {
						throw e;
					}
				}
				try {
					Thread.sleep(500);
				} catch (InterruptedException ie) {
					Thread.currentThread().interrupt();
					throw new RuntimeException("Interrupted while waiting to retry click", ie);
				}
			}
		}
	}

	public void waitForSpinnerToDisappear() {
		By spinnerLocator = By.cssSelector(".ngx-spinner-overlay");
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
		wait.until(currentDriver -> currentDriver.findElements(spinnerLocator).stream()
				.noneMatch(WebElement::isDisplayed));
	}

	public void waitForToastToDisappear() {
		By toastLocator = By.cssSelector(".ngx-toastr");
		List<WebElement> toasts = driver.findElements(toastLocator);
		if (!toasts.isEmpty()) {
			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
			wait.until(ExpectedConditions.invisibilityOfAllElements(toasts));
		}
	}
}
