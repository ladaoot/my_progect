package web.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import web.models.Product;
import web.repository.ProductRepository;

@Controller
public class ProductController {

    @Autowired
    private ProductRepository productRepository;

    @GetMapping("/products")
    public String home(Model model){
        model.addAttribute("title","Our Products");

        Iterable<Product> products = productRepository.findAll();
        model.addAttribute("products",products);

        return "product";
    }
}



















