package gui;

import java.awt.*;
import java.util.HashSet;
import java.util.Set;

import javax.swing.*;

import imp.*;

@SuppressWarnings("serial")
public class IgricaV1 extends Igrica {
	private static final int brojOpcija = 4;
	private JRadioButton[][] opcije = {
		{ new JRadioButton(""), new JRadioButton(""), },
		{ new JRadioButton(""), new JRadioButton("") }
	};
	private ButtonGroup grupa = new ButtonGroup();
	
	public IgricaV1(FormaV1 roditelj) {
		super(roditelj);
		this.inicijalizuj();
	}	

	@Override
	protected void popuniProzor() {
		super.popuniProzor();
		setTitle("Игрица верзија 1");	
        
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
        
		this.add(poljeZnacenje);
        this.add(panel);
        JPanel tmp = new JPanel();
        tmp.add(potvrdi);
        this.add(tmp);
	}
	
	@Override
	protected void dodajOsluskivace() {
		super.dodajOsluskivace();
		
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
			dialog.add(new JLabel(poruka, JLabel.CENTER));
			dialog.setBounds(this.getX() + this.getWidth() / 2 - 200, this.getY() + this.getHeight() / 2 - 100, 400, 200);
			dialog.setVisible(true);
			grupa.clearSelection();
			ucitajPitanje();
		});
	}

	@Override
	protected void ucitajPitanje() {
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
