package org.spring.thymeleaf.dao.impl;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spring.thymeleaf.dao.UserDAO;
import org.spring.thymeleaf.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate4.HibernateTransactionManager;
import org.springframework.stereotype.Repository;

@Repository
public class UserDAOImpl implements UserDAO {

    private static final Logger logger = LoggerFactory.getLogger(UserDAOImpl.class);

    @Autowired
    private HibernateTransactionManager transactionManager;

//    todo: necessary to handle database exceptions
//    todo: need to repair @Transactional
    @Override
    public void addUser(User user) {
        Session session = null;
        Transaction tx = null;
        try {
            session = transactionManager.getSessionFactory().openSession();
            tx = session.beginTransaction();
            session.save(user);
            tx.commit();
        } catch (Exception ex) {
            if (tx != null) tx.rollback();
            logger.error(ex.getMessage());
            throw new RuntimeException(ex);
        } finally {
            if (session != null && session.isOpen()) session.close();
        }
    }

    @Override
    public void removeUser(int id) {
        Session session = null;
        Transaction tx = null;
        try {
            session = transactionManager.getSessionFactory().openSession();
            tx = session.beginTransaction();
            User user = (User) session.get(User.class, id);
            session.delete(user);
            tx.commit();
        } catch (Exception ex) {
            if (tx != null) tx.rollback();
            logger.error(ex.getMessage());
            throw new RuntimeException(ex);
        } finally {
            if (session != null && session.isOpen()) session.close();
        }
    }

    @Override
    public User getUser(int id) {
        Session session = null;
        User user = null;
        try {
            session = transactionManager.getSessionFactory().openSession();
            user = (User) session.get(User.class, id);
        } catch (Exception ex) {
            logger.error(ex.getMessage());
            throw new RuntimeException(ex);
        } finally {
            if (session != null && session.isOpen()) session.close();
        }
        return user;
    }

    @Override
    public User getUser(String email) {
        Session session = null;
        User user = null;
        try {
            session = transactionManager.getSessionFactory().openSession();
            user = (User) session.getNamedQuery("User.findByEmail").setString("email", email).uniqueResult();
        } catch (Exception ex) {
            logger.error(ex.getMessage());
            throw new RuntimeException(ex);
        } finally {
            if (session != null && session.isOpen()) session.close();
        }
        return user;
    }

    @Override
    public User getUserToActivate(String activationKey) {
        Session session = null;
        User user = null;
        try {
            session = transactionManager.getSessionFactory().openSession();
            user = (User) session.getNamedQuery("User.findByActivationKey").setString("activation_key", activationKey).uniqueResult();
        } catch (Exception ex) {
            logger.error(ex.getMessage());
            throw new RuntimeException(ex);
        } finally {
            if (session != null && session.isOpen()) session.close();
        }
        return user;
    }


    @Override
    public void changeUser(User user) {
        Session session = null;
        Transaction tx = null;
        try {
            session = transactionManager.getSessionFactory().openSession();
            tx = session.beginTransaction();
            session.merge(user);
            tx.commit();
        } catch (Exception ex) {
            if (tx != null) tx.rollback();
            logger.error(ex.getMessage());
            throw new RuntimeException(ex);
        } finally {
            if (session != null && session.isOpen()) session.close();
        }
    }
}
