package com.merlin.customer_crud.controller;

import com.merlin.customer_crud.dto.AuthRequest;
import com.merlin.customer_crud.model.Admin;
import com.merlin.customer_crud.service.AdminDetailsService;
import com.merlin.customer_crud.service.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin")
@CrossOrigin(origins = "http://127.0.0.1:5500")
public class AdminController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtService jwtService;

    //This deals with apis related to admin.
    @Autowired
    public AdminDetailsService adminDetailsService;

    //Api to add new user and save to database.
    @PostMapping("/add")
    public ResponseEntity addUser(@RequestBody Admin admin){

        String message = adminDetailsService.addAdmin(admin);
        return new ResponseEntity(message, HttpStatus.ACCEPTED);
    }


    //Api to generate token, which is to be used by other apis
    @PostMapping("/generateToken")
    public String authenticateAndGetToken(@RequestBody AuthRequest authRequest){
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authRequest.getUserName(),authRequest.getPassword()));
        if(authentication.isAuthenticated()){
            return jwtService.generateToken(authRequest.getUserName());
        }
        else{
            throw new UsernameNotFoundException("Invalid user request");
        }
    }


}
