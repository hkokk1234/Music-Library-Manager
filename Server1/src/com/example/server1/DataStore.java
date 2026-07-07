/*
Stavros Moschis icsd22232
Zacharias Kokkinakis icsd22077
 */package com.example.server1;

import com.example.client.model.*;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

// Klasi pou apothikeuei kai fortwnei ta dedomena twn xrhstwn, events kai bookings
public class DataStore {
    private static DataStore instance;

    private final List<User> listaXrhstwn;
    private final List<Event> listaEvents;
    private final List<Booking> listaKrathsewn;

    private final String arxeioXrhstwn = "users.ser";
    private final String arxeioEvents = "events.ser";
    private final String arxeioKrathsewn = "bookings.ser";

    // === Idiwtikos constructor ===
    private DataStore() {
        listaXrhstwn = fortwseLista(arxeioXrhstwn);
        listaEvents = fortwseLista(arxeioEvents);
        listaKrathsewn = fortwseLista(arxeioKrathsewn);
    }

    // === Singleton Access ===
    public static synchronized DataStore getInstance() {
        if (instance == null) {
            instance = new DataStore();
        }
        return instance;
    }

    // === Generic loader gia listas apo arxeio ===
    @SuppressWarnings("unchecked")
    private <T> List<T> fortwseLista(String onomaArxeiou) {
        try (ObjectInputStream eisodos = new ObjectInputStream(new FileInputStream(onomaArxeiou))) {
            return (List<T>) eisodos.readObject();
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

    // === Generic save gia opoiadhpote lista ===
    private <T> void apothikeuseLista(List<T> lista, String onomaArxeiou) {
        try (ObjectOutputStream eksodos = new ObjectOutputStream(new FileOutputStream(onomaArxeiou))) {
            eksodos.writeObject(lista);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // === Apothikeush neou event ===
    public boolean prostheseEvent(Event event) {
        if (event == null) return false;
        listaEvents.add(event);
        apothikeuseEvents();
        return true;
    }

    // === Accessors ===
    public List<User> getXrhstes() {
        return listaXrhstwn;
    }

    public List<Event> getEvents() {
        return listaEvents;
    }

    public List<Booking> getKrathseis() {
        return listaKrathsewn;
    }

    // === Apothikeuse ola ta dedomena ===
    public void apothikeuseOla() {
        apothikeuseLista(listaXrhstwn, arxeioXrhstwn);
        apothikeuseLista(listaEvents, arxeioEvents);
        apothikeuseLista(listaKrathsewn, arxeioKrathsewn);
    }

    // === Merikes apothikeuseis (an xreiazetai) ===
    public void apothikeuseEvents() {
        apothikeuseLista(listaEvents, arxeioEvents);
    }

    public void apothikeuseXrhstes() {
        apothikeuseLista(listaXrhstwn, arxeioXrhstwn);
    }

    public void apothikeuseKrathseis() {
        apothikeuseLista(listaKrathsewn, arxeioKrathsewn);
    }
}
