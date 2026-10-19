package com.example.library.dao.interfaces;

import com.example.library.entity.User;
import com.example.library.exception.DataAccessException;
import com.example.library.exception.DataNotFoundException;

import java.util.List;

public interface IUserDAO {
    void addUser(User user) throws DataAccessException;

    List<User> findAdmins() throws DataNotFoundException;
    User findByID(Long userID) throws DataNotFoundException;
    User findByUsername(String username) throws DataNotFoundException;
    User findByEmail(String email) throws DataNotFoundException;
    List<User> findAll();

    void editProfile(User user) throws DataAccessException;
}
