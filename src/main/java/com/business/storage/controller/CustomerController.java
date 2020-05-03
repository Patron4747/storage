package com.business.storage.controller;

import com.business.storage.model.Customer;
import com.business.storage.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author: mmustafin@context-it.ru
 * @created: 17.04.2020
 */
@RestController
@RequestMapping("/customer")
public class CustomerController {

    @Autowired
    private CustomerRepository repository;

    @PutMapping("/create")
    public ResponseEntity<Customer> create(@RequestBody Map<String, String> payload) {
        String firstName = payload.get("first-name");
        String lastName = payload.get("last-name");

        Customer newCustomer = repository.save(new Customer(firstName, lastName));
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(newCustomer);
    }

    @PutMapping("/bulkCreate")
    public ResponseEntity<?> bulkCreate(@RequestBody Map<String, Object> payload) {
        List<Customer> customersToSave = new ArrayList<>();
        List bulkList = (List) payload.get("bulk");
        bulkList.forEach(customerJson -> {
            Map customerMap = (Map) customerJson;
            String firstName = (String) customerMap.get("first-name");
            String lastName = (String) customerMap.get("last-name");
            customersToSave.add(new Customer(firstName, lastName));
        });
        List<Customer> savedCustomers = repository.saveAll(customersToSave);
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(savedCustomers);
    }

    @GetMapping("/getById")
    public ResponseEntity<?> getCustomerById(@RequestParam String id) {
        Assert.notNull(id, "Customer id must not be null");
        Long longId = Long.parseLong(id);
        Customer customer = repository.findById(longId).orElse(null);
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(customer);
    }

    @GetMapping("/getByFirstName")
    public ResponseEntity<?> getCustomerByFirstName(@RequestParam String firstName) {
        Assert.notNull(firstName, "Customer id must not be null");
        List<Customer> customers = repository.findByFirstName(firstName);
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(customers);
    }

    @GetMapping("/getAll")
    public ResponseEntity<?> getAllCustomers() {
        List<Customer> allCustomers = repository.findAll();
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(allCustomers);
    }

    @DeleteMapping("/deleteById")
    public ResponseEntity<?> deleteById(String id) {
        Long longId = Long.parseLong(id);
        repository.deleteById(longId);
        Map<String, String> response = new LinkedHashMap<>();
        response.put("status", "DELETED");
        response.put("id", longId.toString());
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }
}
