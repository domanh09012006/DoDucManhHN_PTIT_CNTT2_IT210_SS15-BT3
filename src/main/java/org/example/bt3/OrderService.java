package org.example.bt3;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class OrderService {
    @Autowired
    private OrderRepository orderRepository;

    public Page<Order> getOrderHistory(Long userId, String status, int page, int size, String sortBy, String direction) {

        String validSortBy = validateSortProperty(sortBy);
        Sort sort = direction.equalsIgnoreCase("desc") ? Sort.by(validSortBy).descending() : Sort.by(validSortBy).ascending();

        boolean isGetAll = (status == null || status.trim().isEmpty() || status.equalsIgnoreCase("ALL"));

        Pageable pageable = PageRequest.of(0, size, sort);

        Page<Order> initialPage = isGetAll
                ? orderRepository.findByUserId(userId, pageable)
                : orderRepository.findByUserIdAndStatus(userId, status, pageable);

        int totalPages = initialPage.getTotalPages();
        int validPage = (page < 0) ? 0 : (totalPages > 0 && page >= totalPages ? totalPages - 1 : page);

        pageable = PageRequest.of(validPage, size, sort);

        return isGetAll
                ? orderRepository.findByUserId(userId, pageable)
                : orderRepository.findByUserIdAndStatus(userId, status, pageable);
    }

    private String validateSortProperty(String sortBy) {
        List<String> validProperties = Arrays.asList("createdDate", "totalAmount", "id");

        if (validProperties.contains(sortBy)) {
            return sortBy;
        }

        return "createdDate";
    }
}
