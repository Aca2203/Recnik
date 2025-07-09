package gui;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JTextArea;

import imp.Recnik;

@SuppressWarnings("serial")
public abstract class Igrica extends JFrame {
	protected FormaV1 roditelj;
	protected Recnik recnik;
	protected JTextArea poljeZnacenje = new JTextArea(4, 20);
	protected JButton potvrdi = new JButton("Потврди одговор");
	
	protected String tacnaRec;
	
	protected Igrica(FormaV1 roditelj) {
		this.roditelj = roditelj;
		this.recnik = roditelj.dohvatiRecnik();		
	}

	protected final void inicijalizuj() {
		popuniProzor();
		dodajOsluskivace();
		ucitajPitanje();
		setVisible(true);
	}
	
	protected void popuniProzor() {
		setSize(500, 300);
		setLocationRelativeTo(null);
		setResizable(false);
		getContentPane().setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));				
		
		poljeZnacenje.setEditable(false);
		poljeZnacenje.setFocusable(false);
		poljeZnacenje.setLineWrap(true);
		poljeZnacenje.setWrapStyleWord(true);
		poljeZnacenje.setBorder(BorderFactory.createLineBorder(this.getBackground(), 10));
	};

	protected void dodajOsluskivace() {
		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {				
				dispose();
				roditelj.setVisible(true);
			}
		});
	};

	protected abstract void ucitajPitanje();
}
