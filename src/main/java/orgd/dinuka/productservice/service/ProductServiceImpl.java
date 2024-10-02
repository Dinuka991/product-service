package orgd.dinuka.productservice.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import orgd.dinuka.productservice.dao.ProductRepository;
import orgd.dinuka.productservice.entity.Product;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class ProductServiceImpl implements ProductService {

    private final ProductRepository repository;
    private final RestTemplate RestTemplate;
    private final RestTemplate restTemplate;

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
        return repository.findById(id).orElseThrow();
    }

    @Override
    public Product getProductByName(String name) {
        return repository.findByName(name);
    }

    @Override
    public void deleteProduct(Long id) {
        repository.deleteById(id);
    }


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
}
