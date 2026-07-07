/*
Stavros Moschis icsd22232
Zacharias Kokkinakis icsd22077
 */package com.example.client.communication;

import com.example.client.model.*;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

/**
 * Remote interface pou ekti8etai apo ton Server1 kai kalείται apo ton Client.
 */
public interface ClientRMIInterface extends Remote {

    // ====== User Operations ======

    /**
     * Sundesh xrhsth me username/kodiko.
     */
    User login(String username, String password) throws RemoteException;

    /**
     * Eggrafh neou xrhsth.
     */
    boolean registerUser(User user) throws RemoteException;

    /**
     * Epistrofh listas me ola ta events.
     */
    List<Event> getEvents() throws RemoteException;

    /**
     * Kratisi eisitiriwn gia sygkekrimeno show.
     */
    boolean bookTickets(String username, String showId, int tickets, Payment payment) throws RemoteException;

    /**
     * Epistrofh krathsewn pou anikoun se sygkekrimeno xrhsth.
     */
    List<Booking> getUserBookings(String username) throws RemoteException;

    /**
     * Akyrwsi kratisis.
     */
    boolean cancelBooking(String username, String bookingId) throws RemoteException;

    /**
     * Lokikh apothikeush event apo ton admin se topiko server.
     */
    boolean saveEventToLocal(Event event) throws RemoteException;

    // ====== Admin Operations ======

    /**
     * Prosthhkh event sto susthma (kai diadosh an xreiastei).
     */
    boolean addEvent(Event event) throws RemoteException;

    /**
     * Apenergopoihsh sygkekrimenou show.
     */
    boolean deactivateShow(String showId) throws RemoteException;

    /**
     * Diagrafh xrhsth apo to susthma.
     */
    boolean deleteUser(String username) throws RemoteException;

    /**
     * Epistrofh olwn twn eggrafmenwn xrhstwn (gia admin).
     */
    List<User> getAllUsers() throws RemoteException;

    // ====== Client Callbacks ======

    /**
     * Eggrafh client gia callback notifications (e.g. ekptwseis).
     */
    void registerCallback(ClientCallbackInterface callback, String username) throws RemoteException;

    /**
     * Akyrwsi eggrafhs apo callback lista.
     */
    void unregisterCallback(ClientCallbackInterface callback, String username) throws RemoteException;
}
