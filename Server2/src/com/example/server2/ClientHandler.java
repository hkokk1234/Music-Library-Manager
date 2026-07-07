/*
Stavros Moschis icsd22232
Zacharias Kokkinakis icsd22077
 */package com.example.server2;

import java.io.*;
import java.net.Socket;

// Xeirizetai ena client socket gia na diavastei to aitima kai na apanthsei me Response
public class ClientHandler implements Runnable {
    private final Socket sundesh;

    public ClientHandler(Socket sundesh) {
        this.sundesh = sundesh;
    }

    @Override
    public void run() {
        try (
                ObjectInputStream eisodos = new ObjectInputStream(sundesh.getInputStream());
                ObjectOutputStream eksodos = new ObjectOutputStream(sundesh.getOutputStream())
        ) {
            while (true) {
                Object antikeimeno = eisodos.readObject();

                if (!(antikeimeno instanceof Request aitima)) {
                    eksodos.writeObject(new Response(false, "Mi egkyro aitima", null));
                    break;
                }

                System.out.println("📨 Elifthike aitima: " + aitima.getEntoli());

                Response apantisi = diadikasiaAitimatos(aitima);

                eksodos.writeObject(apantisi);
                eksodos.flush();
            }

        } catch (EOFException eof) {
            System.out.println("🔌 O client aposyndethike.");
        } catch (Exception e) {
            System.err.println("❗ Sfalma sto ClientHandler:");
            e.printStackTrace();
        } finally {
            try {
                sundesh.close();
            } catch (IOException ignore) {
            }
        }
    }

    private Response diadikasiaAitimatos(Request aitima) {
        try {
            String entoli = aitima.getEntoli();
            Object[] parametroi = aitima.getParametroi();

            return switch (entoli) {
                case "GET_EVENTS" -> LogicManager.xeirismosLipsisTheamatwn();
                case "ADD_EVENT" -> LogicManager.xeirismosProsthesisTheamatos(parametroi);
                case "DEACTIVATE_SHOW" -> LogicManager.xeirismosApenergopoiisisParastasis(parametroi);
                default -> new Response(false, "Agnwsti entoli: " + entoli, null);
            };

        } catch (Exception e) {
            e.printStackTrace();
            return new Response(false, "Eswteriko sfalma: " + e.getMessage(), null);
        }
    }
}
