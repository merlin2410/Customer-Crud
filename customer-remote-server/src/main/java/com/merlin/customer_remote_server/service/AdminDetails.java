package com.merlin.customer_remote_server.service;

import com.merlin.customer_remote_server.model.Admin;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class AdminDetails implements UserDetails {

    private String userName;
    private String password;
    private List<GrantedAuthority> authorities;

    //Initialise admin AdminDetails using admin
    public AdminDetails(Admin admin){
        userName = admin.getUserName();
        password = admin.getPassword();

        //the roles field in Admin is in string format with different roles separated by comma
        //This is converted to a List of GrantedAuthority objects
        authorities = Arrays.stream(admin.getRoles().split(","))
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }

    //Different getters
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return userName;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    //boolean functions
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
