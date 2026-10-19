package com.example.library.service.implementations;

import com.example.library.dao.interfaces.*;
import com.example.library.entity.*;
import com.example.library.enums.CopyStatus;
import com.example.library.enums.FineReason;
import com.example.library.enums.RequestStatus;
import com.example.library.service.interfaces.BookService;
import com.example.library.service.interfaces.IssuanceService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.sql.Date;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
public class IssuanceServiceImpl implements IssuanceService {
    private final IIssuanceDAO issuanceDAO;
    private final IRequestDAO requestDAO;
    private final ICopyDAO copyDAO;
    private final IFineDAO fineDAO;
    private final INotificationDAO notificationDAO;
    private final BookService bookService;

    public IssuanceServiceImpl(IIssuanceDAO issuanceDAO, IRequestDAO requestDAO,
                               ICopyDAO copyDAO, IFineDAO fineDAO,
                               INotificationDAO notificationDAO,  BookService bookService) {
        this.issuanceDAO = issuanceDAO;
        this.requestDAO = requestDAO;
        this.copyDAO = copyDAO;
        this.fineDAO = fineDAO;
        this.notificationDAO = notificationDAO;
        this.bookService = bookService;
    }

    @Override
    public void issueBook(Long requestID) {

        Request request = requestDAO.findByID(requestID);

        if(request.getStatus() != RequestStatus.AWAITING_ISSUANCE) {
            throw new IllegalArgumentException(
                    "Неможливо видати книгу. Спочатку необхідно схвалити запит."
            );
        }

        Issuance existing =
                issuanceDAO.findIssuance(
                        request.getUserID(),
                        request.getReservedCopyID()
                );

        if(existing != null) {
            throw new IllegalArgumentException(
                    "Ця книга вже видана користувачу"
            );
        }

        LocalDate date = LocalDate.now();

        // книга видається на місяць
        LocalDate dueDate = date.plusMonths(1);

        Issuance issuance = new Issuance(
                null,
                request.getUserID(),
                request.getReservedCopyID(),
                Date.valueOf(date),
                Date.valueOf(dueDate),
                null
        );

        issuanceDAO.registerIssuance(issuance);

        copyDAO.changeStatus(
                request.getReservedCopyID(),
                CopyStatus.ISSUED
        );

        // знаходимо ID книги через примірник
        BookCopy copy =
                copyDAO.findByID(request.getReservedCopyID());

        Long bookID = copy.getBookID();

        // оновлюємо статус книги
        bookService.updateBookStatus(bookID);

        requestDAO.changeStatus(
                requestID,
                RequestStatus.ISSUED
        );
    }
    @Override
    public void returnBook(Long userID, Long copyID, boolean isDamaged) {

        Issuance issuance = issuanceDAO.findIssuance(userID, copyID);

        if(issuance == null) {
            throw new IllegalArgumentException(
                    "Цей користувач не позичав такої книги"
            );
        }

        // знаходимо примірник
        BookCopy copy =
                copyDAO.findByID(copyID);

        Long bookID = copy.getBookID();

        // реєстрація дати повернення
        Long issuanceID = issuance.getIssuanceID();

        issuanceDAO.registerReturning(issuanceID);

        if(isDamaged) {
            copyDAO.changeStatus(
                    issuance.getCopyID(),
                    CopyStatus.DAMAGED
            );

        } else {

            copyDAO.changeStatus(
                    issuance.getCopyID(),
                    CopyStatus.IN_STOCK
            );
        }

        // оновлюємо статус книги
        bookService.updateBookStatus(bookID);

        if(issuanceDAO.isOverdue(issuanceID)) {

            Fine fine = new Fine();

            LocalDate dueDate =
                    issuance.getDueDate().toLocalDate();

            LocalDate now =
                    LocalDate.now();

            // кількість прострочених днів
            long overdueDays =
                    ChronoUnit.DAYS.between(dueDate, now);

            fine.setUserID(issuance.getUserID());

            // 1 грн за день
            fine.setAmount(new BigDecimal(overdueDays));

            fine.setReason(FineReason.OVERDUE);

            fineDAO.addFine(fine);

            Notification notification =
                    new Notification(
                            null,
                            userID,
                            "Вам нараховано штраф",
                            "Вам нараховано штраф за прострочення книги! Поки в вас є несплачені штрафи, ви не зможете вільно користуватись послугами нашої бібліотеки",
                            Date.valueOf(LocalDate.now())
                    );

            notificationDAO.createNotification(notification);
        }
    }

    @Override
    public List<IssuedBookInfo> getUserIssuedBooks (Long userID) {
        return issuanceDAO.showIssuedBookInfo(userID);
    }

    @Override
    public Boolean hasOverdueBooks(Long userID) {
        List<IssuedBookInfo> issuedBooks = issuanceDAO.showIssuedBookInfo(userID);
        for(IssuedBookInfo book : issuedBooks) {
            if (book.getOverdue()) {
                return true;
            }
        }

        return false;
    }

    @Override
    public List<Issuance> getAllActiveIssuances() {

        return issuanceDAO.findAllActive();
    }
}
