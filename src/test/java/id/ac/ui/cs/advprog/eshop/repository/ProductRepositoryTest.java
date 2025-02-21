package id.ac.ui.cs.advprog.eshop.repository;

import id.ac.ui.cs.advprog.eshop.model.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Iterator;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ProductRepositoryTest {

    @InjectMocks
    ProductRepository productRepository;

    @BeforeEach
    void setUp() {
    }

    @Test
    void testCreateAndFind() {
        Product product = new Product();
        product.setProductId("eb558e9f-1c39-460e-8860-71af6af63bd6");
        product.setProductName("Sampo Cap Bambang");
        product.setProductQuantity(100);
        productRepository.create(product);

        Iterator<Product> productIterator = productRepository.findAll();
        assertTrue(productIterator.hasNext());
        Product savedProduct = productIterator.next();
        assertEquals(product.getProductId(), savedProduct.getProductId());
        assertEquals(product.getProductName(), savedProduct.getProductName());
        assertEquals(product.getProductQuantity(), savedProduct.getProductQuantity());
    }

    @Test
    void testCreateProductWithNullId() {
        Product product = new Product();
        product.setProductName("Test Product");
        product.setProductQuantity(100);
        productRepository.create(product);

        assertNotNull(product.getProductId(), "Product ID should not be null");
    }

    @Test
    void testFindAllIfEmpty() {
        Iterator<Product> productIterator = productRepository.findAll();
        assertFalse(productIterator.hasNext());
    }

    @Test
    void testFindAllIfMoreThanOneProduct() {
        Product product1 = new Product();
        product1.setProductId("eb558e9f-1c39-460e-8860-71af6af63bd6");
        product1.setProductName("Sampo Cap Bambang");
        product1.setProductQuantity(100);
        productRepository.create(product1);

        Product product2 = new Product();
        product2.setProductId("a0f9de46-90b1-437d-a0bf-d0821dde9096");
        product2.setProductName("Sampo Cap Usep");
        product2.setProductQuantity(50);
        productRepository.create(product2);

        Iterator<Product> productIterator = productRepository.findAll();
        assertTrue(productIterator.hasNext());
        Product savedProduct = productIterator.next();
        assertEquals(product1.getProductId(), savedProduct.getProductId());
        savedProduct = productIterator.next();
        assertEquals(product2.getProductId(), savedProduct.getProductId());
        assertFalse(productIterator.hasNext());
    }

    @Test
    void testEditProductSuccess() {
        Product product = new Product();
        product.setProductId("test-id");
        product.setProductName("Original Name");
        product.setProductQuantity(100);
        productRepository.create(product);
        Product updatedProduct = new Product();
        updatedProduct.setProductId("test-id");
        updatedProduct.setProductName("Updated Name");
        updatedProduct.setProductQuantity(200);
        Product result = productRepository.update(updatedProduct);
        assertNotNull(result);
        assertEquals("Updated Name", result.getProductName());
        assertEquals(200, result.getProductQuantity());
    }

    @Test
    void testEditProductPartialUpdate() {
        Product product = new Product();
        product.setProductId("test-id");
        product.setProductName("Original Name");
        product.setProductQuantity(100);
        productRepository.create(product);
        Product partialUpdate = new Product();
        partialUpdate.setProductId("test-id");
        partialUpdate.setProductName("Updated Name");
        partialUpdate.setProductQuantity(100);
        Product result = productRepository.update(partialUpdate);
        assertNotNull(result);
        assertEquals("Updated Name", result.getProductName());
        assertEquals(100, result.getProductQuantity());
    }

    @Test
    void testEditProductNotFound() {
        Product nonExistentProduct = new Product();
        nonExistentProduct.setProductId("non-existent-id");
        nonExistentProduct.setProductName("Test");
        nonExistentProduct.setProductQuantity(1);
        Product result = productRepository.update(nonExistentProduct);
        assertNull(result);
    }

    @Test
    void testDeleteProductSuccess() {
        Product product = new Product();
        product.setProductId("test-id");
        product.setProductName("Test Product");
        product.setProductQuantity(100);
        productRepository.create(product);
        productRepository.delete(product.getProductId());
        assertNull(productRepository.findById(product.getProductId()));
    }

    @Test
    void testDeleteProductWithMultipleProducts() {
        Product product1 = new Product();
        product1.setProductId("id-1");
        product1.setProductName("Product 1");
        product1.setProductQuantity(100);
        productRepository.create(product1);
        Product product2 = new Product();
        product2.setProductId("id-2");
        product2.setProductName("Product 2");
        product2.setProductQuantity(200);
        productRepository.create(product2);
        productRepository.delete("id-1");
        Iterator<Product> productIterator = productRepository.findAll();
        assertTrue(productIterator.hasNext());
        Product remainingProduct = productIterator.next();
        assertEquals("id-2", remainingProduct.getProductId());
        assertFalse(productIterator.hasNext());
    }

    @Test
    void testDeleteNonExistentProduct() {
        productRepository.delete("non-existent-id");
        Iterator<Product> productIterator = productRepository.findAll();
        assertFalse(productIterator.hasNext(), "Product list should be empty if no matching product is found to delete");
    }

    @Test
    void testFindByIdSuccess() {
        Product product = new Product();
        product.setProductId("test-id");
        product.setProductName("Test Product");
        product.setProductQuantity(100);
        productRepository.create(product);
        Product result = productRepository.findById(product.getProductId());
        assertNotNull(result);
        assertEquals(product.getProductName(), result.getProductName());
        assertEquals(product.getProductQuantity(), result.getProductQuantity());
    }

    @Test
    void testFindByIdNotFound() {
        Product result = productRepository.findById("non-existent-id");
        assertNull(result, "Product should be null when not found");

        result = productRepository.findById(null);
        assertNull(result, "Product should be null when productId is null");
    }

    @Test
    void testFindByIdNonMatchingId() {
        Product product = new Product();
        product.setProductId("test-id");
        product.setProductName("Test Product");
        product.setProductQuantity(100);
        productRepository.create(product);

        Product result = productRepository.findById("non-matching-id");
        assertNull(result, "Product should be null when IDs do not match");
    }

    @Test
    void testFindByIdWithNullProductId() {
        Product product = new Product();
        product.setProductId(null);  // Set the productId to null
        product.setProductName("Test Product");
        product.setProductQuantity(100);
        productRepository.create(product);

        Product result = productRepository.findById("non-matching-id");
        assertNull(result, "Product should be null when productId is null");
    }

    @Test
    void testUpdateWithNullProductId() {
        Product product = new Product();
        product.setProductId(null);  // Set to null for testing
        product.setProductName("Test Product");
        product.setProductQuantity(100);
        productRepository.create(product);

        Product updatedProduct = new Product();
        updatedProduct.setProductId("new-id");
        updatedProduct.setProductName("Updated Name");
        updatedProduct.setProductQuantity(150);

        Product result = productRepository.update(updatedProduct);
        assertNull(result, "Update should return null when productId is null and doesn't match");
    }

    @Test
    void testUpdateProductWithNonMatchingId() {
        Product product = new Product();
        product.setProductId("existing-id");
        product.setProductName("Test Product");
        product.setProductQuantity(100);
        productRepository.create(product);

        Product updatedProduct = new Product();
        updatedProduct.setProductId("non-matching-id");
        updatedProduct.setProductName("Updated Name");
        updatedProduct.setProductQuantity(150);

        Product result = productRepository.update(updatedProduct);
        assertNull(result, "Update should return null when IDs don't match");
    }

    @Test
    void testDeleteWithNullProductId() {
        Product product = new Product();
        product.setProductId(null);  // Set to null
        product.setProductName("Test Product");
        product.setProductQuantity(100);
        productRepository.create(product);

        productRepository.delete("non-matching-id");
        Iterator<Product> productIterator = productRepository.findAll();
        assertTrue(productIterator.hasNext(), "Product list should not be empty when trying to delete with non-matching id");
    }
}
