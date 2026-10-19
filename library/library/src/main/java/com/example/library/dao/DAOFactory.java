package com.example.library.dao;

import com.example.library.config.DAOConfigurationProperties;
import com.example.library.dao.implementations.*;
import com.example.library.dao.interfaces.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DAOFactory {

    @Bean(name = "BookDAO")
    public IBookDAO getBookDAOInstance(ConnectionManager cm) {
        return new BookDAOImpl(cm);
    }

    @Bean(name = "CopyDAO")
    public ICopyDAO getCopyDAOInstance(ConnectionManager cm) {
        return new CopyDAOImpl(cm);
    }

    @Bean(name = "FineDAO")
    public IFineDAO getFineDAOInstance(ConnectionManager cm) {
        return new FineDAOImpl(cm);
    }

    @Bean(name = "IssuanceDAOImpl")
    public IIssuanceDAO getIssuanceDAOInstance(ConnectionManager cm) {
        return new IssuanceDAOImpl(cm);
    }

    @Bean(name = "NotificationDAO")
    public INotificationDAO getNotificationDAOInstance(ConnectionManager cm) {
        return new NotificationDAOImpl(cm);
    }

    @Bean(name = "RequestDAO")
    public IRequestDAO getRequestDAOInstance(ConnectionManager cm) {
        return new RequestDAOImpl(cm);
    }

    @Bean(name = "UserDAO")
    public IUserDAO getUserDAOInstance(ConnectionManager cm) {
        return new UserDAOImpl(cm);
    }

    @Bean
    public ConnectionManager getConnectionManager(DAOConfigurationProperties config) {
        return new ConnectionManagerImpl(config);
    }
}
