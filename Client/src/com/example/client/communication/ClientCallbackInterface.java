/*
Stavros Moschis icsd22232
Zacharias Kokkinakis icsd22077
 */package com.example.client.communication;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Interface pou ylopoieitai apo ton client.
 * O server kalei tis methodous autou gia na steilei callback notifications.
 */
public interface ClientCallbackInterface extends Remote {

    /**
     * Notification to user about offer or discount for a show.
     *
     * @param showId  o kwdikos ths parastashs
     * @param message to mhnyma ths eidopoihshs
     * @throws RemoteException an yparxei provlima me thn sundesh
     */
    void notifyDiscount(String showId, String message) throws RemoteException;

    // Mporeis na prostheseis epipleon callback methodous edw an xreiastei
}
