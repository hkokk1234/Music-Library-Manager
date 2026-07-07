/*
Stavros Moschis icsd22232
Zacharias Kokkinakis icsd22077
 */package com.example.server1;

import com.example.client.communication.ClientCallbackInterface;
import com.example.client.communication.ClientRMIInterface;
import com.example.client.model.*;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;

public class ClientRMIImpl extends UnicastRemoteObject implements ClientRMIInterface {
    private final DataStore store;
    private final Map<String, ClientCallbackInterface> callbacks;

    public ClientRMIImpl() throws RemoteException {
        super();
        store = DataStore.getInstance();
        callbacks = new HashMap<>();
    }

    // === USER METHODS ===

    @Override
    public User login(String username, String password) throws RemoteException {
        return store.getXrhstes().stream()
                .filter(u -> u.getUsername().equals(username) && u.getPassword().equals(password))
                .findFirst()
                .orElse(null);
    }

    @Override
    public boolean registerUser(User user) throws RemoteException {
        for (User u : store.getXrhstes()) {
            if (u.getUsername().equals(user.getUsername())) {
                return false; // username already taken
            }
        }
        store.getXrhstes().add(user);
        store.apothikeuseXrhstes();
        return true;
    }

    // === EVENTS ===

    @Override
    public List<Event> getEvents() throws RemoteException {
        return store.getEvents();
    }

    @Override
    public boolean addEvent(Event event) throws RemoteException {
        store.getEvents().add(event);
        store.apothikeuseEvents();
        return true;
    }
    @Override
    public boolean saveEventToLocal(Event event) throws RemoteException {
        return DataStore.getInstance().prostheseEvent(event);
    }


    @Override
    public boolean deactivateShow(String showId) throws RemoteException {
        for (Event event : store.getEvents()) {
            for (Show show : event.getShows()) {
                if (show.getShowId().equals(showId)) {
                    show.setActive(false);
                    store.apothikeuseEvents();
                    return true;
                }
            }
        }
        return false;
    }

    // === BOOKINGS ===

    @Override
    public boolean bookTickets(String username, String showId, int tickets, Payment payment) throws RemoteException {
        for (Event event : store.getEvents()) {
            for (Show show : event.getShows()) {
                if (show.getShowId().equals(showId) && show.isActive()) {
                    if (show.getAvailableSeats() >= tickets) {
                        show.setAvailableSeats(show.getAvailableSeats() - tickets);
                        Booking booking = new Booking(
                                "B" + System.currentTimeMillis(),
                                username,
                                event.getTitle(),
                                show.getDate().toString(),
                                show.getTime(),
                                tickets,
                                tickets * show.getPrice()
                        );
                        store.getKrathseis().add(booking);
                        store.apothikeuseEvents();
                        store.apothikeuseKrathseis();
                        return true;
                    }
                }
            }
        }
        return false;
    }

    @Override
    public List<Booking> getUserBookings(String username) throws RemoteException {
        List<Booking> result = new ArrayList<>();
        for (Booking b : store.getKrathseis()) {
            if (b.getUsername().equals(username)) {
                result.add(b);
            }
        }
        return result;
    }

    @Override
    public boolean cancelBooking(String username, String bookingId) throws RemoteException {
        Iterator<Booking> it = store.getKrathseis().iterator();
        while (it.hasNext()) {
            Booking b = it.next();
            if (b.getBookingId().equals(bookingId) && b.getUsername().equals(username)) {
                it.remove();
                store.apothikeuseKrathseis();
                return true;
            }
        }
        return false;
    }

    // === ADMIN ===

    @Override
    public boolean deleteUser(String usernameToDelete) throws RemoteException {
        Iterator<User> it = store.getXrhstes().iterator();
        while (it.hasNext()) {
            if (it.next().getUsername().equals(usernameToDelete)) {
                it.remove();
                store.apothikeuseXrhstes();
                return true;
            }
        }
        return false;
    }

    @Override
    public List<User> getAllUsers() throws RemoteException {
        return store.getXrhstes();
    }

    // === CALLBACKS ===

    @Override
    public void registerCallback(ClientCallbackInterface callback, String username) throws RemoteException {
        callbacks.put(username, callback);
    }

    @Override
    public void unregisterCallback(ClientCallbackInterface callback, String username) throws RemoteException {
        callbacks.remove(username);
    }

    // Optional utility for notification
    private void notifyUser(String username, String message) {
        ClientCallbackInterface cb = callbacks.get(username);
        if (cb != null) {
            try {
                cb.notifyDiscount("N/A", message);
            } catch (RemoteException ignored) {}
        }
    }
}
