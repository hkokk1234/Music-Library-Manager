/*
Stavros Moschis icsd22232
Zacharias Kokkinakis icsd22077
 */package com.example.client.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

// Anaparista ena event pou mporei na exei perissoteres apo mia parastaseis (shows)
public class Event implements Serializable {
    private static final long serialVersionUID = 1L;

    private String kwdikosEvent;
    private String titlos;
    private String typos; // paradeigma: "Theater", "Music", "Sports"
    private List<Show> listaParastasewn;

    public Event(String kwdikosEvent, String titlos, String typos) {
        this.kwdikosEvent = kwdikosEvent;
        this.titlos = titlos;
        this.typos = typos;
        this.listaParastasewn = new ArrayList<>();
    }

    // === Getters & Setters ===

    public String getKwdikosEvent() {
        return kwdikosEvent;
    }

    public void setKwdikosEvent(String kwdikosEvent) {
        this.kwdikosEvent = kwdikosEvent;
    }

    public String getTitlos() {
        return titlos;
    }

    public void setTitlos(String titlos) {
        this.titlos = titlos;
    }

    public String getTypos() {
        return typos;
    }

    public void setTypos(String typos) {
        this.typos = typos;
    }

    public List<Show> getListaParastasewn() {
        return Collections.unmodifiableList(listaParastasewn);
    }

    public void setListaParastasewn(List<Show> shows) {
        this.listaParastasewn = (shows != null) ? shows : new ArrayList<>();
    }

    public void prostheseParastasi(Show show) {
        listaParastasewn.add(show);
    }

    @Override
    public String toString() {
        return titlos + " [" + typos + "] (" + kwdikosEvent + ")";
    }
}
