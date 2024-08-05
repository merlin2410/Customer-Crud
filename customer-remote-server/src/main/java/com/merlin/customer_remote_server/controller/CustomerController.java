package com.merlin.customer_remote_server.controller;

import com.merlin.customer_remote_server.model.Customer;
import com.merlin.customer_remote_server.service.AdminDetailsService;
import com.merlin.customer_remote_server.service.CustomerService;
import com.merlin.customer_remote_server.service.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/customer")
@CrossOrigin(origins = "http://localhost:8080/")
public class CustomerController {

    @Autowired
    public CustomerService customerService;

    @Autowired
    JwtService jwtService;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    AdminDetailsService adminDetailsService;

    private static final Logger logger = LoggerFactory.getLogger(CustomerController.class);


    //Api to add new customer
    @PostMapping("/add")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity addCustomer(@RequestBody Customer customer){
        String message = customerService.addCustomer(customer);
        return new ResponseEntity<>(message, HttpStatus.ACCEPTED);
    }

    @PostMapping("/addBulk")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity addBulkCustomer(@RequestBody List<Customer> customerList){
        String message = customerService.addBulkCustomer(customerList);
        return new ResponseEntity<>(message, HttpStatus.ACCEPTED);
    }


    //Api to update existing customer
    @PutMapping("/update")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity updateCustomer(@RequestBody Customer customer){
        String message = customerService.updateCustomer(customer);
        return new ResponseEntity<>(message,HttpStatus.ACCEPTED);
    }


    //Api to get all customers without pagination or sorting
    @GetMapping("/all")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity getAll(){
        List<Customer> customerList = customerService.getAllCustomers();
        return new ResponseEntity<>(customerList,HttpStatus.OK);
    }


    //Api to delete customer. This takes in the customer id as path variable
    @DeleteMapping("/delete/{customerUuid}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity deleteById(@PathVariable("customerUuid") String customerUuid){
        String message = customerService.deleteCustomerById(customerUuid);
        return new ResponseEntity<>(message,HttpStatus.ACCEPTED);
    }
}
