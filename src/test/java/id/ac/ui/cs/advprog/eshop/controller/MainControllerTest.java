package id.ac.ui.cs.advprog.eshop.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MainControllerTest {

    @InjectMocks
    private MainController mainController;

    @BeforeEach
    void setUp() {
        // Initialize mocks and inject them into the controller
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testMainPage() {
        // Call the method to test the main page
        String viewName = mainController.mainPage();

        // Assert that the returned view name is as expected
        assertEquals("Homepage", viewName, "The view name should be 'Homepage'");
    }
}