/*
Stavros Moschis icsd22232
Zacharias Kokkinakis icsd22077
 */package com.example.server2;

import com.example.client.model.Event;
import com.example.client.model.Show;

import java.util.List;

// Diaxeirizetai tin logiki epexergasias entolwn sto Server2 (Events & Shows)
public class LogicManager {

    private static final DataStore apothiki = DataStore.pareInstance();

    // === GET_EVENTS ===
    public static Response xeirismosLipsisTheamatwn() {
        synchronized (apothiki) {
            List<Event> listaTheamatwn = apothiki.pareEvents();
            return new Response(true, "Lipsi listas theamatwn", listaTheamatwn);
        }
    }

    // === ADD_EVENT ===
    public static Response xeirismosProsthesisTheamatos(Object[] parametroi) {
        if (parametroi == null || parametroi.length == 0 || !(parametroi[0] instanceof Event)) {
            return new Response(false, "Lathos dedomena gia prosthiki theamatos", null);
        }

        Event neoTheama = (Event) parametroi[0];

        synchronized (apothiki) {
            apothiki.prostheseEvent(neoTheama);
            apothiki.apothikeuseOla();
            return new Response(true, "To theama prostethike epituxws", null);
        }
    }

    // === DEACTIVATE_SHOW ===
    public static Response xeirismosApenergopoiisisParastasis(Object[] parametroi) {
        if (parametroi == null || parametroi.length == 0 || !(parametroi[0] instanceof String)) {
            return new Response(false, "Lathos dedomena gia apenergopoiisi", null);
        }

        String kwdikosParastasis = (String) parametroi[0];

        synchronized (apothiki) {
            for (Event theama : apothiki.pareEvents()) {
                for (Show parastasi : theama.getShows()) {
                    if (parastasi.getShowId().equals(kwdikosParastasis)) {
                        parastasi.setActive(false);
                        apothiki.apothikeuseOla();
                        return new Response(true, "H parastasi apenergopoiithike", null);
                    }
                }
            }
            return new Response(false, "Den vrethike parastasi me kwdiko: " + kwdikosParastasis, null);
        }
    }

    // === FALLBACK ===
    public static Response xeirismosAgnwstisEntolis(String entoli) {
        return new Response(false, "Agnwsti entoli: " + entoli, null);
    }
}
