/*
Stavros Moschis icsd22232
Zacharias Kokkinakis icsd22077
 */package project;

import com.example.client.communication.Client;
import com.example.client.model.Booking;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

// Klasi pou deixnei ston xrhsth tis krathseis tou
public class UserBookingsListFrame extends JFrame {
    private final Client pelatis;
    private final String onomaXrhsth;
    private JTable pinakasKrathsewn;

    public UserBookingsListFrame(Client pelatis, String onomaXrhsth) {
        this.pelatis = pelatis;
        this.onomaXrhsth = onomaXrhsth;

        setTitle("My bookings");
        setSize(750, 350);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setResizable(false);

        JPanel panel = new JPanel(new BorderLayout());

        // Pinakas krathsewn
        pinakasKrathsewn = new JTable();
        JScrollPane scrollPane = new JScrollPane(pinakasKrathsewn);

        // Koumpia refresh kai back
        JButton koumpiAnanewsh = new JButton("Refresh");
        JButton koumpiEpistrofi = new JButton("Back");

        JPanel panelKoumpiwn = new JPanel();
        panelKoumpiwn.add(koumpiAnanewsh);
        panelKoumpiwn.add(koumpiEpistrofi);

        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(panelKoumpiwn, BorderLayout.SOUTH);

        add(panel);

        fortwseKrathseis();

        // Listener gia koumpia
        koumpiAnanewsh.addActionListener(e -> fortwseKrathseis());
        koumpiEpistrofi.addActionListener(e -> {
            dispose();
            new MainMenuUserFrame(pelatis, onomaXrhsth).setVisible(true);
        });
    }

    // Fortwnei tis krathseis tou xrhsth kai tis emfanizei ston pinaka
    private void fortwseKrathseis() {
        try {
            List<Booking> krathseis = pelatis.getUserBookings(onomaXrhsth);

            String[] stiles = {
                    "Booking ID", "Event", "Date", "Time", "Tickets", "Total Cost (€)"
            };

            DefaultTableModel montelo = new DefaultTableModel(stiles, 0) {
                @Override
                public boolean isCellEditable(int row, int column) {
                    return false;
                }
            };

            for (Booking kr : krathseis) {
                montelo.addRow(new Object[]{
                        kr.getKwdikosKrathshs(),
                        kr.getOnomaXrhsth(),
                        kr.getHmeromhnia(),
                        kr.getWra(),
                        kr.getArithmosEisitiriwn(),
                        String.format("%.2f", kr.getSunolikoKostos())
                });
            }

            pinakasKrathsewn.setModel(montelo);
            pinakasKrathsewn.getTableHeader().setReorderingAllowed(false);

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Failed to load bookings:\n" + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);

            ex.printStackTrace();
        }
    }

    // Main methodos gia dokimi
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                Client pelatis = new Client("rmi://localhost:9999/BookingService");
                new UserBookingsListFrame(pelatis, "user1").setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}
