package com.tricol.repository;

import com.tricol.entity.Fournisseur;
import org.hibernate.SessionFactory;
import org.hibernate.Session;
import java.util.List;
import java.util.Optional;

public class FournisseurRepository {
    
    private SessionFactory sessionFactory;
    
    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }
    
    public List<Fournisseur> findAll() {
        Session session = sessionFactory.openSession();
        try {
            return session.createQuery("FROM Fournisseur", Fournisseur.class).list();
        } finally {
            session.close();
        }
    }
    
    public Optional<Fournisseur> findById(Long id) {
        Session session = sessionFactory.openSession();
        try {
            Fournisseur fournisseur = session.get(Fournisseur.class, id);
            return Optional.ofNullable(fournisseur);
        } finally {
            session.close();
        }
    }
    
    public Fournisseur save(Fournisseur fournisseur) {
        Session session = sessionFactory.openSession();
        try {
            session.beginTransaction();
            session.saveOrUpdate(fournisseur);
            session.getTransaction().commit();
            return fournisseur;
        } catch (Exception e) {
            session.getTransaction().rollback();
            throw e;
        } finally {
            session.close();
        }
    }
    
    public void deleteById(Long id) {
        Session session = sessionFactory.openSession();
        try {
            session.beginTransaction();
            Fournisseur fournisseur = session.get(Fournisseur.class, id);
            if (fournisseur != null) {
                session.delete(fournisseur);
            }
            session.getTransaction().commit();
        } catch (Exception e) {
            session.getTransaction().rollback();
            throw e;
        } finally {
            session.close();
        }
    }
}