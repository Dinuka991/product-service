package orgd.dinuka.productservice.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import orgd.dinuka.productservice.entity.Address;

@Entity
@Table(name = "supplier")
@Getter
@Setter
public class Supplier {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String name;

    @NotNull
    private String contactNumber;

    @ManyToOne
    @JoinColumn(name = "address_id")
    private Address address;

}
