package az.edu.itbrains.ecommerce.models;


import jakarta.persistence.*;

@Entity
public class Photo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String url;
    private boolean selected;

    @ManyToOne
    private Product product;
}
