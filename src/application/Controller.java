package application;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import javafx.animation.AnimationTimer;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import model.Nagranie;
import model.Porzadek;

public class Controller {
    @FXML
    private TextField txtZapiszFile, nbrLiczbaIteracji;
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
//    @FXML
//    private Label lblProgres, lblCzas;
//    @FXML
//    private ProgressIndicator piProgres;
//    @FXML
//    private ProgressBar progresBar;
    private Long czas;
    // G³ówny w¹tek
    private ExecutorService executor;
//    private Stage stage;
    private long poczatek;
//  private static String plikWe = "src/main/resources/plik.txt";
//private static String plikWy = "src/main/resources/plik.des";
//private static String plikDWe = "src/main/resources/plik.des";
//private static String plikDWy = "src/main/resources/plikD.txt";
//private static String plikWe = "src/main/resources/praca_inzynierska.docx";
//private static String plikWy = "src/main/resources/praca_inzynierska.des";
//private static String plikDWe = "src/main/resources/praca_inzynierska.des";
//private static String plikDWy = "src/main/resources/pd1.docx";

//
    private String plikWe = "src/main/resources/100mega";
    private String plikWy = "src/main/resources/100mega.des";
    private String plikDWe = "src/main/resources/100mega.des";
    private String plikDWy = "src/main/resources/100megaD";
//    private String klucz = "src/main/resources/klucz.txt";
//    private DES taskDES;
//    private DesWspolbiezny taskDesWsp;
//    private DesRownolegly taskDesR;
//Parametry raportu;

//    private final BaseServiceImpl service = new BaseServiceImpl();
    private final Serwis service = new Serwis();
    private static final List<String> TESTOWANE_PAKIETY = Arrays.asList("PPLC_AZZ08_TEST", "PPLC_AZZ08_TEST2", "PPLC_AZZ08_TEST3", "PPLC_AZZ08_TEST4");

