/*
Stavros Moschis icsd22232
Zacharias Kokkinakis icsd22077
 */package com.example.client.model;

import java.io.Serializable;
import java.util.Date;

// Anaparista mia parastasi (show) enos event me imerominia, wra, timh kai plhthos thesewn
public class Show implements Serializable {
    private static final long serialVersionUID = 1L;

    private String kwdikosParastasis;  // Unikos kwdikos show
    private Date hmeromhnia;           // Hmeromhnia parastasis
    private String wra;                // Wra HH:mm
    private double timh;              // Timh eisitiriou
    private int dia8esimesTheseis;    // Plithos diathesimwn thesewn
    private boolean energo;           // True an einai energo gia kratisi

    public Show(String kwdikosParastasis, Date hmeromhnia, String wra,
                double timh, int dia8esimesTheseis, boolean energo) {
        this.kwdikosParastasis = kwdikosParastasis;
        this.hmeromhnia = hmeromhnia;
        this.wra = wra;
        this.timh = timh;
        this.dia8esimesTheseis = dia8esimesTheseis;
        this.energo = energo;
    }

    // === Getters ===
    public String getKwdikosParastasis() {
        return kwdikosParastasis;
    }

    public Date getHmeromhnia() {
        return hmeromhnia;
    }

    public String getWra() {
        return wra;
    }

    public double getTimh() {
        return timh;
    }

    public String getMorfopoiimeniHmeromhnia() {
        return new java.text.SimpleDateFormat("yyyy-MM-dd").format(hmeromhnia);
    }

    public int getDia8esimesTheseis() {
        return dia8esimesTheseis;
    }

    public boolean einaiEnergo() {
        return energo;
    }

    // === Setters ===
    public void setKwdikosParastasis(String kwdikosParastasis) {
        this.kwdikosParastasis = kwdikosParastasis;
    }

    public void setHmeromhnia(Date hmeromhnia) {
        this.hmeromhnia = hmeromhnia;
    }

    public void setWra(String wra) {
        this.wra = wra;
    }

    public void setTimh(double timh) {
        this.timh = timh;
    }

    public void setDia8esimesTheseis(int dia8esimesTheseis) {
        this.dia8esimesTheseis = dia8esimesTheseis;
    }

    public void setEnergo(boolean energo) {
        this.energo = energo;
    }

    @Override
    public String toString() {
        return "Parastasi{" +
                "kwdikos='" + kwdikosParastasis + '\'' +
                ", hmeromhnia=" + hmeromhnia +
                ", wra='" + wra + '\'' +
                ", timh=" + timh +
                ", dia8esimesTheseis=" + dia8esimesTheseis +
                ", energo=" + energo +
                '}';
    }
}
