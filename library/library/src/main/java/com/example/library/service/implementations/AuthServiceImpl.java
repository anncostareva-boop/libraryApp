package com.example.library.service.implementations;

import com.example.library.dao.interfaces.IUserDAO;
import com.example.library.entity.User;
import com.example.library.service.interfaces.AuthService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {
    private final IUserDAO userDAO;

    private final PasswordEncoder
            passwordEncoder;

    public AuthServiceImpl(IUserDAO userDAO, PasswordEncoder passwordEncoder) {
        this.userDAO = userDAO;
        this.passwordEncoder =
                passwordEncoder;
    }

    @Override
    public void register(User user)
            throws Exception {

        if(usernameExists(user.getUserName())) {
            throw new Exception(
                    "Таке ім'я вже існує"
            );
        }

        if(emailExists(user.getEmail())) {
            throw new Exception(
                    "На таку електронну адресу вже зареєстровано користувача"
            );
        }

        user.setPassword(
                passwordEncoder.encode(
                        user.getPassword()
                )
        );

        userDAO.addUser(user);
    }


//    @Override
//    public User authenticate(String username, String password) throws Exception {
//        User user = userDAO.findByUsername(username);
//
//        if(user == null) {
//            throw new Exception(
//                    "Користувача з таким іменем не знайдено"
//            );
//        }
//
//        if(!passwordEncoder.matches(password, user.getPassword())) {
//            throw new Exception("Неправильний пароль");
//        }
//
//        return user;
//    }

    @Override
    public boolean usernameExists(String username) {
        try {
            return userDAO.findByUsername(username) != null;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean emailExists(String email) {
        try {return userDAO.findByEmail(email) != null;
        } catch (Exception e) {
            return false;
        }
    }
}
