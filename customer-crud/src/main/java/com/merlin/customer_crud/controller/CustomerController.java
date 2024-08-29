package com.merlin.customer_crud.controller;

import com.merlin.customer_crud.model.Customer;
import com.merlin.customer_crud.service.AdminDetailsService;
import com.merlin.customer_crud.service.CustomerService;
import com.merlin.customer_crud.service.JwtService;
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

//This deals with APIs related to Customers
@RestController
@RequestMapping("/customer")
@CrossOrigin(origins = "http://127.0.0.1:5500")
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
        return new ResponseEntity<>(customerList,HttpStatus.ACCEPTED);
    }


    //Api to get customer using customer Id
    @GetMapping("/get/{customerId}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity getById(@PathVariable("customerId") int customerId){
        Customer customer = customerService.getCustomerById(customerId);
        return new ResponseEntity<>(customer,HttpStatus.ACCEPTED);
    }


    //Api to delete customer. This takes in the customer id as path variable
    @DeleteMapping("/delete/{customerUuid}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity deleteById(@PathVariable("customerUuid") String customerUuid){
        String message = customerService.deleteCustomerById(customerUuid);
        return new ResponseEntity<>(message,HttpStatus.ACCEPTED);
    }

    //Api to get all customers with pagination and sorting based on the inputs given.
    //Can set number of items per page, page number, sort based on various parameters
    @GetMapping("/allWithPagination/{offset}/{pageSize}/{field}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity getAllCustomerWithPaginationAndSort(@PathVariable("offset") int offset, @PathVariable("pageSize") int pageSize, @PathVariable("field") String field){

        Page<Customer> customerList = customerService.getAllCustomersWithPaginationAndSort(offset, pageSize,field);
        return new ResponseEntity<>(customerList,HttpStatus.ACCEPTED);
    }


    // Search api
    @GetMapping("/search/{offset}/{pageSize}/{keyword}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<Page<Customer>> searchCustomer(@PathVariable("offset") int offset,
                                                         @PathVariable("pageSize") int pageSize,
                                                         @PathVariable("keyword") String keyword){

        Page<Customer> customerList = customerService.searchCustomer(offset,pageSize,keyword);
        return new ResponseEntity<>(customerList,HttpStatus.OK);
    }


    //Api to synchronise customer database with that obtained from remote database
    @CrossOrigin(origins = "http://127.0.0.1:5500")
    @PostMapping("/sync")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity syncCustomers(){
        logger.debug("Entered syncCustomers method");
        customerService.syncCustomers();
        logger.debug("Completed Syncing");
        return new ResponseEntity<>("Customers Synchronised successfully",HttpStatus.OK);
    }

}

