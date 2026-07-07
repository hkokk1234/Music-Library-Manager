/*
Stavros Moschis icsd22232
Zacharias Kokkinakis icsd22077
 */package com.example.server1;

import com.example.client.model.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

// Voithitiki klasi gia diaxeirish topikon dedomenwn (xrhstes, bookings, payments)
public class LocalDataHelper {

    private static final List<User> listaXrhstwn = new ArrayList<>();
    private static final List<Booking> listaKrathsewn = new ArrayList<>();
    private static final List<Payment> listaPlhrwmwn = new ArrayList<>();

    // ========== Diaxeirish Xrhsth ==========

    // Elegxei an uparxei xrhsths me swsta stoixeia
    public static User sundeshXrhsth(String username, String password) {
        for (User xrhsths : listaXrhstwn) {
            if (xrhsths.getUsername().equals(username) && xrhsths.getPassword().equals(password)) {
                return xrhsths;
            }
        }
        return null;
    }

    // Kataxwrei event sto DataStore
    public static boolean prostheseEvent(Event event) {
        try {
            DataStore.getInstance().prostheseEvent(event);
            DataStore.getInstance().apothikeuseOla();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // Egrafh neou xrhsth (topika)
    public static boolean eggrafiXrhsth(User neosXrhsths) {
        for (User x : listaXrhstwn) {
            if (x.getUsername().equals(neosXrhsths.getUsername())) {
                return false;
            }
        }
        listaXrhstwn.add(neosXrhsths);
        return true;
    }

    // Diagrafh xrhsth me vasi to username
    public static boolean diagrafiXrhsth(String username) {
        return listaXrhstwn.removeIf(x -> x.getUsername().equals(username));
    }

    // Epistrofh olwn twn xrhstwn (gia admin)
    public static List<User> pareOlousTousXrhstes() {
        return new ArrayList<>(listaXrhstwn);
    }

    // ========== Krathseis ==========

    // Epistrofh krathsewn enos sygkekrimenou xrhsth
    public static List<Booking> pareKrathseisXrhsth(String username) {
        return listaKrathsewn.stream()
                .filter(b -> b.getUsername().equals(username))
                .collect(Collectors.toList());
    }

    // Akyrwsh krathshs me vasi bookingId kai username
    public static boolean akyrwshKrathshs(String username, String bookingId) {
        return listaKrathsewn.removeIf(b -> b.getBookingId().equals(bookingId) && b.getUsername().equals(username));
    }

    // Kataxwrei krathsh kai plhrwmh (demo version, den elegxei theseis)
    public static boolean krathshEisitiriwn(String username, String showId, int eisitiria, Payment plhrwmh) {
        String bookingId = "BKG" + System.currentTimeMillis();
        Booking krathsh = new Booking(bookingId, username, showId, "2025-01-01", "20:00", eisitiria, plhrwmh.getAmount());

        listaKrathsewn.add(krathsh);
        listaPlhrwmwn.add(plhrwmh);

        return true;
    }
}
