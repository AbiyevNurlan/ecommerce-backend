package az.edu.itbrains.ecommerce.models;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;


@Getter
@Setter
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

    @Builder.Default
    @OneToMany(mappedBy = "product")
    private List<ColorSize> colorSizes = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "product")
    private List<Photo> photos = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "product")
    private List<OrderItem> orderItems = new ArrayList<>();


}
