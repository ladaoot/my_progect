package web.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import web.models.Product;
import web.repository.ProductRepository;
import web.service.ProductService;

import javax.transaction.Transactional;
import java.io.IOException;

@Controller
public class ProductController {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductService productService;

    @GetMapping("/products")
    @Transactional
    public String product (Model model){
        model.addAttribute("title","Our Products");

        Iterable<Product> products = productRepository.findAll();
        model.addAttribute("products",products);

        return "product";
    }

    @GetMapping("/products/add")
    public String addProduct(Model model){
        return "product-add";
    }

    @PostMapping("/products/add")
    @Transactional
    public String addingProduct (Product product, @RequestParam("file")MultipartFile file) throws IOException {

        productService.saveProduct(product,file);

        return "redirect:/products";

    }

    @GetMapping("/products/{id}")
    public String getInfo(Model model, @PathVariable("id") Long id){
        model.addAttribute("el",productService.getProductById(id));
        model.addAttribute("images",productService.getProductById(id).getImages());
        return "product-info";
    }

}



















