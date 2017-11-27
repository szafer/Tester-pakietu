package service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ConcurrentLinkedQueue;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.PersistenceContext;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.scene.chart.XYChart.Data;
import model.Metoda;
import model.Nagranie;

public class SerwisAzz08 extends Task implements SerwisTestujacy {

    private EntityManager entitymanager;
    private List<String> pakiety;
    private Long nagranie;
    private Long zaklad;
    private Date dataOd;
    private Date dataDo;
    private Integer uklad;
    private Long osoba;
    private Integer liczbaIteracji;
    private Metoda funkcja;
    private ConcurrentLinkedQueue<String> komunikaty = new ConcurrentLinkedQueue<String>();

    private long poczatek, poczatekPakietu, poczatekTestu;
    private long czas, czasPakietu, czasTestu;

    public SerwisAzz08(ManagerPolaczenia manager, ConcurrentLinkedQueue<String> komikaty) {
        this.entitymanager = manager.getEntitymanager();
        this.komunikaty = komikaty;
    }

    public void ustaw(List<String> pakiety, Long nagranie, Long zaklad, Date dataOd, Date dataDo, Integer uklad, Long osoba, Integer liczbaIteracji) {
        this.pakiety = pakiety;
        this.nagranie = nagranie;
        this.zaklad = zaklad;
        this.dataOd = dataOd;
        this.dataDo = dataDo;
        this.uklad = uklad;
        this.osoba = osoba;
        this.liczbaIteracji = liczbaIteracji;
    }

    public void funkcja(Metoda funkcja) {
        this.funkcja = funkcja;
    }

    public void nagranie(Long nagranie) {
        this.nagranie = nagranie;
    }

    public void zaklad(Long zaklad) {
        this.zaklad = zaklad;
    }

    @SuppressWarnings("unchecked")
    @Override
    public String testuj() {
        updateProgress(0, 100);
        Random r = new Random();
        poczatekTestu = System.currentTimeMillis();
        double wzrost = 100.0 / (liczbaIteracji * pakiety.size());
        double procent = 0.0;
        for (String pakiet : pakiety) {
            poczatekPakietu = System.currentTimeMillis();
            for (int i = 0; i < liczbaIteracji; i++) {
                procent += wzrost;
                poczatek = System.currentTimeMillis();
//Operacja do wykonania      
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
//                    Thread.currentThread().interrupt();
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                czas = System.currentTimeMillis() - poczatek;
//                System.out.println("Czas: " + czas + " ");
                komunikaty.add(pakiet + " ; iteracja " + (i + 1) + " ; " + czas / 1000.0 + "s");
                updateProgress(procent, 100);
                System.out.println(procent);
            }
            czasPakietu = System.currentTimeMillis() - poczatekPakietu;
//            System.out.println("Czas: " + czasPakietu + " ");
            komunikaty.add(pakiet + " ; " + czasPakietu / 1000.0 + "s");
//            System.out.println(procent);
        }
        czasTestu = System.currentTimeMillis() - poczatekTestu;
//        System.out.println("Czas: " + czasTestu);
        komunikaty.add("Czas testu ; " + czasTestu / 1000.0 + "s");
        updateProgress(100, 100);
        System.out.println(100);
        if (!entitymanager.getTransaction().isActive())
            entitymanager.getTransaction().begin();
        //TODO poczatek czasu
//        for (int i = 0; i < liczbaIteracji; i++) {
//
//            List<Object> wynik = entitymanager.createNativeQuery("SELECT * FROM TABLE("
//                + pakiety
//                + ".generuj_azz08(?, ?, -1 ,?, ?, ?, ?))")
//                .setParameter(1, nagranie)
//                .setParameter(2, zaklad)
//                .setParameter(3, dataOd)
//                .setParameter(4, dataDo)
//                .setParameter(5, uklad)
//                .setParameter(6, osoba)
//                .getResultList();
//            entitymanager.flush();
//        }
        return null;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Nagranie> pobierzNagrania() {

        //TODO odkomentować po testach
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
    @Override
    public List<Long> pobierzIdentyfikatoryOsob() {
        List<BigDecimal> wynik = (List<BigDecimal>) entitymanager
            .createNativeQuery("select osoba_id "
                + " from pczk_v_dostepne_osoby_raportu where nagranie_id = ?  ")
            .setParameter(1, nagranie)
            .getResultList();
        List<Long> wynik2 = new ArrayList<>(wynik.size());
        wynik.forEach(e -> wynik2.add(e.longValue()));
        return wynik2;
    }

    @Override
    public Object call() throws Exception {
        switch (funkcja) {
        case POBIERZ_NAGRANIA:
            return pobierzNagrania();
        case POBIERZ_PRACOWNIKOW:
            return pobierzIdentyfikatoryOsob();
        case TESTUJ:
            return testuj();
        }
        return null;
    }

    public Metoda getFunkcja() {
        return funkcja;
    }

}
