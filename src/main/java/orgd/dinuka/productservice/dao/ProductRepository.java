package orgd.dinuka.productservice.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import orgd.dinuka.productservice.entity.Product;

@Repository
public interface ProductRepository  extends JpaRepository<Product,Long> {

    Product findByName(String name);
}
