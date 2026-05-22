package seleniumcode.com.learn.seleniumframework;

import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;



public class Standalonetest {

    public static void main(String[] args) throws Exception {
    	
    	String productname = "TestProduct";
    		
        // ---------- Chrome Setup ----------
        ChromeOptions options = new ChromeOptions();

        Map<String, Object> prefs = new HashMap<>();
        prefs.put("credentials_enable_service", false);
        prefs.put("profile.password_manager_enabled", false);
        prefs.put("profile.password_manager_leak_detection", false);

        options.setExperimentalOption("prefs", prefs);
        options.addArguments("--disable-notifications");
        options.addArguments("--disable-save-password-bubble");
        options.addArguments("--disable-infobars");

        WebDriver driver = new ChromeDriver(options);
        driver.manage().window().maximize();

        // ---------- Open Site ----------
        driver.get("https://rahulshettyacademy.com/client/#/auth/login");

        // ---------- Login ----------
        driver.findElement(By.id("userEmail")).sendKeys("anshika@gmail.com");
        driver.findElement(By.id("userPassword")).sendKeys("Iamking@000");
        driver.findElement(By.id("login")).click();

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        // ---------- Validate login ----------
        wait.until(driver1 ->
                driver1.getCurrentUrl().contains("dashboard") ||
                driver1.findElements(By.cssSelector("[class*='toast-error']")).size() > 0
        );

        System.out.println("Current URL: " + driver.getCurrentUrl());

        // ---------- Check login failure ----------
        if (!driver.getCurrentUrl().contains("dashboard")) {
            String error = driver.findElement(By.cssSelector("[class*='toast-error']")).getText();
            throw new RuntimeException("Login failed: " + error);
        }

        // ---------- Wait for products ----------
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".mb-3")));

        List<WebElement> products = driver.findElements(By.cssSelector(".mb-3"));
        System.out.println("Product count: " + products.size());

        // ---------- Find product ----------
        WebElement prod = products.stream()
                .filter(p -> p.findElement(By.cssSelector(".card-body b"))
                        .getText().equalsIgnoreCase(productname))
                
                .findFirst()
                .orElse(null);

        // ---------- Null safety ----------
        if (prod == null) {
            throw new RuntimeException("Product not found");
        }

        // ---------- Click Add to Cart ----------
        prod.findElement(By.cssSelector(".card-body button:last-of-type")).click();

        System.out.println("Product added to cart");
        
       WebDriverWait cartWait = new WebDriverWait(driver, Duration.ofSeconds(5));
       wait.until(ExpectedConditions.invisibilityOfElementLocated(
    		    By.cssSelector(".ngx-spinner-overlay")
    		));
        
        driver.findElement(By.xpath("//button[@routerlink='/dashboard/cart']")).click();
        
        
        List<WebElement> cartproduct= driver.findElements(By.cssSelector(".cart"));
        
      boolean match=  cartproduct.stream().anyMatch(n->n.findElement(By.cssSelector("h3")).getText().equalsIgnoreCase(productname));
      
        
Assert.assertTrue(match);

driver.findElement(By.xpath("//*[contains(text(),'Checkout')]")).click();

driver.findElement(By.xpath("//*[@placeholder='Select Country']")).sendKeys("india");

Thread.sleep(2000);

driver.findElement(By.xpath("(//*[contains(text(),' India')])[2]")).click();



    }
}