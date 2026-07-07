/*
Stavros Moschis icsd22232
Zacharias Kokkinakis icsd22077
 */package com.example.server2;

import java.io.Serializable;

// Anaparista ena aitima (entoli + parametroi) apo ton Server1 pros ton Server2
public class Request implements Serializable {
    private static final long serialVersionUID = 1L;

    private String entoli;           // paradeigma: "GET_EVENTS", "ADD_EVENT", ...
    private Object[] parametroi;     // dedomena tou aitimatos

    public Request(String entoli, Object[] parametroi) {
        this.entoli = entoli;
        this.parametroi = parametroi;
    }

    public String getEntoli() {
        return entoli;
    }

    public Object[] getParametroi() {
        return parametroi;
    }

    public void setEntoli(String entoli) {
        this.entoli = entoli;
    }

    public void setParametroi(Object[] parametroi) {
        this.parametroi = parametroi;
    }

    @Override
    public String toString() {
        return "Request{" +
                "entoli='" + entoli + '\'' +
                ", parametroi=" + (parametroi == null ? "null" : parametroi.length + " params") +
                '}';
    }
}
