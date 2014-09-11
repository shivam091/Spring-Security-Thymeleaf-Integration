package org.spring.thymeleaf.dao.impl;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spring.thymeleaf.dao.RoleDAO;
import org.spring.thymeleaf.model.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate4.HibernateTransactionManager;

public class RoleDAOImpl implements RoleDAO {
    private static final Logger logger = LoggerFactory.getLogger(RoleDAOImpl.class);

    @Autowired
    private HibernateTransactionManager transactionManager;

//    todo: necessary to handle database exceptions
//    todo: need to repair @Transactional

    @Override
    public Role getRole(String role) {
        Session session = null;
        try {
            session = transactionManager.getSessionFactory().openSession();
            return (Role) session.getNamedQuery("Role.findByRole").setString("role", role).uniqueResult();
        } catch (Exception ex) {
            logger.error(ex.getMessage());
            throw new RuntimeException(ex);
        } finally {
            if (session != null && session.isOpen()) session.close();
        }
    }

    @Override
    public void addRole(Role role) {
        Session session = null;
        Transaction tx = null;
        try {
            session = transactionManager.getSessionFactory().openSession();
            tx = session.beginTransaction();
            session.save(role);
            tx.commit();
        } catch (Exception ex) {
            if (tx != null) tx.rollback();
            logger.error(ex.getMessage());
            throw new RuntimeException(ex);
        }  finally {
            if (session != null && session.isOpen()) session.close();
        }
    }

    @Override
    public void removeRole(int id) {
        Session session = null;
        Transaction tx = null;
        try {
            session = transactionManager.getSessionFactory().openSession();
            tx = session.beginTransaction();
            Role role = (Role) session.get(Role.class, id);
            session.delete(role);
            tx.commit();
        } catch (Exception ex) {
            if (tx != null) tx.rollback();
            logger.error(ex.getMessage());
            throw new RuntimeException(ex);
        }  finally {
            if (session != null && session.isOpen()) session.close();
        }    }
}
