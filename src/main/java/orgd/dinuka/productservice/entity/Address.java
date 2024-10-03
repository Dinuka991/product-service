package orgd.dinuka.productservice.entity;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "address")
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String line1;
    private String street;
    private String unitNumber;
    private String postalCode;

    @OneToMany(mappedBy = "address", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Supplier> suppliers = new ArrayList<>();

    // Helper methods to maintain bidirectional relationship
    public void addSupplier(Supplier supplier) {
        suppliers.add(supplier);
        supplier.setAddress(this);
    }

    public void removeSupplier(Supplier supplier) {
        suppliers.remove(supplier);
        supplier.setAddress(null);
    }

    // Getters and Setters
}
