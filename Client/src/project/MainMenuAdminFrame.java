/*
Stavros Moschis icsd22232
Zacharias Kokkinakis icsd22077
 */package project;

import com.example.client.communication.Client;
import javax.swing.*;
import java.awt.*;

// Klasi gia to kentriko menu tou admin
public class MainMenuAdminFrame extends JFrame {
    private final String onomaXrhsth;
    private final Client pelatis;

    public MainMenuAdminFrame(Client pelatis, String onomaXrhsth) {
        this.onomaXrhsth = onomaXrhsth;
        this.pelatis = pelatis;

        setTitle("Admin Dashboard");
        setSize(400, 350);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel panel = new JPanel();
        panel.setLayout(null);

        // Mhnuma kalwsorismatos
        JLabel etiketaKalwsorismatos = new JLabel("Welcome admin " + onomaXrhsth + "!", SwingConstants.CENTER);
        etiketaKalwsorismatos.setBounds(40, 10, 320, 25);
        etiketaKalwsorismatos.setFont(new Font("Arial", Font.BOLD, 16));
        panel.add(etiketaKalwsorismatos);

        int y = 50;

        // Koumpi gia prosthiki event
        JButton koumpiProsthikiEvent = new JButton("Add event");
        koumpiProsthikiEvent.setBounds(80, y, 220, 30);
        panel.add(koumpiProsthikiEvent);
        y += 40;

        // Koumpi gia apenergopoihsh show
        JButton koumpiApenergopoihshShow = new JButton("Deactivate show");
        koumpiApenergopoihshShow.setBounds(80, y, 220, 30);
        panel.add(koumpiApenergopoihshShow);
        y += 40;

        // Koumpi gia diagrafi xrhsth
        JButton koumpiDiagrafiXrhsth = new JButton("Delete user");
        koumpiDiagrafiXrhsth.setBounds(80, y, 220, 30);
        panel.add(koumpiDiagrafiXrhsth);
        y += 40;

        // Koumpi gia emfanisi events
        JButton koumpiProvoliEvents = new JButton("View events");
        koumpiProvoliEvents.setBounds(80, y, 220, 30);
        panel.add(koumpiProvoliEvents);
        y += 40;

        // Koumpi logout
        JButton koumpiLogout = new JButton("Logout");
        koumpiLogout.setBounds(80, y, 220, 30);
        panel.add(koumpiLogout);

        add(panel);

        // === Energeies koumpiwn ===
        koumpiProsthikiEvent.addActionListener(e -> {
            new AdminAddEventFrame(pelatis, onomaXrhsth).setVisible(true);
            dispose();
        });

        koumpiApenergopoihshShow.addActionListener(e -> {
            new AdminDeactivateEventFrame(pelatis, onomaXrhsth).setVisible(true);
            dispose();
        });

        koumpiDiagrafiXrhsth.addActionListener(e -> {
            new AdminDeleteUserFrame(pelatis, onomaXrhsth).setVisible(true);
            dispose();
        });

        koumpiProvoliEvents.addActionListener(e -> {
            new AdminEventsListFrame(pelatis, onomaXrhsth).setVisible(true);
            dispose();
        });

        koumpiLogout.addActionListener(e -> {
            new LoginFrame().setVisible(true);
            dispose();
        });
    }

    // Main methodos gia dokimi
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                Client pelatis = new Client("rmi://localhost:9999/BookingService");
                new MainMenuAdminFrame(pelatis, "admin").setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, "Failed to connect to the server", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
    }
}
