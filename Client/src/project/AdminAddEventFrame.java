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
import java.text.SimpleDateFormat;
import java.util.Date;

public class AdminAddEventFrame extends JFrame {

    // Dhlwseis pedion gia ton client kai ton xrhsth (admin)

    private final Client client;
    private final String adminUsername;
    // Text fields gia eisagwgh dedomenwn tou event

    private JTextField titleField, typeField, dateField, timeField, priceField, seatsField;
    // Constructor pou orizei to layout kai ta stoixeia tou parathyrou

    public AdminAddEventFrame(Client client, String adminUsername) {
        this.client = client;
        this.adminUsername = adminUsername;
        // Vazei titlo kai orismena xarakthristika sto JFrame

        setTitle("Add New Event");
        setSize(700, 450);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setResizable(false);
        setLayout(null);

        // Prosthetei etiketes kai pedio gia titlo

        JLabel titleLabel = new JLabel("title:");        titleLabel.setBounds(30, 20, 80, 25);
        add(titleLabel);
        titleField = new JTextField();
        titleField.setBounds(100, 20, 200, 25);
        add(titleField);
        //etiiketa typou event

        JLabel typeLabel = new JLabel("type:");
        typeLabel.setBounds(320, 20, 80, 25);
        add(typeLabel);
        typeField = new JTextField();
        typeField.setBounds(380, 20, 200, 25);
        add(typeField);
        //hmeromhnia me morfh YYYY-MM-DD

        JLabel dateLabel = new JLabel("date (YYYY-MM-DD):");
        dateLabel.setBounds(30, 70, 180, 25);
        add(dateLabel);
        dateField = new JTextField();
        dateField.setBounds(210, 70, 100, 25);
        add(dateField);
        //wra me morfh HH:MM

        JLabel timeLabel = new JLabel("time (HH:MM):");
        timeLabel.setBounds(330, 70, 100, 25);
        add(timeLabel);
        timeField = new JTextField();
        timeField.setBounds(420, 70, 80, 25);
        add(timeField);
        //pedia dhlwshs timh kai theseis

        JLabel priceLabel = new JLabel("price(float) (€):");
        priceLabel.setBounds(30, 110, 60, 25);
        add(priceLabel);
        priceField = new JTextField();
        priceField.setBounds(90, 110, 60, 25);
        add(priceField);

        JLabel seatsLabel = new JLabel("seats:");
        seatsLabel.setBounds(170, 110, 60, 25);
        add(seatsLabel);
        seatsField = new JTextField();
        seatsField.setBounds(230, 110, 60, 25);
        add(seatsField);
        //koumpi gia na apothikeusei to event

        JButton addAndSaveBtn = new JButton("add");
        addAndSaveBtn.setBounds(180, 180, 220, 40);
        add(addAndSaveBtn);
        //koumpi akyrosis kai epistrofh sto menu

        JButton cancelBtn = new JButton("cancel");
        cancelBtn.setBounds(420, 180, 120, 40);
        add(cancelBtn);
        // Listener gia ta koumpia

        addAndSaveBtn.addActionListener(this::addevent);
        cancelBtn.addActionListener(e -> {
            dispose();
            new MainMenuAdminFrame(client, adminUsername).setVisible(true);
        });    }
    // Methodos gia apothikeush event

    private void addevent(ActionEvent e) {

        String title = titleField.getText().trim();
        String type = typeField.getText().trim();
        String dateStr = dateField.getText().trim();
        String time = timeField.getText().trim();

        try {
            //metatroph timis kai thesewn

            double price = Double.parseDouble(priceField.getText().trim());
            int seats = Integer.parseInt(seatsField.getText().trim());
            //elegxos gia keno pedio

            if (title.isEmpty() || type.isEmpty() || dateStr.isEmpty() || time.isEmpty()) {
                throw new IllegalArgumentException("Συμπλήρωσε όλα τα πεδία.");
            }
            //metatroph hmeromhnia

            Date date = new SimpleDateFormat("yyyy-MM-dd").parse(dateStr);
            String showId = "S" + System.currentTimeMillis();
            Show show = new Show(showId, date, time, price, seats, true);

            String eventCode = "E" + System.currentTimeMillis();
            Event event = new Event(eventCode, title, type);
            event.prostheseParastasi(show);
            // Thread gia na apothikeusei to event sto background

            new Thread(() -> {
                try {
                    boolean success = client.saveEventToLocal(event);

                    SwingUtilities.invokeLater(() -> {
                        if (success) {
                            JOptionPane.showMessageDialog(this, "Event added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                            dispose();
                            new MainMenuAdminFrame(client, "admin").setVisible(true);


                        } else {
                            JOptionPane.showMessageDialog(this, "Failed to save!", "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    });
                } catch (Exception ex) {
                    SwingUtilities.invokeLater(() ->
                            JOptionPane.showMessageDialog(this, "Error while saving: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE)
                    );
                    ex.printStackTrace();
                }
            }).start();

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Field error!", "Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    public static void main(String[] args) {
        try {
            Client client = new Client("rmi://localhost:9999/BookingService");
            new AdminAddEventFrame(client, "admin").setVisible(true);
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Failed to connect to the server", "Error", JOptionPane.ERROR_MESSAGE);
        }

    }
}
