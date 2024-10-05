package orgd.dinuka.productservice.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
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
    private final RestTemplate restTemplate; // Keep only one RestTemplate

    @Override
    public Product addProduct(Product product) {
        return repository.save(product);
    }

    @Override
    public List<Product> getAll() {
        return repository.findAll();
    }

    @Override
    public Product getProductById(Long id) {
        // Throwing a meaningful exception when product is not found
        return repository.findById(id).orElseThrow(() ->
                new ProductNotFoundException("Product with ID " + id + " not found"));
    }

    @Override
    public Product getProductByName(String name) {
        return repository.findByName(name);
    }

    @Override
    public void deleteProduct(Long id) {
        repository.deleteById(id);
    }

    // Method to call external API and handle exceptions
    public List<Object> getExtProducts() {
        String url = "https://jsonplaceholder.typicode.com/users";
        try {
            Object[] users = restTemplate.getForObject(url, Object[].class);
            return users != null ? Arrays.asList(users) : Collections.emptyList();
        } catch (RestClientException ex) {
            log.error("Cannot connect to external service", ex);
            return Collections.emptyList();
        }
    }

    @Override
    public Product updateProduct(long id, Product existingProduct) {
        // Check if the product exists before updating
        if (!repository.existsById(id)) {
            throw new ProductNotFoundException("Cannot update. Product with ID " + id + " does not exist.");
        }
        return repository.save(existingProduct);
    }

    @Override
    public void deleteProductById(long id) {
        // Check if the product exists before deletion
        if (!repository.existsById(id)) {
            throw new ProductNotFoundException("Cannot delete. Product with ID " + id + " does not exist.");
        }
        repository.deleteById(id);
    }
}
