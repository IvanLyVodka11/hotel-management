package com.hotel.service;

import com.hotel.model.customer.Customer;
import com.hotel.service.interfaces.IManageable;
import com.hotel.service.interfaces.ISearchable;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Lớp quản lý khách hàng
 * Handles CRUD operations for customers
 */
public class CustomerManager implements IManageable<Customer>, ISearchable<Customer> {
    private List<Customer> customers;

    public CustomerManager() {
        this.customers = new ArrayList<>();
    }

    @Override
    public boolean add(Customer customer) {
        if (customer != null && !exists(customer.getCustomerId())) {
            return customers.add(customer);
        }
        return false;
    }

    @Override
    public boolean update(Customer customer) {
        if (customer != null) {
            for (int i = 0; i < customers.size(); i++) {
                if (customers.get(i).getCustomerId().equals(customer.getCustomerId())) {
                    customers.set(i, customer);
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public boolean delete(String customerId) {
        return customers.removeIf(c -> c.getCustomerId().equals(customerId));
    }

    @Override
    public Customer getById(String customerId) {
        return customers.stream()
                .filter(c -> c.getCustomerId().equals(customerId))
                .findFirst()
                .orElse(null);
    }

    @Override
    public List<Customer> getAll() {
        return new ArrayList<>(customers);
    }

    @Override
    public List<Customer> search(String keyword) {
        String lowerKeyword = keyword.toLowerCase();
        return customers.stream()
                .filter(c -> c.getFullName().toLowerCase().contains(lowerKeyword) ||
                        c.getEmail().toLowerCase().contains(lowerKeyword) ||
                        c.getPhoneNumber().contains(keyword) ||
                        c.getCustomerId().contains(keyword))
                .collect(Collectors.toList());
    }

    @Override
    public List<Customer> filter(Map<String, Object> criteria) {
        List<Customer> result = new ArrayList<>(customers);
        
        if (criteria.containsKey("vip")) {
            boolean isVip = (boolean) criteria.get("vip");
            result = result.stream()
                    .filter(c -> c.isVIP() == isVip)
                    .collect(Collectors.toList());
        }
        
        if (criteria.containsKey("loyaltyPoints")) {
            double minPoints = (double) criteria.get("loyaltyPoints");
            result = result.stream()
                    .filter(c -> c.getLoyaltyPoints() >= minPoints)
                    .collect(Collectors.toList());
        }
        
        return result;
    }
    
    @Override
    public int count() {
        return customers.size();
    }
    
    @Override
    public boolean isEmpty() {
        return customers.isEmpty();
    }
    
    @Override
    public void clear() {
        customers.clear();
    }

    private boolean exists(String customerId) {
        return customers.stream().anyMatch(c -> c.getCustomerId().equals(customerId));
    }

    public int getTotalCustomers() {
        return customers.size();
    }

    public int getVIPCustomers() {
        return (int) customers.stream().filter(Customer::isVIP).count();
    }
}
