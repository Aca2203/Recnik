package gui;

import java.awt.*;
import java.awt.event.*;
import java.util.HashSet;
import java.util.Set;

import javax.swing.*;

import imp.*;

@SuppressWarnings("serial")
public class IgricaV1 extends JFrame {
	private static final int brojOpcija = 4;
	
	private Forma roditelj;
	private Recnik recnik;
	private JRadioButton[][] opcije = {
		{ new JRadioButton(""), new JRadioButton(""), },
		{ new JRadioButton(""), new JRadioButton("") }
	};
	private ButtonGroup grupa = new ButtonGroup();
	private JButton potvrdi = new JButton("Потврди одговор");
	
	private JTextArea poljeZnacenje = new JTextArea(4, 20);
	
	private String tacnaRec;
	
	public IgricaV1(Forma roditelj) {
		this.roditelj = roditelj;
		this.recnik = roditelj.recnik;
		
		setBounds(700, 300, 500, 300);
		setResizable(false);
		setTitle("Игрица верзија 1");
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
        
		JPanel panel = new JPanel(new GridBagLayout());		
		
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(25, 50, 25, 50);
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.CENTER;
		
		for(int i = 0; i < brojOpcija / 2; i++) {
			for(int j = 0; j < brojOpcija / 2; j++) {
				gbc.gridx = i;
				gbc.gridy = j;
				
				grupa.add(opcije[i][j]);
				panel.add(opcije[i][j], gbc);
			}			
		}
        
		poljeZnacenje.setBorder(BorderFactory.createLineBorder(this.getBackground(), 10));
		this.add(poljeZnacenje);
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
			String poruka;
			if(grupa.getSelection() == null) {
				JDialog dialog = new JDialog(this, "Грешка!", true);
				dialog.add(new Label("Изаберите реч!", Label.CENTER));
				dialog.setBounds(this.getX() + this.getWidth() / 2 - 200, this.getY() + this.getHeight() / 2 - 100, 400, 200);
				dialog.setVisible(true);
				return;
			}
			String izabranaRec = grupa.getSelection().getActionCommand();			
			if(izabranaRec == tacnaRec) {
				poruka = "Изабрали сте тачну реч!";
			} else {
				poruka = "Погрешна реч, тачна реч је била: " + tacnaRec;
			}
			
			JDialog dialog = new JDialog(this, "", true);
			dialog.add(new Label(poruka, Label.CENTER));
			dialog.setBounds(this.getX() + this.getWidth() / 2 - 200, this.getY() + this.getHeight() / 2 - 100, 400, 200);
			dialog.setVisible(true);
			grupa.clearSelection();
			ucitajPitanje();
		});
	}

	private void ucitajPitanje() {
		int brojReci = recnik.dohvatiVelicinu();
		if(brojReci < brojOpcija) return;
		
		Set<String> reci = new HashSet<String>();
		
		Element e = recnik.dohvati((int)(Math.random() * brojReci));
		reci.add(e.rec);
		poljeZnacenje.setText(e.znacenje);
		
		tacnaRec = e.rec;
		
		int tacnaRecX = (int)(Math.random() * 2);
		int tacnaRecY = (int)(Math.random() * 2);
		
		opcije[tacnaRecX][tacnaRecY].setText(e.rec);
		opcije[tacnaRecX][tacnaRecY].setActionCommand(e.rec);
		
		for(int i = 0; i < brojOpcija / 2; i++) {
			for(int j = 0; j < brojOpcija / 2; j++) {
				if(i == tacnaRecX && j == tacnaRecY) continue;
				String rec;
				do {
					rec = recnik.dohvati((int)(Math.random() * brojReci)).rec;
				} while(reci.contains(rec));
				reci.add(rec);
				opcije[i][j].setText(rec);
				opcije[i][j].setActionCommand(rec);
			}
		}
	}
}
