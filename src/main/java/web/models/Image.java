package web.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.Type;

import javax.persistence.*;


@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = "product")
public class Image {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private  Long id;

    @Lob
    @Type(type = "org.hibernate.type.ImageType")
    private byte[] bytes;

    private String originalFileName;

    private Long size;

    private String contentType;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.REFRESH)
    private Product product;
}
