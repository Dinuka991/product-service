package orgd.dinuka.productservice.service;

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

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

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
        MockitoAnnotations.openMocks(this);
    }

    // Test to get product by ID when product exists
    @Test
    void testGetProductById_whenProductExists() {
        // Arrange
        Set<Tag> tagSet = new HashSet<>();
        tagSet.add(tag);

        Product product = new Product(1L, "Wax", 44.00, 3, category, tagSet);
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        // Act
        Product resultProduct = productService.getProductById(1L);

        // Assert
        assertNotNull(resultProduct);
        assertEquals("Wax", resultProduct.getName());
        assertEquals(44.00, resultProduct.getPrice());
        assertEquals(1, resultProduct.getTags().size());

        // Verify
        verify(productRepository, times(1)).findById(1L);
    }

    // Test for handling exception when product is not found
    @Test
    void testGetProductById_whenProductDoesNotExist() {
        // Arrange
        when(productRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ProductNotFoundException.class, () -> productService.getProductById(999L));

        // Verify
        verify(productRepository, times(1)).findById(999L);
    }

    // Test to get all products
    @Test
    void testGetAllProducts() {
        // Arrange
        Set<Tag> tagSet = new HashSet<>();
        tagSet.add(tag);

        Product product1 = new Product(1L, "Wax", 44.00, 3, category, tagSet);
        Product product2 = new Product(2L, "Fax", 64.00, 2, category, tagSet);
        List<Product> productList = Arrays.asList(product1, product2);

        when(productRepository.findAll()).thenReturn(productList);

        // Act
        List<Product> resultProductList = productService.getAll();

        // Assert
        assertNotNull(resultProductList);
        assertEquals(2, resultProductList.size());
        assertEquals("Wax", resultProductList.get(0).getName());

        // Verify
        verify(productRepository, times(1)).findAll();
    }

    // Test for adding a product
    @Test
    void testAddProduct_success() {
        // Arrange
        Set<Tag> tagSet = new HashSet<>();
        tagSet.add(tag);

        Product product = new Product(1L, "Wax", 44.00, 3, category, tagSet);
        when(productRepository.save(any(Product.class))).thenReturn(product);

        // Act
        Product resultProduct = productService.addProduct(product);

        // Assert
        assertNotNull(resultProduct);
        assertEquals("Wax", resultProduct.getName());

        // Verify
        verify(productRepository, times(1)).save(product);
    }

    // Test for deleting a product
    @Test
    void testDeleteProductById_success() {
        // Arrange
        doNothing().when(productRepository).deleteById(1L);

        // Act
        productService.deleteProductById(1L);

        // Verify
        verify(productRepository, times(1)).deleteById(1L);
    }

    // Test for updating a product
    @Test
    void testUpdateProduct_success() {
        // Arrange
        Set<Tag> tagSet = new HashSet<>();
        tagSet.add(tag);

        Product existingProduct = new Product(1L, "Wax", 44.00, 3, category, tagSet);
        when(productRepository.findById(1L)).thenReturn(Optional.of(existingProduct));
        when(productRepository.save(any(Product.class))).thenReturn(existingProduct);

        // Act
        Product updatedProduct = productService.updateProduct(1L, existingProduct);

        // Assert
        assertNotNull(updatedProduct);
        assertEquals("Wax", updatedProduct.getName());

        // Verify
        verify(productRepository, times(1)).findById(1L);
        verify(productRepository, times(1)).save(existingProduct);
    }
}
