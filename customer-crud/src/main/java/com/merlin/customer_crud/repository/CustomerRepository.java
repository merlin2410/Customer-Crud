package com.merlin.customer_crud.repository;

import com.merlin.customer_crud.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface CustomerRepository extends JpaRepository<Customer,Integer> {

    //To customer by first name
    @Query("SELECT c FROM Customer c WHERE c.first_name = :first_name")
    List<Customer> findByFirst_name(String first_name);

    //To get list of customers by city from database
    List<Customer> findByCity(String city);

    //To get customers by email. As of now, email is not set as unique.
    List<Customer> findByEmail(String email);

    //To get customers by phone. As of now, phone is not set a unique.
    List<Customer> findByPhone(String phone);

    //To find customer by uuid. Optional is used to make exception handling easy.
    Optional<Customer> findByUuid(String uuid);

    public void deleteByUuid(String uuid);
}

