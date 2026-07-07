/*
Stavros Moschis icsd22232
Zacharias Kokkinakis icsd22077
 */package project;

import com.example.client.communication.Client;
import com.example.client.model.Event;
import com.example.client.model.Show;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;

// Klasi pou deixnei ta dia8esima shows gia kratisi apo xrhsth
public class UserEventsListFrame extends JFrame {
    private JTable pinakasShows;
    private DefaultTableModel montelo;
    private JButton koumpiKratisi, koumpiAnanewsh, koumpiEpistrofi;

    private final String onomaXrhsth;
    private final Client pelatis;

    public UserEventsListFrame(Client pelatis, String onomaXrhsth) {
        this.pelatis = pelatis;
        this.onomaXrhsth = onomaXrhsth;

        setTitle("Shows list");
        setSize(800, 350);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel(new BorderLayout());

        String[] sthles = {
                "Code", "Title", "Type", "Date", "Time", "Price", "Available Seats"
        };
        montelo = new DefaultTableModel(sthles, 0) {
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        pinakasShows = new JTable(montelo);
        JScrollPane scrollPane = new JScrollPane(pinakasShows);

        JPanel panelKoumpiwn = new JPanel();
        koumpiKratisi = new JButton("Book");
        koumpiAnanewsh = new JButton("Refresh");
        koumpiEpistrofi = new JButton("Back");
        panelKoumpiwn.add(koumpiKratisi);
        panelKoumpiwn.add(koumpiAnanewsh);
        panelKoumpiwn.add(koumpiEpistrofi);

        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(panelKoumpiwn, BorderLayout.SOUTH);

        add(panel);

        // Fortwnei dedomena apo server
        fortwseShowsApoServer();

        // Energeies koumpiwn
        koumpiKratisi.addActionListener((ActionEvent e) -> epilekseShow());
        koumpiAnanewsh.addActionListener((ActionEvent e) -> fortwseShowsApoServer());
        koumpiEpistrofi.addActionListener(e -> {
            dispose();
            new MainMenuUserFrame(pelatis, onomaXrhsth).setVisible(true);
        });

        // Diplo klik sto show gia metabhsh sto booking
        pinakasShows.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                if (evt.getClickCount() == 2) {
                    epilekseShow();
                }
            }
        });
    }

    // Fortwnei ola ta dia8esima shows apo ton server
    private void fortwseShowsApoServer() {
        try {
            if (pelatis == null) {
                throw new IllegalStateException("H sundesh me ton server den einai dia8esimh.");
            }

            List<Event> events = pelatis.getEvents();
            montelo.setRowCount(0); // Katharismos pinaka

            for (Event event : events) {
                for (Show show : event.getListaParastasewn()) {
                    if (show.einaiEnergo()) {
                        montelo.addRow(new Object[]{
                                show.getKwdikosParastasis(),
                                event.getTitlos(),
                                event.getTypos(),
                                show.getHmeromhnia(),
                                show.getWra(),
                                String.format("%.2f", show.getTimh()),
                                show.getDia8esimesTheseis()
                        });
                    }
                }
            }

            pinakasShows.getTableHeader().setReorderingAllowed(false); // Midenizei metakinisi sthlwn

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Failed to load shows:\n" + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    // Otan epilegetai ena show gia kratisi
    private void epilekseShow() {
        int grammh = pinakasShows.getSelectedRow();
        if (grammh == -1) {
            JOptionPane.showMessageDialog(this,
                    "Please select a show.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String showId = (String) pinakasShows.getValueAt(grammh, 0);
        String titlos = (String) pinakasShows.getValueAt(grammh, 1);

        int epivevaiwsh = JOptionPane.showConfirmDialog(this,
                "Do you want to continue with booking for:\n" + titlos + " (" + showId + ")?",
                "confirm", JOptionPane.YES_NO_OPTION);

        if (epivevaiwsh == JOptionPane.YES_OPTION) {
            try {
                new UserBookingFrame(pelatis, onomaXrhsth, showId).setVisible(true);
                dispose();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this,
                        "Error opening booking form:\n" + e.getMessage(),
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
                new UserEventsListFrame(pelatis, "user1").setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}
