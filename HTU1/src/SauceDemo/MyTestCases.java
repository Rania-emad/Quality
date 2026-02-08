package SauceDemo;


import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Date;
import java.util.List;
import java.util.Random;

import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.google.common.io.Files;

public class MyTestCases {

    String myWebsite = "https://www.saucedemo.com/";
    WebDriver driver = new EdgeDriver();

    String userName = "standard_user";
    String password = "secret_sauce";
    Random rand = new Random();

    @BeforeTest
    public void mySetUp()  {
        driver.get(myWebsite);
        driver.manage().window().maximize();
        
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

    }

    @Test(priority = 1)
    public void login() throws InterruptedException {
        driver.findElement(By.id("user-name")).sendKeys(userName);
        driver.findElement(By.id("password")).sendKeys(password);
        driver.findElement(By.id("login-button")).click();
        WebElement productsTitle = driver.findElement(By.className("title"));
        Assert.assertEquals(productsTitle.getText(), "Products",
                "Login failed: Products page not displayed");

    }

    @Test(priority = 2)
    public void aDDTocartbtn() throws InterruptedException {
        List<WebElement> addButtons =
                driver.findElements(By.cssSelector(".btn.btn_primary.btn_small.btn_inventory"));
        for (WebElement addbutton : addButtons) {
            addbutton.click();
        }
    }

    @Test(priority = 3)
    public void removebtn() throws InterruptedException {
        List<WebElement> removeButtons =
                driver.findElements(By.cssSelector(".btn.btn_secondary.btn_small.btn_inventory"));
        for (WebElement removeButton : removeButtons) {
            String buttonId = removeButton.getAttribute("id");
            if (buttonId == null || !buttonId.contains("sauce-labs-backpack")) {
                removeButton.click();
                WebElement cartBadge = driver.findElement(By.className("shopping_cart_badge"));
                Assert.assertTrue(Integer.parseInt(cartBadge.getText()) > 0,
                        "No items were added to the cart");

                Thread.sleep(500);
            }
        }
    }

    @Test(priority = 4)
    public void sorting() throws InterruptedException {
        WebElement sortDropdown =
                driver.findElement(By.className("product_sort_container"));
        Select select = new Select(sortDropdown);

        String[] sortOptions = {
                "Name (A to Z)",
                "Name (Z to A)",
                "Price (low to high)",
                "Price (high to low)"
        };

        int randomIndex = rand.nextInt(sortOptions.length);

        sortDropdown.click();
        Thread.sleep(500);
        select.selectByVisibleText(sortOptions[randomIndex]);

        System.out.println("Randomly selected option: " + sortOptions[randomIndex]);

        Thread.sleep(1000);
        WebElement cartIcon =
                driver.findElement(By.className("shopping_cart_link"));
        cartIcon.click();
        Thread.sleep(2000);
    }

    @Test(priority = 5)
    public void cArt() throws InterruptedException {
        driver.findElement(By.id("checkout")).click();
        Thread.sleep(2000);
    }

    @Test(priority = 6)
    public void yourINFO() throws InterruptedException {
        WebElement firstNameInput =
                driver.findElement(By.id("first-name"));

        String[] names = { "Sara", "Huda", "Yasmeen", "Omar", "Salah" };
        int randomIndex = rand.nextInt(names.length);
        firstNameInput.sendKeys(names[randomIndex]);

        WebElement lastNameInput =
                driver.findElement(By.id("last-name"));

        String[] Lnames = { "Ahmed", "omar", "Amjad", "Hasan", "Ehab" };
        int LrandomIndex = rand.nextInt(Lnames.length);
        lastNameInput.sendKeys(Lnames[LrandomIndex]);

        WebElement postalCodeInput =
                driver.findElement(By.id("postal-code"));

        StringBuilder postalCode = new StringBuilder();
        for (int i = 0; i < 4; i++) {
            int digit = rand.nextInt(10);
            postalCode.append(digit);
        }

        postalCodeInput.sendKeys(postalCode.toString());

        System.out.println(
                "Random first name entered: " + names[randomIndex] + " " +
                "Random last name entered:" + Lnames[LrandomIndex] + " " +
                "Random postal code entered: " + postalCode
        );

        Thread.sleep(500);
        driver.findElement(By.id("continue")).click();
        driver.findElement(By.id("finish")).click();
        Thread.sleep(3000);
        WebElement completeHeader =
                driver.findElement(By.className("complete-header"));

        Assert.assertEquals(completeHeader.getText(),
                "Thank you for your order!",
                "Order was not completed successfully");

        driver.findElement(By.id("back-to-products")).click();
        Thread.sleep(3000);
    }

    @Test(priority = 7)
    public void addanotherprodect() throws InterruptedException {
        String[] productIds = {
                "add-to-cart-test.allthethings()-t-shirt-(red)",
                "add-to-cart-sauce-labs-onesie"
        };

        for (String id : productIds) {
            driver.findElement(By.id(id)).click();
        }

        WebElement cartIcon =
                driver.findElement(By.className("shopping_cart_link"));
        cartIcon.click();
        Thread.sleep(1000);

        driver.findElement(
                By.id("remove-test.allthethings()-t-shirt-(red)")
        ).click();

        Thread.sleep(1000);
        driver.findElement(By.id("continue-shopping")).click();
        Thread.sleep(1000);

        driver.findElement(By.id("item_2_title_link")).click();
        Thread.sleep(1000);

        driver.findElement(By.id("remove")).click();
        Thread.sleep(3000);
    }

    @Test(priority = 8)
    public void logout() throws InterruptedException {
        driver.findElement(By.id("react-burger-menu-btn")).click();
        Thread.sleep(3000);
        driver.findElement(By.id("logout_sidebar_link")).click();
        Assert.assertTrue(driver.getCurrentUrl().contains("saucedemo.com"),
                "Logout failed");

    }
    
    @AfterMethod
    public void takeScreenshotOnFailure(ITestResult result) {

        if (ITestResult.FAILURE == result.getStatus()) {

            TakesScreenshot ts = (TakesScreenshot) driver;
            File src = ts.getScreenshotAs(OutputType.FILE);

            String timeStamp =
                    new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());

            File dest = new File("screenshots/"
                    + result.getName() + "_" + timeStamp + ".png");

            try {
                Files.copy(src, dest);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @AfterTest
    public void AfterMyTest() {
    	driver.quit();

    }
}
