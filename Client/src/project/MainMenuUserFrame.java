/*
Stavros Moschis icsd22232
Zacharias Kokkinakis icsd22077
 */package project;

import com.example.client.communication.Client;

import javax.swing.*;
import java.awt.*;

// Klasi gia to kentriko menu tou xrhsth
public class MainMenuUserFrame extends JFrame {
    private final String onomaXrhsth;
    private final Client pelatis;

    public MainMenuUserFrame(Client pelatis, String onomaXrhsth) {
        this.pelatis = pelatis;
        this.onomaXrhsth = onomaXrhsth;

        setTitle("User dashboard");
        setSize(400, 300);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel panel = new JPanel();
        panel.setLayout(null);

        // Kalwsorisma
        JLabel etiketaKalwsorismatos = new JLabel("Welcome " + onomaXrhsth + "!", SwingConstants.CENTER);
        etiketaKalwsorismatos.setBounds(40, 10, 320, 25);
        etiketaKalwsorismatos.setFont(new Font("Arial", Font.BOLD, 16));
        panel.add(etiketaKalwsorismatos);

        int y = 50;

        // Koumpi gia kratisi eisitiriwn
        JButton koumpiKratisi = new JButton("Book tickets");
        koumpiKratisi.setBounds(80, y, 220, 30);
        panel.add(koumpiKratisi);
        y += 40;

        // Koumpi gia emfanish krathsewn
        JButton koumpiKrathseisMou = new JButton("My bookings");
        koumpiKrathseisMou.setBounds(80, y, 220, 30);
        panel.add(koumpiKrathseisMou);
        y += 40;

        // Koumpi gia akyrwsi kratisis
        JButton koumpiAkyrwsi = new JButton("Cancel booking");
        koumpiAkyrwsi.setBounds(80, y, 220, 30);
        panel.add(koumpiAkyrwsi);
        y += 40;

        // Koumpi logout
        JButton koumpiLogout = new JButton("Logout");
        koumpiLogout.setBounds(80, y, 220, 30);
        panel.add(koumpiLogout);

        add(panel);

        // === Energeies koumpiwn ===

        // Anoigei lista me dia8esima events gia kratisi
        koumpiKratisi.addActionListener(e -> {
            new UserEventsListFrame(pelatis, onomaXrhsth).setVisible(true);
            dispose();
        });

        // Emfanizei lista me krathseis tou xrhsth
        koumpiKrathseisMou.addActionListener(e -> {
            new UserBookingsListFrame(pelatis, onomaXrhsth).setVisible(true);
            dispose();
        });

        // Epitrepei akyrwsi kapoias kratisis
        koumpiAkyrwsi.addActionListener(e -> {
            new UserCancelBookingFrame(pelatis, onomaXrhsth).setVisible(true);
            dispose();
        });

        // Logout kai epistrofi sto parathyro syndesis
        koumpiLogout.addActionListener(e -> {
            try {
                pelatis.logout();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error during logout", "Error", JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
            new LoginFrame().setVisible(true);
            dispose();
        });
    }

    // main methodos gia dokimi
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                Client pelatis = new Client("rmi://localhost:9999/BookingService");
                new MainMenuUserFrame(pelatis, "user1").setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, "Failed to connect to the server.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
    }
}
