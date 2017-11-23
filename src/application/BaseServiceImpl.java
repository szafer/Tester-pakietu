package application;

import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.PersistenceContext;

import model.Nagranie;

public class BaseServiceImpl<ID, T> implements BaseService<ID, T> {

    @PersistenceContext
    EntityManagerFactory emfactory = Persistence.createEntityManagerFactory("tester");
    EntityManager entitymanager = emfactory.createEntityManager();
    private Class<T> klasaEncji;

    @SuppressWarnings("unchecked")
    public BaseServiceImpl() {
        // entitymanager.setFlushMode(FlushModeType.AUTO);
        try {
            this.klasaEncji = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass())
                .getActualTypeArguments()[1];
        } catch (Exception ex) {
        }
    }

//    public Class<T> getKlasaEncji() {
//        return klasaEncji;
//    }
//
//    public EntityManager getEntitymanager() {
//        return entitymanager;
//    }
//
//    @Override
//    public void delete(T entity) {
//        if (entity != null) {
//            if (!entitymanager.getTransaction().isActive())
//                entitymanager.getTransaction().begin();
//            entitymanager.remove(entitymanager.getReference(getKlasaEncji(), ((Persistent<ID>) entity).getId()));
//            entitymanager.flush();
//            entitymanager.getTransaction().commit();
//        }
//    }
//
//    @SuppressWarnings("unchecked")
//    @Override
//    public List<T> selectAll() {
//        if (!entitymanager.getTransaction().isActive())
//            entitymanager.getTransaction().begin();
//        entitymanager.clear();
//        List<T> lista = entitymanager
//            .createQuery("SELECT e FROM " + getKlasaEncji().getSimpleName() + " e order by e.id").getResultList();
//
//        for (T t : lista)
//            entitymanager.refresh(t);
//        entitymanager.getTransaction().commit();
//        return lista;
//
//    }
//
//    @Override
//    public void removeAll(List<T> entities) {
//        if (entities != null) {
//            for (T entity : entities) {
//                delete(entity);
//            }
//        }
//    }
//
//    @Override
//    public T saveOrUpdate(T entity) {
//        Persistent<ID> encja = (Persistent<ID>) entity;
//        if (czyNowa(encja)) {
//            entity = save(entity);
//        } else {
//            if (!entitymanager.getTransaction().isActive())
//                entitymanager.getTransaction().begin();
//            entitymanager.clear();
//            entity = entitymanager.merge(entity);
//            entitymanager.flush();
//            entitymanager.getTransaction().commit();
//        }
//        return entity;
//    }
//
//    @Override
//    public T save(T entity) {
//        if (!entitymanager.getTransaction().isActive())
//            entitymanager.getTransaction().begin();
//        getEntitymanager().clear();
//        getEntitymanager().persist(entity);
//        entitymanager.flush();
//        entitymanager.getTransaction().commit();
//        return entity;
//    }
//
//    @Override
//    public T update(T entity) {
//        return entitymanager.merge(entity);
//
//    }
//
//    public boolean czyNowa(Persistent<?> encja) {
//        return encja.getId() == null;
//    }
//
//    @Override
//    public T find(ID id) throws NieZnalezionoEncjiException {
//        // LOG.info("Wyszukiwanie encji: " + getKlasaEncji().getName()
//        // + " po id: " + id);
//        try {
//            return entitymanager.find(getKlasaEncji(), id);
//        } catch (EntityNotFoundException e) {
//            throw new NieZnalezionoEncjiException(e);
//        }
//    }

    @SuppressWarnings("unchecked")
    @Override
    public String testuj(String nazwaPakietu, Long nagranie, Long zaklad, Date dataOd, Date dataDo, Integer uklad, Long osoba, int liczbaIteracji) {
        if (!entitymanager.getTransaction().isActive())
            entitymanager.getTransaction().begin();
        //TODO poczatek czasu
        for (int i = 0; i < liczbaIteracji; i++) {

            List<Object> wynik = entitymanager.createNativeQuery("SELECT * FROM TABLE("
                + nazwaPakietu
                + ".generuj_azz08(:a_nagranie_id, :a_zaklad_id, -1 ,:a_data_od,  :a_data_do, :a_uklad_id, :a_osoba_id ))")
                .setParameter("a_nagranie_id", nagranie)
                .setParameter("a_zaklad_id", zaklad)
                .setParameter("a_data_od", dataOd)
                .setParameter("a_data_do", dataDo)
                .setParameter("a_uklad_id", uklad)
                .setParameter("a_osoba_id", osoba)
                .getResultList();
            entitymanager.flush();
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Nagranie> pobierzNagrania(Long zaklad) {
        if (!entitymanager.getTransaction().isActive())
            entitymanager.getTransaction().begin();
        List<Object[]> wynik = (List<Object[]>) entitymanager
            .createNativeQuery("select nagranie_Id, count(distinct osoba_Id) liczba "
                + " from pczk_v_dostepne_osoby_raportu where zaklad_Id = :zaklad "
                + " group by nagranie_id order by 2 desc ")
            .setParameter("zaklad", zaklad)
            .getResultList();
        List<Nagranie> lista = new ArrayList<>(wynik.size());
        for (Object[] obj : wynik) {
            lista.add(new Nagranie(((Number) obj[0]).longValue(), ((Number) obj[1]).longValue()));
        }
        return lista;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Long> pobierzIdentyfikatoryOsob(Long nagranie) {
        List<Long> wynik = (List<Long>) entitymanager
            .createNativeQuery("select osoba_id "
                + " from pczk_v_dostepne_osoby_raportu where nagranie_id = :nagranie  ")
            .setParameter("nagranie", nagranie)
            .getResultList();
        return wynik;
    }

}
