package com.merlin.customer_crud.service;

import com.merlin.customer_crud.model.Admin;
import org.springframework.security.core.userdetails.UserDetailsService;

//An interface which extends UserDetailService
//This is implemented by AdminDetailsService
public interface AdminDetailsServiceInterface extends UserDetailsService {
    public String addAdmin(Admin admin);
}
