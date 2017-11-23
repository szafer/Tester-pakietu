package application;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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

public class Controller {
    @FXML
    private TextField txtZapiszFile, nbrLiczbaIteracji;
    @FXML
    private ComboBox<Long> comboZaklad, comboPrac;
    @FXML
    private ComboBox<String> comboPorzadek, comboPakiet;
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
    private Long czas;
    private int operacja = 1;
    private int szyfr = 1;
    private int sekwencjaSzyfrowania = 1;
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
    private String klucz = "src/main/resources/klucz.txt";
//    private DES taskDES;
//    private DesWspolbiezny taskDesWsp;
//    private DesRownolegly taskDesR;

    private final BaseService service = new BaseServiceImpl();

    public void initialize() {
        ObservableList<Long> zaklady = FXCollections.observableArrayList(1350L, 1941L, 1951L, 1952L, 1953L, 1954L, 1955L, 1956L, 1957L, 
            1958L, 1959L, 1960L, 1961L, 1971L, 1972L, 1973L, 1974L,
            1981L, 1982L, 1983L, 1984L);
        comboZaklad.setItems(zaklady);
        ObservableList<String> pakiety = FXCollections.observableArrayList("PPLC_AZZ08_TEST", "PPLC_AZZ08_TEST2", "PPLC_AZZ08_TEST3", "PPLC_AZZ08_TEST4");
        comboPakiet.setItems(pakiety);
        //        rbDES.setToggleGroup(tgCodingType);
//        rbAES.setToggleGroup(tgCodingType);
//        rbTwoFish.setToggleGroup(tgCodingType);
//        rbAES.setDisable(true);
//        rbTwoFish.setDisable(true);
//        rbDES.setSelected(true);
//        tgCodingType.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
//
//            public void changed(ObservableValue<? extends Toggle> observable, Toggle oldValue, Toggle newValue) {
//                if (tgCodingType.getSelectedToggle() != null) {
//                    if (rbDES.equals(tgCodingType.getSelectedToggle())) {
//                        szyfr = 1;
//                    } else if (rbAES.equals(tgCodingType.getSelectedToggle())) {
//                        szyfr = 2;
//                    } else if (rbTwoFish.equals(tgCodingType.getSelectedToggle())) {
//                        szyfr = 3;
//                    } else {
//                        szyfr = -1;
//                    }
//                    czyWlaczycWykonaj();
//                }
//            }
//        });
//
//        rbSekw.setToggleGroup(tgCodingSequence);
//        rbWspb.setToggleGroup(tgCodingSequence);
//        rbRow.setToggleGroup(tgCodingSequence);
//        rbSekw.setSelected(true);
//        tgCodingSequence.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
//
//            public void changed(ObservableValue<? extends Toggle> observable, Toggle oldValue, Toggle newValue) {
//                if (tgCodingSequence.getSelectedToggle() != null) {
//                    if (rbSekw.equals(tgCodingSequence.getSelectedToggle())) {
//                        sekwencjaSzyfrowania = 1;
//                    } else if (rbWspb.equals(tgCodingSequence.getSelectedToggle())) {
//                        sekwencjaSzyfrowania = 2;
//                    } else if (rbRow.equals(tgCodingSequence.getSelectedToggle())) {
//                        sekwencjaSzyfrowania = 3;
//                    } else {
//                        sekwencjaSzyfrowania = -1;
//                    }
//                    czyWlaczycWykonaj();
//                }
//            }
//        });
//
//        ObservableList<String> opcje = FXCollections.observableArrayList("Szyfrowanie", "Deszyfrowanie");
//        cbTryb.setItems(opcje);
//        cbTryb.valueProperty().addListener(new ChangeListener<String>() {
//
//            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
//                if (newValue == "Szyfrowanie") {
//                    operacja = 1;
//                } else {
//                    operacja = 2;
//                }
//                czyWlaczycWykonaj();
//            }
//        });
//        txtOdczytFile.setEditable(false);
//        txtOdczytSize.setEditable(false);
//        txtZapiszFile.setEditable(false);
//        txtZapiszSize.setEditable(false);
//
//        btnWykonaj.setDisable(true);
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

//        service.
//        if (txtKlucz.getText().isEmpty()) {
//
//        } else {
//            Task copyWorker = createWorker();
//            progresBar.progressProperty().unbind();
//            progresBar.progressProperty().bind(copyWorker.progressProperty());
//            copyWorker.messageProperty().addListener(new ChangeListener<String>() {
//
//                @Override
//                public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
//                    System.out.println(newValue);
//                }
//            });
//            new Thread(copyWorker).start();

//            switch (sekwencjaSzyfrowania) {
//            case 1:
//                wykonajSekwencyjne();
//                break;
//            case 2:
//                wykonajWspolbiezne();
//                break;
//            case 3:
//                wykonajRownolegle();
//                break;
//            }
//        }
    }

