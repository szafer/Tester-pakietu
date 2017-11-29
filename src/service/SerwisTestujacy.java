package service;

import java.util.List;

import model.Nagranie;

public interface SerwisTestujacy {

    String testuj();

    List<Nagranie> pobierzNagrania();

    List<Long> pobierzIdentyfikatoryOsob();
}
