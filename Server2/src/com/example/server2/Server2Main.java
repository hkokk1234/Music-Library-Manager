/*
Stavros Moschis icsd22232
Zacharias Kokkinakis icsd22077
 */package com.example.server2;

import java.net.ServerSocket;
import java.net.Socket;

// Kentriki klasi ekkinisis tou Server2 (socket server)
// Akouei stin porta 5000 kai diaxeirizetai mono Events
public class Server2Main {

    public static final int PORTA = 5000;

    public static void main(String[] args) {
        // Fortwsi events apo arxeio kata tin ekkinisi
        DataStore.pareInstance().fortwseEvents();

        try (ServerSocket socketServer = new ServerSocket(PORTA)) {
            System.out.println("✅ Server2 (Socket server) xekinhse stin porta " + PORTA);

            while (true) {
                Socket pelatis = socketServer.accept();
                System.out.println("🔗 Nea sundesi apo: " + pelatis.getInetAddress());

                // Dimiourgia handler gia kathe client
                ClientHandler xeiristis = new ClientHandler(pelatis);
                new Thread(xeiristis).start();
            }

        } catch (Exception e) {
            System.err.println("❌ Sfálma sto Server2Main:");
            e.printStackTrace();
        }
    }
}
