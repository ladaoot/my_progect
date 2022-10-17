package web.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String description;

    private Integer price;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    private List<Image> images= new ArrayList<>();

    public void addImageToProduct(Image image) {

        image.setProduct(this);
        images.add(image);

    }
}
