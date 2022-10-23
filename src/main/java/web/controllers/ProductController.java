package web.controllers;

import lombok.SneakyThrows;
import org.postgresql.util.PSQLException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import web.models.Category;
import web.models.Image;
import web.models.Product;
import web.models.User;
import web.repository.CategoryRepository;
import web.repository.ImageRepository;
import web.repository.ProductRepository;
import web.service.CategoryConverter;
import web.service.ImageService;
import web.service.ProductService;
import web.service.UserService;

import javax.transaction.Transactional;
import javax.validation.Valid;
import java.io.IOException;
import java.security.Principal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Controller
public class ProductController {

    private boolean isCategoryOnn=false;

    private List<Product> productList= new ArrayList<>();

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductService productService;

    @Autowired
    UserService userService;

    @Autowired
    private ImageService imageService;

    @Autowired
    private CategoryRepository categoryRepository;

    @GetMapping("/products")
    @Transactional
    public String product (Model model, Principal principal){
        model.addAttribute("title","Our Products");
        model.addAttribute("user",userService.getUserByPrincipal(principal));

        if(!isCategoryOnn) {
            ArrayList<Product> products = (ArrayList<Product>) productRepository.findAll();
            productList=products;

        }

        model.addAttribute("products", productList);
        List<Category> categoryList = (List<Category>) categoryRepository.findAll();

        model.addAttribute("categories",categoryList);
        model.addAttribute("listSelectedCategories", new ArrayList<String>());

        return "product";
    }

    @PostMapping("/products")
    public String productsWithCategories(Model model, User user){
       var categoryArrayList= user.getActualCategories();
        if(categoryArrayList.isEmpty()){
            isCategoryOnn=false;
            return "redirect:/products";
        }
        isCategoryOnn=true;
        List<Product> products = (ArrayList<Product>) productRepository.findAll();
        products = products.stream()
                .filter(product -> {
                    for ( Category category:categoryArrayList) {
                        if(!product.getCategories().contains(category)){
                            return false;
                        }
                    }
                    return true;
                })
                .collect(Collectors.toList());

        productList=products;
        model.addAttribute("products",products);
        return "redirect:/products";
    }

    @GetMapping("/products/add")
    public String addProduct(Model model,Principal principal){

        List<Category> categoryList = (List<Category>) categoryRepository.findAll();

        model.addAttribute("categoriesList",categoryList);

        model.addAttribute("product",new Product());

        model.addAttribute("user", userService.getUserByPrincipal(principal));
        return "product-add";
    }

    @PostMapping("/products/add")
    @Transactional
    public String addingProduct (Product product, @RequestParam("file")MultipartFile file) throws IOException {

        productService.saveProduct(product,file);

        return "redirect:/products";

    }

    @GetMapping("/products/description/{id}")
    public String getInfo(Model model, @PathVariable("id") Long id, Principal principal){

        model.addAttribute("user", userService.getUserByPrincipal(principal));
        model.addAttribute("el",productService.getProductById(id));
        model.addAttribute("images",productService.getProductById(id).getImages());
        return "product-info";
    }

    @PostMapping("/products/{id}/delete")
    public String deleteProduct(Model model, Principal principal,@PathVariable("id") Long id){

        model.addAttribute("user",userService.getUserByPrincipal(principal));
        productService.deleteProduct(id);
        return "redirect:/products";

    }

    @GetMapping("/products/{id}/update")
    public String updateProduct(Model model, Principal principal,@PathVariable("id") Long id){

        List<Category> categoryList = (List<Category>) categoryRepository.findAll();

        model.addAttribute("categoriesList",categoryList);
        model.addAttribute("user", userService.getUserByPrincipal(principal));
        model.addAttribute("product",productService.getProductById(id));
        return "product-update";
    }

    @PostMapping("/products/{id}/update")
    @Transactional
    public String updatingProduct (Product product, @RequestParam("file")MultipartFile file) throws IOException {

        Product product1 = productRepository.findById(product.getId()).get();
        product.setImages(product1.getImages());

        if(file.getBytes().length==0){
            productRepository.save(product);
        }else {
            Image image = new Image();
            image = productService.toImageEntity(file);

            product.addImageToProduct(image);
            productRepository.save(product);
        }

        return "redirect:/products";

    }

    @SneakyThrows
    @PostMapping("/products/{id}/delete_img/{img_id}")
    public String deleteImgInProduct(Model model,Principal principal,
                                     @PathVariable("id") Long productId,
                                     @PathVariable("img_id") Long imgId){
        Product product = productRepository.findById(productId).get();
        Image image = imageService.findImgById(imgId);
        product.getImages().remove(image);
        imageService.deleteImg(image);

        return "redirect:/products/{id}/update";


    }
}



