    public void zamknij() {
        System.out.println("KONIEC");
        System.exit(0);
    }

    private void wykonajRownolegle() throws FileNotFoundException, IOException {
        switch (operacja) {
        case 1:
            szyfrowanieRow();
            break;
        case 2:
            deszyfrowanieRow();
            break;
        case 0:
            break;
        }
    }

    private void wykonajSekwencyjne() throws FileNotFoundException, IOException {
        switch (operacja) {
        case 1:
            szyfrowanieSek();
            break;
        case 2:
            deszyfrowanieSek();
            break;
        }
    }

    private void wykonajWspolbiezne() throws FileNotFoundException, IOException {
        switch (operacja) {
        case 1:
            szyfrowanieWsp();
            break;
        case 2:
            deszyfrowanieWsp();
            break;
        case 0:

            break;
        }
    }

    private String pobierzKlucz() {
        return klucz;
//        return txtKlucz.getText();
    }

    private void szyfrowanieRow() throws FileNotFoundException, IOException {
//        System.gc();
//        disableButtons(true);
//        lblCzas.setText("");
//        poczatek = System.currentTimeMillis();
//        taskDesR = Szyfratory.desRownolegly();
//        taskDesR.dane(new DanePlikowe(plikWe))
//            .klucz(new KluczPlikowy(pobierzKlucz()))
//            .wyjscie(new Plik(plikWy))
//            .szyfruj(true);
//        progresBar.progressProperty().addListener(new ChangeListener<Number>() {
//
//            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
//                if (newValue.doubleValue() == 1) {
//                    lblProgres.setText("Zakończono");
//                    lblProgres.setTextFill(Color.GREEN);
//                } else {
//                    lblProgres.setTextFill(Color.BLUE);
//                    if (operacja == 1)
//                        lblProgres.setText("Szyfrowanie");
//                    else
//                        lblProgres.setText("Deszyfrowanie");
//                }
//            }
//        });
//        taskDesR.setOnSucceeded(new EventHandler<Event>() {
//
//            public void handle(Event event) {
//                czas = System.currentTimeMillis() - poczatek;
//                wpiszRozmiarZapisanegoPliku();
//                System.out.println("Czas: " + czas);
//                lblCzas.setText("Czas: " + czas + " ms");
//                disableButtons(false);
//            }
//        });
//        taskDesR.setOnFailed(new EventHandler<Event>() {
//
//            public void handle(Event event) {
//                disableButtons(false);
//                progresBar.progressProperty().unbind();
//                piProgres.progressProperty().unbind();
//                lblCzas.setText("");
//                lblProgres.setText("Wykonanie nie powiodło się");
//                lblProgres.setTextFill(Color.RED);
//            }
//
//        });
//
//        progresBar.progressProperty().unbind();
//        progresBar.progressProperty().bind(taskDesR.progressProperty());
//
//        piProgres.progressProperty().unbind();
//        piProgres.progressProperty().bind(taskDesR.progressProperty());
//        Thread t2 = new Thread(taskDesR);
//        t2.start();
    }

