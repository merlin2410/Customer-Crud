package com.merlin.customer_remote_server.repository;

import com.merlin.customer_remote_server.model.Admin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AdminRepository extends JpaRepository<Admin,Integer> {
    //To find admin by name
    Optional<Admin> findByUserName(String userName);
}
