/*
Stavros Moschis icsd22232
Zacharias Kokkinakis icsd22077
 */package com.example.client.communication;

import com.example.client.model.*;

import javax.swing.*;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;

// Client class that communicates with the RMI server and handles callbacks
public class Client extends UnicastRemoteObject implements ClientCallbackInterface {
    private final ClientRMIInterface serverStub;
    private String onomaXrhsth;

    // Constructor - connects to server via RMI URL
    public Client(String serverUrl) throws Exception {
        this.serverStub = (ClientRMIInterface) Naming.lookup(serverUrl);
    }

    // Saves an event locally (used by admin)
    public boolean saveEventToLocal(Event event) throws RemoteException {
        return serverStub.saveEventToLocal(event);
    }

    // Login - returns User object if credentials are valid
    public User login(String onomaXrhsth, String kwdikos) throws RemoteException {
        User xrhsths = serverStub.login(onomaXrhsth, kwdikos);
        if (xrhsths != null) {
            this.onomaXrhsth = onomaXrhsth;
            serverStub.registerCallback(this, onomaXrhsth); // Enables discount notifications
        }
        return xrhsths;
    }

    // Register new user
    public boolean registerUser(User xrhsths) throws RemoteException {
        return serverStub.registerUser(xrhsths);
    }

    // Fetch all available events
    public List<Event> getEvents() throws RemoteException {
        return serverStub.getEvents();
    }

    // Booking method
    public boolean bookTickets(String xrhsths, String showId, int eisitiria, Payment plhrwmh) throws RemoteException {
        return serverStub.bookTickets(xrhsths, showId, eisitiria, plhrwmh);
    }

    // Get bookings for specific user
    public List<Booking> getUserBookings(String xrhsths) throws RemoteException {
        return serverStub.getUserBookings(xrhsths);
    }

    // Cancel a user's booking
    public boolean cancelBooking(String xrhsths, String bookingId) throws RemoteException {
        return serverStub.cancelBooking(xrhsths, bookingId);
    }

    // Admin functionality - Add event across servers
    public boolean addEvent(Event event) throws RemoteException {
        return serverStub.addEvent(event);
    }

    // Admin functionality - Deactivate a show
    public boolean deactivateShow(String showId) throws RemoteException {
        return serverStub.deactivateShow(showId);
    }

    // Admin functionality - Delete a user
    public boolean deleteUser(String xrhsthsGiaDiagrafi) throws RemoteException {
        return serverStub.deleteUser(xrhsthsGiaDiagrafi);
    }

    // Admin - Fetch all registered users
    public List<User> getAllUsers() throws RemoteException {
        return serverStub.getAllUsers();
    }

    // Callback - receive notification from server (e.g., discount alert)
    @Override
    public void notifyDiscount(String showId, String message) throws RemoteException {
        JOptionPane.showMessageDialog(null, "Discount for show: " + showId + "\n" + message,
                "Notification", JOptionPane.INFORMATION_MESSAGE);
    }

    // Logout & unregister callback from server
    public void logout() throws RemoteException {
        if (onomaXrhsth != null) {
            serverStub.unregisterCallback(this, onomaXrhsth);
            onomaXrhsth = null;
        }
    }
}
