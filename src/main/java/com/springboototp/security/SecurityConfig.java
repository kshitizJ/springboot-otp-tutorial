package com.springboototp.security;

import com.springboototp.service.UsersService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
// import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.access.AccessDeniedHandler;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    // @Autowired
    // private AccessDeniedHandler accessDeniedHandler;

    @Autowired
    private UsersService userService;

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.csrf().disable(); // disable security

        http.authorizeRequests()
                .antMatchers("/", "/aboutUs").permitAll() // dashboard and about us page will be available to everyone
                .antMatchers("/admin/**").hasAnyRole("ADMIN") // only admin users can login
                .antMatchers("/user/**").hasAnyRole("USER") // only normal users can login
                .anyRequest().authenticated() // rest of all request needs authentication
                .and()
                .formLogin()
                .loginPage("/login").defaultSuccessUrl("/dashboard").failureUrl("/login?error").permitAll()
                .and()
                .logout().permitAll()
                .and()
                // If logged in users try to access other URLs, for which he or she is not
                // allowed then Access Denied will occur.
                .exceptionHandling().accessDeniedHandler(accessDeniedHandler());
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        auth.userDetailsService(userService).passwordEncoder(passwordEncoder);
    }

    /**
     * 
     * Defining our own custom class to Handle the Access Denied scenario
     * 
     * @return: CustomAccessDeniedHandler instance.
     */
    @Bean
    public AccessDeniedHandler accessDeniedHandler() {
        return new CustomAccessDeniedHandler();
    }

}
