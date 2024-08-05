package com.merlin.customer_remote_server.service;

import com.merlin.customer_remote_server.model.Admin;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface AdminDetailsServiceInterface extends UserDetailsService {
    public String addAdmin(Admin admin);
}
