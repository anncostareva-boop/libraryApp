package com.example.library.service.implementations;

import com.example.library.dao.interfaces.IFineDAO;
import com.example.library.dao.interfaces.INotificationDAO;
import com.example.library.entity.Fine;
import com.example.library.entity.Notification;
import com.example.library.service.interfaces.FineService;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

@Service
public class FineServiceImpl implements FineService {
    private final IFineDAO fineDAO;
    private final INotificationDAO notificationDAO;

    public FineServiceImpl(IFineDAO fineDAO, INotificationDAO notificationDAO){
        this.fineDAO = fineDAO;
        this.notificationDAO = notificationDAO;
    }

    @Override
    public List<Fine> getUserFines(Long userID) {
        return fineDAO.showUserFines(userID);
    }

    @Override
    public void createFine(Fine fine) {
        fineDAO.addFine(fine);
        Notification notification =
                new Notification(
                        null,
                        fine.getUserID(),
                        "Вам нараховано штраф",
                        "Вам нараховано штраф за пошкодження книги! Поки в вас є несплачені штрафи, " +
                                "ви не зможете вільно користуватись послугами нашої бібліотеки",
                        Date.valueOf(LocalDate.now())
                );
        notificationDAO.createNotification(notification);
    }

    @Override
    public void payFine(Long fineID) {
        fineDAO.payFine(fineID);
    }

    @Override
    public Boolean hasUnpaidFines(Long userID) {
        List<Fine> fines = fineDAO.showUserFines(userID);
        return fines.stream().anyMatch(fine -> !fine.getPaid());
    }

    @Override
    public List<Fine> getAllFines() {
        return fineDAO.findAll();
    }
}
