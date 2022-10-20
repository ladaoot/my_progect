package web.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import web.models.*;
import web.models.role.OrderStatus;
import web.repository.OrderDetailsRepository;
import web.repository.OrderRepository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class OrderService {

    @Autowired
    private OrderDetailsRepository orderDetailsRepository;

    @Autowired
    private OrderRepository orderRepository;

    public Order createOrder(Bucket bucket, User user, String address) {
        Order order = new Order();
        order.setCreatedTime(LocalDateTime.now());
        order.setOrderStatus(OrderStatus.NEW);
        order.setAddress(address);
        order.setUser(user);
        order.setAddress(address);

        orderRepository.save(order);

        List<OrderDetails> orderDetails = new ArrayList<>();

        orderDetails = bucket.getProducts()
                .stream()
                .map(productCount -> {
                    OrderDetails orderDetail = new OrderDetails();
                    orderDetail.setOrder(order);
                    orderDetail.setProduct(productCount.getProduct());
                    orderDetail.setPrice(BigDecimal.valueOf(productCount.getProduct().getPrice()));
                    orderDetail.setAmount(productCount.getAmount());

                    orderDetailsRepository.save(orderDetail);
                    return orderDetail;
                })
                .collect(Collectors.toList());

        order.setOrderDetails(orderDetails);

        orderRepository.save(order);
        return order;
    }
}
























