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

// Klasi gia emfanish listas me events gia ton admin
public class AdminEventsListFrame extends JFrame {
    private final Client pelatis; // Client gia epikoinwnia
    private final String onomaAdmin;
    private JTable pinakasEvents; // Pinakas pou deixnei ta shows
    private DefaultTableModel monteloPinaka; // Montelo tou pinaka

    public AdminEventsListFrame(Client pelatis, String onomaAdmin) {
        this.pelatis = pelatis;
        this.onomaAdmin = onomaAdmin;

        setTitle("Events List (Admin)");
        setSize(850, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        // Orismos sthlwn pinaka
        String[] stiles = {
                "Code", "Title", "Type", "Date", "Time", "Price", "Available Seats", "Active"
        };

        monteloPinaka = new DefaultTableModel(stiles, 0) {
            public boolean isCellEditable(int grammi, int stili) {
                return false;
            }
        };

        pinakasEvents = new JTable(monteloPinaka);
        JScrollPane scrollPane = new JScrollPane(pinakasEvents);

        // Koumpia xeirismou
        JButton koumpiDetails = new JButton("Details");
        JButton koumpiAnanewsi = new JButton("Refresh");
        JButton koumpiEpistrofi = new JButton("Back");

        JPanel panelKoumpiwn = new JPanel();
        panelKoumpiwn.add(koumpiDetails);
        panelKoumpiwn.add(koumpiAnanewsi);
        panelKoumpiwn.add(koumpiEpistrofi);

        setLayout(new BorderLayout());
        add(scrollPane, BorderLayout.CENTER);
        add(panelKoumpiwn, BorderLayout.SOUTH);

        // Listener gia koumpia
        koumpiDetails.addActionListener((ActionEvent e) -> deikseDetailsEpilegmenouShow());
        koumpiAnanewsi.addActionListener(e -> fortwseEvents());
        koumpiEpistrofi.addActionListener(e -> {
            dispose();
            new MainMenuAdminFrame(pelatis, onomaAdmin).setVisible(true);
        });

        // Diplo klik gia leptomeries show
        pinakasEvents.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                if (evt.getClickCount() == 2) {
                    deikseDetailsEpilegmenouShow();
                }
            }
        });

        fortwseEvents(); // Arxikh fortwsh
    }

    // Fortwnei ta shows ston pinaka
    private void fortwseEvents() {
        try {
            monteloPinaka.setRowCount(0); // Katharismos pinaka
            List<Event> events = pelatis.getEvents();

            for (Event e : events) {
                for (Show show : e.getListaParastasewn()) {
                    monteloPinaka.addRow(new Object[]{
                            show.getKwdikosParastasis(),
                            e.getTitlos(),
                            e.getTypos(),
                            show.getHmeromhnia(),
                            show.getWra(),
                            show.getTimh(),
                            show.getDia8esimesTheseis(),
                            show.einaiEnergo() ? "Yes" : "No"
                    });
                }
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error loading events:\n" + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    // Emfanizei leptomeries tou epilegmenou show
    private void deikseDetailsEpilegmenouShow() {
        int grammh = pinakasEvents.getSelectedRow();
        if (grammh == -1) {
            JOptionPane.showMessageDialog(this, "Please select an event first.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String showId = (String) monteloPinaka.getValueAt(grammh, 0);
        String titlos = (String) monteloPinaka.getValueAt(grammh, 1);
        String hmeromhnia = monteloPinaka.getValueAt(grammh, 3).toString();
        String wra = (String) monteloPinaka.getValueAt(grammh, 4);

        JOptionPane.showMessageDialog(this,
                "Show Details:\n" +
                        "Show ID: " + showId +
                        "\nTitle: " + titlos +
                        "\nDate: " + hmeromhnia +
                        "\nTime: " + wra,
                "Information", JOptionPane.INFORMATION_MESSAGE);
    }

    // Main methodos gia dokimi
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                Client pelatis = new Client("rmi://localhost:9999/BookingService");
                new AdminEventsListFrame(pelatis, "admin").setVisible(true);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Connection error with the server", "Error", JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }
        });
    }
}
