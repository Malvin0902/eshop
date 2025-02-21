package id.ac.ui.cs.advprog.eshop.service;

import id.ac.ui.cs.advprog.eshop.model.Product;
import id.ac.ui.cs.advprog.eshop.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertNull;

@ExtendWith(MockitoExtension.class)
public class ProductServiceImplTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductServiceImpl productService;

    private Product product;

    @BeforeEach
    void setUp() {
        product = new Product();
        product.setProductId("1");
        product.setProductName("Test Product");
        product.setProductQuantity(10);
    }

    @Test
    void testCreateProductSuccess() {
        // Mock the behavior of the repository's create method
        when(productRepository.create(product)).thenReturn(product);

        Product createdProduct = productService.create(product);

        // Verify that the repository's create method is called
        verify(productRepository).create(product);
        assertNotNull(createdProduct, "Created product should not be null");
        assertEquals("Test Product", createdProduct.getProductName(), "Product name should match");
    }

    @Test
    void testCreateProductWithInvalidName() {
        product.setProductName(""); // Invalid name

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            productService.create(product);
        });

        assertEquals("Product name cannot be empty", exception.getMessage(), "Exception message should match");
    }

    @Test
    void testCreateProductWithNegativeQuantity() {
        product.setProductQuantity(-1); // Invalid quantity

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            productService.create(product);
        });

        assertEquals("Product quantity cannot be negative", exception.getMessage(), "Exception message should match");
    }

    @Test
    void testFindAllProducts() {
        List<Product> products = Arrays.asList(product);
        when(productRepository.findAll()).thenReturn(products.iterator());

        List<Product> allProducts = productService.findAll();

        assertNotNull(allProducts, "The list of products should not be null");
        assertEquals(1, allProducts.size(), "The list should contain 1 product");
        assertEquals("Test Product", allProducts.get(0).getProductName(), "Product name should match");
    }

    @Test
    void testFindProductById() {
        when(productRepository.findById("1")).thenReturn(product);

        Product foundProduct = productService.findById("1");

        assertNotNull(foundProduct, "Product should not be null");
        assertEquals("Test Product", foundProduct.getProductName(), "Product name should match");
    }

    @Test
    void testFindProductByIdNotFound() {
        when(productRepository.findById("1")).thenReturn(null);

        Product foundProduct = productService.findById("1");

        assertNull(foundProduct, "Product should be null when not found");
    }

    @Test
    void testUpdateProductSuccess() {
        when(productRepository.update(product)).thenReturn(product);

        Product updatedProduct = productService.update(product);

        verify(productRepository).update(product);
        assertNotNull(updatedProduct, "Updated product should not be null");
        assertEquals("Test Product", updatedProduct.getProductName(), "Product name should match");
    }

    @Test
    void testUpdateProductWithInvalidName() {
        product.setProductName(""); // Invalid name

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            productService.update(product);
        });

        assertEquals("Product name cannot be empty", exception.getMessage(), "Exception message should match");
    }

    @Test
    void testDeleteProduct() {
        doNothing().when(productRepository).delete("1");

        productService.delete("1");

        verify(productRepository).delete("1");
    }
}