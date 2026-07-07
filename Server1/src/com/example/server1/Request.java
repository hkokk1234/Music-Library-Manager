/*
Stavros Moschis icsd22232
Zacharias Kokkinakis icsd22077
 */
package com.example.server1;

import java.io.Serializable;

// Klasi pou anaparista ena aitima (command) me parametroys gia metadosi/storisi
public class Request implements Serializable {
    private static final long serialVersionUID = 1L;

    private String entoli;
    private Object[] parametroi;

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
