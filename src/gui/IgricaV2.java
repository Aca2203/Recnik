package gui;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

import imp.Element;
import imp.Recnik;

@SuppressWarnings("serial")
public class IgricaV2 extends JFrame {
	private Forma roditelj;
	private Recnik recnik;
	private JButton potvrdi = new JButton("Потврди одговор");
	private JTextArea poljeZnacenje = new JTextArea(4, 20);
	private JTextField poljeRec = new JTextField(20);
	private String tacnaRec;
	
	public IgricaV2(Forma roditelj) {
		this.roditelj = roditelj;
		this.recnik = roditelj.recnik;
		
		setSize(500, 300);
		setLocationRelativeTo(null);
		setResizable(false);
		setTitle("Игрица верзија 2");
		getContentPane().setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
		
		popuniProzor();
		dodajOsluskivace();
		ucitajPitanje();
		
		setVisible(true);
	}
	
	private void popuniProzor() {
		poljeZnacenje.setEditable(false);
		poljeZnacenje.setFocusable(false);
		poljeZnacenje.setLineWrap(true);
		poljeZnacenje.setWrapStyleWord(true);
        
		poljeZnacenje.setBorder(BorderFactory.createLineBorder(this.getBackground(), 10));
		this.add(poljeZnacenje);
		
		JPanel panel = new JPanel(new GridLayout(1, 2, 10, 0));
		JLabel labela = new JLabel("Ваш одговор:", JLabel.RIGHT);
		panel.add(labela);
		panel.add(poljeRec);
		
		poljeRec.setBorder(BorderFactory.createLineBorder(this.getBackground(), 10));
		this.add(panel);
		
        JPanel tmp = new JPanel();
        tmp.add(potvrdi);
        this.add(tmp);
	}
	
	private void dodajOsluskivace() {
		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {				
				dispose();
				roditelj.setVisible(true);
			}
		});
		
		potvrdi.addActionListener((ae) -> {
			String izabranaRec = poljeRec.getText().strip();
			String poruka;
			if(izabranaRec.equals(tacnaRec)) {
				poruka = "Изабрали сте тачну реч!";
			} else {
				poruka = "Погрешна реч, тачна реч је била: " + tacnaRec;
			}
			
			JDialog dialog = new JDialog(this, "", true);
			dialog.add(new JLabel(poruka, JLabel.CENTER));
			dialog.setBounds(this.getX() + this.getWidth() / 2 - 200, this.getY() + this.getHeight() / 2 - 100, 400, 200);
			dialog.setVisible(true);
			poljeRec.setText("");
			poljeRec.requestFocus();
			ucitajPitanje();
		});
	}

	private void ucitajPitanje() {
		int brojReci = recnik.dohvatiVelicinu();
		
		Element e = recnik.dohvati((int)(Math.random() * brojReci));
		poljeZnacenje.setText(e.znacenje);
		
		tacnaRec = e.rec;
	}

}
