package application;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

import javafx.animation.AnimationTimer;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.concurrent.Worker.State;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import model.Metoda;
import model.Nagranie;
import model.Porzadek;
import service.ManagerPolaczenia;
import service.SerwisAzz08;

public class TesterController {
    @FXML
    private TextField nbrLiczbaIteracji;
    @FXML
    private ComboBox<Long> comboZaklad, comboPrac;
    @FXML
    private ComboBox<String> comboPakiet;
    @FXML
    private ComboBox<Porzadek> comboPorzadek;
    @FXML
    private ComboBox<Nagranie> comboNagranie;
    @FXML
    private Button btnZapisz, btnWykonaj, btnZamknij, btnStop;
    @FXML
    private AnchorPane mainPanel;
    @FXML
    private DatePicker dtDataOd, dtDataDo;
    @FXML
    private TextArea txtWynik;
    @FXML
    private CheckBox cbWszystkie;
    @FXML
    private Label lblProgres, lblCzas;
    @FXML
    private ProgressIndicator piProgres;
    @FXML
    private ProgressBar progresBar;
    @FXML
    private GridPane panelParametry;
    @FXML
    private BorderPane panelLog;
    @FXML
    private Pane panelPostep;
    private long poczatek;
    private Long czas;
    private ManagerPolaczenia manager = new ManagerPolaczenia();
    private String plikWy;

    private SerwisAzz08 service;
    private static final List<String> TESTOWANE_PAKIETY = Arrays.asList("PPLC_AZZ08_TEST", "PPLC_AZZ08_TEST2", "PPLC_AZZ08_TEST3", "PPLC_AZZ08_TEST4");
    private AnimationTimer czytaczLogu;
    private ConcurrentLinkedQueue<String> komunikaty = new ConcurrentLinkedQueue<String>();

