package web.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import web.models.Image;
import web.models.Product;
import web.repository.ProductRepository;

import javax.transaction.Transactional;
import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductService {

    private final ProductRepository productRepository;

    @Transactional
    public List<Product> listProducts(String name) {
        if (name != null) return productRepository.findByName(name);
        return (List<Product>) productRepository.findAll();
    }

    @Transactional
    public void saveProduct(Product product, MultipartFile file) throws IOException {
        Image image;
        if (file.getSize() != 0) {
            image = toImageEntity(file);
            product.addImageToProduct(image);
        }

        log.info("Saving new Product. Title: {}", product.getName());
        productRepository.save(product);
    }

    public Image toImageEntity(MultipartFile file) throws IOException {
        Image image = new Image();
        image.setOriginalFileName(file.getOriginalFilename());
        image.setContentType(file.getContentType());
        image.setSize(file.getSize());
        image.setBytes(file.getBytes());
        return image;
    }

    @Transactional
    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }

    @Transactional
    public Product getProductById(Long id) {
        return productRepository.findById(id).orElse(null);
    }
}
