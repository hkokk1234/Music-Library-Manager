/*
Stavros Moschis icsd22232
Zacharias Kokkinakis icsd22077
 */package project;

import com.example.client.communication.Client;
import com.example.client.model.User;

import javax.swing.*;
import java.awt.*;

// Parathuro gia eggrafh neou xrhsth
public class RegisterFrame extends JFrame {
    private JTextField pedioOnoma, pedioThlefwnou, pedioEmail, pedioUsername;
    private JPasswordField pedioKwdikou;
    private JButton koumpiEggrafi, koumpiAkyrosh;

    public RegisterFrame() {
        setTitle("New User Registration");
        setSize(400, 320);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel panel = new JPanel();
        panel.setLayout(null);

        // Titlos
        JLabel etiketaTitlou = new JLabel("Registration", SwingConstants.CENTER);
        etiketaTitlou.setFont(new Font("Arial", Font.BOLD, 16));
        etiketaTitlou.setBounds(120, 10, 180, 25);
        panel.add(etiketaTitlou);

        // Fullname
        JLabel etiketaOnoma = new JLabel("fullname:");
        etiketaOnoma.setBounds(30, 50, 120, 25);
        panel.add(etiketaOnoma);
        pedioOnoma = new JTextField();
        pedioOnoma.setBounds(160, 50, 200, 25);
        panel.add(pedioOnoma);

        // Thlefwnos
        JLabel etiketaThlefwnou = new JLabel("Phone:");
        etiketaThlefwnou.setBounds(30, 85, 120, 25);
        panel.add(etiketaThlefwnou);
        pedioThlefwnou = new JTextField();
        pedioThlefwnou.setBounds(160, 85, 200, 25);
        panel.add(pedioThlefwnou);

        // Email
        JLabel etiketaEmail = new JLabel("Email:");
        etiketaEmail.setBounds(30, 120, 120, 25);
        panel.add(etiketaEmail);
        pedioEmail = new JTextField();
        pedioEmail.setBounds(160, 120, 200, 25);
        panel.add(pedioEmail);

        // Username
        JLabel etiketaUsername = new JLabel("Username:");
        etiketaUsername.setBounds(30, 155, 120, 25);
        panel.add(etiketaUsername);
        pedioUsername = new JTextField();
        pedioUsername.setBounds(160, 155, 200, 25);
        panel.add(pedioUsername);

        // Kwdikos
        JLabel etiketaKwdikou = new JLabel("Password:");
        etiketaKwdikou.setBounds(30, 190, 120, 25);
        panel.add(etiketaKwdikou);
        pedioKwdikou = new JPasswordField();
        pedioKwdikou.setBounds(160, 190, 200, 25);
        panel.add(pedioKwdikou);

        // Koumpia
        koumpiEggrafi = new JButton("confirm");
        koumpiEggrafi.setBounds(80, 235, 100, 30);
        panel.add(koumpiEggrafi);

        koumpiAkyrosh = new JButton("cancel");
        koumpiAkyrosh.setBounds(220, 235, 100, 30);
        panel.add(koumpiAkyrosh);

        add(panel);

        // Energeies koumpiwn
        koumpiEggrafi.addActionListener(e -> xeirismosEggrafis());
        koumpiAkyrosh.addActionListener(e -> {
            new LoginFrame().setVisible(true);
            dispose();
        });
    }

    // Xeirismos eggrafhs xrhsth
    private void xeirismosEggrafis() {
        String onoma = pedioOnoma.getText().trim();
        String thlefwnos = pedioThlefwnou.getText().trim();
        String email = pedioEmail.getText().trim();
        String username = pedioUsername.getText().trim();
        String kwdikos = new String(pedioKwdikou.getPassword());
        String rolos = "user";

        // Elegxoi kenwn pediwn
        if (onoma.isEmpty() || thlefwnos.isEmpty() || email.isEmpty() || username.isEmpty() || kwdikos.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill in all fields!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Elegxos gia thlefwniko arithmo
        if (!thlefwnos.matches("\\d{10,}")) {
            JOptionPane.showMessageDialog(this, "Phone number must be at least 10 digits.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Elegxos email
        if (!email.matches("^\\S+@\\S+\\.\\S+$")) {
            JOptionPane.showMessageDialog(this, "Invalid email format.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Dimiourgia xrhsth kai apostolh sto server
        User xrhsths = new User(username, kwdikos, onoma, thlefwnos, email, rolos);

        try {
            Client pelatis = new Client("rmi://localhost:9999/BookingService");
            boolean epituxia = pelatis.registerUser(xrhsths);

            if (epituxia) {
                JOptionPane.showMessageDialog(this, "Registration successful! You can now log in.");
                new LoginFrame().setVisible(true);
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Username is already taken.", "Failure", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Failed to connect to the server:\n" + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    // main methodos gia dokimi
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new RegisterFrame().setVisible(true));
    }
}
