package web.controllers;

import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import web.models.Bucket;
import web.models.ProductCount;
import web.models.User;
import web.repository.BucketRepository;
import web.repository.ProductCountRepository;
import web.repository.ProductRepository;
import web.service.ProductService;
import web.service.UserService;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BinaryOperator;
import java.util.function.Predicate;

@Controller
public class BucketController {

    @Autowired
    UserService userService;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    BucketRepository bucketRepository;

    @Autowired
    ProductCountRepository productCountRepository;

    Session session;

    @GetMapping("/user/{id}/bucket")
    public String yourBucket(Model model, Principal principal) {
        var user = userService.getUserByPrincipal(principal);
        model.addAttribute("user", user);
        model.addAttribute("products", user.getBucket().getProducts());
        model.addAttribute("title", "Bucket");
        return "bucket";

    }

    @PostMapping("/user/{id}/bucket/add/{product_id}")
    public String addProductToBucket(Model model, Principal principal, @PathVariable("product_id") Long productId) {
        User user = userService.getUserByPrincipal(principal);
        Bucket bucket = user.getBucket();
        var optionalProduct = productRepository.findById(productId);
        if (optionalProduct.isPresent()) {

            var product = optionalProduct.get();
            var products = bucket.getProducts();

            if (products.stream()
                    .anyMatch(productCount -> productCount.getProduct().equals(product))) {

                products.stream()
                        .filter(a -> a.getProduct().getId().equals(productId))
                        .forEach(a -> a.setAmount(a.getAmount().add(BigDecimal.ONE)));
            } else {
                var productCount = new ProductCount();
                productCount.setProduct(product);
                productCount.setAmount(BigDecimal.ONE);
                products.add(productCount);

            }

            bucket.setProducts(products);

        } else {
            model.addAttribute("error", "WE don't have this product");
            return "redirect:/";
        }
        bucketRepository.save(bucket);
        return "redirect:/user/{id}/bucket";

    }

    @Transactional
    @PostMapping("/user/{id}/bucket/add/{product_id}/add_more")
    public String updateAmount(Model model, Principal principal, @PathVariable("product_id") Long productId, @RequestParam("amount") Long amount){

        ProductCount productCount = userService.getUserByPrincipal(principal)
                .getBucket()
                .getProducts()
                .stream()
                .filter(a->a.getProduct().getId().equals(productId))
                .findAny()
                .get();

        if(amount <= 0){

            Bucket bucket = userService.getUserByPrincipal(principal)
                    .getBucket();
            bucket.getProducts().remove(productCount);
            bucketRepository.save(bucket);
            return "redirect:/user/{id}/bucket";


        }

        productCount.setAmount(BigDecimal.valueOf(amount));
        productCountRepository.save(productCount);
        return "redirect:/user/{id}/bucket";
    }

    @PostMapping("/user/{id}/bucket/add/{product_id}/delete")
    public String deleteProduct(Model model,Principal principal,@PathVariable("product_id") Long id){
        Bucket bucket =userService.getUserByPrincipal(principal)
                .getBucket();
        ProductCount productCount = bucket
                .getProducts()
                .stream()
                .filter(a->a.getProduct().getId().equals(id))
                .findAny()
                .get();

        bucket.getProducts().remove(productCount);
        bucketRepository.save(bucket);

        return "redirect:/user/{id}/bucket";
    }

    @PostMapping("/user/{id}/bucket/clean")
    public String cleanBucket(Model model,Principal principal){

        Bucket bucket =userService.getUserByPrincipal(principal)
                .getBucket();
        bucket.setProducts(new ArrayList<>());
        bucketRepository.save(bucket);

        return "redirect:/user/{id}/bucket";

    }
}
