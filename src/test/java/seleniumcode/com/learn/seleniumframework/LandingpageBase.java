package seleniumcode.com.learn.seleniumframework;

import org.testng.Assert;
import org.testng.annotations.Test;

import seleniumcode.com.learn.seleniumframework.pageobjects.CartPage;
import seleniumcode.com.learn.seleniumframework.pageobjects.CheckoutPage;
import seleniumcode.com.learn.seleniumframework.pageobjects.DashboardPage;
import seleniumcode.com.learn.seleniumframework.pageobjects.Productcatalogue;

public class LandingpageBase extends BaseTest {

	@Test(priority = 1, description = "Test user login with valid credentials")
	public void testLogin() {
		Assert.assertTrue(getDriver().getCurrentUrl().contains("dashboard"), "Login did not land on dashboard");
	}

	@Test(priority = 2, description = "Test adding product to cart")
	public void testAddProductToCart() {
		Productcatalogue productcatalogue = new Productcatalogue(getDriver());
		String productname = configReader.getProductName();
		productcatalogue.addProductToCart(productname);

		DashboardPage dashboardpage = new DashboardPage(getDriver());
		dashboardpage.goToCart();

		CartPage cartpage = new CartPage(getDriver());
		Assert.assertTrue(cartpage.verifyProductInCart(productname), "Product was not added to cart");
	}

	@Test(priority = 3, description = "Test verifying product in cart")
	public void testVerifyProductInCart() {
		Productcatalogue productcatalogue = new Productcatalogue(getDriver());
		String productname = configReader.getProductName();
		productcatalogue.addProductToCart(productname);

		DashboardPage dashboardpage = new DashboardPage(getDriver());
		dashboardpage.goToCart();

		CartPage cartpage = new CartPage(getDriver());
		boolean match = cartpage.verifyProductInCart(productname);
		Assert.assertTrue(match, "Product not found in cart!");
	}

	@Test(priority = 4, description = "Test complete checkout flow")
	public void testCheckoutFlow() {
		Productcatalogue productcatalogue = new Productcatalogue(getDriver());
		String productname = configReader.getProductName();
		productcatalogue.addProductToCart(productname);

		DashboardPage dashboardpage = new DashboardPage(getDriver());
		dashboardpage.goToCart();

		CartPage cartpage = new CartPage(getDriver());
		boolean match = cartpage.verifyProductInCart(productname);
		Assert.assertTrue(match, "Product not found in cart!");

		cartpage.goToCheckout();
		CheckoutPage checkoutpage = new CheckoutPage(getDriver());
		checkoutpage.selectCountry("india");

		Assert.assertTrue(getDriver().getCurrentUrl().contains("order"),
				"Checkout page did not progress after country selection");
	}
}
