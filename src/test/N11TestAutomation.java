/*
 * N11 Test Automation using selenium and JUNit
 * Ebubekir Durukal
 * June 30th 2019
 */

package test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class N11TestAutomation {

	@Test
	public void n11Test() throws InterruptedException {

		Logger logger = Logger.getLogger("N11TestAutomation"); //Logger Object
		PropertyConfigurator.configure("./SeleniumTest/Log4j.properties");	  //Logger Configuration File
		/*
		 * This test is written in a macboook. If you are trying it on a 
		 * Windows PC, it should work without a problem since 2 different versions of 
		 * chrome webdriver included in lib directory.
		 */
		String os = System.getProperty("os.name").toLowerCase();	
		if (os.contains("win")) {
			System.setProperty("webdriver.chrome.driver", "./SeleniumTest/lib/chromedriver_win.exe"); 

		} else if (os.contains("mac")) {
			System.setProperty("webdriver.chrome.driver", "./SeleniumTest/lib/chromedriver_macos"); 

		}
		WebDriver driver = new ChromeDriver(); // Initialize Chrome Driver
		WebDriverWait wait = new WebDriverWait(driver, 5); // Timeout after 5 secs.
		driver.manage().window().maximize();
		logger.info(" New test initialized");
		/*
		 * Below are the necessary methods for the scenario. Each method contains its
		 * own assertion for testing.
		 */

		login(driver, logger, wait);
		search(logger, driver, wait);
		add2Favorites(logger, driver, wait);
		erase(logger, driver, wait);
		logout(driver, logger);

	}

	private void erase(Logger logger, WebDriver driver, WebDriverWait wait) throws InterruptedException {
		WebElement erase = driver.findElement(By.className("deleteProFromFavorites"));
		erase.click();
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("message")));
		assertTrue(driver.getPageSource().contains("silindi"));
		logger.info("Erase Verified");
		WebElement tamam = driver.findElement(By.xpath("/html/body/div[5]/div/div/span"));
		tamam.click();
		Thread.sleep(1500);
	}

	private void logout(WebDriver driver, Logger logger) throws InterruptedException {
		WebElement hesabım1 = driver
				.findElement(By.xpath("//*[@id=\'header\']/div/div/div[2]/div[2]/div[2]/div[1]/a[1]"));
		WebElement logout1 = driver
				.findElement(By.xpath("//*[@id=\'header\']/div/div/div[2]/div[2]/div[2]/div[2]/div/a[8]"));
		Actions builder = new Actions(driver);
		builder.moveToElement(hesabım1).build().perform();
		logout1.click();
		Thread.sleep(4000);
		driver.close();
		logger.info("Browser closed");
	}

	private void add2Favorites(Logger logger, WebDriver driver, WebDriverWait wait) throws InterruptedException {
		JavascriptExecutor js = ((JavascriptExecutor) driver);
		js.executeScript("window.scrollTo(0, document.body.scrollHeight)");
		wait.until(ExpectedConditions.visibilityOfElementLocated(
				By.cssSelector("#contentListing > div > div > div.productArea > div.pagination > a:nth-child(2)")));
		WebElement secondPage = driver.findElement(
				By.cssSelector("#contentListing > div > div > div.productArea > div.pagination > a:nth-child(2)"));
		secondPage.click();
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("filterList")));
		assertEquals(driver.getCurrentUrl(), "https://www.n11.com/arama?q=Iphone&pg=2");
		logger.info("Verified Second Page");
		js.executeScript("window.scrollBy(0,1000)", "");
		WebElement favourite = driver.findElement(By.xpath("//*[@id='p-355878477']/div[2]/span[2]"));
		favourite.click();
		js.executeScript("window.scrollTo(0, 0)");
		WebElement hesabım = driver
				.findElement(By.xpath("//*[@id=\'header\']/div/div/div[2]/div[2]/div[2]/div[1]/a[1]"));
		hesabım.click();
		Thread.sleep(500);
		WebElement istek_listem = driver
				.findElement(By.xpath("//*[@id=\'myAccount\']/div[1]/div[1]/div[2]/ul/li[5]/a"));
		istek_listem.click();
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("listItemTitle")));
		WebElement favourites = driver.findElement(By.className("listItemTitle"));
		favourites.click();
		Thread.sleep(250);
		assertEquals(driver.getCurrentUrl(), "https://www.n11.com/hesabim/favorilerim");
		logger.info("Favourites Page Verified");

	}

	private void search(Logger logger, WebDriver driver, WebDriverWait wait) {
		WebElement search = driver.findElement(By.id("searchData"));
		search.sendKeys("Iphone");
		WebElement searchButton = driver.findElement(By.xpath("//*[@id=\'header\']/div/div/div[2]/div[1]/a/span"));
		searchButton.click();
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("filterList")));
		assertTrue("Text not found!",
				driver.findElements(By.xpath("//*[contains(text(),'" + "iPhone" + "')]")).size() > 10);
		logger.info("Navigated to Iphone page. "
				+ driver.findElements(By.xpath("//*[contains(text(),'" + "iPhone" + "')]")).size() + " iPhones found");
	}

	private void login(WebDriver driver, Logger logger, WebDriverWait wait) {
		driver.get("https://www.n11.com/");
		String url = driver.getCurrentUrl();
		assertEquals(url, "https://www.n11.com/");
		logger.info("URL Verified");
		wait.until(ExpectedConditions
				.visibilityOfElementLocated(By.xpath("//*[@id=\'header\']/div/div/div[2]/div[2]/div[2]/div/div/a[1]")));
		WebElement login = driver.findElement(By.className("btnSignIn"));
		login.click();
		WebElement email = driver.findElement(By.name("email"));
		WebElement password = driver.findElement(By.name("password"));
		WebElement girisyap = driver.findElement(By.id("loginButton"));
		email.sendKeys("ebubekirdr12@gmail.com");
		password.sendKeys(".eba1996");
		girisyap.click();
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("searchData")));
		assertTrue(driver.findElement(By.className("myAccount")).isDisplayed());
		logger.info("Login Verified");
	}

}
