package com.merlin.customer_remote_server.service;

import com.merlin.customer_remote_server.controller.CustomerController;
import com.merlin.customer_remote_server.model.Customer;
import com.merlin.customer_remote_server.repository.CustomerRepository;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class CustomerService {

    private static final Logger logger = LoggerFactory.getLogger(CustomerController.class);

    @Autowired
    public CustomerRepository customerRepository;


    public String addCustomer(Customer customer){
        String uuid = String.valueOf(UUID.randomUUID());
        customer.setUuid(uuid);
        customerRepository.save(customer);
        return "Customer added successfully";
    }

    public String addBulkCustomer(List<Customer> customerList){
        for(Customer customer: customerList){
            String uuid = String.valueOf(UUID.randomUUID());
            customer.setUuid(uuid);
            customerRepository.save(customer);
        }
        return "Customer List Added Successfully";
    }

    public String updateCustomer(Customer customer){
        Customer savedCustomer = customerRepository.findById(customer.getId()).get();
        savedCustomer.setFirst_name(customer.getFirst_name());
        savedCustomer.setLast_name(customer.getLast_name());
        savedCustomer.setStreet(customer.getStreet());
        savedCustomer.setAddress(customer.getAddress());
        savedCustomer.setCity(customer.getCity());
        savedCustomer.setState(customer.getState());
        savedCustomer.setEmail(customer.getEmail());
        savedCustomer.setPhone(customer.getPhone());

        customerRepository.save(savedCustomer);

        return "Customer updated Successfully";

    }

    public List<Customer> getAllCustomers(){
        return customerRepository.findAll();
    }

    public String deleteCustomerById(String uuid){
        logger.debug("Uuid is: "+uuid);
        customerRepository.deleteByUuid(uuid);
        return "Customer deleted Successfully";
    }

}
