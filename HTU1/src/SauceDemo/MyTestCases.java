package SauceDemo;


import java.util.List;
import java.util.Random;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.support.ui.Select;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;


public class MyTestCases {
	
	String myWebsite = "https://www.saucedemo.com/";
	WebDriver driver= new EdgeDriver();
	
	String userName = "standard_user";
	
	String password = "secret_sauce";
	Random rand = new Random();
	@BeforeTest
	public void mySetUp() throws InterruptedException {
		
		driver.get(myWebsite);
		
		driver.manage().window().maximize();
		
		Thread.sleep(1000);
	
	}
	

	@Test(priority=1)
    public void login() throws InterruptedException {
		
		
		driver.findElement(By.id("user-name")).sendKeys(userName);
		driver.findElement(By.id("password")).sendKeys(password);
		driver.findElement(By.id("login-button")).click();
		
		
		
	}
	

	@Test(priority=2)
    public void aDDTocartbtn() throws InterruptedException  {
		
		
		List<WebElement> addButtons = driver.findElements(By.cssSelector(".btn.btn_primary.btn_small.btn_inventory"));
		for (WebElement addbutton : addButtons) {
		    addbutton.click();
		    
		        }
		    }
		
		
	@Test(priority=3)
	 public void removebtn() throws InterruptedException
	{
		
		List<WebElement> removeButtons = driver.findElements(
		        By.cssSelector(".btn.btn_secondary.btn_small.btn_inventory")
		);


		for (WebElement removeButton : removeButtons) {
		    String buttonId = removeButton.getAttribute("id");
		    if (buttonId == null || !buttonId.contains("sauce-labs-backpack")) {
		        removeButton.click();
		        Thread.sleep(500); 
		    }
		}}
	

	@Test(priority=4)
	 public void sorting() throws InterruptedException {
		WebElement sortDropdown = driver.findElement(By.className("product_sort_container"));
		Select select = new Select(sortDropdown);

		String[] sortOptions = { "Name (A to Z)", "Name (Z to A)", "Price (low to high)", "Price (high to low)" };

		int randomIndex = rand.nextInt(sortOptions.length);

		sortDropdown.click();
		Thread.sleep(500);

		select.selectByVisibleText(sortOptions[randomIndex]);

		System.out.println("Randomly selected option: " + sortOptions[randomIndex]);
		

		Thread.sleep(1000);
		
		WebElement cartIcon = driver.findElement(By.className("shopping_cart_link"));
		cartIcon.click();
		Thread.sleep(2000);

	}
	
	@Test(priority=5)
	 public void cArt() throws InterruptedException {
		
		driver.findElement(By.id("checkout")).click();
		Thread.sleep(2000);
	}
		
	@Test(priority=6)
	 public void yourINFO() throws InterruptedException	{
		
		WebElement firstNameInput = driver.findElement(By.id("first-name"));

		String[] names = { "Sara", "Huda", "Yasmeen", "Omar", "Salah" };

		int randomIndex = rand.nextInt(names.length);

		firstNameInput.sendKeys(names[randomIndex]);

		//System.out.println("Random first name entered: " + names[randomIndex]);
		
		WebElement lastNameInput = driver.findElement(By.id("last-name"));

		String[] Lnames = { "Ahmed", "omar", "Amjad", "Hasan", "Ehab" };

		int LrandomIndex = rand.nextInt(Lnames.length);

		lastNameInput.sendKeys(Lnames[LrandomIndex]);

		
		
		WebElement postalCodeInput = driver.findElement(By.id("postal-code"));

		StringBuilder postalCode = new StringBuilder();

		for (int i = 0; i < 4; i++) {
		    int digit = rand.nextInt(10);
		    postalCode.append(digit);
		}

		postalCodeInput.sendKeys(postalCode.toString());

		System.out.println("Random first name entered: "+names[randomIndex]+"   "+ "Random last name entered:"+Lnames[LrandomIndex]+"   "+"Random postal code entered: " + postalCode);
	
		
		Thread.sleep(500);
		
		driver.findElement(By.id("continue")).click();
		driver.findElement(By.id("finish")).click();
		driver.findElement(By.id("back-to-products")).click();
		Thread.sleep(3000);

	}
	
	@Test(priority=7)
	public void addanotherprodect() throws InterruptedException {
		
		String[] productIds = {"add-to-cart-test.allthethings()-t-shirt-(red)", "add-to-cart-sauce-labs-onesie"};

			for (String id : productIds) {
			    driver.findElement(By.id(id)).click();
			}
			WebElement cartIcon = driver.findElement(By.className("shopping_cart_link"));
			cartIcon.click();
			Thread.sleep(1000);
			driver.findElement(By.id("remove-test.allthethings()-t-shirt-(red)")).click();
			Thread.sleep(1000);
			driver.findElement(By.id("continue-shopping")).click();
			Thread.sleep(1000);
			driver.findElement(By.id("item_2_title_link")).click();
			Thread.sleep(1000);
			driver.findElement(By.id("remove")).click();
		Thread.sleep(3000);
		
	}
	@Test(priority=8)
	public void logout() throws InterruptedException {
		driver.findElement(By.id("react-burger-menu-btn")).click();
		Thread.sleep(3000);
		driver.findElement(By.id("logout_sidebar_link")).click();

	}

	

	
	@AfterTest
    public void AfterMyTest() {	
		
		driver.close();
	}
}
