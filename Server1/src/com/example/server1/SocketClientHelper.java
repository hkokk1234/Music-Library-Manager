/*
Stavros Moschis icsd22232
Zacharias Kokkinakis icsd22077
 */package com.example.server1;

import com.example.client.model.Event;

import java.io.*;
import java.net.Socket;
import java.util.List;

// Voithitiki klasi gia epikoinwnia me ton Server2 mesw sockets MONO gia Events
public class SocketClientHelper {

    private static final String SERVER2_HOST = "localhost";
    private static final int SERVER2_PORT = 9999;

    // ========== Pare Events apo Server2 ==========
    @SuppressWarnings("unchecked")
    public static List<Event> pareEvents() {
        Object apantisi = steileAitima("GET_EVENTS", null);
        return (List<Event>) apantisi;
    }

    // ========== Prosthhkh Event ==========
    public static boolean prostheseEvent(Event event) {
        Object apantisi = steileAitima("ADD_EVENT", new Object[]{event});
        return apantisi instanceof Boolean && (Boolean) apantisi;
    }

    // ========== Apenergopoihsh Show ==========
    public static boolean apenergopoihseShow(String showId) {
        Object apantisi = steileAitima("DEACTIVATE_SHOW", new Object[]{showId});
        return apantisi instanceof Boolean && (Boolean) apantisi;
    }

    // ========== Eswterikh methodos epikoinwnias ==========
    private static Object steileAitima(String entoli, Object[] parametroi) {
        try (
                Socket socket = new Socket(SERVER2_HOST, SERVER2_PORT);
                ObjectOutputStream eksodos = new ObjectOutputStream(socket.getOutputStream());
                ObjectInputStream eisodos = new ObjectInputStream(socket.getInputStream());
        ) {
            Request aitima = new Request(entoli, parametroi);
            eksodos.writeObject(aitima);
            eksodos.flush();

            Response apantisi = (Response) eisodos.readObject();
            if (apantisi.isEpituxia()) {
                return apantisi.getDedomena();
            } else {
                System.err.println("❌ Error [" + entoli + "]: " + apantisi.getMhnyma());
                return null;
            }
        } catch (Exception e) {
            System.err.println("💥 SocketClientHelper: Error while sending [" + entoli + "]");
            e.printStackTrace();
            return null;
        }
    }
}

