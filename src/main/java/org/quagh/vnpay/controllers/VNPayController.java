package org.quagh.vnpay.controllers;

import jakarta.servlet.http.HttpServletRequest;
import org.quagh.vnpay.services.VNPayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class VNPayController {
    @Autowired
    private VNPayService vnPayService;

    @PostMapping("/submitOrder")
    public String submidOrder(@RequestParam("amount") int orderTotal,
                              @RequestParam("orderInfo") String orderInfo,
                              HttpServletRequest request){
        String baseUrl = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort();
        return vnPayService.createOrder(orderTotal, orderInfo, baseUrl,request);
    }

    @GetMapping("/vnpay-payment")
    public ResponseEntity<?> GetMapping(HttpServletRequest request){
        int paymentStatus =vnPayService.orderReturn(request);

        String orderInfo = request.getParameter("vnp_OrderInfo");
        String paymentTime = request.getParameter("vnp_PayDate");
        String transactionId = request.getParameter("vnp_TransactionNo");
        String totalPrice = request.getParameter("vnp_Amount");

        Map<String, Object> response = new HashMap<>();
        response.put("paymentStatus", paymentStatus);
        response.put("orderInfo", orderInfo);
        response.put("paymentTime", paymentTime);
        response.put("transactionId", transactionId);
        response.put("totalPrice", totalPrice);


        return paymentStatus == 1 ? ResponseEntity.ok(response) : ResponseEntity.badRequest().body("orderfail");
    }
}
