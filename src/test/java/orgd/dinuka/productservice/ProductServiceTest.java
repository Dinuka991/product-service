package orgd.dinuka.productservice;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import orgd.dinuka.productservice.dao.ProductRepository;
import orgd.dinuka.productservice.entity.Category;
import orgd.dinuka.productservice.entity.Product;
import orgd.dinuka.productservice.entity.Tag;
import orgd.dinuka.productservice.exception.ProductNotFoundException;
import orgd.dinuka.productservice.service.ProductServiceImpl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;


import java.util.*;

public class ProductServiceTest {

    @InjectMocks
    private ProductServiceImpl productService;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private Category category;

    @Mock
    private Tag tag;

    @BeforeEach
    void setUp() {
        // Initialize mocks
        MockitoAnnotations.openMocks(this);
    }

    // Test to get product by ID when product exists
    @Test
    void getProductById_whenProductExists() {
        // Arrange: Create mock Product with Category and Tags
        Set<Tag> tagSet = new HashSet<>();
        tagSet.add(tag);  // Adding mock tag to the set

        Product product = new Product(1L, "Wax", 44.00, 3, category, tagSet);

        // Mock the repository behavior
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        // Act: Call the service method
        Product product1 = productService.getProductById(1L);

        // Assert: Validate the result
        assertNotNull(product1);
        assertEquals("Wax", product1.getName());

        // Verify that repository's findById was called once
        verify(productRepository, times(1)).findById(1L);
    }

    // Test for handling exception when product is not found
    @Test
    void getProductById_whenProductDoesNotExist() {
        // Arrange: Mock the repository to return empty
        when(productRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert: Expect ProductNotFoundException
        assertThrows(ProductNotFoundException.class, () -> productService.getProductById(999L));

        // Verify that findById was called
        verify(productRepository, times(1)).findById(999L);
    }

    // Test to get all products
    @Test
    void getAllProducts() {
        // Arrange: Mock Product and Tag objects
        Set<Tag> tagSet = new HashSet<>();
        tagSet.add(tag);  // Adding mock tag to the set

        Product product = new Product(1L, "Wax", 44.00, 3, category, tagSet);
        Product product2 = new Product(2L, "Fax", 64.00, 3, category, tagSet);

        // Create a list of products and add the products to it
        List<Product> productList = new ArrayList<>();
        productList.add(product);
        productList.add(product2);

        // Mock the repository behavior
        when(productRepository.findAll()).thenReturn(productList);

        // Act: Call the service method
        List<Product> productList1 = productService.getAll();

        // Assert: Validate that the returned product list is not null
        assertNotNull(productList1);
        assertEquals(2, productList1.size());
        assertEquals("Wax", productList1.get(0).getName());

        // Verify that the repository's findAll method was called exactly once
        verify(productRepository, times(1)).findAll();
    }

    // Test for adding a product
    @Test
    void addProduct_success() {
        // Arrange: Create a mock Product
        Set<Tag> tagSet = new HashSet<>();
        tagSet.add(tag);

        Product product = new Product(1L, "Wax", 44.00, 3, category, tagSet);

        // Mock the repository's save method
        when(productRepository.save(any(Product.class))).thenReturn(product);

        // Act: Call the service method
        Product savedProduct = productService.addProduct(product);

        // Assert: Validate the result
        assertNotNull(savedProduct);
        assertEquals("Wax", savedProduct.getName());

        // Verify that repository's save method was called once
        verify(productRepository, times(1)).save(product);
    }

    // Test for deleting a product
    @Test
    void deleteProductById_success() {
        // Arrange: Mock the repository's behavior
        doNothing().when(productRepository).deleteById(1L);

        // Act: Call the service method
        productService.deleteProductById(1L);

        // Verify that repository's deleteById method was called once
        verify(productRepository, times(1)).deleteById(1L);
    }

    // Test for updating a product
    @Test
    void updateProduct_success() {
        // Arrange: Create a mock Product
        Set<Tag> tagSet = new HashSet<>();
        tagSet.add(tag);

        Product existingProduct = new Product(1L, "Wax", 44.00, 3, category, tagSet);

        // Mock the repository's findById and save methods
        when(productRepository.findById(1L)).thenReturn(Optional.of(existingProduct));
        when(productRepository.save(any(Product.class))).thenReturn(existingProduct);

        // Act: Call the service method
        Product updatedProduct = productService.updateProduct(1L, existingProduct);

        // Assert: Validate the result
        assertNotNull(updatedProduct);
        assertEquals("Wax", updatedProduct.getName());

        // Verify that repository methods were called
        verify(productRepository, times(1)).findById(1L);
        verify(productRepository, times(1)).save(existingProduct);
    }

}
