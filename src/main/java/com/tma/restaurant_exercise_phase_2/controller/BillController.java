package com.tma.restaurant_exercise_phase_2.controller;

import com.tma.restaurant_exercise_phase_2.dtos.BillDTO;
import com.tma.restaurant_exercise_phase_2.dtos.BillDetailsDTO;
import com.tma.restaurant_exercise_phase_2.dtos.OrderItemDTO;
import com.tma.restaurant_exercise_phase_2.model.bill.Bill;
import com.tma.restaurant_exercise_phase_2.dtos.CollectionResponse;
import com.tma.restaurant_exercise_phase_2.service.BillService;
import com.tma.restaurant_exercise_phase_2.service.OrderItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.security.InvalidParameterException;
import java.time.LocalDateTime;

@RestController
@RequestMapping(path = "/bill")
public class BillController {
    private final BillService billService;
    private final OrderItemService orderItemService;

    @Autowired
    public BillController(BillService billService, OrderItemService orderItemService) {
        this.billService = billService;
        this.orderItemService = orderItemService;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public CollectionResponse<BillDTO> getAllBills(@RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "20") int perPage) {
        if (page < 1 || perPage <1) throw new InvalidParameterException("page AND perPage MUST BE LARGER THAN 0");

        return billService.getAllBills(page, perPage);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public String createBill() {
        Bill newBill = billService.save(new Bill(LocalDateTime.now()));
        return "Bill " + newBill.getId() + " created";
    }

    @GetMapping("/details/{id}")
    @ResponseStatus(HttpStatus.OK)
    public BillDetailsDTO getBillById(@PathVariable("id") int id) {
        return billService.getBillDetailsById(id);
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.OK)
    public String deleteBillById(@RequestParam int id) {
        return billService.deleteById(id);
    }

    @PostMapping(path = "/order-item")
    @ResponseStatus(HttpStatus.OK)
    public String addItemToBill(@RequestBody OrderItemDTO orderItemDTO) {
        return billService.addItemToBill(orderItemDTO);
    }

    @PutMapping(path = "/order-item")
    @ResponseStatus(HttpStatus.OK)
    public String updateItem(@RequestBody OrderItemDTO orderItemDTO) {
        return orderItemService.updateOrderItem(orderItemDTO);
    }

    @DeleteMapping(path = "/order-item")
    @ResponseStatus(HttpStatus.OK)
    public String deleteOrderItem(@RequestParam int id) {
        return orderItemService.deleteById(id);
    }
}
