package com.example.library.service.implementations;

import com.example.library.dao.interfaces.IBookDAO;
import com.example.library.dao.interfaces.ICopyDAO;
import com.example.library.entity.BookCopy;
import com.example.library.enums.CopyStatus;
import com.example.library.service.interfaces.CopyService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CopyServiceImpl implements CopyService {
    private final ICopyDAO copyDAO;

    public CopyServiceImpl(ICopyDAO copyDAO) {
        this.copyDAO = copyDAO;
    }

    @Override
    public List<BookCopy> showCopies(Long bookCopyID) {
        return copyDAO.showCopies(bookCopyID);
    }

}
