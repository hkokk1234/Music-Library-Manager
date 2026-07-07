/*
Stavros Moschis icsd22232
Zacharias Kokkinakis icsd22077
 */package project;

import com.example.client.communication.Client;
import com.example.client.model.Event;
import com.example.client.model.Payment;
import com.example.client.model.Show;

import javax.swing.*;
import java.awt.*;
import java.util.List;

// Klasi pou epitrepei ston xrhsth na kleisei eisitiria gia show
public class BookingFrame extends JFrame {
    private JComboBox<String> listaEventwn;
    private JComboBox<String> listaShow;
    private JTextField pedioEisitiria;
    private JButton koumpiEpivevaiwsh, koumpiAkyrosh;

    private List<Event> listaEvents;
    private Client pelatis;
    private String xrhstis;

    public BookingFrame(Client pelatis, String xrhstis) {
        this.pelatis = pelatis;
        this.xrhstis = xrhstis;

        setTitle("Ticket Booking");
        setSize(450, 250);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(null);

        // Label kai lista eventwn
        JLabel etiketaEvent = new JLabel("Event:");
        etiketaEvent.setBounds(30, 30, 100, 25);
        add(etiketaEvent);

        listaEventwn = new JComboBox<>();
        listaEventwn.setBounds(130, 30, 250, 25);
        add(listaEventwn);

        // Label kai lista shows
        JLabel etiketaShow = new JLabel("Show:");
        etiketaShow.setBounds(30, 70, 100, 25);
        add(etiketaShow);

        listaShow = new JComboBox<>();
        listaShow.setBounds(130, 70, 250, 25);
        add(listaShow);

        // Label kai pedio gia arithmo eisitiriwn
        JLabel etiketaEisitiria = new JLabel("Number of Tickets:");
        etiketaEisitiria.setBounds(30, 110, 150, 25);
        add(etiketaEisitiria);

        pedioEisitiria = new JTextField();
        pedioEisitiria.setBounds(180, 110, 50, 25);
        add(pedioEisitiria);

        // Koumpia gia epivevaiwsh kai akyrosh
        koumpiEpivevaiwsh = new JButton("Confirm");
        koumpiEpivevaiwsh.setBounds(80, 160, 120, 30);
        add(koumpiEpivevaiwsh);

        koumpiAkyrosh = new JButton("Cancel");
        koumpiAkyrosh.setBounds(230, 160, 120, 30);
        add(koumpiAkyrosh);

        // Fortwsh eventwn
        fortwseEvents();

        // Listener gia tropopoihsh shows otan allaksei event
        listaEventwn.addActionListener(e -> fortwseShows());

        // Listener gia epivevaiwsh kratisis
        koumpiEpivevaiwsh.addActionListener(e -> kratiseEisitiria());

        // Epistrofh sto menu xrhsth
        koumpiAkyrosh.addActionListener(e -> {
            dispose();
            new MainMenuUserFrame(pelatis, xrhstis).setVisible(true);
        });
    }

    // Fortwnei ta events kai to prwto show
    private void fortwseEvents() {
        try {
            listaEvents = pelatis.getEvents();
            listaEventwn.removeAllItems();

            for (Event event : listaEvents) {
                listaEventwn.addItem(event.getTitlos());
            }

            fortwseShows(); // arxiki fortwsh show gia to prwto event
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error loading events", "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    // Fortwnei ta shows gia to epilegmeno event
    private void fortwseShows() {
        listaShow.removeAllItems();
        int index = listaEventwn.getSelectedIndex();
        if (index >= 0 && listaEvents != null) {
            List<Show> shows = listaEvents.get(index).getListaParastasewn();
            for (Show show : shows) {
                if (show.einaiEnergo()) {
                    listaShow.addItem(show.getKwdikosParastasis() + " (" + show.getHmeromhnia() + " " + show.getWra() + ")");
                }
            }
        }
    }

    // Xeirismos kratisis eisitiriwn
    private void kratiseEisitiria() {
        int eventIndex = listaEventwn.getSelectedIndex();
        int showIndex = listaShow.getSelectedIndex();

        if (eventIndex < 0 || showIndex < 0) {
            JOptionPane.showMessageDialog(this, "Please select an event and show", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String plithosText = pedioEisitiria.getText().trim();
        if (!plithosText.matches("\\d+")) {
            JOptionPane.showMessageDialog(this, "Invalid number of tickets", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int plithos = Integer.parseInt(plithosText);
        if (plithos <= 0) {
            JOptionPane.showMessageDialog(this, "Number of tickets must be positive", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        List<Show> energopoihmenaShows = listaEvents.get(eventIndex).getListaParastasewn().stream()
                .filter(Show::einaiEnergo)
                .toList();

        Show epilegmenoShow = energopoihmenaShows.get(showIndex);

        if (plithos > epilegmenoShow.getDia8esimesTheseis()) {
            JOptionPane.showMessageDialog(this, "Not enough available seats", "Failure", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Dimiourgia kratisis kai plhrwmhs
        String kratisiId = "B" + System.currentTimeMillis();
        String plhrwmhId = "P" + System.currentTimeMillis();
        double synolikoPoso = epilegmenoShow.getTimh() * plithos;
        Payment plhrwmh = new Payment(plhrwmhId, kratisiId, synolikoPoso, "Karta");

        try {
            boolean epituxia = pelatis.bookTickets(xrhstis, epilegmenoShow.getKwdikosParastasis(), plithos, plhrwmh);
            if (epituxia) {
                JOptionPane.showMessageDialog(this, "Booking completed successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Booking failed", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error sending booking request", "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    // main methodos gia dokimi
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                Client pelatis = new Client("rmi://localhost:9999/BookingService");
                new BookingFrame(pelatis, "user1").setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}
