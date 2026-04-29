package org.example.bt3;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/orders")
public class OrderController {
    @Autowired
    private OrderService orderService;

    @GetMapping
    public String viewOrderHistory(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(required = false) String status,
            @RequestParam(defaultValue = "createdDate") String sortBy,
            @RequestParam(defaultValue = "desc") String direction,
            Model model) {

        Long currentUserId = 1L;

        Page<Order> orderPage = orderService.getOrderHistory(
                currentUserId, status, page, 10, sortBy, direction);

        model.addAttribute("orderPage", orderPage);
        model.addAttribute("currentPage", orderPage.getNumber());
        model.addAttribute("totalPages", orderPage.getTotalPages());
        model.addAttribute("statusFilter", status);

        return "order-history";
    }
}
