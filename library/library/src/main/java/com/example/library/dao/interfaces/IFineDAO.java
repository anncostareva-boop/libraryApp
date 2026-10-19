package com.example.library.dao.interfaces;

import com.example.library.entity.Fine;
import com.example.library.exception.DataAccessException;
import com.example.library.exception.DataNotFoundException;
import org.springframework.context.annotation.Bean;

import java.math.BigDecimal;
import java.util.List;

public interface IFineDAO {
    List<Fine> showUserFines(Long userID) throws DataNotFoundException;

    void addFine(Fine fine) throws DataAccessException;

    void payFine(Long fineID) throws DataAccessException;

    List<Fine> findAll();
}
