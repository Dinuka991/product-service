package orgd.dinuka.productservice;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

import orgd.dinuka.productservice.dao.ProductRepository;
import orgd.dinuka.productservice.entity.Category;
import orgd.dinuka.productservice.entity.Product;
import orgd.dinuka.productservice.entity.Tag;
import orgd.dinuka.productservice.service.ProductService;
import orgd.dinuka.productservice.service.ProductServiceImpl;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

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
        MockitoAnnotations.openMocks(this);  // Initialize mocks
    }

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
}
