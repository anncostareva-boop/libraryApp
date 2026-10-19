package com.example.library.service.implementations;

import com.example.library.dao.interfaces.*;
import com.example.library.entity.*;
import com.example.library.enums.CopyStatus;
import com.example.library.enums.RequestStatus;
import com.example.library.service.interfaces.BookService;
import com.example.library.service.interfaces.RequestService;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

@Service
public class RequestServiceImpl implements RequestService {
    private final IRequestDAO reqDAO;
    private final ICopyDAO copyDAO;
    private final IFineDAO fineDAO;
    private final IIssuanceDAO issuanceDAO;
    private final INotificationDAO notificationDAO;
    private final IUserDAO userDAO;
    private final BookService bookService;

    public RequestServiceImpl(IRequestDAO reqDAO, ICopyDAO copyDAO,
                              IFineDAO fineDAO, IIssuanceDAO issuanceDAO,
                              INotificationDAO notificationDAO, IUserDAO userDAO,
                              BookService bookService) {
        this.reqDAO = reqDAO;
        this.copyDAO = copyDAO;
        this.fineDAO = fineDAO;
        this.issuanceDAO = issuanceDAO;
        this.notificationDAO = notificationDAO;
        this.userDAO = userDAO;
        this.bookService = bookService;
    }

    @Override
    public List<Request> getActiveRequests() { return reqDAO.showActiveRequests(); }

    @Override
    public List<Request> getUserActiveRequests(Long userID) {
        return reqDAO.showActiveUserRequests(userID);
    }

    @Override
    public void createRequest(Long userID, Long bookID) throws Exception {
        // перевірка на несплачені штрафи
        List<Fine> fines = fineDAO.showUserFines(userID);
        if(fines.stream().anyMatch(fine -> !fine.getPaid())) {
            throw new Exception("Не можна замовити книгу: у вас є несплачені штрафи!");
        }

        // перевірка на прострочені книги
        List<IssuedBookInfo> issuedBooks = issuanceDAO.showIssuedBookInfo(userID);
        for(IssuedBookInfo book : issuedBooks) {
            if (book.getOverdue()) {
                throw new Exception("Не можна замовити книгу: у вас є прострочені книги!");
            }
        }

        // перевірка на наявність копій
        BookCopy copy = copyDAO.findAvailableCopy(bookID);
        if(copy == null) {
            throw new Exception("Не можна замовити книгу: немає наявних примірників книги");
        }

        reqDAO.create(userID, bookID);

        // відправлення адміністраторам повідомлення
        List<User> admins = userDAO.findAdmins();

        for(User admin : admins) {
            Notification notification =
                    new Notification(null,
                            admin.getUserID(),
                            "Новий запит на видачу",
                            "Створено новий запит на видачу книги!",
                            Date.valueOf(LocalDate.now()));
            notificationDAO.createNotification(notification);
        }
    }

    @Override
    public void approveRequest(Long requestID) throws Exception {
        Request request = reqDAO.findByID(requestID);

        if(request.getStatus() != RequestStatus.PENDING) {
            throw new Exception(
                    "Цей запит уже був оброблений"
            );
        }
        BookCopy copy = copyDAO.findAvailableCopy(request.getBookID());

        if(copy == null) {
            throw new Exception("Неможливо видати книгу: немає доступних копій!");
        }

        List<Fine> fines = fineDAO.showUserFines(request.getUserID());
        if(fines.stream().anyMatch(fine -> !fine.getPaid())) {
            throw new Exception("Не можна прийняти запит: в користувача є несплачені штрафи!");
        }

        List<IssuedBookInfo> issuedBooks = issuanceDAO.showIssuedBookInfo(request.getUserID());
        for(IssuedBookInfo book : issuedBooks) {
            if (book.getOverdue()) {
                throw new Exception("Не можна прийняти запит: в користувача є прострочені книги!");
            }
        }



        copyDAO.changeStatus(copy.getCopyID(), CopyStatus.RESERVED);
        reqDAO.setReservedCopy(requestID, copy.getCopyID());
        reqDAO.changeStatus(requestID, RequestStatus.AWAITING_ISSUANCE);
        bookService.updateBookStatus(request.getBookID());

        // відправлення користувачу повідомлення
        Notification notification =
                new Notification(null,
                        request.getUserID(),
                        "Запит на видачу прийнято",
                        "Ваш запит на видачу книги прийнято! Ваша книга чекає на вас в бібліотеці",
                        Date.valueOf(LocalDate.now()));

        notificationDAO.createNotification(notification);
    }

    @Override
    public void rejectRequest(Long requestID) throws Exception {

        Request request = reqDAO.findByID(requestID);
        if(request.getStatus() != RequestStatus.PENDING) {
            throw new Exception("Неможливо відхилити вже схвалений запит");
        }

        reqDAO.changeStatus(requestID, RequestStatus.REJECTED);

        Notification notification = new Notification();

        notification.setReceiverID(request.getUserID());
        notification.setTitle("Запит на видачу відхилено");
        notification.setMessage("Ваш запит на видачу книги було відхилено. Спробуйте іншим разом!");
        notification.setCreatedAt(new java.sql.Date(System.currentTimeMillis()));
        notificationDAO.createNotification(notification);
    }
}