    private void szyfrowanieWsp() throws FileNotFoundException, IOException {
//        System.gc();
//        disableButtons(true);
//        lblCzas.setText("");
//        poczatek = System.currentTimeMillis();
//        taskDesWsp = Szyfratory.desWspolbiezny();
//        taskDesWsp.dane(new DanePlikowe(plikWe))
//            .klucz(new KluczPlikowy(pobierzKlucz()))
//            .wyjscie(new Plik(plikWy))
//            .szyfruj(true);
//        progresBar.progressProperty().addListener(new ChangeListener<Number>() {
//
//            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
//                if (newValue.doubleValue() == 1) {
//                    lblProgres.setText("Zakończono");
//                    lblProgres.setTextFill(Color.GREEN);
//                } else {
//                    lblProgres.setTextFill(Color.BLUE);
//                    if (operacja == 1)
//                        lblProgres.setText("Szyfrowanie");
//                    else
//                        lblProgres.setText("Deszyfrowanie");
//                }
//            }
//        });
//        taskDesWsp.setOnSucceeded(new EventHandler<Event>() {
//
//            public void handle(Event event) {
//                czas = System.currentTimeMillis() - poczatek;
//                wpiszRozmiarZapisanegoPliku();
//                System.out.println("Czas: " + czas);
//                lblCzas.setText("Czas: " + czas + " ms");
//                disableButtons(false);
//            }
//        });
//        taskDesWsp.setOnFailed(new EventHandler<Event>() {
//
//            public void handle(Event event) {
//                disableButtons(false);
//                progresBar.progressProperty().unbind();
//                piProgres.progressProperty().unbind();
//                lblCzas.setText("");
//                lblProgres.setText("Wykonanie nie powiodło się");
//                lblProgres.setTextFill(Color.RED);
//            }
//
//        });
//
//        progresBar.progressProperty().unbind();
//        progresBar.progressProperty().bind(taskDesWsp.progressProperty());
//
//        piProgres.progressProperty().unbind();
//        piProgres.progressProperty().bind(taskDesWsp.progressProperty());
//        Thread t2 = new Thread(taskDesWsp);
//        t2.start();
    }

    private void szyfrowanieSek() throws FileNotFoundException, IOException {
//        System.gc();
//        disableButtons(true);
//        lblCzas.setText("");
//        poczatek = System.currentTimeMillis();
//        taskDES = Szyfratory.des();
//        taskDES.dane(new DanePlikowe(plikWe))
//            .klucz(new KluczPlikowy(pobierzKlucz()))
//            .wyjscie(new Plik(plikWy))
//            .szyfruj(true);
//        progresBar.progressProperty().addListener(new ChangeListener<Number>() {
//
//            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
//                if (newValue.doubleValue() == 1) {
//                    lblProgres.setText("Zakończono");
//                    lblProgres.setTextFill(Color.GREEN);
//                } else {
//                    lblProgres.setTextFill(Color.BLUE);
//                    if (operacja == 1)
//                        lblProgres.setText("Szyfrowanie");
//                    else
//                        lblProgres.setText("Deszyfrowanie");
//                }
//            }
//        });
////        task = taskCreator(null);
//        taskDES.setOnSucceeded(new EventHandler<Event>() {
//
//            public void handle(Event event) {
//                czas = System.currentTimeMillis() - poczatek;
//                wpiszRozmiarZapisanegoPliku();
//                System.out.println("Czas: " + czas);
//                lblCzas.setText("Czas: " + czas + " ms");
//                disableButtons(false);
//            }
//        });
//        taskDES.setOnFailed(new EventHandler<Event>() {
//
//            public void handle(Event event) {
//                disableButtons(false);
//                progresBar.progressProperty().unbind();
//                piProgres.progressProperty().unbind();
//                lblCzas.setText("");
//                lblProgres.setText("Wykonanie nie powiodło się");
//                lblProgres.setTextFill(Color.RED);
//            }
//
//        });
//
//        progresBar.progressProperty().unbind();
//        progresBar.progressProperty().bind(taskDES.progressProperty());
//
//        piProgres.progressProperty().unbind();
//        piProgres.progressProperty().bind(taskDES.progressProperty());
//        Thread t2 = new Thread(taskDES);
//        t2.start();
    }

