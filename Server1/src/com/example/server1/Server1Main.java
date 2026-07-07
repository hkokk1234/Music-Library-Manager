/*
Stavros Moschis icsd22232
Zacharias Kokkinakis icsd22077
 */package com.example.server1;

import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;

// Main klasi gia na ekkinhsei o RMI Server1
public class Server1Main {

    public static void main(String[] args) {
        try {
            // Ekkinhsh tou RMI registry sth porta 9999
            LocateRegistry.createRegistry(9999);
            System.out.println("✅ RMI registry started on port 9999.");

            // Dhmiourgia tou Remote Object
            ClientRMIImpl remoteAntikeimeno = new ClientRMIImpl();

            // Desmeush tou Remote Object sto RMI Registry
            Naming.rebind("rmi://localhost:9999/BookingService", remoteAntikeimeno);

            System.out.println("🎭 Server1 is ready and waiting for connections...");

        } catch (Exception e) {
            System.err.println("❌ Error while starting Server1:");
            e.printStackTrace();
        }
    }
}
