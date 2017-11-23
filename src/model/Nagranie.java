package model;

public class Nagranie {

    private Long id = 0L;
    private Long liczba = 0L;

    public Nagranie(Long id, Long liczba) {
        this.id = id;
        this.liczba = liczba;
    }

    public Long getId() {
        return id;
    }

    public Long getLiczba() {
        return liczba;
    }

    @Override
    public String toString() {
        return id + " Liczba pracowników: " + liczba;
    }
}
