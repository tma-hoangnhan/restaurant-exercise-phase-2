package com.tma.restaurant_exercise_phase_2.service;

import com.tma.restaurant_exercise_phase_2.dtos.BillDetailsDTO;
import com.tma.restaurant_exercise_phase_2.dtos.OrderItemDTO;
import com.tma.restaurant_exercise_phase_2.exceptions.NoItemFoundException;
import com.tma.restaurant_exercise_phase_2.model.Item;
import com.tma.restaurant_exercise_phase_2.model.bill.Bill;
import com.tma.restaurant_exercise_phase_2.dtos.BillDTO;
import com.tma.restaurant_exercise_phase_2.model.bill.OrderItem;
import com.tma.restaurant_exercise_phase_2.dtos.CollectionResponse;
import com.tma.restaurant_exercise_phase_2.repository.BillRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

@Service
public class BillService {
    private final BillRepository billRepository;
    private final OrderItemService orderItemService;
    private final ItemService itemService;

    @Autowired
    public BillService(BillRepository billRepository, OrderItemService orderItemService, ItemService itemService) {
        this.billRepository = billRepository;
        this.orderItemService = orderItemService;
        this.itemService = itemService;
    }

    public CollectionResponse<BillDTO> getAllBills(int page, int perPage) {
        Page<Bill> billPage = billRepository.getAllBills(PageRequest.of(page - 1, perPage));

        CollectionResponse<BillDTO> billCollectionResponse = new CollectionResponse<>();
        billCollectionResponse.setPage(billPage.getNumber());
        billCollectionResponse.setPerPage(billPage.getSize());
        billCollectionResponse.setTotalPages(billPage.getTotalPages());
        billCollectionResponse.setTotalItems(billPage.getTotalElements());
        billCollectionResponse.setContents(billPage.stream().map(Bill::toDTO).collect(Collectors.toList()));

        return billCollectionResponse;
    }

    public Bill createNewBill() {
        return billRepository.save(new Bill(LocalDateTime.now()));
    }

    public Bill findById(int id) {
        return billRepository.findById(id).orElseThrow(() -> new NoItemFoundException("NO BILL FOUND WITH ID: " + id));
    }

    public BillDetailsDTO getBillDetailsById(int id) {
        Bill bill = findById(id);
        return bill.toBillDetailsDTO();
    }

    public String addItemToBill(OrderItemDTO orderItemDTO) {
        int itemId = orderItemDTO.getItem().getId();
        int quantity = orderItemDTO.getQuantity();

        Item item = itemService.findById(itemId);
        Bill bill = findById(orderItemDTO.getBillId());

        for (OrderItem oi : bill.getOrderItemList()) {
            if (oi.getItem().getId() == itemId) {
                oi.setQuantity(oi.getQuantity() + quantity);
                orderItemService.save(oi);
                return quantity + " " + item.getName() + " have been added into Bill " + bill.getId();
            }
        }

        OrderItem newOrderItem = new OrderItem();
        newOrderItem.setItem(item);
        newOrderItem.setBill(bill);
        newOrderItem.setQuantity(quantity);
        orderItemService.save(newOrderItem);

        return quantity + " " + item.getName() + " have been added into Bill " + bill.getId();
    }

    public String deleteById(int id) {
        findById(id);
        billRepository.deleteById(id);
        return "Bill with ID: " + id + " deleted";
    }

}
