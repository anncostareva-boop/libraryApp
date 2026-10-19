package com.example.library.service.implementations;

import com.example.library.dao.interfaces.IUserDAO;
import com.example.library.entity.User;
import com.example.library.service.interfaces.UserService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    private final IUserDAO userDAO;

    public UserServiceImpl(IUserDAO userDAO) {
        this.userDAO = userDAO;
    }

    @Override
    public User getUserByID(Long userID) {
        return userDAO.findByID(userID);
    }

    @Override
    public User getByUsername(String username) {
        return userDAO.findByUsername(username);
    }

    @Override
    public void editProfile(User user) {
        userDAO.editProfile(user);
    }

    @Override
    public List<User> getAllUsers() {
        return userDAO.findAll();
    }

}
