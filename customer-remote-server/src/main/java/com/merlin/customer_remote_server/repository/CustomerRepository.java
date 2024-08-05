package com.merlin.customer_remote_server.repository;

import com.merlin.customer_remote_server.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface CustomerRepository extends JpaRepository<Customer,Integer> {


    //To find customer by uuid. Optional is used to make exception handling easy.
    Optional<Customer> findByUuid(String uuid);

    public void deleteByUuid(String uuid);
}
