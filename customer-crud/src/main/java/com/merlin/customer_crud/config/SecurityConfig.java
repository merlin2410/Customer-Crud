package com.merlin.customer_crud.config;

import com.merlin.customer_crud.filter.JwtAuthFilter;
import com.merlin.customer_crud.service.AdminDetailsServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {


//    @Autowired
//    private JwtAuthFilter jwtAuthFilter;
//    @Autowired
//    private AdminDetailsService adminDetailsService;

//    @Autowired
//    public SecurityConfig(JwtAuthFilter jwtAuthFilter,AdminDetailsService adminDetailsService){
//        this.adminDetailsService = adminDetailsService;
//        this.jwtAuthFilter = jwtAuthFilter;
//    }

//    @Bean
//    public UserDetailsService userDetailsService(){
//        return new AdminDetailsService();
//    }

    private final AdminDetailsServiceInterface adminDetailsService;

    //The constructor injects bean of AdminDetailsServiceInterface.
    //Lazy annotation is used to suggest that this bean should be lazily initialized
    //This was done to circumvent circular dependency
    @Autowired
    public SecurityConfig(@Lazy AdminDetailsServiceInterface adminDetailsService) {
        this.adminDetailsService = adminDetailsService;
    }

    //Configures the security filter chain
    //Determines how incoming requests are handled based on security rules.
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, JwtAuthFilter jwtAuthFilter) throws Exception{
        return http.csrf(csrf->csrf.disable())
                .authorizeHttpRequests(auth->
                        auth.requestMatchers("/admin/add","/admin/generateToken").permitAll())
                .authorizeHttpRequests(auth->
                        auth.requestMatchers("/customer/**").authenticated())
                .sessionManagement(sess->
                        sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider())
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    //Encoding password. This bean is used to encrypt password.
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    //Bean for authentication provider
    //Configures a DaoAuthenticationProvider which uses the AdminDetailServiceInterface
    //to load user details and the PasswordEncoder to verify password
    @Bean
    public AuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(adminDetailsService);
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        return authenticationProvider;
    }

    //Bean for authenticationManager obtained from AuthenticationConfiguration
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception{
        return config.getAuthenticationManager();
    }
}

