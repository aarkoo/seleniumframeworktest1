package seleniumcode.com.learn.seleniumframework.pageobjects;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import Abstractcomponents.Abstractcomponent;

public class Productcatalogue extends Abstractcomponent {

	WebDriver driver;

	public Productcatalogue(WebDriver driver) {
		super(driver);
		this.driver = driver;
		PageFactory.initElements(driver, this);
	}

	@FindBy(css = ".mb-3")
	List<WebElement> products;

	By productsby = By.cssSelector(".mb-3");
	By addToCartButton = By.cssSelector(".card-body button:last-of-type");
	By spinner = By.cssSelector(".ngx-spinner-overlay");

	public List<WebElement> getProductList() {
		waitforelementtoappear(productsby);
		return products;
	}

	public WebElement getProductByName(String productname) {
		List<WebElement> productList = getProductList();
		WebElement prod = productList.stream()
				.filter(p -> p.findElement(By.cssSelector(".card-body b")).getText().equalsIgnoreCase(productname))
				.findFirst()
				.orElse(null);
		return prod;
	}

	public void addProductToCart(String productname) {
		WebElement prod = getProductByName(productname);
		if (prod == null) {
			throw new RuntimeException("Product not found: " + productname);
		}
		waitForToastToDisappear();
		WebElement addButton = prod.findElement(addToCartButton);
		clickWhenReady(addButton);
		waitForSpinnerToDisappear();
	}
	
	

}
