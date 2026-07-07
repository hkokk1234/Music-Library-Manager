/*
Stavros Moschis icsd22232
Zacharias Kokkinakis icsd22077
 */package com.example.server1;

import com.example.client.communication.ClientCallbackInterface;

import java.rmi.RemoteException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

// Diaxeirizetai ta callbacks twn xrhstwn pou sundedontai me ton server
public class CallbackManager {

    // Thread-safe map pou apothikeuei ta callback objects analoga me to username
    private static final Map<String, ClientCallbackInterface> xrhstesCallbacks = new ConcurrentHashMap<>();

    // Egrafh xrhsth sto callback system
    public static void eggrafiCallback(ClientCallbackInterface callback, String onomaXrhsth) {
        xrhstesCallbacks.put(onomaXrhsth, callback);
        System.out.println("✅ Callback registered for user: " + onomaXrhsth);
    }

    // Apografh xrhsth apo callback
    public static void diagrafiCallback(ClientCallbackInterface callback, String onomaXrhsth) {
        xrhstesCallbacks.remove(onomaXrhsth);
        System.out.println("❌ Callback unregistered for user: " + onomaXrhsth);
    }

    // Stelnei eidopoihsh mono se sugkekrimeno xrhsth (π.χ. gia ekptwsh)
    public static void eidopoihshXrhsthEkptwsh(String onomaXrhsth, String showId, String mhnyma) {
        ClientCallbackInterface callback = xrhstesCallbacks.get(onomaXrhsth);
        if (callback != null) {
            try {
                callback.notifyDiscount(showId, mhnyma);
                System.out.println("🔔 Notification sent to user: " + onomaXrhsth);
            } catch (RemoteException e) {
                System.err.println("⚠️ Error sending callback to " + onomaXrhsth);
                e.printStackTrace();
            }
        } else {
            System.out.println("ℹ️ No active callback for user: " + onomaXrhsth);
        }
    }

    // Stelnei eidopoihsh se OLOUS tous sundedemenous xrhstes
    public static void broadcastEidopoihsh(String showId, String mhnyma) {
        for (Map.Entry<String, ClientCallbackInterface> entry : xrhstesCallbacks.entrySet()) {
            String xrhsths = entry.getKey();
            ClientCallbackInterface callback = entry.getValue();
            try {
                callback.notifyDiscount(showId, mhnyma);
                System.out.println("📢 Notification sent to: " + xrhsths);
            } catch (RemoteException e) {
                System.err.println("⚠️ Callback error for user " + xrhsths);
                e.printStackTrace();
            }
        }
    }
}
