package seleniumcode.com.learn.seleniumframework.pageobjects;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import Abstractcomponents.Abstractcomponent;

public class CartPage extends Abstractcomponent {

	WebDriver driver;

	public CartPage(WebDriver driver) {
		super(driver);
		this.driver = driver;
		PageFactory.initElements(driver, this);
	}

	@FindBy(css = ".cart")
	List<WebElement> cartProducts;

	@FindBy(xpath = "//*[contains(text(),'Checkout')]")
	WebElement checkoutButton;

	By cartProductsBy = By.cssSelector(".cart");

	public List<WebElement> getCartProducts() {
		return cartProducts;
	}

	public boolean verifyProductInCart(String productname) {
		waitforelementtoappear(cartProductsBy);
		return cartProducts.stream()
				.anyMatch(product -> product.findElement(By.cssSelector("h3")).getText()
						.equalsIgnoreCase(productname));
	}

	public void goToCheckout() {
		waitForToastToDisappear();
		clickWhenReady(checkoutButton);
	}

	public void clearCart() {
		List<WebElement> cartItems = getCartProducts();
		for (WebElement item : cartItems) {
			List<WebElement> deleteButtons = item.findElements(By.xpath(".//button[@class='btn btn-link']"));
			if (!deleteButtons.isEmpty()) {
				WebElement deleteButton = deleteButtons.get(0);
				clickWhenReady(deleteButton);
				waitForToastToDisappear();
			}
		}
	}
}
