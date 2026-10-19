package com.example.library.service.implementations;

import com.example.library.config.CustomUserDetails;
import com.example.library.dao.interfaces.IUserDAO;
import com.example.library.entity.User;
import com.example.library.exception.DataNotFoundException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    private final IUserDAO userDAO;

    public CustomUserDetailsService(IUserDAO userDAO) {
        this.userDAO = userDAO;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        try {
            User user =
                    userDAO
                            .findByUsername(username);

            if (user == null) {

                throw new
                        UsernameNotFoundException(
                        "User not found"
                );
            }
            return new CustomUserDetails(user);

        } catch (DataNotFoundException e) {
            throw new
                    UsernameNotFoundException(
                    "User not found"
            );
        }
    }
}
