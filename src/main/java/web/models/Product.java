package web.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = "images")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String description;

    private Integer price;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL,orphanRemoval = true,fetch = FetchType.EAGER)
    private List<Image> images = new ArrayList<>();

    @ManyToMany
    @JoinTable(joinColumns = @JoinColumn(name = "product_id"),
            inverseJoinColumns = @JoinColumn(name = "category_id"))
    private List<Category> categories;


    public void addImageToProduct(Image image) {

        image.setProduct(this);
        images.add(image);

    }


}


