    public void initialize() {
        txtWynik.setWrapText(true); // New line of the text exceeds the text area
        ObservableList<Long> zaklady = FXCollections.observableArrayList(1350L, 1941L, 1951L, 1952L, 1953L, 1954L, 1955L, 1956L, 1957L,
            1958L, 1959L, 1960L, 1961L, 1971L, 1972L, 1973L, 1974L,
            1981L, 1982L, 1983L, 1984L);
        comboZaklad.setItems(zaklady);
        ObservableList<String> pakiety = FXCollections.observableArrayList(TESTOWANE_PAKIETY);
        comboPakiet.setItems(pakiety);
        ObservableList<Porzadek> sort = FXCollections.observableArrayList(new Porzadek(1, "Numer pracownika"), new Porzadek(2, "Nazwisko i imiê"));
        comboPorzadek.setItems(sort);
        txtZapiszFile.setEditable(false);
        nbrLiczbaIteracji.textProperty().addListener(new ChangeListener<String>() {

            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
//                if (!newValue.matches("\\d*")) {
                nbrLiczbaIteracji.setText(newValue.replaceAll("[^\\d]", ""));
//                }
                czyWlaczycWykonaj();
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
//        btnWykonaj.setDisable(true);
        btnZapisz.setDisable(true);
    }

    public void wybranotryb() {
        czyWlaczycWykonaj();
    }

    public void odczytaj() {
//        Stage stage = (Stage) mainPanel.getScene().getWindow();
//        FileChooser fileChooser = new FileChooser();
//        fileChooser.setTitle("Wybierz plik");
//        fileChooser.setInitialDirectory(new File("src/main/resources"));
//        File _plikWe = fileChooser.showOpenDialog(stage);
//        if (_plikWe != null) {
//            plikWe = _plikWe.getAbsolutePath();
//            txtOdczytFile.setText(_plikWe.getAbsolutePath());
//            txtOdczytSize.setText(((Long) _plikWe.length()).toString());
//        } else {
//            txtOdczytFile.clear();
//            txtOdczytSize.clear();
//        }
//        czyWlaczycWykonaj();
    }

    public void zapisz() {
        Stage stage = (Stage) mainPanel.getScene().getWindow();
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Wybierz plik");
        fileChooser.setInitialDirectory(new File("src/main/resources"));
        File _plikWy = fileChooser.showSaveDialog(stage);

        if (_plikWy != null) {
//        try {
            plikWy = _plikWy.getPath();
            txtZapiszFile.setText(_plikWy.getPath());
//            txtZapiszSize.setText(((Long) plikWy.length()).toString());
//        }
        } else {
            txtZapiszFile.clear();
        }
//        txtZapiszSize.clear();
        czyWlaczycWykonaj();
    }

    public void wykonaj() throws IOException {
        Task copyWorker = createWorker();
//        txtWynik.textProperty().unbind();
//        txtWynik.textProperty().bind(copyWorker.messageProperty());
//        txtWynik.textProperty().addListener(new ChangeListener<String>() {
//
//            @Override
//            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
//                txtWynik.appendText(newValue.replace(oldValue, ""));
//            }
//        });
//        progresBar.progressProperty().unbind();
//        progresBar.progressProperty().bind(copyWorker.progressProperty());
        copyWorker.messageProperty().addListener(new ChangeListener<String>() {

            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                System.out.println(newValue);
                txtWynik.appendText(newValue);
            }
        });
        new Thread(copyWorker).start();
        System.gc();
//        disableButtons(true);
//        lblCzas.setText("");
//        poczatek = System.currentTimeMillis();
//          taskDES = Szyfratory.des();
//          taskDES.dane(new DanePlikowe(plikWe))
//              .klucz(new KluczPlikowy(pobierzKlucz()))
//              .wyjscie(new Plik(plikWy))
//              .szyfruj(true);
//        progresBar.progressProperty().addListener(new ChangeListener<Number>() {
//
//            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
//                if (newValue.doubleValue() == 1) {
//                    lblProgres.setText("Zakoñczono");
//                    lblProgres.setTextFill(Color.GREEN);
//                } else {
//                    lblProgres.setTextFill(Color.BLUE);
//                    lblProgres.setText("Testowanie");
//                }
//            }
//        });
//          task = taskCreator(null);
//          taskDES.setOnSucceeded(new EventHandler<Event>() {
//  
//              public void handle(Event event) {
//                  czas = System.currentTimeMillis() - poczatek;
//                  wpiszRozmiarZapisanegoPliku();
//                  System.out.println("Czas: " + czas);
//                  lblCzas.setText("Czas: " + czas + " ms");
//                  disableButtons(false);
//              }
//          });
//          taskDES.setOnFailed(new EventHandler<Event>() {
//  
//              public void handle(Event event) {
//                  disableButtons(false);
//                  progresBar.progressProperty().unbind();
//                  piProgres.progressProperty().unbind();
//                  lblCzas.setText("");
//                  lblProgres.setText("Wykonanie nie powiodÅ‚o siÄ™");
//                  lblProgres.setTextFill(Color.RED);
//              }
//  
//          });

//          progresBar.progressProperty().unbind();
//          progresBar.progressProperty().bind(taskDES.progressProperty());
//  
//          piProgres.progressProperty().unbind();
//          piProgres.progressProperty().bind(taskDES.progressProperty());
//          Thread t2 = new Thread(taskDES);
//          t2.start();

//        executor = Executors.newCachedThreadPool(new ThreadFactory() {
//            @Override
//            public Thread newThread(Runnable r) {
//                Thread thread = new Thread(r);
//                thread.setDaemon(true);
//                return thread;
//            }
//        });
//        executor.execute(algorytm);
        // -- Prepare Timeline
//        prepareTimeline();
    }

    public void zamknij() {
        System.out.println("KONIEC");
        System.exit(0);
    }

    private void disableButtons(Boolean disable) {
        btnWykonaj.setDisable(disable);
        btnZamknij.setDisable(disable);
        btnZapisz.setDisable(disable);
    }

    private void wpiszRozmiarZapisanegoPliku() {
//        File _plikWy = new File(plikWy);
//        if (_plikWy != null) {
//            txtZapiszSize.setText(((Long) _plikWy.length()).toString());
//        } else {
//            txtZapiszSize.clear();
//        }
    }

    private void czyWlaczycWykonaj() {
        btnWykonaj.setDisable(dtDataOd.getValue() == null
            || dtDataDo.getValue() == null
            || comboZaklad.getValue() == null
            || comboNagranie.getValue() == null
            || comboPorzadek.getValue() == null
            || nbrLiczbaIteracji.getText() == null);
    }

    public Task createWorker() {
        return new Task() {

            @Override
            protected Object call() throws Exception {
//                String m = "";
                for (int i = 0; i < 10; i++) {
                    Thread.sleep(2000);
//                    m.concat("\n message" + i);
                    updateMessage("\n message" + i);
                    updateProgress(i + 1, 10);
                }
                return true;
            }

        };
    }

    public void stop() {
//        if (sekwencjaSzyfrowania == 1) {
//            if (taskDES != null && taskDES.isRunning() && taskDES.cancel()) {
//                btnWykonaj.setDisable(false);
//                progresBar.progressProperty().unbind();
//                piProgres.progressProperty().unbind();
//                lblCzas.setText("");
//                lblProgres.setText("Anulowano");
//                lblProgres.setTextFill(Color.RED);
//            }
//        } else if (sekwencjaSzyfrowania == 2) {
//            if (taskDesWsp != null && taskDesWsp.isRunning() && taskDesWsp.cancel()) {
//                btnWykonaj.setDisable(false);
//                progresBar.progressProperty().unbind();
//                piProgres.progressProperty().unbind();
//                lblCzas.setText("");
//                lblProgres.setText("Anulowano wsp");
//                lblProgres.setTextFill(Color.RED);
//            }
//        }
    }

    @FXML
    public void wybranoZaklad() {
        Long zaklad = comboZaklad.getValue();
        if (zaklad != null) {
            ExecutorService executor = Executors.newSingleThreadExecutor();
            service.funkcja(0);
            service.zaklad(zaklad);
            Thread t2 = new Thread(service);
            t2.start();
//            Future<Object> future = executor.submit(service);
            List<Nagranie> n = (List<Nagranie>) service.getValue();
            ObservableList<Nagranie> nagrania2 = FXCollections.observableArrayList((List<Nagranie>) service.getValue());//returns 2 or raises an exception if the thread dies, so safer
            executor.shutdown();
//            ObservableList<Nagranie> nagrania2 = FXCollections.observableArrayList(service.pobierzNagrania(zaklad));
            comboNagranie.setItems(nagrania2);
            comboNagranie.setDisable(false);
        } else {
            comboNagranie.setItems(null);
            comboNagranie.setDisable(true);
        }
    }

    @FXML
    public void wybranoNagranie() {
        Nagranie nagranie = comboNagranie.getValue();
        if (nagranie != null) {
//            ObservableList<Long> osoby = FXCollections.observableArrayList(service.pobierzIdentyfikatoryOsob(nagranie.getId()));
//            comboPrac.setItems(osoby);
            comboPrac.setDisable(false);
            comboPorzadek.setDisable(false);
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

    // -- Timeline gets called in the JavaFX Main thread
    private void prepareTimeline() {
        // Every frame to take any data from queue and add to chart
        new AnimationTimer() {
            @Override
            public void handle(long now) {
                addDataToSeries();
            }
        }.start();
    }

    private void addDataToSeries() {
        txtWynik.appendText("\nstring");//TODO tutaj text do pobrania
//        for (int i = 0; i < 20; i++) { // -- add 20 numbers to the plot+
//            if (algorytm.getData().isEmpty())
//                break;
//            series1.getData().add(algorytm.getData().remove());
//        }
        // // remove points to keep us at no more than MAX_DATA_POINTS
        // if (series1.getData().size() > MAX_DATA_POINTS) {
        // series1.getData().remove(0, series1.getData().size() -
        // MAX_DATA_POINTS);
        // }
        // // update
        // xAxis.setLowerBound(xSeriesData - MAX_DATA_POINTS);
        // xAxis.setUpperBound(xSeriesData - 1);
    }

}
