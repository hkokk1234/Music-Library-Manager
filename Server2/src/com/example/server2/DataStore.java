/*
Stavros Moschis icsd22232
Zacharias Kokkinakis icsd22077
 */package com.example.server2;

import com.example.client.model.Event;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

// DataStore gia ton Server2 – diaxeirizetai MONO ta Events
public class DataStore {
    private static DataStore monadikoInstance = null;
    private List<Event> listaEvents;

    private static final String ARXEIO_EVENTS = "events.ser";

    private DataStore() {
        listaEvents = new ArrayList<>();
    }

    // Singleton getter
    public static synchronized DataStore pareInstance() {
        if (monadikoInstance == null) {
            monadikoInstance = new DataStore();
        }
        return monadikoInstance;
    }

    // Epistrofi listas events
    public synchronized List<Event> pareEvents() {
        return listaEvents;
    }

    // Prosthesi neou event
    public synchronized void prostheseEvent(Event event) {
        listaEvents.add(event);
    }

    // Apothikeuei ta panta (mono events edw)
    public synchronized void apothikeuseOla() {
        apothikeuseEvents();
    }

    // Apothikeusi events se arxeio
    public synchronized void apothikeuseEvents() {
        try (ObjectOutputStream eksodos = new ObjectOutputStream(new FileOutputStream(ARXEIO_EVENTS))) {
            eksodos.writeObject(listaEvents);
            System.out.println("✅ Events apothikeuthikan sto arxeio.");
        } catch (IOException e) {
            System.err.println("❌ Sfálma kata tin apothikeusi events:");
            e.printStackTrace();
        }
    }

    // Fortwnei events apo arxeio (an uparxei)
    public synchronized void fortwseEvents() {
        File arxeio = new File(ARXEIO_EVENTS);
        if (!arxeio.exists()) {
            listaEvents = new ArrayList<>();
            System.out.println("ℹ️ Den vrethike arxeio events. Ksekiname keno.");
            return;
        }

        try (ObjectInputStream eisodos = new ObjectInputStream(new FileInputStream(arxeio))) {
            listaEvents = (List<Event>) eisodos.readObject();
            System.out.println("✅ Events fortwthikan epituxws.");
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("❌ Sfálma kata ti fortwsi events:");
            e.printStackTrace();
            listaEvents = new ArrayList<>();
        }
    }
}
