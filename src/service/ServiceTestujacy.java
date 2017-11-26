package service;

import java.util.List;

import model.Nagranie;

public interface ServiceTestujacy {

    String testuj();

    List<Nagranie> pobierzNagrania();

    List<Long> pobierzIdentyfikatoryOsob();
}