    private void disableButtons(Boolean disable) {
//        btnWykonaj.setDisable(disable);
//        btnZamknij.setDisable(disable);
//        btnOdczyt.setDisable(disable);
//        btnZapisz.setDisable(disable);
    }

    private void wpiszRozmiarZapisanegoPliku() {
//        File _plikWy = new File(plikWy);
//        if (_plikWy != null) {
//            txtZapiszSize.setText(((Long) _plikWy.length()).toString());
//        } else {
//            txtZapiszSize.clear();
//        }
    }

    private void deszyfrowanieWsp() throws FileNotFoundException, IOException {
//        poczatek = System.currentTimeMillis();
//        Szyfratory.desWspolbiezny()
//            .dane(new DanePlikowe(plikDWe))
//            .klucz(new KluczPlikowy(pobierzKlucz()))
//            .wyjscie(new Plik(plikDWy))
//            .deszyfruj();
//        wpiszRozmiarZapisanegoPliku();
//        czas = System.currentTimeMillis() - poczatek;
//        System.out.println("Czas realizacji " + czas);
    }

    private void deszyfrowanieSek() throws FileNotFoundException, IOException {
//
//        poczatek = System.currentTimeMillis();
//        Szyfratory.des()
//            .dane(new DanePlikowe(plikDWe))
//            .klucz(new KluczPlikowy(pobierzKlucz()))
//            .wyjscie(new Plik(plikDWy))
//            .deszyfruj();
//        wpiszRozmiarZapisanegoPliku();
//        czas = System.currentTimeMillis() - poczatek;
//        System.out.println("Czas realizacji " + czas);
    }

    private void deszyfrowanieRow() throws FileNotFoundException, IOException {
//        poczatek = System.currentTimeMillis();
//        Szyfratory.desRownolegly().dane(new DanePlikowe(plikDWe))
//            .klucz(new KluczPlikowy(pobierzKlucz()))
//            .wyjscie(new Plik(plikDWy))
//            .deszyfruj();
//        wpiszRozmiarZapisanegoPliku();
//        czas = System.currentTimeMillis() - poczatek;
//        System.out.println("Czas realizacji " + czas);
    }

    private void czyWlaczycWykonaj() {
//        btnWykonaj.setDisable(txtKlucz.getText().isEmpty() || txtOdczytFile.getText().isEmpty() || txtZapiszFile.getText().isEmpty());
    }

//    public Task createWorker() {
//        return new Task() {
//
//            @Override
//            protected Object call() throws Exception {
//                for (int i = 0; i < 10; i++) {
//                    Thread.sleep(2000);
//                    updateMessage("2000 mili");
//                    updateProgress(i + 1, 10);
//                }
//                return true;
//            }
//
//        };
//    }

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
//        List<Nagranie> nagrania = service.pobierzNagrania(zaklad); 
        ObservableList<Nagranie> nagrania2 = FXCollections.observableArrayList(service.pobierzNagrania(zaklad));
        comboNagranie.setItems(nagrania2);
        comboNagranie.setDisable(false);
    }

    @FXML
    public void wybranoNagranie() {
        Nagranie nagranie = comboNagranie.getValue();
        ObservableList<Long> osoby = FXCollections.observableArrayList(service.pobierzIdentyfikatoryOsob(nagranie.getId()));
        comboPrac.setItems(osoby);
        comboPrac.setDisable(false);
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
        Boolean value = cbWszystkie.isSelected();
        
    }
}
