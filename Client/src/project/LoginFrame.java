/*
Stavros Moschis icsd22232
Zacharias Kokkinakis icsd22077
 */package project;

import com.example.client.communication.Client;
import com.example.client.model.User;

import javax.swing.*;
import java.awt.*;

// Klasi gia to parathuro syndesis xrhsth (Login)
public class LoginFrame extends JFrame {
    private JTextField pedioOnomaXrhsth;
    private JPasswordField pedioKwdikos;
    private JButton koumpiSyndesi, koumpiEggrafi;
    private Client pelatis;

    public LoginFrame() {
        setTitle("Reservation System Login");
        setSize(350, 220);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel panel = new JPanel();
        panel.setLayout(null);

        // Titlos pano apo ta pedia
        JLabel etiketaTitlou = new JLabel("Authentication system", SwingConstants.CENTER);
        etiketaTitlou.setBounds(70, 10, 200, 25);
        etiketaTitlou.setFont(new Font("Arial", Font.BOLD, 16));
        panel.add(etiketaTitlou);

        // Onoma xrhsth
        JLabel etiketaXrhsth = new JLabel("Username:");
        etiketaXrhsth.setBounds(30, 50, 100, 25);
        panel.add(etiketaXrhsth);

        pedioOnomaXrhsth = new JTextField();
        pedioOnomaXrhsth.setBounds(140, 50, 150, 25);
        panel.add(pedioOnomaXrhsth);

        // Kwdikos
        JLabel etiketaKwdikou = new JLabel("Password:");
        etiketaKwdikou.setBounds(30, 90, 100, 25);
        panel.add(etiketaKwdikou);

        pedioKwdikos = new JPasswordField();
        pedioKwdikos.setBounds(140, 90, 150, 25);
        panel.add(pedioKwdikos);

        // Koumpi gia sundesi
        koumpiSyndesi = new JButton("Login");
        koumpiSyndesi.setBounds(50, 140, 100, 30);
        panel.add(koumpiSyndesi);

        // Koumpi gia eggrafh
        koumpiEggrafi = new JButton("Register");
        koumpiEggrafi.setBounds(170, 140, 100, 30);
        panel.add(koumpiEggrafi);

        add(panel);

        // Listener gia koumpia
        koumpiSyndesi.addActionListener(e -> xeirismosSyndesis());
        koumpiEggrafi.addActionListener(e -> anoigmaEggrafis());
    }

    // Xeirismos login
    private void xeirismosSyndesis() {
        String onoma = pedioOnomaXrhsth.getText().trim();
        String kwdikos = new String(pedioKwdikos.getPassword());

        if (onoma.isEmpty() || kwdikos.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill in all fields!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Topikos elegxos gia admin xwris RMI
        if (onoma.equals("admin") && kwdikos.equals("admin123")) {
            JOptionPane.showMessageDialog(this, "Welcome, admin!");
            new MainMenuAdminFrame(pelatis, "admin").setVisible(true);
            dispose();
            return;
        }

        // Syndesi me server gia ypoloipous xrhstes
        try {
            Client pelatis = new Client("rmi://localhost:9999/BookingService");
            User xrhsths = pelatis.login(onoma, kwdikos);

            if (xrhsths != null) {
                JOptionPane.showMessageDialog(this, "Welcome " + xrhsths.getName() + "!");

                if ("admin".equalsIgnoreCase(xrhsths.getRole())) {
                    new MainMenuAdminFrame(pelatis, onoma).setVisible(true);
                } else {
                    new MainMenuUserFrame(pelatis, onoma).setVisible(true);
                }

                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Incorrect username or password.", "Error", JOptionPane.ERROR_MESSAGE);
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Failed to connect to server:\n" + e.getMessage(), "Network Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    // Anoigei parathuro eggrafhs
    private void anoigmaEggrafis() {
        new RegisterFrame().setVisible(true);
        dispose();
    }

    // main methodos gia dokimi
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new LoginFrame().setVisible(true));
    }
}
