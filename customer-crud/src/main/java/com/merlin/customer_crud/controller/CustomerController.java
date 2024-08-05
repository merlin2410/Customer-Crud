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


    //Api to get a list of customers with the given first name
    @GetMapping("/getByFirstName/{firstName}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity getCustomerByFirstName(@PathVariable("firstName") String firstName){
        List<Customer> customerList = customerService.getCustomerByFirstName(firstName);
        return new ResponseEntity<>(customerList,HttpStatus.ACCEPTED);
    }


    //Api to get list of customers based on give city
    @GetMapping("/getByCity/{city}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity getCustomerByCity(@PathVariable("city") String city){
        List<Customer> customerList = customerService.getCustomerByCity(city);
        return new ResponseEntity<>(customerList,HttpStatus.ACCEPTED);
    }

    //Api to get customer by email
    @GetMapping("/getByEmail/{email}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity getCustomerByEmail(@PathVariable("email") String email){
        List<Customer> customerList = customerService.getCustomerByCity(email);
        return new ResponseEntity<>(customerList,HttpStatus.ACCEPTED);
    }


    //Api to get customer by phone number
    @GetMapping("/getByPhone/{phone}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity getCustomerByPhone(@PathVariable("phone") String phone){
        List<Customer> customerList = customerService.getCustomerByPhone(phone);
        return new ResponseEntity<>(customerList,HttpStatus.ACCEPTED);
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

