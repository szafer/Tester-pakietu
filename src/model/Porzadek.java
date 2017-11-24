package model;

public class Porzadek {

    private Integer id;
    private String opis;

    public Porzadek(Integer id, String opis) {
        this.id = id;
        this.opis = opis;
    }

    public Integer getId() {
        return id;
    }

    public String getOpis() {
        return opis;
    }

    @Override
    public String toString() {
        return opis;
    }
}
