package com.alim.spring_demo.controller;

import com.alim.spring_demo.dto.OrderRequest;
import com.alim.spring_demo.dto.OrderResponse;
import com.alim.spring_demo.entity.OrderStatus;
import com.alim.spring_demo.mapper.OrderMapper;
import com.alim.spring_demo.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;
    private final OrderMapper orderMapper;

    @GetMapping
    public ResponseEntity<Page<OrderResponse>> getAllOrders(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        return ResponseEntity.ok(
            orderService.getAllOrdersPaged(pageable).map(orderMapper::toResponse)
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderResponse> getOrderById(@PathVariable Long id) {
        return ResponseEntity.ok(orderMapper.toResponse(
                orderService.getOrderById(id)));
    }

    @PostMapping
    public ResponseEntity<OrderResponse> createOrder(
            @RequestBody OrderRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(orderMapper.toResponse(
                        orderService.createOrder(
                                request.getCustomerId(),
                                request.getDeliveryAddress())));
    }

    @PatchMapping("/{id}/assign")
    public ResponseEntity<OrderResponse> assignDriver(
            @PathVariable Long id,
            @RequestParam Long driverId) {
        return ResponseEntity.ok(orderMapper.toResponse(
                orderService.assignDriver(id, driverId)));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<OrderResponse> updateStatus(
            @PathVariable Long id,
            @RequestBody Map<String, String> body) {
        OrderStatus status = OrderStatus.valueOf(body.get("status"));
        return ResponseEntity.ok(orderMapper.toResponse(
                orderService.updateStatus(id, status)));
    }

    @GetMapping("/customer/{customerId}")
    public ResponseEntity<List<OrderResponse>> getOrdersByCustomer(
            @PathVariable Long customerId) {
        return ResponseEntity.ok(orderService.getOrdersByCustomer(customerId)
                .stream().map(orderMapper::toResponse).toList());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrder(@PathVariable Long id) {
        orderService.deleteOrder(id);
        return ResponseEntity.noContent().build();
    }
}