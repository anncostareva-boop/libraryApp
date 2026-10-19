package com.example.library.service.interfaces;

import com.example.library.entity.Fine;

import java.util.List;

public interface FineService {
    // знаходить штрафи користувача
    List<Fine> getUserFines(Long userID);

    // створює новий штраф
    void createFine(Fine fine);

    // змінює статус штрафу
    void payFine(Long fineID);

    // перевірка на несплачені штрафи
    Boolean hasUnpaidFines(Long userID);

    List<Fine> getAllFines();
}
