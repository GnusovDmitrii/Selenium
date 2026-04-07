package ru.netology;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


public class CardOrderTest {
    private WebDriver driver;

    @BeforeAll
    public static void setUpAll() {
        WebDriverManager.chromedriver().setup();
    }

    @BeforeEach
    public void beforeEach() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--no-sandbox");
        options.addArguments("--headless"); // Для CI
        driver = new ChromeDriver(options);
        driver.get("http://localhost:9999");
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));
    }

    @AfterEach
    public void afterEach() {
        if (driver != null) {
            driver.quit();
        }
    }

    @Test
    public void positiveTest() {

        WebElement form = driver.findElement(By.className("App_appContainer__3jRx1"));
        form.findElement(By.cssSelector("[data-test-id=name] input")).sendKeys("Иванов Иван");
        form.findElement(By.cssSelector("[data-test-id=phone] input")).sendKeys("+79001234567");
        form.findElement(By.cssSelector("[data-test-id=agreement]")).click();
        form.findElement(By.cssSelector(".button__content")).click();
        WebElement result = driver.findElement(By.cssSelector("[data-test-id='order-success']"));
        assertTrue(result.isDisplayed());
        assertEquals("Ваша заявка успешно отправлена! Наш менеджер свяжется с вами в ближайшее время.", result.getText().trim());

    }

    @Test
    public void negativeTestInvalidName() {

        WebElement form = driver.findElement(By.className("App_appContainer__3jRx1"));
        form.findElement(By.cssSelector("[data-test-id=name] input")).sendKeys("Ivan Ivanov");
        form.findElement(By.cssSelector("[data-test-id=phone] input")).sendKeys("+79001234567");
        form.findElement(By.cssSelector("[data-test-id=agreement]")).click();
        form.findElement(By.cssSelector(".button__content")).click();
        String text = driver.findElement(By.cssSelector("[data-test-id=name].input_invalid .input__sub")).getText();
        assertEquals("Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы.", text.trim());


    }

    @Test
    public void negativeTestInvalidPhone() {

        WebElement form = driver.findElement(By.className("App_appContainer__3jRx1"));
        form.findElement(By.cssSelector("[data-test-id=name] input")).sendKeys("Ivan Ivanov");
        form.findElement(By.cssSelector("[data-test-id=phone] input")).sendKeys("+790123456");
        form.findElement(By.cssSelector("[data-test-id=agreement]")).click();
        form.findElement(By.cssSelector(".button__content")).click();
        String text = driver.findElement(By.cssSelector("[data-test-id='phone'] .input__sub")).getText();
    }

    @Test
    public void negativeTestInvalidCheckbox() {
        driver.findElement(By.cssSelector("[data-test-id=name] .input__control")).sendKeys("Василий");
        driver.findElement(By.cssSelector("[data-test-id=phone] .input__control")).sendKeys("+79270000000");
        driver.findElement(By.className("button")).click();
        WebElement invalidElement = driver.findElement(By.cssSelector("[data-test-id=agreement].input_invalid"));
        assertEquals(true, invalidElement.isDisplayed());
    }

    @Test
    public void negativeTestInvalidTelefon() {
        driver.findElement(By.cssSelector("[data-test-id=name] .input__control")).sendKeys("Василий-Иван");
        driver.findElement(By.cssSelector("[data-test-id=agreement] .checkbox__box")).click();
        driver.findElement(By.className("button")).click();
        String text = driver.findElement(By.cssSelector("[data-test-id=phone].input_invalid .input__sub")).getText();
        assertEquals("Поле обязательно для заполнения", text.trim());
    }

    @Test
    public void negativeTestInvalidName2() {
        driver.findElement(By.cssSelector("[data-test-id=phone] .input__control")).sendKeys("+792700000000");
        driver.findElement(By.cssSelector("[data-test-id=agreement] .checkbox__box")).click();
        driver.findElement(By.className("button")).click();
        String text = driver.findElement(By.cssSelector("[data-test-id=name].input_invalid .input__sub")).getText();
        assertEquals("Поле обязательно для заполнения", text.trim());


    }


}