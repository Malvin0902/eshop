package id.ac.ui.cs.advprog.eshop.controller;

import id.ac.ui.cs.advprog.eshop.model.Product;
import id.ac.ui.cs.advprog.eshop.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class ProductControllerTest {

    @Mock
    private ProductService productService;

    @Mock
    private Model model;

    @Mock
    private BindingResult bindingResult;

    @InjectMocks
    private ProductController productController;

    private Product product;

    @BeforeEach
    void setUp() {
        product = new Product();
        product.setProductId("1");
        product.setProductName("Test Product");
        product.setProductQuantity(10);
    }

    @Test
    void testCreateProductPage() {
        String viewName = productController.createProductPage(model);
        assertEquals("createProduct", viewName);
    }

    @Test
    void testCreateProductPostSuccess() {
        when(bindingResult.hasErrors()).thenReturn(false);

        String viewName = productController.createProductPost(product, bindingResult, model);
        verify(productService).create(product);
        assertEquals("redirect:list", viewName);
    }

    @Test
    void testCreateProductPostValidationError() {
        when(bindingResult.hasErrors()).thenReturn(true);

        String viewName = productController.createProductPost(product, bindingResult, model);
        assertEquals("createProduct", viewName);
    }

    @Test
    void testProductListPage() {
        List<Product> products = Arrays.asList(product);
        when(productService.findAll()).thenReturn(products);

        String viewName = productController.productListPage(model);
        assertEquals("productList", viewName);
    }

    @Test
    void testEditProductPageProductFound() {
        when(productService.findById("1")).thenReturn(product);

        String viewName = productController.editProductPage("1", model);
        assertEquals("EditProduct", viewName);
    }

    @Test
    void testEditProductPageProductNotFound() {
        when(productService.findById("1")).thenReturn(null);

        String viewName = productController.editProductPage("1", model);
        assertEquals("redirect:/product/list", viewName);
    }

    @Test
    void testEditProductPostSuccess() {
        when(bindingResult.hasErrors()).thenReturn(false);

        String viewName = productController.editProductPost(product, bindingResult);
        verify(productService).update(product);
        assertEquals("redirect:list", viewName);
    }

    @Test
    void testEditProductPostValidationError() {
        when(bindingResult.hasErrors()).thenReturn(true);

        String viewName = productController.editProductPost(product, bindingResult);
        assertEquals("EditProduct", viewName);
    }

    @Test
    void testDeleteProduct() {
        String viewName = productController.deleteProduct("1");
        verify(productService).delete("1");
        assertEquals("redirect:/product/list", viewName);
    }
}