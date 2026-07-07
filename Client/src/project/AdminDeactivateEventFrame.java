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
import java.util.List;
// Constructor tou parathyrou

public class AdminDeactivateEventFrame extends JFrame {
    private final Client client;
    private final String adminUsername;
    private JTable showsTable;
    private DefaultTableModel tableModel;

    public AdminDeactivateEventFrame(Client client, String adminUsername) {
        this.client = client;
        this.adminUsername = adminUsername;
        // Basikes rythmiseis tou JFrame

        setTitle("Deactivate Show");
        setSize(900, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());
        // Pinakas me stiles gia ta stoixeia twn shows

        String[] columns = {"Code", "Title", "Type", "Date", "Time", "Price", "Seats", "Active"};
        tableModel = new DefaultTableModel(columns, 0) {
            public boolean isCellEditable(int row, int col) {
                return false;
            }
        };
        showsTable = new JTable(tableModel);
        add(new JScrollPane(showsTable), BorderLayout.CENTER);

        JPanel btnPanel = new JPanel();
        JButton deactivateBtn = new JButton("Deactivate");
        JButton refreshBtn = new JButton("Refresh");
        JButton backBtn = new JButton("Back");

        btnPanel.add(deactivateBtn);
        btnPanel.add(refreshBtn);
        btnPanel.add(backBtn);
        add(btnPanel, BorderLayout.SOUTH);
        //actions gia ta koumpia

        refreshBtn.addActionListener(e -> showload());
        deactivateBtn.addActionListener(e -> handleDeactivate());
        backBtn.addActionListener(e -> {
            dispose();
            new MainMenuAdminFrame(client, adminUsername).setVisible(true);
        });

        // Proairetika: diplo klik se grammh pinaka kanei apenergopoihsh
        showsTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                if (evt.getClickCount() == 2) {
                    handleDeactivate();
                }
            }
        });

        showload();
    }
    // Methodos gia na fortwsei ola ta shows ston pinaka

    private void showload() {
        try {
            List<Event> events = client.getEvents();
            tableModel.setRowCount(0);

            for (Event e : events) {
                for (Show s : e.getListaParastasewn()) {
                    tableModel.addRow(new Object[]{
                            s.getKwdikosParastasis(),
                            e.getTitlos(),
                            e.getTypos(),
                            s.getHmeromhnia(),
                            s.getWra(),
                            s.getTimh(),
                            s.getDia8esimesTheseis(),
                            s.einaiEnergo() ? "Yes" : "No"
                    });
                }
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Failed to load shows", "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
    //methodos gia na apenergopoihsei ena epilegmeno show

    private void handleDeactivate() {
        int row = showsTable.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Please select a show!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        //pairnei ta stoixeia tou show apo ton pinaka

        String showId = (String) tableModel.getValueAt(row, 0);
        String title = (String) tableModel.getValueAt(row, 1);
        String date = String.valueOf(tableModel.getValueAt(row, 3));
        String time = String.valueOf(tableModel.getValueAt(row, 4));
        String activeStatus = (String) tableModel.getValueAt(row, 7);

        if ("No".equals(activeStatus)) {
            JOptionPane.showMessageDialog(this, "The show is already deactivated.");
            return;
        }
        //epivevaiwsh apenergopoihshs apo ton xrhsth

        int confirm = JOptionPane.showConfirmDialog(this,
                "Deactivate the show:\n" +
                        "Title: " + title + "\n" +
                        "Date: " + date + " Time: " + time,
                "Confirmation",
                JOptionPane.YES_NO_OPTION);



        if (confirm == JOptionPane.YES_OPTION) {
            try {
                boolean success = client.deactivateShow(showId);
                if (success) {
                    JOptionPane.showMessageDialog(this, "The show has been deactivated.");
                    tableModel.setValueAt("No", row, 7);
                } else {
                    JOptionPane.showMessageDialog(this, "Deactivation failed!", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Communication error with the server", "Error", JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
        }
    }
    // Main methodos gia testing

    public static void main(String[] args) {
        try {
            Client client = new Client("rmi://localhost:9999/BookingService");
            SwingUtilities.invokeLater(() -> new AdminDeactivateEventFrame(client, "admin").setVisible(true));
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Failed to connect to the server", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
