package orgd.dinuka.productservice.controller;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import orgd.dinuka.productservice.entity.Category;
import orgd.dinuka.productservice.entity.Product;
import orgd.dinuka.productservice.entity.Tag;
import orgd.dinuka.productservice.service.ProductService;

import java.util.*;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WebMvcTest(ProductController.class)
public class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductService productService;

    @Test
    void testGetAllProducts() throws Exception {
        Category category = new Category(1L, "Electronics", "Electronic items", null);
        Tag tag1 = new Tag(1L, "Home Appliances", new HashSet<>());
        Tag tag2 = new Tag(2L, "Gadgets", new HashSet<>());

        Set<Tag> tagSet = new HashSet<>(Arrays.asList(tag1, tag2));
        Product product1 = new Product(1L, "TV", 500.00, 10, category, tagSet);
        Product product2 = new Product(2L, "Laptop", 1000.00, 5, category, tagSet);

        List<Product> products = Arrays.asList(product1, product2);

        Mockito.when(productService.getAll()).thenReturn(products);

        mockMvc.perform(get("/product/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("TV"))
                .andExpect(jsonPath("$[1].name").value("Laptop"));
    }

    @Test
    void testGetProductById() throws Exception {
        Category category = new Category(1L, "Electronics", "Electronic items", null);
        Tag tag1 = new Tag(1L, "Home Appliances", new HashSet<>());
        Set<Tag> tagSet = new HashSet<>(List.of(tag1));

        Product product = new Product(1L, "TV", 500.00, 10, category, tagSet);

        Mockito.when(productService.getProductById(1L)).thenReturn(product);

        mockMvc.perform(get("/product/get/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("TV"))
                .andExpect(jsonPath("$.price").value(500.00))
                .andExpect(jsonPath("$.tags[0].name").value("Home Appliances"));
    }

    @Test
    void testGetProductByName() throws Exception {
        Category category = new Category(1L, "Electronics", "Electronic items", null);
        Tag tag1 = new Tag(1L, "Home Appliances", new HashSet<>());
        Set<Tag> tagSet = new HashSet<>(List.of(tag1));

        Product product = new Product(1L, "TV", 500.00, 10, category, tagSet);

        Mockito.when(productService.getProductByName("TV")).thenReturn(product);

        mockMvc.perform(get("/product/get/TV"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("TV"))
                .andExpect(jsonPath("$.category.name").value("Electronics"))
                .andExpect(jsonPath("$.tags[0].name").value("Home Appliances"));
    }

    @Test
    void testSaveProduct() throws Exception {
        Category category = new Category(1L, "Electronics", "Electronic items", null);
        Tag tag1 = new Tag(1L, "Home Appliances", new HashSet<>());
        Set<Tag> tagSet = new HashSet<>(List.of(tag1));

        Product product = new Product(1L, "Fridge", 1200.00, 2, category, tagSet);

        Mockito.when(productService.addProduct(Mockito.any(Product.class))).thenReturn(product);

        mockMvc.perform(post("/product/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Fridge\", \"price\":1200.00, \"quantity\":2, \"category\":{\"id\":1, \"name\":\"Electronics\", \"description\":\"Electronic items\"}, \"tags\":[{\"id\":1, \"name\":\"Home Appliances\"}]}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Fridge"))
                .andExpect(jsonPath("$.price").value(1200.00))
                .andExpect(jsonPath("$.category.name").value("Electronics"))
                .andExpect(jsonPath("$.tags[0].name").value("Home Appliances"));
    }

    @Test
    void testDeleteProduct() throws Exception {
        Mockito.doNothing().when(productService).deleteProduct(1L);

        mockMvc.perform(delete("/product/delete/1"))
                .andExpect(status().isOk());
    }

    @Test
    void testGetUsers() throws Exception {
        Product externalProduct = new Product(1L, "External Product", 700.00, 5, null, new HashSet<>());
        Set<Product> externalProducts = new HashSet<>(List.of(externalProduct));

        Mockito.when(productService.getExtProducts()).thenReturn(Collections.singletonList(externalProducts));

        mockMvc.perform(get("/product/test"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("External Product"));
    }
}
