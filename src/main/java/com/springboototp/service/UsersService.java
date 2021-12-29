package com.springboototp.service;

import java.util.Arrays;

import com.springboototp.model.User;
import com.springboototp.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UsersService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        // getting the user by username
        User user = userRepository.findByUsername(username);

        // setting the authority by user's role.
        GrantedAuthority authority = new SimpleGrantedAuthority(user.getRole());

        // creating a userDetails with the help of User class of Spring security which
        // adds the username, password and authority to the userDetail instance
        UserDetails userDetails = new org.springframework.security.core.userdetails.User(user.getUsername(),
                user.getPassword(), Arrays.asList(authority));

        return userDetails;
    }

}
