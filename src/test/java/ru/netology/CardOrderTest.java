package ru.netology;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CardOrderTest {
    private WebDriver driver;
    private WebDriverWait wait;

    @BeforeAll
    public static void setupAll() {
        WebDriverManager.chromedriver().setup();
    }

    @BeforeEach
    public void beforeEach() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--start-maximized");
        driver = new ChromeDriver(options);
        wait = new WebDriverWait(driver, Duration.ofSeconds(15));
    }

    @AfterEach
    public void afterEach() {
        if (driver != null) {
            driver.quit();
        }
    }

    @Test
    public void positiveTest() {
        driver.get("http://localhost:9999");

        // Заполняем поля
        driver.findElement(By.name("name")).sendKeys("Иванов Иван");
        driver.findElement(By.name("phone")).sendKeys("+79001234567");

        // Клик по чекбоксу через JavaScript
        WebElement checkbox = driver.findElement(By.name("agreement"));
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", checkbox);

        // Клик по кнопке
        driver.findElement(By.className("button")).click();

        // Ждем и проверяем сообщение об успехе
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("App_appContainer__3jRx1")));

        String actualText = driver.findElement(By.className("App_appContainer__3jRx1")).getText();
        assertTrue(actualText.contains("успешно отправлена"));
    }

    @Test
    public void negativeTestInvalidName() {
        driver.get("http://localhost:9999");

        driver.findElement(By.name("name")).sendKeys("Ivan Ivanov");
        driver.findElement(By.name("phone")).sendKeys("+79001234567");

        // Клик по чекбоксу через JavaScript
        WebElement checkbox = driver.findElement(By.name("agreement"));
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", checkbox);

        // Клик по кнопке
        driver.findElement(By.className("button")).click();

        // Ждем сообщение об ошибке
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("[data-test-id='name'] .input__sub")));

        WebElement errorMessage = driver.findElement(By.cssSelector("[data-test-id='name'] .input__sub"));
        assertTrue(errorMessage.isDisplayed());
    }

    @Test
    public void negativeTestInvalidPhone() {
        driver.get("http://localhost:9999");

        driver.findElement(By.name("name")).sendKeys("Иванов Иван");
        driver.findElement(By.name("phone")).sendKeys("+790012345");

        // Клик по чекбоксу через JavaScript
        WebElement checkbox = driver.findElement(By.name("agreement"));
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", checkbox);

        // Клик по кнопке
        driver.findElement(By.className("button")).click();

        // Ждем сообщение об ошибке
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("[data-test-id='phone'] .input__sub")));

        WebElement errorMessage = driver.findElement(By.cssSelector("[data-test-id='phone'] .input__sub"));
        assertTrue(errorMessage.isDisplayed());
    }
}
