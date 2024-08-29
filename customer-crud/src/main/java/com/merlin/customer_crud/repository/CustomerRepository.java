package com.merlin.customer_crud.repository;

import com.merlin.customer_crud.model.Customer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer,Integer> {


    // Search Customer based on a keyword
    @Query("SELECT c FROM Customer c WHERE "+
            "LOWER(c.first_name) LIKE LOWER(CONCAT('%',:keyword,'%')) OR "+
            "LOWER(c.last_name) LIKE LOWER(CONCAT('%',:keyword,'%')) OR "+
            "LOWER(c.city) LIKE LOWER(CONCAT('%',:keyword,'%')) OR "+
            "LOWER(c.state) LIKE LOWER(CONCAT('%',:keyword,'%'))")
    Optional<Page<Customer>> searchCustomer(String keyword, Pageable pageable);

    //To find customer by uuid. Optional is used to make exception handling easy.
    Optional<Customer> findByUuid(String uuid);

    public void deleteByUuid(String uuid);
}

