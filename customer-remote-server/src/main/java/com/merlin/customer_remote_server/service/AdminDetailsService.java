package com.merlin.customer_remote_server.service;

import com.merlin.customer_remote_server.model.Admin;
import com.merlin.customer_remote_server.repository.AdminRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AdminDetailsService implements AdminDetailsServiceInterface {

//    @Autowired
//    private AdminRepository adminRepository;
//
//    @Autowired
//    private PasswordEncoder encoder;

//    private final AdminRepository adminRepository;
//    private final PasswordEncoder encoder;
//
//    @Autowired
//    public AdminDetailsService(AdminRepository adminRepository,PasswordEncoder encoder){
//        this.adminRepository = adminRepository;
//        this.encoder = encoder;
//    }


    private final AdminRepository adminRepository;
    private PasswordEncoder encoder;

    //Constructor injection is used to circumvent circular dependency
    @Autowired
    public AdminDetailsService(AdminRepository adminRepository) {
        this.adminRepository = adminRepository;
    }

    //Setter injection is used to circumvent circular dependency
    @Autowired
    public void setPasswordEncoder(PasswordEncoder encoder) {
        this.encoder = encoder;
    }


    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        Optional<Admin> admin = adminRepository.findByUserName(userName);
        // Converting admin to AdminDetails and returning
        //If no user found, ith throws an error
        return admin.map(AdminDetails::new).orElseThrow(()->
                new UsernameNotFoundException("User not found"+userName));
    }

    //Saves new admin to database. Password is encoded
    public String addAdmin(Admin admin){
        admin.setPassword(encoder.encode(admin.getPassword()));
        adminRepository.save(admin);

        return "Admin added Successfully";
    }
}

