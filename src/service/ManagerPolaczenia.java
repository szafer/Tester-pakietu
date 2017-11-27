package service;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.PersistenceContext;

public class ManagerPolaczenia {

    @PersistenceContext
    EntityManagerFactory emfactory = Persistence.createEntityManagerFactory("tester");
    EntityManager entitymanager = emfactory.createEntityManager();

    public ManagerPolaczenia() {

    }

    public EntityManager getEntitymanager() {
        return entitymanager;
    }
}
