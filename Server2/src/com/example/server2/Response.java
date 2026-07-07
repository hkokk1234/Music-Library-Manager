/*
Stavros Moschis icsd22232
Zacharias Kokkinakis icsd22077
 */package com.example.server2;

import java.io.Serializable;

// Anaparista tin apantisi tou Server2 se kapoio aitima apo ton Server1
// Perilamvanei epituxia, minima kai proairetika dedomena
public class Response implements Serializable {
    private static final long serialVersionUID = 1L;

    private boolean epituxia;
    private String mhnyma;
    private Object dedomena;

    public Response(boolean epituxia, String mhnyma, Object dedomena) {
        this.epituxia = epituxia;
        this.mhnyma = mhnyma;
        this.dedomena = dedomena;
    }

    public boolean isEpituxia() {
        return epituxia;
    }

    public String getMhnyma() {
        return mhnyma;
    }

    public Object getDedomena() {
        return dedomena;
    }

    public void setEpituxia(boolean epituxia) {
        this.epituxia = epituxia;
    }

    public void setMhnyma(String mhnyma) {
        this.mhnyma = mhnyma;
    }

    public void setDedomena(Object dedomena) {
        this.dedomena = dedomena;
    }

    @Override
    public String toString() {
        return "Response{" +
                "epituxia=" + epituxia +
                ", mhnyma='" + mhnyma + '\'' +
                ", dedomena=" + (dedomena == null ? "null" : dedomena.getClass().getSimpleName()) +
                '}';
    }
}
