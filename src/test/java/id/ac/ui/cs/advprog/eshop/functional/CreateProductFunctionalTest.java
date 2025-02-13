package id.ac.ui.cs.advprog.eshop.functional;

import io.github.bonigarcia.seljup.SeleniumJupiter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest(webEnvironment = RANDOM_PORT)
@ExtendWith(SeleniumJupiter.class)
public class CreateProductFunctionalTest {

    @LocalServerPort
    private int serverPort;

    @Value("${app.baseUrl:http://localhost}")
    private String testBaseUrl;

    private String baseUrl;

    @BeforeEach
    void setup() {
        baseUrl = String.format("%s:%d", testBaseUrl, serverPort);
    }

    /**
     * Positive scenario:
     * Test creating a valid product and verifying it appears in the list.
     */
    @Test
    void createProduct_validInput_shouldAppearInList(ChromeDriver driver) {
        // Navigate to the "Create Product" page
        driver.get(baseUrl + "/product/create");

        // Fill in product name and quantity with valid data
        driver.findElement(By.id("nameInput")).sendKeys("ValidProduct");
        driver.findElement(By.id("quantityInput")).sendKeys("10");

        // Submit
        WebElement submitButton = driver.findElement(By.cssSelector("button[type='submit']"));
        submitButton.click();

        // Verify the new product is in the list
        List<WebElement> productRows = driver.findElements(By.xpath("//table//tbody//tr"));
        assertFalse(productRows.isEmpty(), "Expecting at least one product in the list after creation.");

        boolean found = productRows.stream().anyMatch(row -> row.getText().contains("ValidProduct"));
        assertTrue(found, "Newly created product should appear in the product list.");
    }

    /**
     * Negative scenario:
     * Test creating a product with empty name. Expect it NOT to appear in the list.
     */
    @Test
    void createProduct_emptyName_shouldNotAppearInList(ChromeDriver driver) {
        // Navigate to the "Create Product" page
        driver.get(baseUrl + "/product/create");

        // Fill in invalid name (empty) and valid quantity
        driver.findElement(By.id("nameInput")).sendKeys("");
        driver.findElement(By.id("quantityInput")).sendKeys("10");

        // Submit
        WebElement submitButton = driver.findElement(By.cssSelector("button[type='submit']"));
        submitButton.click();
        driver.get(baseUrl + "/product/list");

        // Check product list to verify the product with empty name does not appear
        List<WebElement> productRows = driver.findElements(By.xpath("//table//tbody//tr"));
        boolean found = productRows.stream().anyMatch(row -> row.getText().contains("Unnamed"));
        // In a real scenario, you'd expect some error message—here we simply check it's not found.
        assertFalse(found, "Product with empty name must NOT appear in the product list.");
    }

    /**
     * Negative scenario:
     * Test creating a product with a quantity of zero. Expect it NOT to appear in the list.
     */
    @Test
    void createProduct_zeroQuantity_shouldNotAppearInList(ChromeDriver driver) {
        // Navigate to the "Create Product" page
        driver.get(baseUrl + "/product/create");

        // Fill in valid name but invalid quantity (0)
        driver.findElement(By.id("nameInput")).sendKeys("InvalidQuantityProduct");
        driver.findElement(By.id("quantityInput")).sendKeys("0");

        // Submit
        WebElement submitButton = driver.findElement(By.cssSelector("button[type='submit']"));
        submitButton.click();
        driver.get(baseUrl + "/product/list");

        // Check product list to verify the product with invalid quantity does not appear
        List<WebElement> productRows = driver.findElements(By.xpath("//table//tbody//tr"));
        boolean found = productRows.stream().anyMatch(row -> row.getText().contains("InvalidQuantityProduct"));
        assertFalse(found, "Product with zero quantity must NOT appear in the product list.");
    }
}