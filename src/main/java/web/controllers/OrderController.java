package web.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import web.models.Bucket;
import web.models.Order;
import web.models.OrderDetails;
import web.models.User;
import web.models.role.OrderStatus;
import web.repository.BucketRepository;
import web.repository.OrderRepository;
import web.repository.UserRepository;
import web.service.OrderService;
import web.service.UserService;

import java.math.BigDecimal;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.BinaryOperator;
import java.util.stream.Collectors;

@Controller
public class OrderController {

    @Autowired
    UserService userService;

    @Autowired
    UserRepository userRepository;

    @Autowired
    BucketRepository bucketRepository;

    @Autowired
    OrderService orderService;

    @Autowired
    OrderRepository orderRepository;


    @GetMapping("/user/{id}/order/create")
    public String createOrder(Model model, Principal principal) {

        User user = new User();
        user = userService.getUserByPrincipal(principal);
        model.addAttribute("user", user);
        List<OrderDetails> orderDetailsList = new ArrayList<>();
        orderDetailsList = user.getBucket()
                .getProducts()
                .stream()
                .map(a -> {
                    OrderDetails orderDetails = new OrderDetails();
                    orderDetails.setProduct(a.getProduct());
                    orderDetails.setPrice(BigDecimal.valueOf(a.getProduct().getPrice()));
                    orderDetails.setAmount(a.getAmount());
                    return orderDetails;
                })
                .collect(Collectors.toList());
        model.addAttribute("list_order_details", orderDetailsList);

        var sum = orderDetailsList.stream()
                .map(a -> {
                    var amount = a.getAmount();
                    var price = a.getPrice();
                    return price.multiply(amount);
                })
                .reduce(BigDecimal::add)
                .get();
        model.addAttribute("sum", sum);


        return "order-create";

    }

    @PostMapping("/user/{id}/order/create")
    public String saveOrder(Model model, Principal principal,
                            @RequestParam("address") String address, RedirectAttributes redirectAttributes) {

        User user = userService.getUserByPrincipal(principal);

        var order = orderService.createOrder(user.getBucket(), user, address);
        model.addAttribute("order", order);

        user.getOrders().add(order);
        var bucket = user.getBucket();
        bucket.setProducts(new ArrayList<>());

        bucketRepository.save(bucket);
        userRepository.save(user);

        model.addAttribute("user",user);

        redirectAttributes.addAttribute("order_id",order.getId());

        return "redirect:/user/{id}/order/{order_id}/pay";
    }

    @GetMapping("/user/{id}/order/{order_id}/pay")
    public String returnAllForPay(Model model, Principal principal, @PathVariable("order_id") Long orderId){
        model.addAttribute("user",userService.getUserByPrincipal(principal));
        model.addAttribute("order",orderRepository.findById(orderId).get());
        return "order-pay";
    }

    @PostMapping("/user/{id}/order/{order_id}/pay")
    public String getPayed(Model model,@PathVariable("order_id") Long orderId,Principal principal){
        Optional<Order> optionalOrder =orderRepository.findById(orderId);

        if(optionalOrder.isEmpty()){
            model.addAttribute("error","Order is not found");
            return "redirect:/";
        }

        Order order =optionalOrder.get();
        order.setOrderStatus(OrderStatus.PAID);
        order.setUpdatedTime(LocalDateTime.now());
        orderRepository.save(order);
        model.addAttribute("user",userService.getUserByPrincipal(principal));
        model.addAttribute("order",order);
        return "redirect:/user/{id}";

    }


    @GetMapping("/user/{id}/order/{order_id}")
    public String orderInfo(Model model, Principal principal,
                            @PathVariable("order_id") Long orderId){
        model.addAttribute("user",userService.getUserByPrincipal(principal));
        model.addAttribute("order",orderRepository.findById(orderId).get());
        return "order-info";

    }

}
