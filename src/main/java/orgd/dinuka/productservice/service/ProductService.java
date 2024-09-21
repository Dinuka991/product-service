package orgd.dinuka.productservice.service;

import orgd.dinuka.productservice.entity.Product;

import java.util.List;

public interface ProductService {

    Product addProduct(Product product);
    List<Product> getAll();
    Product getProductById(Long id);
    Product getProductByName(String name);
    void  deleteProduct(Long id);
}
