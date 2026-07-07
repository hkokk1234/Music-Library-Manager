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

// Parathuro pou epitrepei ston xrhsth na akyrwsei krathseis tou
public class UserCancelBookingFrame extends JFrame {
    private final Client pelatis;
    private final String onomaXrhsth;
    private JTable pinakasKrathsewn;

    public UserCancelBookingFrame(Client pelatis, String onomaXrhsth) {
        this.pelatis = pelatis;
        this.onomaXrhsth = onomaXrhsth;

        setTitle("Cancel booking");
        setSize(750, 350);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setResizable(false);

        JPanel panel = new JPanel(new BorderLayout());

        pinakasKrathsewn = new JTable();
        JScrollPane scrollPane = new JScrollPane(pinakasKrathsewn);

        JButton koumpiAkyrwsh = new JButton("Cancel booking");
        JButton koumpiAnanewsh = new JButton("Refresh");
        JButton koumpiEpistrofi = new JButton("Back");

        JPanel panelKoumpiwn = new JPanel();
        panelKoumpiwn.add(koumpiAkyrwsh);
        panelKoumpiwn.add(koumpiAnanewsh);
        panelKoumpiwn.add(koumpiEpistrofi);

        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(panelKoumpiwn, BorderLayout.SOUTH);

        add(panel);

        fortwseKrathseis();

        koumpiAkyrwsh.addActionListener(e -> akyrwshEpilegmenhsKrathshs());
        koumpiAnanewsh.addActionListener(e -> fortwseKrathseis());
        koumpiEpistrofi.addActionListener(e -> {
            dispose();
            new MainMenuUserFrame(pelatis, onomaXrhsth).setVisible(true);
        });

        // Diplo klik gia grigori akyrwsh
        pinakasKrathsewn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                if (evt.getClickCount() == 2) {
                    akyrwshEpilegmenhsKrathshs();
                }
            }
        });
    }

    // Fortwnei tis krathseis tou xrhsth ston pinaka
    private void fortwseKrathseis() {
        try {
            List<Booking> krathseis = pelatis.getUserBookings(onomaXrhsth);

            String[] stiles = {
                    "Booking ID", "Event", "Date", "Time", "Tickets", "Total (€)"
            };

            DefaultTableModel montelo = new DefaultTableModel(stiles, 0) {
                @Override
                public boolean isCellEditable(int row, int column) {
                    return false;
                }
            };

            if (krathseis != null) {
                for (Booking kr : krathseis) {
                    Object[] grammh = {
                            kr.getKwdikosKrathshs(),
                            kr.getOnomaXrhsth(),
                            kr.getTitlosEvent(),
                            kr.getHmeromhnia(),
                            kr.getArithmosEisitiriwn(),
                            String.format("%.2f", kr.getSunolikoKostos())
                    };
                    montelo.addRow(grammh);
                }
            }

            pinakasKrathsewn.setModel(montelo);
            pinakasKrathsewn.getTableHeader().setReorderingAllowed(false);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Failed to load bookings:\n" + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    // Akyrwnei thn epilegmenh krathsh
    private void akyrwshEpilegmenhsKrathshs() {
        int grammh = pinakasKrathsewn.getSelectedRow();
        if (grammh == -1) {
            JOptionPane.showMessageDialog(this, "Please select a booking.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String bookingId = pinakasKrathsewn.getValueAt(grammh, 0).toString();

        int epivevaiwsh = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to cancel booking: " + bookingId + "?",
                "Cancel Confirmation",
                JOptionPane.YES_NO_OPTION);

        if (epivevaiwsh == JOptionPane.YES_OPTION) {
            try {
                boolean epituxia = pelatis.cancelBooking(onomaXrhsth, bookingId);
                if (epituxia) {
                    JOptionPane.showMessageDialog(this, "Booking cancelled successfully.");
                    fortwseKrathseis();
                } else {
                    JOptionPane.showMessageDialog(this, "Cancellation failed.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Communication error:\n" + e.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }
        }
    }

    // Main methodos gia test
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                Client pelatis = new Client("rmi://localhost:9999/BookingService");
                new UserCancelBookingFrame(pelatis, "user1").setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}
