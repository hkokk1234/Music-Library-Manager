/*
Stavros Moschis icsd22232
Zacharias Kokkinakis icsd22077
 */package project;

import com.example.client.communication.Client;
import com.example.client.model.User;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

// Klasi pou epitrepei ston admin na diagrapsei xrhstes
public class AdminDeleteUserFrame extends JFrame {
    private final Client pelatis; // Client antikeimeno gia epikoinwnia
    private final String onomaAdmin; // Username tou admin
    private JTable pinakaXrhstwn; // Pinakas gia emfanisi xrhstwn
    private DefaultTableModel monteloPinaka; // Montelo pinaka

    // Constructor - arxikopoiei to UI kai fortwnei xrhstes
    public AdminDeleteUserFrame(Client pelatis, String onomaAdmin) {
        this.pelatis = pelatis;
        this.onomaAdmin = onomaAdmin;

        setTitle("Delete user");
        setSize(700, 350);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        // Orismos sthlwn gia ton pinaka
        String[] stiles = {"Username", "Name", "Email", "Phone", "Role"};
        monteloPinaka = new DefaultTableModel(stiles, 0) {
            public boolean isCellEditable(int grammi, int stili) {
                return false;
            }
        };

        pinakaXrhstwn = new JTable(monteloPinaka);
        JScrollPane scrollPane = new JScrollPane(pinakaXrhstwn);

        // Koumpia katw apo ton pinaka
        JPanel panelKoumpiwn = new JPanel();
        JButton koumpiDiagrafis = new JButton("Delete");
        JButton koumpiAnanewshs = new JButton("Refresh");
        JButton koumpiEpistrofis = new JButton("Back");

        panelKoumpiwn.add(koumpiDiagrafis);
        panelKoumpiwn.add(koumpiAnanewshs);
        panelKoumpiwn.add(koumpiEpistrofis);

        setLayout(new BorderLayout());
        add(scrollPane, BorderLayout.CENTER);
        add(panelKoumpiwn, BorderLayout.SOUTH);

        // Syndeseis koumpiwn me methodous
        koumpiDiagrafis.addActionListener(e -> diagrapsiEpilegmenouXrhsth());
        koumpiAnanewshs.addActionListener(e -> fortwseXrhstes());
        koumpiEpistrofis.addActionListener(e -> {
            dispose();
            new MainMenuAdminFrame(pelatis, onomaAdmin).setVisible(true);
        });

        // Proairetika: diplo klik gia diagrafi
        pinakaXrhstwn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent event) {
                if (event.getClickCount() == 2) {
                    diagrapsiEpilegmenouXrhsth();
                }
            }
        });

        fortwseXrhstes();
    }

    // Fortwnei olous tous xrhstes ston pinaka
    private void fortwseXrhstes() {
        try {
            List<User> xrhstes = pelatis.getAllUsers();
            monteloPinaka.setRowCount(0); // Katharizei ton pinaka

            for (User x : xrhstes) {
                monteloPinaka.addRow(new Object[]{
                        x.getUsername(),
                        x.getName(),
                        x.getEmail(),
                        x.getPhone(),
                        x.getRole()
                });
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error loading users:\n" + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    // Methodos pou diagrafei ton epilegmeno xrhsth
    private void diagrapsiEpilegmenouXrhsth() {
        int grammh = pinakaXrhstwn.getSelectedRow();

        if (grammh == -1) {
            JOptionPane.showMessageDialog(this, "Please select a user!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String xrhstwnOnoma = (String) monteloPinaka.getValueAt(grammh, 0);
        String rolos = (String) monteloPinaka.getValueAt(grammh, 4);

        // Apokleismos diagrafis admin
        if ("admin".equalsIgnoreCase(rolos)) {
            JOptionPane.showMessageDialog(this, "You cannot delete an administrator!", "Forbidden", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Apokleismos autodiagrafis
        if (xrhstwnOnoma.equals(onomaAdmin)) {
            JOptionPane.showMessageDialog(this, "You cannot delete yourself!", "Forbidden", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Epivevaiwsi diagrafis
        int epivevaiwsi = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to delete user: " + xrhstwnOnoma + "?",
                "Confirmation",
                JOptionPane.YES_NO_OPTION);

        if (epivevaiwsi == JOptionPane.YES_OPTION) {
            try {
                boolean epituxia = pelatis.deleteUser(xrhstwnOnoma);
                if (epituxia) {
                    JOptionPane.showMessageDialog(this, "User successfully deleted.");
                    fortwseXrhstes();
                } else {
                    JOptionPane.showMessageDialog(this, "Deletion failed.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Communication error with the server:\n" + e.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }
        }
    }

    // Main methodos gia dokimi
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                Client pelatis = new Client("rmi://localhost:9999/BookingService");
                new AdminDeleteUserFrame(pelatis, "admin").setVisible(true);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Failed to connect to the server", "Error", JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }
        });
    }
}
