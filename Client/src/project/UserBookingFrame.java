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

// Klasi pou epitrepsei se xrhsth na kleisei kratisi gia sygkekrimeno show
public class UserBookingFrame extends JFrame {
    private JLabel etiketaEvent, etiketaHmeromhnia, etiketaWra, etiketaTheseis, etiketaTimh;
    private JTextField pedioEisitiria, pedioOnoma, pedioKarta;
    private JButton koumpiKratisi, koumpiAkyrosh;

    private final Client pelatis;
    private final String onomaXrhsth;
    private final String showId;

    private Show epilegmenoShow;
    private Event antistoixoEvent;

    public UserBookingFrame(Client pelatis, String onomaXrhsth, String showId) {
        this.pelatis = pelatis;
        this.onomaXrhsth = onomaXrhsth;
        this.showId = showId;

        setTitle("Ticket Booking");
        setSize(420, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setResizable(false);

        JPanel panel = new JPanel();
        panel.setLayout(null);

        etiketaEvent = new JLabel("Event:");
        etiketaEvent.setBounds(20, 20, 380, 25);
        panel.add(etiketaEvent);

        etiketaHmeromhnia = new JLabel("Date:");
        etiketaHmeromhnia.setBounds(20, 50, 180, 25);
        panel.add(etiketaHmeromhnia);

        etiketaWra = new JLabel("Time:");
        etiketaWra.setBounds(220, 50, 180, 25);
        panel.add(etiketaWra);

        etiketaTheseis = new JLabel("Available seats:");
        etiketaTheseis.setBounds(20, 80, 180, 25);
        panel.add(etiketaTheseis);

        etiketaTimh = new JLabel("Ticket price:");
        etiketaTimh.setBounds(220, 80, 180, 25);
        panel.add(etiketaTimh);

        JLabel etiketaEisitiria = new JLabel("Number of tickets:");
        etiketaEisitiria.setBounds(20, 120, 150, 25);
        panel.add(etiketaEisitiria);
        pedioEisitiria = new JTextField();
        pedioEisitiria.setBounds(180, 120, 50, 25);
        panel.add(pedioEisitiria);

        JLabel etiketaOnomatos = new JLabel("fullname:");
        etiketaOnomatos.setBounds(20, 160, 120, 25);
        panel.add(etiketaOnomatos);
        pedioOnoma = new JTextField();
        pedioOnoma.setBounds(160, 160, 220, 25);
        panel.add(pedioOnoma);

        JLabel etiketaKartas = new JLabel("cardnumber:");
        etiketaKartas.setBounds(20, 200, 120, 25);
        panel.add(etiketaKartas);
        pedioKarta = new JTextField();
        pedioKarta.setBounds(160, 200, 220, 25);
        panel.add(pedioKarta);

        koumpiKratisi = new JButton("Complete");
        koumpiKratisi.setBounds(90, 260, 230, 35);
        panel.add(koumpiKratisi);

        koumpiAkyrosh = new JButton("cancel");
        koumpiAkyrosh.setBounds(150, 310, 100, 30);
        panel.add(koumpiAkyrosh);

        add(panel);

        fortwseShowApoServer();

        koumpiKratisi.addActionListener(e -> ekteleseKratisi());
        koumpiAkyrosh.addActionListener(e -> {
            dispose();
            new MainMenuUserFrame(pelatis, onomaXrhsth).setVisible(true);
        });
    }

    // Fortwnei stoixeia show me to sygkekrimeno ID apo to server
    private void fortwseShowApoServer() {
        try {
            List<Event> events = pelatis.getEvents();

            for (Event e : events) {
                for (Show s : e.getListaParastasewn()) {
                    if (s.getKwdikosParastasis().equals(showId)) {
                        antistoixoEvent = e;
                        epilegmenoShow = s;

                        etiketaEvent.setText("event: " + e.getTitlos());
                        etiketaHmeromhnia.setText("date: " + s.getHmeromhnia());
                        etiketaWra.setText("hour: " + s.getWra());
                        etiketaTheseis.setText("availability: " + s.getDia8esimesTheseis());
                        etiketaTimh.setText("price: " + s.getTimh() + " €");
                        return;
                    }
                }
            }

            JOptionPane.showMessageDialog(this, "Η παράσταση δεν βρέθηκε.", "Σφάλμα", JOptionPane.ERROR_MESSAGE);
            dispose();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Αποτυχία φόρτωσης παραστάσεων από τον server.", "Σφάλμα", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
            dispose();
        }
    }

    // Ektelei thn kratisi an ola einai swsta
    private void ekteleseKratisi() {
        String plithosEisitiriwn = pedioEisitiria.getText().trim();
        String pliresOnoma = pedioOnoma.getText().trim();
        String arKarths = pedioKarta.getText().trim();

        if (plithosEisitiriwn.isEmpty() || pliresOnoma.isEmpty() || arKarths.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Συμπλήρωσε όλα τα πεδία!", "Σφάλμα", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int plithos;
        try {
            plithos = Integer.parseInt(plithosEisitiriwn);
            if (plithos <= 0) throw new NumberFormatException();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Άκυρος αριθμός εισιτηρίων.", "Σφάλμα", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (plithos > epilegmenoShow.getDia8esimesTheseis()) {
            JOptionPane.showMessageDialog(this, "Δεν υπάρχουν αρκετές διαθέσιμες θέσεις.", "Σφάλμα", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!arKarths.matches("\\d{16}")) {
            JOptionPane.showMessageDialog(this, "Ο αριθμός κάρτας πρέπει να έχει 16 ψηφία.", "Σφάλμα", JOptionPane.ERROR_MESSAGE);
            return;
        }

        double synolikoPoso = plithos * epilegmenoShow.getTimh();
        Payment plhrwmh = new Payment("PMT" + System.currentTimeMillis(), epilegmenoShow.getKwdikosParastasis(), synolikoPoso, pliresOnoma);
        plhrwmh.setCardNumber(arKarths);
        plhrwmh.setPaymentDate(java.time.LocalDate.now().toString());

        try {
            boolean epituxia = pelatis.bookTickets(onomaXrhsth, epilegmenoShow.getKwdikosParastasis(), plithos, plhrwmh);
            if (epituxia) {
                JOptionPane.showMessageDialog(this, "Η κράτηση ολοκληρώθηκε με επιτυχία!", "Επιτυχία", JOptionPane.INFORMATION_MESSAGE);
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Η κράτηση απέτυχε.", "Αποτυχία", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Σφάλμα σύνδεσης με τον server.", "Σφάλμα", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    // Main methodos gia dokimi
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                Client pelatis = new Client("rmi://localhost:9999/BookingService");
                new UserBookingFrame(pelatis, "user1", "S001").setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}
