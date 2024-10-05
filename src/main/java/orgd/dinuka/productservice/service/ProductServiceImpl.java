package orgd.dinuka.productservice.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import orgd.dinuka.productservice.dao.ProductRepository;
import orgd.dinuka.productservice.entity.Product;
import orgd.dinuka.productservice.exception.ProductNotFoundException;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class ProductServiceImpl implements ProductService {

    private final ProductRepository repository;
    private final RestTemplate restTemplate;

    @Override
    public Product addProduct(Product product) {
        log.info("Adding new product: {}", product.getName());
        return repository.save(product);
    }

    @Override
    public List<Product> getAll() {
        log.info("Fetching all products from the repository");
        return repository.findAll();
    }

    @Override
    public Product getProductById(Long id) {
        log.info("Fetching product by ID: {}", id);
        return repository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("Product with ID " + id + " not found"));
    }

    @Override
    public Product getProductByName(String name) {
        log.info("Fetching product by name: {}", name);
        Product product = repository.findByName(name);
        if (product == null) {
            log.warn("Product with name {} not found", name);
            throw new ProductNotFoundException("Product with name " + name + " not found");
        }
        return product;
    }

    @Override
    public void deleteProduct(Long id) {
        log.info("Deleting product by ID: {}", id);
        if (!repository.existsById(id)) {
            log.warn("Product with ID {} not found for deletion", id);
            throw new ProductNotFoundException("Product with ID " + id + " does not exist");
        }
        repository.deleteById(id);
        log.info("Product with ID {} successfully deleted", id);
    }

    // External API call method with enhanced error handling and logging
    public List<Object> getExtProducts() {
        String url = "https://jsonplaceholder.typicode.com/users";
        log.info("Calling external service to fetch users");
        try {
            Object[] users = restTemplate.getForObject(url, Object[].class);
            log.info("Successfully fetched external users");
            return users != null ? Arrays.asList(users) : Collections.emptyList();
        } catch (HttpClientErrorException ex) {
            log.error("Client error when calling external service, HTTP status: {}", ex.getStatusCode(), ex);
            return Collections.emptyList();
        } catch (RestClientException ex) {
            log.error("Failed to connect to external service", ex);
            return Collections.emptyList();
        }
    }

    @Override
    public Product updateProduct(long id, Product updatedProduct) {
        log.info("Updating product with ID: {}", id);
        if (!repository.existsById(id)) {
            log.warn("Product with ID {} not found for update", id);
            throw new ProductNotFoundException("Cannot update. Product with ID " + id + " does not exist.");
        }
        updatedProduct.setId(id); // Ensure the ID is set
        Product savedProduct = repository.save(updatedProduct);
        log.info("Product with ID {} successfully updated", id);
        return savedProduct;
    }

    @Override
    public void deleteProductById(long id) {
        log.info("Deleting product by ID: {}", id);
        if (!repository.existsById(id)) {
            log.warn("Product with ID {} not found for deletion", id);
            throw new ProductNotFoundException("Cannot delete. Product with ID " + id + " does not exist.");
        }
        repository.deleteById(id);
        log.info("Product with ID {} successfully deleted", id);
    }
}