    @SuppressWarnings("unchecked")
    public void initialize() {
        piProgres.setVisible(false);
        txtWynik.setWrapText(true);
        txtWynik.setEditable(false);

        ObservableList<Long> zaklady = FXCollections.observableArrayList(1350L, 1941L, 1951L, 1952L, 1953L, 1954L, 1955L, 1956L, 1957L,
            1958L, 1959L, 1960L, 1961L, 1971L, 1972L, 1973L, 1974L,
            1981L, 1982L, 1983L, 1984L);
        comboZaklad.setItems(zaklady);
        ObservableList<String> pakiety = FXCollections.observableArrayList(TESTOWANE_PAKIETY);
        comboPakiet.setItems(pakiety);
        ObservableList<Porzadek> sort = FXCollections.observableArrayList(new Porzadek(1, "Numer pracownika"), new Porzadek(2, "Nazwisko i imi�"));
        comboPorzadek.setItems(sort);
        nbrLiczbaIteracji.textProperty().addListener(new ChangeListener<String>() {

            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                nbrLiczbaIteracji.setText(newValue.replaceAll("[^\\d]", ""));
                ustawBtnWykonaj();
            }
        });
        dtDataOd.valueProperty().addListener(new ChangeListener<LocalDate>() {
            @Override
            public void changed(ObservableValue<? extends LocalDate> observableValue,
                LocalDate oldValue, LocalDate newValue) {
                System.out.println(oldValue + " -> " + newValue);
                if (newValue != null && dtDataDo.getValue() != null) {
                    if (!newValue.isBefore(dtDataDo.getValue())) {
                        dtDataDo.setValue(null);
                    }
                }
            }
        });
        dtDataDo.valueProperty().addListener(new ChangeListener<LocalDate>() {
            @Override
            public void changed(ObservableValue<? extends LocalDate> observableValue,
                LocalDate oldValue, LocalDate newValue) {
                System.out.println(oldValue + " -> " + newValue);
                if (newValue != null && dtDataOd.getValue() != null) {
                    if (newValue.isBefore(dtDataOd.getValue())) {
                        dtDataOd.setValue(null);
                    }
                }
            }
        });
        comboNagranie.setDisable(true);
        comboPrac.setDisable(true);
        comboPorzadek.setDisable(true);
        comboPakiet.setDisable(true);
        btnStop.setDisable(true);
        btnWykonaj.setDisable(true);
        btnZapisz.setDisable(true);

    }

    @SuppressWarnings("unchecked")
    private void createTask() {
        service = new SerwisAzz08(manager, komunikaty);
        service.setOnSucceeded(new EventHandler<Event>() {

            public void handle(Event event) {
                if (service.getFunkcja() != null) {
                    switch (service.getFunkcja()) {
                    case POBIERZ_NAGRANIA:
                        ObservableList<Nagranie> nagrania = FXCollections.observableArrayList((List<Nagranie>) service.getValue());//returns 2 or raises an exception if the thread dies, so safer
                        comboNagranie.setItems(nagrania);
                        comboNagranie.setDisable(false);
                        piProgres.setVisible(false);
                        piProgres.progressProperty().unbind();
                        break;
                    case POBIERZ_PRACOWNIKOW:
                        ObservableList<Long> osoby = FXCollections.observableArrayList((List<Long>) service.getValue());
                        comboPrac.setItems(osoby);
                        comboPrac.setDisable(false);
                        comboPorzadek.setDisable(false);
                        piProgres.setVisible(false);
                        piProgres.progressProperty().unbind();
                        break;
                    case TESTUJ:
                        czytaczLogu.stop();
                        piProgres.setVisible(false);
                        piProgres.progressProperty().unbind();
                        progresBar.progressProperty().unbind();
                        break;
                    }
                }
//              czas = System.currentTimeMillis() - poczatek;
//              wpiszRozmiarZapisanegoPliku();
                System.out.println("Czas: " + czas);
//              lblCzas.setText("Czas: " + czas + " ms");
                disableButtons(false);
            }
        });
        service.setOnFailed(new EventHandler<Event>() {

            public void handle(Event event) {
                switch (service.getFunkcja()) {
                case POBIERZ_NAGRANIA:
                    comboNagranie.setItems(null);
                    comboNagranie.setDisable(true);
                    comboPrac.setItems(null);
                    comboPrac.setDisable(true);
                    comboPorzadek.setDisable(true);
                    piProgres.setVisible(false);
                    piProgres.progressProperty().unbind();
                    break;
                case POBIERZ_PRACOWNIKOW:
                    comboPrac.setItems(null);
                    comboPrac.setDisable(true);
                    comboPorzadek.setDisable(true);
                    piProgres.setVisible(false);
                    piProgres.progressProperty().unbind();
                    break;
                case TESTUJ:
//TODO 
                    czytaczLogu.stop();
                    piProgres.setVisible(false);
                    piProgres.progressProperty().unbind();
                    progresBar.progressProperty().unbind();
                    lblCzas.setText("");
                    lblProgres.setText("Wykonanie nie powiodło się");
                    lblProgres.setTextFill(Color.RED);
                    break;
                }
                disableButtons(false);
            }

        });
        service.setOnRunning(new EventHandler<Event>() {

            @Override
            public void handle(Event event) {
                btnStop.setDisable(false);
                btnWykonaj.setDisable(true);
            }
        });
//        service.messageProperty().addListener(new ChangeListener<String>() {
//
//            @Override
//            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
//                System.out.println(newValue);
//                txtWynik.appendText(newValue + System.getProperty("line.separator"));
//            }
//        });
        Task t = new Task<Void>() {

            @Override
            protected Void call() throws Exception {
                // TODO Auto-generated method stub
                return null;
            }
        };
        piProgres.progressProperty().bind(t.progressProperty());
        piProgres.setVisible(true);
        progresBar.progressProperty().addListener(new ChangeListener<Number>() {

            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                if (newValue.doubleValue() == 1) {
                    lblProgres.setText("Zakończono");
                    lblProgres.setTextFill(Color.GREEN);
                } else {
                    lblProgres.setTextFill(Color.BLUE);
                    lblProgres.setText("Testowanie");
                }
            }
        });
    }

    public void zapisz() {
        Stage stage = (Stage) mainPanel.getScene().getWindow();
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Wybierz plik");
        fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));
        File _plikWy = fileChooser.showSaveDialog(stage);

        if (_plikWy != null) {
            ObservableList<CharSequence> paragraph = txtWynik.getParagraphs();
            Iterator<CharSequence> iter = paragraph.iterator();
            try {
                BufferedWriter bf = new BufferedWriter(new FileWriter(_plikWy));
                while (iter.hasNext()) {
                    CharSequence seq = iter.next();
                    bf.append(seq);
                    bf.newLine();
                }
                bf.flush();
                bf.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            plikWy = _plikWy.getPath();
        } else {
        }
        ustawBtnWykonaj();
    }

    private void dodajDoLogu(String string) {
        SimpleDateFormat simpleDateHere = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss");
        txtWynik.setText(txtWynik.getText() + simpleDateHere.format(new Date()) + "  " + string + "\n");

    }

    public void wykonaj() throws IOException {
        Long zaklad = comboZaklad.getValue();
        Nagranie nagranie = comboNagranie.getValue();
        Long osoba = comboPrac.getValue();
        Porzadek porzadek = comboPorzadek.getValue();
        String pakiet = comboPakiet.getValue();
        String liczbaIteracji = nbrLiczbaIteracji.getText();
        LocalDate lddataOd = dtDataOd.getValue();
        LocalDate lddataDo = dtDataDo.getValue();
        if (zaklad != null && nagranie != null && porzadek != null && liczbaIteracji != null && (cbWszystkie.isSelected() || pakiet != null) && lddataOd != null
            && lddataDo != null) {
            Instant instant = Instant.from(lddataOd.atStartOfDay(ZoneId.systemDefault()));
            Date dataOd = Date.from(instant);
            instant = Instant.from(lddataOd.atStartOfDay(ZoneId.systemDefault()));
            Date dataDo = Date.from(instant);
            txtWynik.clear();
            komunikaty.clear();
            prepareTimeline();
            createTask();
            service.funkcja(Metoda.TESTUJ);
            service.ustaw(cbWszystkie.isSelected() ? TESTOWANE_PAKIETY : Arrays.asList(pakiet), nagranie.getId(), zaklad, dataOd, dataDo, porzadek.getId(), osoba,
                Integer.parseInt(liczbaIteracji));

            new Thread(service).start();
            progresBar.progressProperty().bind(service.progressProperty());
        } else {
            comboNagranie.setItems(null);
            comboNagranie.setDisable(true);
        }
    }

    public void zamknij() {
        System.out.println("KONIEC");
        System.exit(0);
    }

    private void disableButtons(Boolean disable) {
        btnZamknij.setDisable(disable);
        btnZapisz.setDisable(disable || txtWynik.getText().isEmpty());
        ustawBtnWykonaj();
        btnStop.setDisable(disable || !btnWykonaj.isDisabled()
            || (service != null && service.getState() != State.RUNNING));
    }

    private void ustawBtnWykonaj() {
        btnWykonaj.setDisable(dtDataOd.getValue() == null
            || dtDataDo.getValue() == null
            || comboZaklad.getValue() == null
            || comboNagranie.getValue() == null
            || comboPorzadek.getValue() == null
            || nbrLiczbaIteracji.getText() == null
            || (service != null && service.getState() == State.RUNNING));
    }

    public void stop() {
        if (service != null && service.isRunning() && service.cancel()) {
            btnWykonaj.setDisable(false);
            progresBar.progressProperty().unbind();
            piProgres.progressProperty().unbind();
            lblCzas.setText("");
            lblProgres.setText("Anulowano");
            lblProgres.setTextFill(Color.RED);
        } else {

        }
    }

    @FXML
    public void wybranoZaklad() {
        Long zaklad = comboZaklad.getValue();
        if (zaklad != null) {
            createTask();
            service.funkcja(Metoda.POBIERZ_NAGRANIA);
            service.zaklad(zaklad);
            new Thread(service).start();
        } else {
            comboNagranie.setItems(null);
            comboNagranie.setDisable(true);
        }
    }

    @FXML
    public void wybranoNagranie() {
        Nagranie nagranie = comboNagranie.getValue();
        if (nagranie != null) {
            createTask();
            service.funkcja(Metoda.POBIERZ_PRACOWNIKOW);
            service.nagranie(nagranie.getId());
            new Thread(service).start();
        } else {
            comboPrac.setItems(null);
            comboPrac.setDisable(true);
            comboPorzadek.setDisable(true);
        }
    }

    @FXML
    public void wybranoPracownika() {

    }

    @FXML
    public void wybranoPorzadek() {

    }

    @FXML
    public void wybranoPakiet() {

    }

    @FXML
    public void zmienionoPakiety() {
        comboPakiet.setDisable(cbWszystkie.isSelected());
    }

    @FXML
    public void wyczysc() {
        txtWynik.clear();

    }

    /**
     * Metoda do aktualizacji txtALog
     * FIXME Trochę ładniej wyglądałoby bindProperty ale takie wykonanie też działa
     */
    private void prepareTimeline() {
        czytaczLogu = new AnimationTimer() {
            @Override
            public void handle(long now) {
                for (int i = 0; i < 10; i++) {
                    if (komunikaty.isEmpty())
                        break;
                    dodajDoLogu(komunikaty.remove());
                }
            }
        };
        czytaczLogu.start();
    }
}
