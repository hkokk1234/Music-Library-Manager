/*
Stavros Moschis icsd22232
Zacharias Kokkinakis icsd22077
 */package com.example.client.model;

import java.io.Serializable;

// Anaparista mia krathsh eisitiriwn pou ekane o xrhsths gia sygkekrimeno show
public class Booking implements Serializable {
    private static final long serialVersionUID = 1L;

    private String kwdikosKrathshs;
    private String onomaXrhsth;
    private String titlosEvent;
    private String hmeromhnia;
    private String wra;
    private int arithmosEisitiriwn;
    private double sunolikoKostos;

    // Constructor me ola ta pedia
    public Booking(String kwdikosKrathshs, String onomaXrhsth, String titlosEvent,
                   String hmeromhnia, String wra, int arithmosEisitiriwn, double sunolikoKostos) {
        this.kwdikosKrathshs = kwdikosKrathshs;
        this.onomaXrhsth = onomaXrhsth;
        this.titlosEvent = titlosEvent;
        this.hmeromhnia = hmeromhnia;
        this.wra = wra;
        this.arithmosEisitiriwn = arithmosEisitiriwn;
        this.sunolikoKostos = sunolikoKostos;
    }

    // === Getters ===
    public String getKwdikosKrathshs() {
        return kwdikosKrathshs;
    }

    public String getOnomaXrhsth() {
        return onomaXrhsth;
    }

    public String getTitlosEvent() {
        return titlosEvent;
    }

    public String getHmeromhnia() {
        return hmeromhnia;
    }

    public String getWra() {
        return wra;
    }

    public int getArithmosEisitiriwn() {
        return arithmosEisitiriwn;
    }

    public double getSunolikoKostos() {
        return sunolikoKostos;
    }

    // === Setters ===
    public void setKwdikosKrathshs(String kwdikosKrathshs) {
        this.kwdikosKrathshs = kwdikosKrathshs;
    }

    public void setOnomaXrhsth(String onomaXrhsth) {
        this.onomaXrhsth = onomaXrhsth;
    }

    public void setTitlosEvent(String titlosEvent) {
        this.titlosEvent = titlosEvent;
    }

    public void setHmeromhnia(String hmeromhnia) {
        this.hmeromhnia = hmeromhnia;
    }

    public void setWra(String wra) {
        this.wra = wra;
    }

    public void setArithmosEisitiriwn(int arithmosEisitiriwn) {
        this.arithmosEisitiriwn = arithmosEisitiriwn;
    }

    public void setSunolikoKostos(double sunolikoKostos) {
        this.sunolikoKostos = sunolikoKostos;
    }

    @Override
    public String toString() {
        return String.format("Krathsh[%s | %s | %s | %s %s | %d eisitiria | %.2f€]",
                kwdikosKrathshs, onomaXrhsth, titlosEvent, hmeromhnia, wra, arithmosEisitiriwn, sunolikoKostos);
    }
}

