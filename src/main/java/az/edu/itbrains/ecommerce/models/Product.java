package az.edu.itbrains.ecommerce.models;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;


@Getter
@Setter
@Entity
@NoArgsConstructor
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


    public Product(String name, String description, String shortDescription, String specification, double price, double discount, String barcode, Category category) {
        this.name = name;
        this.description = description;
        this.shortDescription = shortDescription;
        this.specification = specification;
        this.price = price;
        this.discount = discount;
        this.barcode = barcode;
        this.category = category;
    }
}
