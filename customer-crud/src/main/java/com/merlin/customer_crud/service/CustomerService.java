package com.merlin.customer_crud.service;

import com.merlin.customer_crud.controller.CustomerController;
import com.merlin.customer_crud.model.Customer;
import com.merlin.customer_crud.repository.CustomerRepository;
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

    @Autowired
    private RemoteApiService remoteApiService;

    public String addCustomer(Customer customer){
        String uuid = String.valueOf(UUID.randomUUID());
        customer.setUuid(uuid);
        customerRepository.save(customer);
        return "Customer added successfully";
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

    public Customer getCustomerById(int id){
        return customerRepository.findById(id).get();
    }

    @Transactional
    public String deleteCustomerById(String uuid){
        String token = remoteApiService.authenticate();
        logger.debug("Uuid is: "+uuid);
        customerRepository.deleteByUuid(uuid);
        return remoteApiService.deleteCustomer(token,uuid);
    }

    public Page<Customer> getAllCustomersWithPaginationAndSort(int offset, int pageSize, String field){
        Page<Customer> customerList = customerRepository.findAll(PageRequest.of(offset, pageSize).withSort(Sort.by(field)));
        return customerList;
    }

    public List<Customer> getCustomerByFirstName(String firstName){
        return customerRepository.findByFirst_name(firstName);
    }

    //Get customer list by city
    public List<Customer> getCustomerByCity(String city){
        return customerRepository.findByCity(city);
    }

    //Get customer list by email
    public List<Customer> getCustomerByEmail(String email){
        return customerRepository.findByEmail(email);
    }

    //Get customer list by phone
    public List<Customer> getCustomerByPhone(String phone){
        return customerRepository.findByPhone(phone);
    }

    //Synchronising the customers obtained from remote server to database.
    public void syncCustomers(){
        String token = remoteApiService.authenticate();
        logger.debug("Token in sycnCustomer is: "+token);
        //Fetching the remote customers using the api
        List<Customer> remoteCustomers = remoteApiService.fetchCustomerList(token);

        //Looping throughout the customer list. If customer already exists, the customer is updated.
        //If not present, new customer is added
        for(Customer remoteCustomer: remoteCustomers){
            Optional<Customer> existingCustomerOpt = customerRepository.findByUuid(remoteCustomer.getUuid());
            if(existingCustomerOpt.isPresent()){
                Customer existingCustomer = existingCustomerOpt.get();

                existingCustomer.setFirst_name(remoteCustomer.getFirst_name());
                existingCustomer.setLast_name(remoteCustomer.getLast_name());
                existingCustomer.setStreet(remoteCustomer.getStreet());
                existingCustomer.setAddress(remoteCustomer.getAddress());
                existingCustomer.setCity(remoteCustomer.getCity());
                existingCustomer.setState(remoteCustomer.getState());
                existingCustomer.setEmail(remoteCustomer.getEmail());
                existingCustomer.setPhone(remoteCustomer.getPhone());
                customerRepository.save(existingCustomer);
            }
            else {
                customerRepository.save(remoteCustomer);
            }
        }
    }
}
