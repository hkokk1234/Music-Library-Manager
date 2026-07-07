/*
Stavros Moschis icsd22232
Zacharias Kokkinakis icsd22077
 */package com.example.client.communication;

import com.example.client.model.*;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

/**
 * RMI Interface που εκτίθεται από τον Server1 και χρησιμοποιείται από τον Client.
 */
public interface ClientRMIInterface extends Remote {

    // ====== User Operations ======

    User login(String username, String password) throws RemoteException;

    boolean registerUser(User user) throws RemoteException;

    List<Event> getEvents() throws RemoteException;
    boolean saveEventToLocal(Event event) throws RemoteException;

    boolean bookTickets(String username, String showId, int tickets, Payment payment) throws RemoteException;

    List<Booking> getUserBookings(String username) throws RemoteException;

    boolean cancelBooking(String username, String bookingId) throws RemoteException;

    // ====== Admin Operations ======

    boolean addEvent(Event event) throws RemoteException;

    boolean deactivateShow(String showId) throws RemoteException;

    boolean deleteUser(String username) throws RemoteException;

    List<User> getAllUsers() throws RemoteException;

    // ====== Client Callbacks ======

    void registerCallback(ClientCallbackInterface callback, String username) throws RemoteException;

    void unregisterCallback(ClientCallbackInterface callback, String username) throws RemoteException;
}
