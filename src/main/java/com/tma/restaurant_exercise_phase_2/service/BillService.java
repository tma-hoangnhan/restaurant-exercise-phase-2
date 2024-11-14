package com.tma.restaurant_exercise_phase_2.service;

import com.tma.restaurant_exercise_phase_2.dtos.BillDetailsDTO;
import com.tma.restaurant_exercise_phase_2.dtos.OrderItemDTO;
import com.tma.restaurant_exercise_phase_2.exceptions.CannotAddItemToBillException;
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
import org.springframework.transaction.annotation.Transactional;

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

    /**
     * Get all Bills existing in the database
     * @param page page number
     * @param perPage number of items per one page
     * @return CollectionResponse containing Pagination properties and list of Bills
     */
    public CollectionResponse<BillDTO> getAllBills(int page, int perPage) {
        Page<Bill> billPage = billRepository.getAllBills(PageRequest.of(page - 1, perPage));

        return CollectionResponse
                .<BillDTO>builder()
                .page(billPage.getNumber() + 1)
                .perPage(billPage.getSize())
                .totalPages(billPage.getTotalPages())
                .totalItems(billPage.getTotalElements())
                .contents(billPage.stream().map(Bill::toDTO).toList())
                .build();
    }

    /**
     * Save the Bill entity
     * @param bill entity requested to save
     * @return Bill entity after saving to database
     */
    public Bill save(Bill bill) {
        return billRepository.save(bill);
    }

    /**
     * Find a Bill by its ID
     * @param id id of a Bill
     * @return Bill
     * @exception NoItemFoundException if no ID found
     */
    public Bill findById(int id) {
        return billRepository.findById(id).orElseThrow(() -> new NoItemFoundException("NO BILL FOUND WITH ID: " + id));
    }

    /**
     * Find a Bill by its ID and convert it into DTO model
     * @param id id of a Bill
     * @return DTO model of Bill
     */
    public BillDetailsDTO getBillDetailsById(int id) {
        Bill bill = findById(id);
        return bill.toBillDetailsDTO();
    }

    /**
     * Add an Item into Bill
     * @param orderItemDTO DTO model containing OrderItem properties
     * @return message of successfully adding Item into Bill
     * @exception CannotAddItemToBillException if Item is not available or the ordered quantity is exceeded the available one
     */
    @Transactional
    public String addItemToBill(OrderItemDTO orderItemDTO) {
        int itemId = orderItemDTO.getItem().getId();
        Item item = itemService.findById(itemId);
        if (!item.available())
            throw new CannotAddItemToBillException("ITEM WITH ID: " + itemId + " IS NOT AVAILABLE");

        // Check quantity condition
        int quantity = orderItemDTO.getQuantity();
        if (quantity > item.getQuantity())
            throw new CannotAddItemToBillException("QUANTITY OF ORDERED " + item.getName() + "(" + quantity + ") IS LARGER THAN THE AVAILABLE ONE(" + item.getQuantity() + ")");
        else if (quantity < 1)
            throw new CannotAddItemToBillException("QUANTITY OF ORDER ITEM MUST BE >= 1");

        Bill bill = findById(orderItemDTO.getBillId());
        // Look for the existing item in the OrderItem List then update the quantity instead of creating new Object
        for (OrderItem oi : bill.getOrderItemList()) {
            if (oi.getItem().getId() == itemId) {
                oi.setQuantity(oi.getQuantity() + quantity);
                orderItemService.save(oi);
                itemService.updateItemQuantity(item, quantity);
                return quantity + " " + item.getName() + " have been added into Bill " + bill.getId();
            }
        }

        OrderItem newOrderItem = new OrderItem();
        newOrderItem.setItem(item);
        newOrderItem.setBill(bill);
        newOrderItem.setQuantity(quantity);
        orderItemService.save(newOrderItem);

        itemService.updateItemQuantity(item, quantity);
        return quantity + " " + item.getName() + " have been added into Bill " + bill.getId();
    }

    /**
     * Delete a Bill from database
     * @param id ID of a Bill
     * @return message of successfully deleted
     */
    public String deleteById(int id) {
        findById(id);
        billRepository.deleteById(id);
        return "Bill with ID: " + id + " deleted";
    }

}
