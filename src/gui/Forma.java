package gui;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JRadioButton;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import imp.Element;

@SuppressWarnings("serial")
public abstract class Forma extends JFrame {
	JTextField poljeRec = new JTextField(20);
	JTextArea poljeZnacenje;
	
	ButtonGroup grupa = new ButtonGroup();
	JRadioButton imenica = new JRadioButton("Именица");
	JRadioButton glagol = new JRadioButton("Глагол");
	JRadioButton pridev = new JRadioButton("Придев");
	
	JButton dodajRec = new JButton("Додај реч");
	JButton izmeniRec = new JButton("Измени реч");
	JButton obrisiRec = new JButton("Обриши реч");
	JButton pretraziRec = new JButton("Претражи реч");
	
	Biranje biranje;
	
	protected Forma(Biranje biranje) {
		this.biranje = biranje;
		setLocationRelativeTo(null);
		
		setResizable(false);
		setTitle("Речник");
	}
	
	protected final void inicializuj() {
		popuniProzor();
		dodajOsluskivace();
		popuniRecnik();
		setVisible(true);
	}
	
	protected final void postaviRadioDugmad() {
		imenica.setActionCommand("0");
		glagol.setActionCommand("1");
		pridev.setActionCommand("2");
		
		grupa.add(imenica);
		grupa.add(glagol);
		grupa.add(pridev);
	}

	protected abstract void popuniProzor();
	
	protected void dodajOsluskivace() {
		dodajRec.addActionListener((ae) -> {
			Element e = proveraPoljaRecZnacenje();
			if(e == null) return;
			dodajElement(e);
		});
		
		izmeniRec.addActionListener((ae) -> {
			Element e = proveraPoljaRecZnacenje();
			if(e == null) return;
			izmeniElement(e);
		});
		
		obrisiRec.addActionListener((ae) -> {
			String rec = proveraPoljeRec();
			obrisiRec(rec);
		});
		
		pretraziRec.addActionListener((ae) -> {
			pretraziRec();
		});
	}	

	protected abstract void dodajElement(Element e);

	protected abstract void izmeniElement(Element e);
	
	protected abstract void obrisiRec(String rec);
	
	protected void pretraziRec() {
		String rec = proveraPoljeRec();
		if(rec == null) return;
		int[] indeks = new int[1];
		Element e = pretrazi(rec, indeks);
		if(!proveraPretrazivanje(e)) return;
		poljeZnacenje.setText(e.znacenje);
		postaviRadioDugmice(vrstaUTekst(e.vrsta));
		oznaciUTabeli(indeks);
	}
	
	protected abstract void oznaciUTabeli(int[] indeks);

	protected abstract Element pretrazi(String rec, int[] indeks);

	protected abstract void popuniRecnik();
	
	public Element proveraPoljaRecZnacenje() {
		String rec = poljeRec.getText().strip();
		String znacenje = poljeZnacenje.getText().strip();
		if(rec.isEmpty() || znacenje.isEmpty()) {
			JOptionPane.showMessageDialog(
	                null,
	                "Унесите реч и значење!",
	                "Грешка!",
	                JOptionPane.ERROR_MESSAGE
	        );
			return null;
		}
		if(grupa.getSelection() == null) {
			JOptionPane.showMessageDialog(
	                null,
	                "Изаберите врсту речи!",
	                "Грешка!",
	                JOptionPane.ERROR_MESSAGE
	        );
			return null;
		}
		
		return new Element(rec, Integer.parseInt(grupa.getSelection().getActionCommand()), znacenje);
	}
	
	public String proveraPoljeRec() {
		String rec = poljeRec.getText().strip();
		if(rec.isEmpty()) {
			JOptionPane.showMessageDialog(
	                null,
	                "Унесите реч!",
	                "Грешка!",
	                JOptionPane.ERROR_MESSAGE
	        );
			return null;
		}
		
		return rec;
	}
	
	public boolean proveriIndeks(int indeks, String poruka) {
		if(indeks == -1) {
			JOptionPane.showMessageDialog(
	                null,
	                poruka,
	                "Грешка!",
	                JOptionPane.ERROR_MESSAGE
	        );
			return false;
		}
		
		return true;
	}
	
	public boolean proveraPretrazivanje(Element e) {
		if(e == null) {
			JOptionPane.showMessageDialog(
	                null,
	                "Реч не постоји у речнику!",
	                "Грешка!",
	                JOptionPane.ERROR_MESSAGE
	        );
			return false;
		}
		
		return true;
	}
	
	public boolean provera(int povratnaVrednost) {
		if(povratnaVrednost == 0) {
			JOptionPane.showMessageDialog(
	                null,
	                "Реч не постоји у речнику!",
	                "Грешка!",
	                JOptionPane.ERROR_MESSAGE
	        );
			return false;
		}
		
		return true;
	}
	
	protected String vrstaUTekst(int v) {
		String vrsta = "";
		switch (v) {
			case 0: {
				vrsta = "Именица";
				break;
			}
				
			case 1: {
				vrsta = "Глагол";
				break;
			}
				
			case 2: {
				vrsta = "Придев";
				break;
			}
			default: {
				break;
			}
		}
		return vrsta;
	}
	
	protected void postaviRadioDugmice(String vrsta) {
		switch (vrsta) {
			case "Именица": {
				imenica.setSelected(true);
				break;
			}
			case "Глагол": {
				glagol.setSelected(true);
				break;
			}
			case "Придев": {
				pridev.setSelected(true);
				break;
			}
			default: {
				break;
			}
		}
	}
}
