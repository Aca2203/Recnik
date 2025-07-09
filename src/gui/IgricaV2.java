package gui;

import java.awt.*;

import javax.swing.*;

import imp.Element;

@SuppressWarnings("serial")
public class IgricaV2 extends Igrica {	
	private JTextField poljeRec = new JTextField(20);
	
	public IgricaV2(FormaV1 roditelj) {
		super(roditelj);
		this.inicijalizuj();
	}
	
	@Override
	protected void popuniProzor() {
		super.popuniProzor();
		setTitle("Игрица верзија 2");
        
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
	
	@Override
	protected void dodajOsluskivace() {
		super.dodajOsluskivace();
		
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

	@Override
	protected void ucitajPitanje() {
		int brojReci = recnik.dohvatiVelicinu();
		
		Element e = recnik.dohvati((int)(Math.random() * brojReci));
		poljeZnacenje.setText(e.znacenje);
		
		tacnaRec = e.rec;
	}

}
