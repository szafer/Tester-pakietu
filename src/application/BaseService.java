package application;

import java.util.Date;
import java.util.List;

import javafx.util.Callback;
import model.Nagranie;

public interface BaseService<ID, T> {

//	void delete(T user);
//
//	List<T> selectAll();
//
//	T saveOrUpdate(T entity);
//
//	T save(T entity);
//
//	T update(T entity);
//
//	T find(ID id) throws NieZnalezionoEncjiException;
//
//	void removeAll(List<T> entities);

    String testuj(String nazwaPakietu, Long nagranie, Long zaklad, Date dataOd, Date dataDo, Integer uklad, Long osoba, int liczbaIteracji);

    List<Nagranie> pobierzNagrania(Long zaklad);

    List<Long> pobierzIdentyfikatoryOsob(Long nagranie);
}