package application;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentLinkedQueue;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.PersistenceContext;

import javafx.concurrent.Task;
import javafx.scene.chart.XYChart.Data;
import model.Nagranie;

public class Serwis extends Task  {

    @PersistenceContext
    EntityManagerFactory emfactory = Persistence.createEntityManagerFactory("tester");
    EntityManager entitymanager = emfactory.createEntityManager();
    private String nazwaPakietu;
    private Long nagranie;
    private Long zaklad;
    private Date dataOd;
    private Date dataDo;
    private Integer uklad;
    private Long osoba;
    private Integer liczbaIteracji;
    private Integer funkcja;

    @SuppressWarnings("unchecked")
    public Serwis() {

    }

    public void ustaw(String nazwaPakietu, Long nagranie, Long zaklad, Date dataOd, Date dataDo, Integer uklad, Long osoba, Integer liczbaIteracji) {
        this.nazwaPakietu = nazwaPakietu;
        this.nagranie = nagranie;
        this.zaklad = zaklad;
        this.dataOd = dataOd;
        this.dataDo = dataDo;
        this.uklad = uklad;
        this.osoba = osoba;
        this.liczbaIteracji = liczbaIteracji;
    }

    public void funkcja(Integer funkcja) {
        this.funkcja = funkcja;
    }

    public void nagranie(Long nagranie) {
        this.nagranie = nagranie;
    }

    public void zaklad(Long zaklad) {
        this.zaklad = zaklad;
    }

    @SuppressWarnings("unchecked")
    public String testuj() {
        if (!entitymanager.getTransaction().isActive())
            entitymanager.getTransaction().begin();
        //TODO poczatek czasu
        for (int i = 0; i < liczbaIteracji; i++) {

            List<Object> wynik = entitymanager.createNativeQuery("SELECT * FROM TABLE("
                + nazwaPakietu
                + ".generuj_azz08(?, ?, -1 ,?, ?, ?, ?))")
                .setParameter(1, nagranie)
                .setParameter(2, zaklad)
                .setParameter(3, dataOd)
                .setParameter(4, dataDo)
                .setParameter(5, uklad)
                .setParameter(6, osoba)
                .getResultList();
            entitymanager.flush();
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    public List<Nagranie> pobierzNagrania() {
        if (!entitymanager.getTransaction().isActive())
            entitymanager.getTransaction().begin();
        List<Object[]> wynik = (List<Object[]>) entitymanager
            .createNativeQuery("select nagranie_Id, count(distinct osoba_Id) liczba "
                + " from pczk_v_dostepne_osoby_raportu where zaklad_Id = ? "
                + " group by nagranie_id order by 2 desc ")
            .setParameter(1, zaklad)
            .getResultList();
        List<Nagranie> lista = new ArrayList<>(wynik.size());
        for (Object[] obj : wynik) {
            lista.add(new Nagranie(((Number) obj[0]).longValue(), ((Number) obj[1]).longValue()));
        }
        return lista;
    }

    @SuppressWarnings("unchecked")
    public List<Long> pobierzIdentyfikatoryOsob() {
        List<Long> wynik = (List<Long>) entitymanager
            .createNativeQuery("select osoba_id "
                + " from pczk_v_dostepne_osoby_raportu where nagranie_id = ?  ")
            .setParameter(1, nagranie)
            .getResultList();
        return wynik;
    }

    @Override
    public Object call() throws Exception {
        switch (funkcja) {
        case 0:
            return pobierzNagrania();
        case 1:
            return pobierzIdentyfikatoryOsob();
        case 2:
            return testuj();
        }
        // System.out.println("Koniec ");
        return wykonaj();
    }

//    @Override
    public Boolean wykonaj() {
        Long it = 0L;

        try {
            pasekPostepu(0L);
            updateMessage("new message");
//            for (byte[] blok : dane) {
//                odbiorca.przetworz(szyfrujBlok(klucz.pobierz(), blok, szyfruj));
//                it++;
//                if (it % daneRozmiarProcent == 0){
//                    pasekPostepu(it);
//                }
//            }
        } finally {
            try {
//                odbiorca.close();
//                pasekPostepu(daneRozmiar);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        //  System.out.println("Koniec ");
        return true;
    }

    protected void pasekPostepu(long wykonanie) {
        try {
//            updateProgress(wykonanie, liczbaWykonan);
        } catch (Exception e) {

        }

    }

//    @Override
    public ConcurrentLinkedQueue<Data<Double, Double>> getData() {
        // TODO Auto-generated method stub
        return null;
    }
}
