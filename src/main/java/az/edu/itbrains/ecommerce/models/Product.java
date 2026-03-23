package az.edu.itbrains.ecommerce.models;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;


@Data
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    @Column(length = 1000)
    private String description;
    private String shortDescription;
    private String specification;
    private double price;
    private double discount;
    private String barcode;
    @Column(columnDefinition = "boolean default false")
    private boolean featured;
    @Column(columnDefinition = "boolean default false")
    private boolean hotTrending;

    @ManyToOne
    private Category category;

    @OneToMany(mappedBy = "product")
    private List<ColorSize> colorSizes = new ArrayList<>();

    @OneToMany(mappedBy = "product")
    private List<Photo> photos = new ArrayList<>();

    @OneToMany(mappedBy = "product")
    private List<OrderItem> orderItems = new ArrayList<>();


}
