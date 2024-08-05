package com.merlin.customer_crud.filter;

import com.merlin.customer_crud.controller.CustomerController;
import com.merlin.customer_crud.service.AdminDetailsServiceInterface;
import com.merlin.customer_crud.service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {

//    @Autowired
//    private JwtService jwtService;
//    @Autowired
//    private AdminDetailsService adminService;

//    private final JwtService jwtService;
//    private final AdminDetailsService adminService;

//    public JwtAuthFilter(JwtService jwtService, @Lazy AdminDetailsService adminService){
//        this.jwtService = jwtService;
//        this.adminService = adminService;
//    }

    private final JwtService jwtService;
    private AdminDetailsServiceInterface adminService;
    private static final Logger logger = LoggerFactory.getLogger(CustomerController.class);

    //Constructor based injection for JwtService
    //To circumvent circular dependency
    @Autowired
    public JwtAuthFilter(JwtService jwtService) {
        this.jwtService = jwtService;
    }


    //Setter based injection for AdminDetailsServiceInterface
    //To circumvent circular dependency
    @Autowired
    public void setAdminService(AdminDetailsServiceInterface adminService) {
        this.adminService = adminService;
    }

    //Handles extraction and validation of JWT tokens and setting up the security context for authenticated users
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        logger.debug("Inside doFilter Internal");
        //Get Authorization header from the request
        String authHeader = request.getHeader("Authorization");

        logger.debug("AuthHeader"+authHeader);
        String token = null;
        String userName = null;

        //Checking if authorization header is present and it starts with Bearer
        if(authHeader!=null && authHeader.startsWith("Bearer ")){
            //Token is extracted after removing Bearer
            token = authHeader.substring(7);

            //Username is extracted from Jwt token
            userName = jwtService.extractUsername(token);
        }
        logger.debug("Username"+userName);
        //Checks if username is present and if authentication not already present
        if(userName!=null && SecurityContextHolder.getContext().getAuthentication()==null){
            //load user details
            UserDetails adminDetails = adminService.loadUserByUsername(userName);

            //Validates the token against user details
            if(jwtService.validateToken(token,adminDetails)){
                //Create an authentication token with the user details and authority
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(adminDetails,null,adminDetails.getAuthorities());

                //Set additional details on authentication token
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                //Set the authentication in the security context
                SecurityContextHolder.getContext().setAuthentication(authToken);

            }

        }

        //Continue with the next filter in the chain
        filterChain.doFilter(request,response);
    }
}
