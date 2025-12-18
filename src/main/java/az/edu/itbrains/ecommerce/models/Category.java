package az.edu.itbrains.ecommerce.models;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String seoUrl;
    @Column(columnDefinition = "boolean default false")
    private boolean featured;

    public Category(String name, String seoUrl) {
        this.name = name;
        this.seoUrl = seoUrl;
    }
}
