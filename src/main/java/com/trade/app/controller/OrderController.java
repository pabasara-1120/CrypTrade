package com.trade.app.controller;

import com.trade.app.entity.Coin;
import com.trade.app.entity.Order;
import com.trade.app.entity.User;
import com.trade.app.enums.OrderType;
import com.trade.app.request.OrderRequest;
import com.trade.app.service.CoinService;
import com.trade.app.service.OrderService;
import com.trade.app.service.UserService;
import org.aspectj.weaver.ast.Or;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private UserService userService;

    @Autowired
    private CoinService coinService;

    @PostMapping("/pay")
    public ResponseEntity<Order> orderPayment(@RequestHeader("Authorization") String jwt, @RequestBody OrderRequest orderRequest) throws Exception {
        User user = userService.findUserProfileByJwt(jwt);
        Coin coin = coinService.findById(orderRequest.getCoinId());

        Order order = orderService.processOrder(coin,orderRequest.getQuantity(),orderRequest.getOrderType(),user);
        return ResponseEntity.ok(order);

    }

    @GetMapping("/{orderId}")
    public ResponseEntity<Order> getOrderById(@RequestHeader("Authorization") String jwt,@PathVariable Long orderId) throws Exception {
        if(jwt==null){
            throw new Exception("Token missing");
        }
        User user = userService.findUserProfileByJwt(jwt);
        Order order = orderService.findOrderById(orderId);
        if(order.getUser().getId().equals(user.getId())){
            return ResponseEntity.ok(order);
        }else {
            throw new Exception("Access denied");
        }
    }

    @GetMapping()
    public ResponseEntity<List<Order>> getAllOrdersByUser(@RequestHeader("Authorization") String jwt,
                                                          @RequestParam(required = false) OrderType order_type,
                                                          @RequestParam(required = false) String asset_symbol) throws Exception {
        if(jwt==null){
            throw new Exception("Missing token");
        }
        Long userId = userService.findUserProfileByJwt(jwt).getId();
        List<Order> orderList = orderService.getALlOrdersByUser(userId,order_type,asset_symbol);
        return ResponseEntity.ok(orderList);
    }


}
